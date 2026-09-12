package com.example.data.seed

import com.example.data.model.Article
import com.example.data.model.ArticleSourceRef
import com.example.data.model.LocationTier
import com.example.data.model.NewsSource

object InitialDataSeed {

    val sources: List<NewsSource> = listOf(
        NewsSource(
            id = "src_pib",
            name = "Press Information Bureau (PIB)",
            websiteUrl = "https://pib.gov.in",
            feedUrl = "https://pib.gov.in/RssMain.aspx?ModId=6",
            licenseStatus = "Government Open Data / Public Domain",
            category = "Politics",
            country = "India",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_wb_gov",
            name = "West Bengal Information & Cultural Affairs",
            websiteUrl = "https://wb.gov.in",
            feedUrl = "https://wb.gov.in/portal/web/guest/news",
            licenseStatus = "State Public Domain Notice",
            category = "Local",
            country = "India",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_kolkata_pulse",
            name = "Kolkata Civic & Urban Wire",
            websiteUrl = "https://kmcgov.in",
            feedUrl = "https://kmcgov.in/KMCPortal/HomeNewsRss",
            licenseStatus = "Municipal Open Data",
            category = "Local",
            country = "India",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_reuters_open",
            name = "Reuters International Wire",
            websiteUrl = "https://www.reuters.com",
            feedUrl = "https://feeds.reuters.com/reuters/worldNews",
            licenseStatus = "Licensed Wire Syndication",
            category = "World",
            country = "Global",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_bbc_open",
            name = "BBC Global Feed",
            websiteUrl = "https://www.bbc.com/news",
            feedUrl = "https://feeds.bbci.co.uk/news/world/rss.xml",
            licenseStatus = "Open RSS Attribution License",
            category = "World",
            country = "Global",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_techcrunch_open",
            name = "TechCrunch Public RSS",
            websiteUrl = "https://techcrunch.com",
            feedUrl = "https://techcrunch.com/feed/",
            licenseStatus = "Open RSS Syndication",
            category = "Technology",
            country = "Global",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_isro_public",
            name = "ISRO Space Releases",
            websiteUrl = "https://isro.gov.in",
            feedUrl = "https://isro.gov.in/rss.xml",
            licenseStatus = "Space Agency Public Release",
            category = "Science",
            country = "India",
            language = "en",
            isActive = true
        ),
        NewsSource(
            id = "src_reserve_bank",
            name = "Reserve Bank of India Communications",
            websiteUrl = "https://rbi.org.in",
            feedUrl = "https://rbi.org.in/rss/pressreleases.xml",
            licenseStatus = "Central Bank Public Release",
            category = "Finance",
            country = "India",
            language = "en",
            isActive = true
        )
    )

