package com.example.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.data.model.UserLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

sealed interface LocationResult {
    data class Success(val userLocation: UserLocation) : LocationResult
    data class Error(val message: String) : LocationResult
    object PermissionDenied : LocationResult
}

class NovyraLocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Checks if either FINE or COARSE location permission is granted.
     */
    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    /**
     * Fetches current device location using FusedLocationProviderClient and resolves
     * city, district, state, and country via reverse geocoding.
     */
    suspend fun getCurrentLocation(): LocationResult = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext LocationResult.PermissionDenied
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()
            
            // Request balanced power accuracy suitable for news feeds (city-level accuracy)
            val locationTask = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            )

            var location: Location? = locationTask.awaitTask()

            // Fall back to last known location if immediate fix is null
            if (location == null) {
                val lastLocationTask = fusedLocationClient.lastLocation
                location = lastLocationTask.awaitTask()
            }

            if (location == null) {
                return@withContext LocationResult.Error("Unable to acquire location fix. Please ensure location services are enabled.")
            }

            val address = reverseGeocode(location.latitude, location.longitude)
            val city = address?.locality 
                ?: address?.subAdminArea 
                ?: address?.subLocality 
                ?: "Local"
            val state = address?.adminArea ?: "State"
            val district = address?.subAdminArea ?: address?.locality ?: city
            val country = address?.countryName ?: "India"

            val userLocation = UserLocation(
                country = country,
                state = state,
                district = district,
                city = city,
                isPersonalizationEnabled = true,
                isGpsActive = true
            )

            LocationResult.Success(userLocation)
        } catch (e: SecurityException) {
            LocationResult.PermissionDenied
        } catch (e: Exception) {
            LocationResult.Error(e.localizedMessage ?: "Unknown location error occurred")
        }
    }

    /**
     * Resolves geographical coordinates into an Address object using Geocoder.
     */
    private suspend fun reverseGeocode(latitude: Double, longitude: Double): Address? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (!Geocoder.isPresent()) {
                return@withContext null
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            continuation.resume(addresses.firstOrNull())
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resume(null)
                        }
                    })
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Await helper for Play Services Tasks using Coroutine continuation.
 */
private suspend fun <T> Task<T>.awaitTask(): T? = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        if (continuation.isActive) {
            continuation.resume(result)
        }
    }
    addOnFailureListener {
        if (continuation.isActive) {
            continuation.resume(null)
        }
    }
    addOnCanceledListener {
        if (continuation.isActive) {
            continuation.resume(null)
        }
    }
}
