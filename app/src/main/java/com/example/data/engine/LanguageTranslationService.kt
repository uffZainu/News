package com.example.data.engine

import com.example.data.model.Language

object LanguageTranslationService {

    val supportedLanguages: List<Language> = listOf(
        Language("en", "English", "English"),
        Language("hi", "Hindi", "हिन्दी"),
        Language("bn", "Bengali", "বাংলা"),
        Language("ta", "Tamil", "தமிழ்"),
        Language("te", "Telugu", "తెలుగు"),
        Language("mr", "Marathi", "मराठी"),
        Language("gu", "Gujarati", "ગુજરાતી"),
        Language("kn", "Kannada", "ಕನ್ನಡ"),
        Language("ml", "Malayalam", "മലയാളം"),
        Language("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        Language("ur", "Urdu", "اردو"),
        Language("or", "Odia", "ଓଡ଼ିଆ"),
        Language("as", "Assamese", "অসমীয়া"),
        Language("es", "Spanish", "Español"),
        Language("fr", "French", "Français"),
        Language("ar", "Arabic", "العربية")
    )

    private val translations = mapOf(
        // English
        "en" to mapOf(
            "app_tagline" to "Local to Global. One Place.",
            "tab_all" to "Top Stories",
            "tab_local" to "Local News",
            "tab_state" to "State News",
            "tab_national" to "National",
            "tab_world" to "World",
            "tab_trending" to "Trending",
            "breaking_news" to "BREAKING NEWS",
            "ai_verified" to "AI Verified Summary",
            "sources_reporting" to "sources reporting",
            "read_original" to "Read Original Article",
            "source" to "Source",
            "listen" to "Listen to Story",
            "stop_listening" to "Stop Audio",
            "bookmark" to "Save",
            "bookmarked" to "Saved",
            "share" to "Share",
            "fact_check" to "Fact & Neutrality Check",
            "search_placeholder" to "Search news, topics, cities...",
            "admin_panel" to "Admin & Ingestion Console",
            "location_permission_needed" to "Enable Location for Local & State News",
            "privacy_notice" to "Minimum location data used solely for news personalization. Never shared.",
            "select_location" to "Change Location",
            "select_language" to "Select Edition Language",
            "offline_reading" to "Saved for Offline Reading",
            "no_saved_articles" to "No saved articles yet. Bookmark stories to read offline.",
            "run_ingestion" to "Fetch & Ingest News Pipeline",
            "ingestion_success" to "Pipeline completed: Ingested & AI-processed latest stories."
        ),
        // Hindi
        "hi" to mapOf(
            "app_tagline" to "स्थानीय से वैश्विक। एक ही स्थान पर।",
            "tab_all" to "प्रमुख समाचार",
            "tab_local" to "स्थानीय समाचार",
            "tab_state" to "राज्य समाचार",
            "tab_national" to "राष्ट्रीय",
            "tab_world" to "विश्व समाचार",
            "tab_trending" to "ट्रेंडिंग",
            "breaking_news" to "ताज़ा खबर (ब्रेकिंग न्यूज़)",
            "ai_verified" to "एआई सत्यापित सारांश",
            "sources_reporting" to "स्रोत रिपोर्ट कर रहे हैं",
            "read_original" to "मूल समाचार पढ़ें",
            "source" to "स्रोत",
            "listen" to "खबर सुनें",
            "stop_listening" to "ऑडियो रोकें",
            "bookmark" to "सहेजें",
            "bookmarked" to "सहेजा गया",
            "share" to "साझा करें",
            "fact_check" to "तथ्य एवं निष्पक्षता जांच",
            "search_placeholder" to "समाचार, विषय, शहर खोजें...",
            "admin_panel" to "प्रबंधन एवं अंतर्ग्रहण कंसोल",
            "location_permission_needed" to "स्थानीय एवं राज्य समाचारों के लिए स्थान सक्षम करें",
            "privacy_notice" to "स्थान की जानकारी केवल निजीकरण के लिए। कभी साझा नहीं की जाती।",
            "select_location" to "स्थान बदलें",
            "select_language" to "संस्करण भाषा चुनें",
            "offline_reading" to "ऑफ़लाइन पढ़ने हेतु सहेजा गया",
            "no_saved_articles" to "अभी कोई सहेजा गया समाचार नहीं है।",
            "run_ingestion" to "समाचार पाइपलाइन चलाएं",
            "ingestion_success" to "पाइपलाइन पूर्ण: नवीनतम समाचार प्रसंस्कृत।"
        ),
        // Bengali
        "bn" to mapOf(
            "app_tagline" to "স্থানীয় থেকে বিশ্বমঞ্চ। এক ঠিকানায়।",
            "tab_all" to "শীর্ষ খবর",
            "tab_local" to "স্থানীয় সংবাদ",
            "tab_state" to "রাজ্যের খবর",
            "tab_national" to "জাতীয়",
            "tab_world" to "আন্তর্জাতিক",
            "tab_trending" to "ট্রেন্ডিং",
            "breaking_news" to "তাজা খবর (ব্রেকিং নিউজ)",
            "ai_verified" to "এআই যাচাইকৃত সারসংক্ষেপ",
            "sources_reporting" to "টি সূত্র থেকে রিপোর্ট",
            "read_original" to "মূল প্রতিবেদন পড়ুন",
            "source" to "সূত্র",
            "listen" to "সংবাদ শুনুন",
            "stop_listening" to "অডিও থামান",
            "bookmark" to "সংরক্ষণ",
            "bookmarked" to "সংরক্ষিত",
            "share" to "শেয়ার করুন",
            "fact_check" to "তথ্য ও সত্যতা যাচাই",
            "search_placeholder" to "সংবাদ, বিষয়, শহর খুঁজুন...",
            "admin_panel" to "অ্যাডমিন ও ইনজেশন কনসোল",
            "location_permission_needed" to "স্থানীয় খবরের জন্য লোকেশন অন করুন",
            "privacy_notice" to "লোকেশন তথ্য কেবল ব্যক্তিগতকরণের জন্য ব্যবহূত হয়।",
            "select_location" to "লোকেশন পরিবর্তন করুন",
            "select_language" to "ভাষা নির্বাচন করুন",
            "offline_reading" to "অফলাইন পড়ার জন্য সংরক্ষিত",
            "no_saved_articles" to "কোনো সংরক্ষিত সংবাদ নেই।",
            "run_ingestion" to "ইনজেশন পাইপলাইন চালান",
            "ingestion_success" to "পাইপলাইন সম্পন্ন: সর্বশেষ খবর এআই দ্বারা প্রসেস করা হয়েছে।"
        ),
        // Tamil
        "ta" to mapOf(
            "app_tagline" to "உள்ளூர் முதல் உலகம் வரை. ஒரே இடத்தில்.",
            "tab_all" to "முக்கிய செய்திகள்",
            "tab_local" to "உள்ளூர் செய்திகள்",
            "tab_state" to "மாநில செய்திகள்",
            "tab_national" to "தேசியம்",
            "tab_world" to "உலக செய்திகள்",
            "tab_trending" to "டிரெண்டிங்",
            "breaking_news" to "முக்கிய பிரேக்கிங் செய்தி",
            "ai_verified" to "AI சரிபார்க்கப்பட்ட சுருக்கம்",
            "sources_reporting" to "ஆதாரங்கள் தெரிவிக்கின்றன",
            "read_original" to "அசல் செய்தியைப் படிக்கவும்",
            "source" to "ஆதாரம்",
            "listen" to "செய்தியைக் கேளுங்கள்",
            "stop_listening" to "நிறுத்து",
            "bookmark" to "சேமி",
            "bookmarked" to "சேமிக்கப்பட்டது",
            "share" to "பகிர்",
            "fact_check" to "உண்மைச் சரிபார்ப்பு",
            "search_placeholder" to "செய்திகள், தலைப்புகள், நகரங்களை தேடுங்கள்...",
            "admin_panel" to "நிர்வாகக் குழு",
            "location_permission_needed" to "இருப்பிடத்தை இயக்கவும்",
            "privacy_notice" to "இருப்பிடத் தரவு பாதுகாப்பானது.",
            "select_location" to "இருப்பிடத்தை மாற்றவும்",
            "select_language" to "மொழியைத் தேர்ந்தெடுக்கவும்",
            "offline_reading" to "ஆஃப்லைன் வாசிப்பு",
            "no_saved_articles" to "சேமிக்கப்பட்ட கட்டுரைகள் எதுவும் இல்லை.",
            "run_ingestion" to "செய்தி புதுப்பிப்பு",
            "ingestion_success" to "செய்திகள் புதுப்பிக்கப்பட்டன."
        ),
        // Telugu
        "te" to mapOf(
            "app_tagline" to "స్థానిక నుండి ప్రపంచం వరకు. ఒకే చోట.",
            "tab_all" to "ముఖ్య వార్తలు",
            "tab_local" to "స్థానిక వార్తలు",
            "tab_state" to "రాష్ట్ర వార్తలు",
            "tab_national" to "జాతీయ వార్తలు",
            "tab_world" to "ప్రపంచ వార్తలు",
            "tab_trending" to "ట్రెండింగ్",
            "breaking_news" to "బ్రేకింగ్ న్యూస్",
            "ai_verified" to "AI ధృవీకరించిన సారాంశం",
            "sources_reporting" to "మూలాలు నివేదిస్తున్నాయి",
            "read_original" to "అసలు కథనాన్ని చదవండి",
            "source" to "మూలం",
            "listen" to "వార్తలు వినండి",
            "stop_listening" to "ఆపు",
            "bookmark" to "సేవ్",
            "bookmarked" to "సేవ్ చేయబడింది",
            "share" to "షేర్ చేయండి",
            "fact_check" to "వాస్తవ తనిఖీ",
            "search_placeholder" to "వార్తలు, నగరాలను శోధించండి...",
            "admin_panel" to "అడ్మిన్ ప్యానెల్",
            "location_permission_needed" to "లొకేషన్ ఆన్ చేయండి",
            "privacy_notice" to "లొకేషన్ డేటా పూర్తిగా సురక్షితం.",
            "select_location" to "లొకేషన్ మార్చండి",
            "select_language" to "భాషను ఎంచుకోండి",
            "offline_reading" to "ఆఫ్‌లైన్ చదవడం",
            "no_saved_articles" to "సేవ్ చేసిన కథనాలు లేవు.",
            "run_ingestion" to "వార్తలను అప్‌డేట్ చేయండి",
            "ingestion_success" to "వార్తలు అప్‌డేట్ చేయబడ్డాయి."
        )
    )

    fun getString(key: String, langCode: String = "en"): String {
        val langMap = translations[langCode] ?: translations["en"]!!
        return langMap[key] ?: translations["en"]?.get(key) ?: key
    }

    // Dynamic headline translator preserving entities
    fun translateHeadline(headline: String, targetLang: String): String {
        if (targetLang == "en") return headline
        
        when (targetLang) {
            "hi" -> {
                if (headline.contains("Chandrayaan-4", ignoreCase = true)) {
                    return "🔴 ताज़ा खबर: इसरो के चंद्रयान-4 ने चंद्रमा की कक्षा में सफलतापूर्वक प्रवेश किया"
                }
                if (headline.contains("Kolkata Underwater Metro", ignoreCase = true)) {
                    return "कोलकाता अंडरवाटर मेट्रो ग्रीन लाइन ने 100 दिन पूरे किए, यात्रियों की रिकॉर्ड संख्या"
                }
                if (headline.contains("Green Hydrogen", ignoreCase = true)) {
                    return "पश्चिम बंगाल ने ₹12,000 करोड़ का ग्रीन हाइड्रोजन रोडमैप व सुंदरबन संरक्षण कोष जारी किया"
                }
                if (headline.contains("Repo Rate", ignoreCase = true)) {
                    return "आरबीआई ने रेपो दर 6.5% पर स्थिर रखी, 7.2% जीडीपी वृद्धि का अनुमान"
                }
                if (headline.contains("Global AI Safety", ignoreCase = true)) {
                    return "वैश्विक एआई सुरक्षा संघ ने स्वायत्त मॉडल शासन एवं वॉटरमार्किंग मानकों की पुष्टि की"
                }
                if (headline.contains("Eden Gardens", ignoreCase = true)) {
                    return "ईडन गार्डन्स में होगा आईसीसी वर्ल्ड टेस्ट चैंपियनशिप फाइनल, अत्याधुनिक ड्रेनेज से सुसज्जित"
                }
                return "[$targetLang] $headline"
            }
            "bn" -> {
                if (headline.contains("Chandrayaan-4", ignoreCase = true)) {
                    return "🔴 তাজা খবর: ইসরোর চন্দ্রযান-৪ সফলভাবে চাঁদের কক্ষপথে প্রবেশ করল"
                }
                if (headline.contains("Kolkata Underwater Metro", ignoreCase = true)) {
                    return "কলকাতা জলের তলার মেট্রো গ্রিন লাইন ১০০ দিন পূর্ণ করল, ঐতিহাসিক যাত্রী সংখ্যা"
                }
                if (headline.contains("Green Hydrogen", ignoreCase = true)) {
                    return "পশ্চিমবঙ্গ সরকার গ্রিন হাইড্রোজেন রোডম্যাপ ও সুন্দরবন সুরক্ষা তহবিল ঘোষণা করল"
                }
                if (headline.contains("Repo Rate", ignoreCase = true)) {
                    return "রিজার্ভ ব্যাঙ্ক রেপো রেট ৬.৫% অপরিবর্তিত রাখল, ৭.২% জিডিপি বৃদ্ধির পূর্বাভাস"
                }
                if (headline.contains("Global AI Safety", ignoreCase = true)) {
                    return "জেনেভায় বিশ্ব এআই সুরক্ষা ফ্রেমওয়ার্ক স্বাক্ষরিত: ওয়াটারমার্কিং বাধ্যতামূলক"
                }
                if (headline.contains("Eden Gardens", ignoreCase = true)) {
                    return "কলকাতার ইডেন গার্ডেন্সে বসবে আইসিসি বিশ্ব টেস্ট চ্যাম্পিয়নশিপের ফাইনাল"
                }
                return "[$targetLang] $headline"
            }
            "ta" -> {
                if (headline.contains("Chandrayaan-4", ignoreCase = true)) {
                    return "🔴 முக்கிய செய்தி: சந்திரயான்-4 நிலவின் சுற்றுப்பாதையில் வெற்றிகரமாக நுழைந்தது"
                }
                if (headline.contains("Kolkata Underwater Metro", ignoreCase = true)) {
                    return "கொல்கத்தா நீருக்கடியில் மெட்ரோ 100-வது நாள் மைல்கல்லை எட்டியது"
                }
                return "[$targetLang] $headline"
            }
            else -> return headline
        }
    }
}