    val articles: List<Article> = listOf(
        // 1. LOCAL (Kolkata City News)
        Article(
            id = "art_local_kolkata_1",
            title = "Kolkata Underwater Metro Green Line Reaches 100-Day Milestone with Record Ridership",
            slug = "local/india/west-bengal/kolkata/underwater-metro-100-days",
            summary = "The Kolkata Metro underwater corridor connecting Howrah Maidan to Esplanade has crossed 7.5 million passengers in its first 100 days of commercial operation, reducing trans-river commute times to 45 seconds.",
            fullContent = """
                The underwater stretch of Kolkata's East-West Metro corridor (Green Line) beneath the Hooghly River has achieved an operational milestone, completing 100 consecutive days of passenger service with zero safety anomalies.
                
                Civic authorities and Kolkata Metro Rail Corporation (KMRC) reported that over 7.5 million commuters have traversed the subterranean river tunnel since inaugural ceremonies. The 520-meter tunnel segment, excavated 32 meters beneath the riverbed, links the historic transport hubs of Howrah and Sealdah with downtown Esplanade.
                
                The corridor has alleviated severe traffic bottlenecks across the Howrah Bridge and Vidyasagar Setu, cutting peak-hour transit durations between Howrah and Central Kolkata from 45 minutes by road to under 12 minutes by rail.
                
                Environmental audits released by the West Bengal Pollution Control Board also noted an estimated 14% localized dip in carbon emissions around the Strand Road and BBD Bagh corridors due to modal shift toward the electric rail system.
            """.trimIndent(),
            category = "Local",
            importanceScore = 8,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Cross-verified with Metro Railway Kolkata official statement & West Bengal Department of Transport bulletin.",
            publishedAt = System.currentTimeMillis() - (2 * 3600 * 1000L),
            updatedAt = System.currentTimeMillis() - (1 * 3600 * 1000L),
            country = "India",
            state = "West Bengal",
            district = "Kolkata",
            city = "Kolkata",
            locationTier = LocationTier.LOCAL,
            primarySourceName = "Kolkata Civic & Urban Wire",
            primarySourceUrl = "https://kmcgov.in",
            originalArticleUrl = "https://kmcgov.in/KMCPortal/press/underwater-metro-ridership",
            clusterSources = listOf(
                ArticleSourceRef("Kolkata Civic & Urban Wire", "https://kmcgov.in", "https://kmcgov.in/KMCPortal/press/underwater-metro-ridership", "Municipal Open Data"),
                ArticleSourceRef("Press Information Bureau (PIB)", "https://pib.gov.in", "https://pib.gov.in/PressReleasePage.aspx?PRID=1987452", "Government Open Data"),
                ArticleSourceRef("The Bengal Chronicle Wire", "https://wb.gov.in", "https://wb.gov.in/metro-record-2026", "Public License")
            ),
            imageUrl = "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Kolkata Metro Green Line train arriving at Esplanade underground interchange.",
            tags = listOf("Kolkata", "Metro", "Infrastructure", "Public Transit", "Howrah"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 3,
            viewsCount = 1420
        ),

        // 2. LOCAL (Kolkata Tech / Jobs)
        Article(
            id = "art_local_kolkata_2",
            title = "Silicon Valley of the East: Salt Lake Sector V & New Town Announce 15,000 AI Engineering Openings",
            slug = "local/india/west-bengal/kolkata/sector-v-newtown-ai-jobs",
            summary = "A consortium of IT majors and homegrown semiconductor startups at Kolkata's Salt Lake Sector V and New Town tech corridors announced an expansion drive for 15,000 artificial intelligence and chip architecture positions.",
            fullContent = """
                Salt Lake Sector V and New Town—the premier technology clusters of Kolkata—are witnessing a surge in hiring for specialized technical roles. The Bengal Silicon Valley Tech Park announced that four multinational enterprise software firms and three domestic deep-tech incubators will open engineering centers by Q4.
                
                The new facilities will recruit across machine learning inference, enterprise cloud security, full-stack software architecture, and VLSI chip verification. Jadavpur University and IIEST Shibpur announced joint curriculum tracks with the tech park to upskill engineering graduates.
                
                Local authorities have simultaneously rolled out additional eco-friendly electric bus routes and dedicated cycle tracks connecting Rajarhat and Sector V to ensure seamless transit for tech campus employees.
            """.trimIndent(),
            category = "Jobs & Careers",
            importanceScore = 7,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Confirmed via Bengal Chamber of Commerce and IT & Electronics Department filings.",
            publishedAt = System.currentTimeMillis() - (5 * 3600 * 1000L),
            country = "India",
            state = "West Bengal",
            district = "North 24 Parganas / Kolkata",
            city = "Kolkata",
            locationTier = LocationTier.LOCAL,
            primarySourceName = "West Bengal Information & Cultural Affairs",
            primarySourceUrl = "https://wb.gov.in",
            originalArticleUrl = "https://wb.gov.in/portal/web/guest/news/-/asset_publisher/ai-jobs-announcement",
            clusterSources = listOf(
                ArticleSourceRef("West Bengal Information Dept", "https://wb.gov.in", "https://wb.gov.in/portal/web/guest/news/-/asset_publisher/ai-jobs-announcement", "State Public Domain"),
                ArticleSourceRef("TechBengal News Feed", "https://kmcgov.in", "https://kmcgov.in/tech-corridor-jobs", "Open RSS")
            ),
            imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800&auto=format&fit=crop&q=80",
            imageCaption = "New Town technology park campus in Kolkata.",
            tags = listOf("Kolkata", "Jobs", "Technology", "AI", "New Town"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 4,
            viewsCount = 980
        ),

        // 3. STATE (West Bengal News)
        Article(
            id = "art_state_wb_1",
            title = "West Bengal Unveils Comprehensive Green Hydrogen Roadmap & Sunderbans Biosphere Protection Fund",
            slug = "state/india/west-bengal/green-hydrogen-sunderbans-plan",
            summary = "The Government of West Bengal has ratified a ₹12,000-crore clean energy deployment package focused on green hydrogen hubs in Durgapur-Haldia and enhanced mangrove restoration barriers in the fragile Sunderbans delta.",
            fullContent = """
                State ministers today approved the West Bengal Clean Energy & Coastal Resilience Charter, committing substantial state allocations paired with international climate finance to double renewable capacity.
                
                Key highlights include:
                1. Dual Green Hydrogen Production Facilities situated along the industrial belts of Durgapur and the Haldia port complex to supply decarbonized feedstock to steel and chemical manufacturers.
                2. Sunderbans Mangrove Shielding: A permanent fund dedicated to bio-shield creation with saline-resistant mangrove varieties to safeguard coastal communities from cyclonic storm surges.
                3. Solar Rooftop Incentives for state universities, municipal hospitals, and agricultural cold chains across Burdwan and Murshidabad districts.
                
                The Chief Secretary emphasized that the roadmap balances industrial manufacturing modernization with urgent biodiversity safeguards.
            """.trimIndent(),
            category = "Environment",
            importanceScore = 8,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "State Cabinet Resolution No. 42/2026 documented and verified.",
            publishedAt = System.currentTimeMillis() - (8 * 3600 * 1000L),
            country = "India",
            state = "West Bengal",
            district = "Statewide",
            city = "Kolkata",
            locationTier = LocationTier.STATE,
            primarySourceName = "West Bengal Information & Cultural Affairs",
            primarySourceUrl = "https://wb.gov.in",
            originalArticleUrl = "https://wb.gov.in/portal/cabinet-resolutions/green-hydrogen-charter",
            clusterSources = listOf(
                ArticleSourceRef("West Bengal Information Dept", "https://wb.gov.in", "https://wb.gov.in/portal/cabinet-resolutions/green-hydrogen-charter", "State Public Domain"),
                ArticleSourceRef("Press Information Bureau (PIB)", "https://pib.gov.in", "https://pib.gov.in/PressReleasePage.aspx?PRID=1998311", "Government Open Data")
            ),
            imageUrl = "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Renewable energy infrastructure and coastal environmental conservation.",
            tags = listOf("West Bengal", "Green Energy", "Sunderbans", "Environment", "Climate"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 4,
            viewsCount = 2100
        ),

        // 4. NATIONAL (India Breaking News / Space)
        Article(
            id = "art_nat_india_1",
            title = "🔴 BREAKING: ISRO Successfully Inserts Chandrayaan-4 Lunar Sample Return Craft into Lunar Orbit",
            slug = "national/india/isro-chandrayaan-4-lunar-orbit-success",
            summary = "ISRO's mission control at Bengaluru confirmed that Chandrayaan-4 achieved precise lunar orbit insertion at 11:42 IST. The mission aims to harvest core samples from the Moon's South Pole and return them safely to Earth.",
            fullContent = """
                The Indian Space Research Organisation (ISRO) has achieved another milestone in interplanetary exploration as the Chandrayaan-4 composite stack successfully completed its critical Lunar Orbit Insertion (LOI) burn.
                
                The liquid apogee propulsion engine fired for 1,142 seconds, decelerating the spacecraft to enter an elliptical lunar trajectory of 164 km x 18,074 km, as confirmed by telemetry data tracked from the Deep Space Network station in Byalalu.
                
                Chandrayaan-4 carries a sophisticated robotic drilling arm designed to collect 2-meter deep permafrost and regolith cores near the rim of the Shackleton crater. The return module is scheduled to dock in lunar transfer orbit before setting trajectory back toward Earth later this year.
                
                Prime Minister and international space agency chiefs congratulated the scientific teams at ISRO Telemetry, Tracking and Command Network (ISTRAC).
            """.trimIndent(),
            category = "Science",
            importanceScore = 10,
            isBreaking = true,
            isFactChecked = true,
            factCheckNotes = "Directly verified from ISRO Mission Telemetry Dispatch & PIB National Bulletin.",
            publishedAt = System.currentTimeMillis() - (35 * 60 * 1000L), // 35 min ago
            updatedAt = System.currentTimeMillis() - (10 * 60 * 1000L),
            country = "India",
            state = "National",
            district = "Central",
            city = "New Delhi",
            locationTier = LocationTier.NATIONAL,
            primarySourceName = "ISRO Space Releases",
            primarySourceUrl = "https://isro.gov.in",
            originalArticleUrl = "https://isro.gov.in/Chandrayaan4_LOI_Successful.html",
            clusterSources = listOf(
                ArticleSourceRef("ISRO Space Releases", "https://isro.gov.in", "https://isro.gov.in/Chandrayaan4_LOI_Successful.html", "Space Agency Public Domain"),
                ArticleSourceRef("Press Information Bureau (PIB)", "https://pib.gov.in", "https://pib.gov.in/PressReleasePage.aspx?PRID=2001144", "Government Open Data"),
                ArticleSourceRef("Reuters International Wire", "https://www.reuters.com", "https://www.reuters.com/science/india-isro-chandrayaan-4-moon-orbit", "Licensed Wire")
            ),
            imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Artist rendering of lunar orbital insertion maneuvers.",
            tags = listOf("ISRO", "Chandrayaan", "Space", "Science", "Breaking News", "India"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 4,
            viewsCount = 8900
        ),

        // 5. NATIONAL (India Finance / Economy)
        Article(
            id = "art_nat_india_2",
            title = "RBI Keeps Policy Repo Rate Steady at 6.5%, Forecasts Robust 7.2% GDP Growth with Tamed Inflation",
            slug = "national/india/rbi-monetary-policy-repo-rate-gdp-forecast",
            summary = "The Reserve Bank of India's Monetary Policy Committee unanimously held the benchmark repo rate at 6.50% while projecting real GDP expansion of 7.2% for the current fiscal year backed by rural consumption and capital investment.",
            fullContent = """
                The Reserve Bank of India (RBI) Governor unveiled the bi-monthly monetary policy decision following the MPC deliberations, maintaining a stance of 'withdrawal of accommodation' while ensuring liquidity stability in the financial system.
                
                Headline CPI inflation for the fourth consecutive quarter has remained aligned within the RBI's target tolerance band of 4% (+/- 2%), supported by softening foodgrain supply dynamics and balanced crude oil prices.
                
                Domestic manufacturing indicators and gross goods and services tax (GST) receipts continue to demonstrate resilient growth trajectories. The Governor highlighted that India's digital public infrastructure, particularly UPI cross-border linkages, is expanding foreign remittances and trade settlement efficiency.
            """.trimIndent(),
            category = "Finance",
            importanceScore = 7,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Derived from RBI Governor statement and MPC resolution document.",
            publishedAt = System.currentTimeMillis() - (6 * 3600 * 1000L),
            country = "India",
            state = "Maharashtra",
            district = "Mumbai",
            city = "Mumbai",
            locationTier = LocationTier.NATIONAL,
            primarySourceName = "Reserve Bank of India Communications",
            primarySourceUrl = "https://rbi.org.in",
            originalArticleUrl = "https://rbi.org.in/Scripts/BS_PressReleaseDisplay.aspx?prid=57412",
            clusterSources = listOf(
                ArticleSourceRef("Reserve Bank of India", "https://rbi.org.in", "https://rbi.org.in/Scripts/BS_PressReleaseDisplay.aspx?prid=57412", "Central Bank Public Release"),
                ArticleSourceRef("Press Information Bureau (PIB)", "https://pib.gov.in", "https://pib.gov.in/PressReleasePage.aspx?PRID=1999201", "Government Open Data"),
                ArticleSourceRef("Reuters Finance Wire", "https://www.reuters.com", "https://www.reuters.com/markets/india-rbi-holds-repo-rate-gdp", "Licensed Wire")
            ),
            imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Stock and currency trading indices reflecting stable financial policy indicators.",
            tags = listOf("RBI", "Economy", "Finance", "GDP", "Inflation", "Banking"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 3,
            viewsCount = 3450
        ),

        // 6. WORLD (Global AI Summit / Technology)
        Article(
            id = "art_world_tech_1",
            title = "Global AI Safety Consortium Ratifies Open Standards for Autonomous Model Governance and Watermarking",
            slug = "world/global/international-ai-safety-standards-ratified",
            summary = "Envoys and chief scientists from 42 nations reached a landmark consensus in Geneva establishing unified open verification benchmarks, provenance watermarks, and catastrophic risk red-teaming standards for frontier AI models.",
            fullContent = """
                A multinational summit on Artificial Intelligence Governance concluded in Geneva today with the formal signing of the Global AI Safety Framework Accord.
                
                The treaty establishes three binding evaluation pillars:
                1. Mandatory Cryptographic Watermarking: All generative multimedia—including synthetic audio, hyperrealistic video, and news-mimicking copy—must embed tamper-resistant cryptographic signatures identifying the generating engine and training provenance.
                2. Autonomous Capability Circuit-Breakers: Frontier models displaying emergent cyber-offensive replication or chemical-biological weapon synthesis vectors must be subjected to sandboxed multi-party red teaming.
                3. Open Scientific Auditing: Sovereign AI institutes will share anonymized vulnerability datasets to prevent monopolistic safety silos.
                
                Technology research leads underscored that these protocols protect journalism integrity and combat automated deepfake disinformation campaigns while accelerating constructive medical and scientific breakthroughs.
            """.trimIndent(),
            category = "Technology",
            importanceScore = 9,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Signed treaty communiques cross-checked with UN ITU and participating sovereign delegations.",
            publishedAt = System.currentTimeMillis() - (4 * 3600 * 1000L),
            country = "Switzerland",
            state = "Geneva",
            district = "International",
            city = "Geneva",
            locationTier = LocationTier.WORLD,
            primarySourceName = "Reuters International Wire",
            primarySourceUrl = "https://www.reuters.com",
            originalArticleUrl = "https://www.reuters.com/technology/global-ai-treaty-geneva-watermarking-safety",
            clusterSources = listOf(
                ArticleSourceRef("Reuters International Wire", "https://www.reuters.com", "https://www.reuters.com/technology/global-ai-treaty-geneva-watermarking-safety", "Licensed Wire"),
                ArticleSourceRef("BBC Global Feed", "https://www.bbc.com/news", "https://www.bbc.com/news/world-technology-681923", "Open RSS Attribution"),
                ArticleSourceRef("TechCrunch Public RSS", "https://techcrunch.com", "https://techcrunch.com/geneva-ai-governance-pact", "Open RSS")
            ),
            imageUrl = "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Global technology and ethics conference hall during consensus voting.",
            tags = listOf("Artificial Intelligence", "Technology", "Governance", "World", "Safety"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 5,
            viewsCount = 5120
        ),

        // 7. WORLD (Health / Science)
        Article(
            id = "art_world_health_1",
            title = "WHO Certifies Universal mRNA Tuberculosis Vaccine Candidate Following Phase 3 Clinical Triumphs",
            slug = "world/health/who-tb-vaccine-phase3-success",
            summary = "World Health Organization medical panels issued emergency approval for a next-generation mRNA tuberculosis vaccine demonstrating 88% long-term efficacy across adult trial cohorts spanning 18 countries.",
            fullContent = """
                In a monumental stride for global epidemiology, the World Health Organization (WHO) and international research partners celebrated the successful completion of multinational Phase 3 clinical evaluations for the M-72/AS01 mRNA tuberculosis vaccine.
                
                Tuberculosis, which has claimed over 1.3 million lives annually primarily across developing nations, has historically lacked an effective adult booster. The clinical trial, covering 26,000 subjects over a 36-month follow-up window, demonstrated an unprecedented 88.4% protection rate against pulmonary progression.
                
                Global health consortiums and philanthropic partners announced royalty-free patent licensing agreements to permit generic mass-manufacturing by regional vaccine institutes across India, South Africa, and Brazil, with first batch rollouts slated for early 2027.
            """.trimIndent(),
            category = "Health",
            importanceScore = 8,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Verified via WHO press briefing release and peer-reviewed medical trial disclosures.",
            publishedAt = System.currentTimeMillis() - (12 * 3600 * 1000L),
            country = "Global",
            state = "Global",
            district = "World",
            city = "Geneva",
            locationTier = LocationTier.WORLD,
            primarySourceName = "BBC Global Feed",
            primarySourceUrl = "https://www.bbc.com/news",
            originalArticleUrl = "https://www.bbc.com/news/health-world-tb-vaccine-triumph",
            clusterSources = listOf(
                ArticleSourceRef("BBC Global Feed", "https://www.bbc.com/news", "https://www.bbc.com/news/health-world-tb-vaccine-triumph", "Open RSS Attribution"),
                ArticleSourceRef("Reuters International Wire", "https://www.reuters.com", "https://www.reuters.com/business/healthcare-pharmaceuticals/who-tb-mrna-vaccine-clearance", "Licensed Wire")
            ),
            imageUrl = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Scientific researchers validating immunology test assays in certified laboratory.",
            tags = listOf("Health", "Medicine", "Vaccine", "Science", "WHO"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 4,
            viewsCount = 4210
        ),

        // 8. SPORTS (Cricket / World News)
        Article(
            id = "art_sports_cricket_1",
            title = "Eden Gardens to Host ICC World Test Championship Final with Revolutionary Smart Drainage System",
            slug = "sports/cricket/kolkata-eden-gardens-wtc-final-announced",
            summary = "Kolkata's legendary Eden Gardens stadium has been selected by the International Cricket Council to stage the ICC World Test Championship Final, equipped with a vacuum sub-surface drainage system capable of restarting play 15 minutes post-torrential downpour.",
            fullContent = """
                The Board of Control for Cricket in India (BCCI) and the ICC officially declared Kolkata's iconic Eden Gardens as the venue for the prestigious World Test Championship final.
                
                The Cricket Association of Bengal (CAB) concluded comprehensive venue modernization, including:
                - SubAir Automated Evacuation Drainage: Underground suction pumps capable of clearing 200,000 liters of rainwater per minute from the turf surface.
                - Advanced LED Sports Broadcast Lighting meeting 4K high-speed super-slow-motion imaging requirements.
                - Expanded hospitality stands and barrier-free wheelchair accessible concourses throughout all tier enclosures.
                
                Cricket captains and sports historians lauded the historic selection, bringing the pinnacle of Test cricket to the historic 68,000-capacity amphitheater on the banks of the Hooghly.
            """.trimIndent(),
            category = "Sports",
            importanceScore = 7,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Joint media dispatch from ICC Dubai Headquarters & CAB Secretary.",
            publishedAt = System.currentTimeMillis() - (7 * 3600 * 1000L),
            country = "India",
            state = "West Bengal",
            district = "Kolkata",
            city = "Kolkata",
            locationTier = LocationTier.LOCAL,
            primarySourceName = "Kolkata Civic & Urban Wire",
            primarySourceUrl = "https://kmcgov.in",
            originalArticleUrl = "https://kmcgov.in/sports/eden-gardens-wtc-announcement",
            clusterSources = listOf(
                ArticleSourceRef("Kolkata Civic & Urban Wire", "https://kmcgov.in", "https://kmcgov.in/sports/eden-gardens-wtc-announcement", "Municipal Open Data"),
                ArticleSourceRef("Reuters Sports Wire", "https://www.reuters.com", "https://www.reuters.com/lifestyle/sports/icc-eden-gardens-test-final", "Licensed Wire")
            ),
            imageUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Night-lit cricket stadium arena prepared for international test matches.",
            tags = listOf("Cricket", "Sports", "Eden Gardens", "Kolkata", "WTC Final"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 3,
            viewsCount = 6720
        ),

        // 9. ACCIDENTS / EMERGENCIES (Sensitive story demonstration with proper neutral attribution)
        Article(
            id = "art_emergency_1",
            title = "Prompt Disaster Response Averts Major Damage in Hooghly Industrial Gas Leak; Zero Fatalities Confirmed",
            slug = "emergencies/west-bengal/hooghly-industrial-response-safe",
            summary = "National Disaster Response Force (NDRF) and West Bengal Fire Services teams neutralized a localized nitrogen-ammonia pipeline leak at a manufacturing facility in Hooghly district within 90 minutes. Medical superintendents confirmed all 14 precautionary admissions are in stable condition.",
            fullContent = """
                Emergency services mobilized swiftly following an automated sensor alarm indicating a localized pressure rupture along an auxiliary nitrogen transmission line in an industrial estate near Serampore, Hooghly district.
                
                District Magistrate and NDRF Battalion commanders established a 500-meter safety cordon, deploying water-fog neutralizing curtains to dissipate vapors. The state emergency operations room verified that the line was completely valved off by technical crews at 03:20 AM.
                
                Fourteen workers and nearby residents who reported mild eye irritation were evacuated to the Serampore Walsh Sub-divisional Hospital. The Chief Medical Officer of Health confirmed in an afternoon press briefing that all individuals have been medically stabilized with zero fatalities or serious toxicity complications.
                
                An independent technical inspection committee comprising civil engineers and the Directorate of Factories has been constituted to inspect pipeline integrity before factory operations resume.
            """.trimIndent(),
            category = "Accidents / Emergencies",
            importanceScore = 7,
            isBreaking = false,
            isFactChecked = true,
            factCheckNotes = "Directly verified with District Disaster Management Authority Hooghly & Hospital Chief Medical Officer.",
            publishedAt = System.currentTimeMillis() - (10 * 3600 * 1000L),
            country = "India",
            state = "West Bengal",
            district = "Hooghly",
            city = "Serampore",
            locationTier = LocationTier.STATE,
            primarySourceName = "West Bengal Information & Cultural Affairs",
            primarySourceUrl = "https://wb.gov.in",
            originalArticleUrl = "https://wb.gov.in/portal/emergency/hooghly-response-report",
            clusterSources = listOf(
                ArticleSourceRef("West Bengal Disaster Authority", "https://wb.gov.in", "https://wb.gov.in/portal/emergency/hooghly-response-report", "Official Release"),
                ArticleSourceRef("Press Information Bureau (PIB)", "https://pib.gov.in", "https://pib.gov.in/NDRF_Hooghly_Update", "Government Open Data")
            ),
            imageUrl = "https://images.unsplash.com/photo-1582139329536-e7284fece509?w=800&auto=format&fit=crop&q=80",
            imageCaption = "Emergency first responder vehicles deployed on scene during coordinated safety operation.",
            tags = listOf("West Bengal", "Emergency", "Safety", "NDRF", "Health"),
            language = "en",
            verifiedAiSummary = true,
            readTimeMinutes = 3,
            viewsCount = 1890,
            isSensitive = true,
            sensitivityNotice = "Sensitive Report: Source attribution strictly preserved with verified official emergency communications. Allegations and rumors avoided."
        )
    )
}
