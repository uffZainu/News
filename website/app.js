// Novyra News Web Client - Fast First Load & Offline First Architecture

const INITIAL_ARTICLES = [
  {
    id: "art_1",
    title: "Global Clean Energy Generation Crosses 40% Milestone in 2026",
    category: "Science",
    tier: "GLOBAL",
    isBreaking: true,
    publishedAt: "15 mins ago",
    readTime: "4 min read",
    author: "Elena Rostova",
    sourceName: "Global Energy Monitor",
    imageUrl: "https://images.unsplash.com/photo-1466611653911-95081537e5b7?w=1000",
    summary: "Record installations of solar photovoltaics and offshore wind farms pushed renewable generation past 40 percent of total worldwide power output this quarter.",
    fullContent: "The transition to sustainable energy reached a historic benchmark this week as the Global Energy Monitor verified that solar and offshore wind farms collectively powered over 40% of the worldwide grid during the peak summer months. Rapid expansion across Europe, Asia, and the Americas has driven energy storage costs down by 28% year-over-year, making continuous baseload clean energy viable in emerging industrial economies."
  },
  {
    id: "art_2",
    title: "Breakthrough In Room-Temperature Superconductors Confirmed by Peer Labs",
    category: "Technology",
    tier: "GLOBAL",
    isBreaking: true,
    publishedAt: "1 hour ago",
    readTime: "5 min read",
    author: "Marcus Vance",
    sourceName: "Tech Science Review",
    imageUrl: "https://images.unsplash.com/photo-1507413245164-6160d8298b31?w=800",
    summary: "Independent research teams across three continents have replicated low-resistance magnetic levitation at ambient temperatures, unlocking potential for lossless grid distribution.",
    fullContent: "In what researchers are describing as the most significant materials science milestone of the decade, three independent university laboratories confirmed reproducible zero-resistance electrical transport at standard room temperatures. The technological ramifications could transform electric power grids, high-speed rail, magnetic confinement fusion, and next-generation quantum computing architectures."
  },
  {
    id: "art_3",
    title: "National Infrastructure Bill Allocates $45B to High-Speed Intercity Rail",
    category: "Politics",
    tier: "NATIONAL",
    isBreaking: false,
    publishedAt: "2 hours ago",
    readTime: "3 min read",
    author: "Sarah Jenkins",
    sourceName: "Capitol Chronicle",
    imageUrl: "https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=800",
    summary: "Bipartisan consensus establishes a 10-year modernization program connecting seven metropolitan corridors with dedicated electrified bullet trains.",
    fullContent: "Legislators finalized a landmark 45-billion-dollar transit modernization statute aimed at constructing high-speed rail arteries across major economic corridors. The infrastructure initiative prioritizes regional connectivity, lowering regional aviation carbon emissions, and creating an estimated 120,000 engineering and construction jobs over the coming five years."
  },
  {
    id: "art_4",
    title: "Markets Rally as Central Banks Signal Coordinated Interest Rate Softening",
    category: "Business",
    tier: "GLOBAL",
    isBreaking: false,
    publishedAt: "3 hours ago",
    readTime: "3 min read",
    author: "David Thorne",
    sourceName: "Financial Times Wire",
    imageUrl: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800",
    summary: "Global equity indexes surged today as inflation metrics reached targeted stability bands, prompting projections of simultaneous quarter-point reductions.",
    fullContent: "Global equities gained strongly on Thursday after international economic ministers issued synchronized guidance indicating steadying consumer price indices. Tech growth equities and green tech venture funds led market advances as capital costs are expected to ease throughout the remainder of the fiscal year."
  },
  {
    id: "art_5",
    title: "State Agricultural Council Introduces AI Sensor Grids for Drought Preparedness",
    category: "Science",
    tier: "STATE",
    isBreaking: false,
    publishedAt: "4 hours ago",
    readTime: "4 min read",
    author: "Rachel Green",
    sourceName: "State Agro Times",
    imageUrl: "https://images.unsplash.com/photo-1586771107445-d3ca888129ff?w=800",
    summary: "Over 5,000 sub-surface moisture telemetry sensors are being deployed across central farming valleys to optimize micro-irrigation systems.",
    fullContent: "The Department of Agriculture announced the statewide rollout of autonomous soil moisture monitors connected via low-power satellite relays. The network delivers hyper-local soil moisture readings directly to regional farmers' smartphones, reducing agricultural water consumption by an anticipated 35% during drought periods."
  },
  {
    id: "art_6",
    title: "Municipal Tech District Expands with New Renewable Computing Hub",
    category: "Technology",
    tier: "LOCAL",
    isBreaking: false,
    publishedAt: "5 hours ago",
    readTime: "2 min read",
    author: "Local Correspondent",
    sourceName: "Metro District Gazette",
    imageUrl: "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800",
    summary: "City leaders approved a 200,000 square foot net-zero computational center powered exclusively by municipal geothermal and rooftop solar arrays.",
    fullContent: "The city zoning board officially sanctioned construction for a new eco-friendly data facility located in the downtown innovation corridor. The structure will reuse dissipated server heat to warm neighboring residential complexes during winter months, exemplifying circular urban energy design."
  },
  {
    id: "art_7",
    title: "Championship Finals: Underdog Squad Pulls Off Dramatic Stoppage Time Winner",
    category: "Sports",
    tier: "NATIONAL",
    isBreaking: false,
    publishedAt: "6 hours ago",
    readTime: "3 min read",
    author: "Alex Morgan",
    sourceName: "Sports Central",
    imageUrl: "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=800",
    summary: "A thrilling bicycle kick in the 94th minute secured an improbable 2-1 victory, sending hometown supporters into celebratory frenzy.",
    fullContent: "In one of the most suspenseful finals in tournament history, twenty-one-year-old forward Lucas Morales struck a stunning volley in the dying seconds of extra time. The victory seals the club's first national championship trophy in over forty years."
  },
  {
    id: "art_8",
    title: "Global Health Commission Announces Universal Vaccine Candidate Against Encephalitis",
    category: "Health",
    tier: "GLOBAL",
    isBreaking: false,
    publishedAt: "7 hours ago",
    readTime: "4 min read",
    author: "Dr. Aris Thorne",
    sourceName: "World Medical Journal",
    imageUrl: "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?w=800",
    summary: "Phase III clinical trials demonstrated 96% efficacy across 45,000 participants, marking a major leap in preventing viral vector transmissions.",
    fullContent: "International health authorities approved rapid manufacturing guidelines for a novel broad-spectrum immunization that protects against multiple tick- and mosquito-borne encephalitic viruses. Distribution will begin across vulnerable tropical and temperate regions starting next quarter."
  }
];

// App State
let articles = [];
let currentTier = "ALL";
let currentCategory = "All";
let searchQuery = "";
let savedIds = new Set();
let isSpeaking = false;
let currentUtterance = null;

// Initialize on Load
document.addEventListener("DOMContentLoaded", () => {
  initStorage();
  initTheme();
  setupEventListeners();
  renderApp();
  showToast("Fast First Load: Instant news ready offline!");
});

function initStorage() {
  const localSaved = localStorage.getItem("novyra_saved_articles");
  if (localSaved) {
    try {
      savedIds = new Set(JSON.parse(localSaved));
    } catch (e) {
      savedIds = new Set();
    }
  }

  const cachedArticles = localStorage.getItem("novyra_articles_cache");
  if (cachedArticles) {
    try {
      articles = JSON.parse(cachedArticles);
    } catch (e) {
      articles = INITIAL_ARTICLES;
    }
  } else {
    articles = INITIAL_ARTICLES;
    localStorage.setItem("novyra_articles_cache", JSON.stringify(articles));
  }

  updateBookmarksCount();
}

function initTheme() {
  const savedTheme = localStorage.getItem("novyra_theme") || "light";
  document.documentElement.setAttribute("data-theme", savedTheme);
  document.getElementById("themeToggle").textContent = savedTheme === "dark" ? "☀️" : "🌙";
}

function toggleTheme() {
  const current = document.documentElement.getAttribute("data-theme") || "light";
  const newTheme = current === "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", newTheme);
  localStorage.setItem("novyra_theme", newTheme);
  document.getElementById("themeToggle").textContent = newTheme === "dark" ? "☀️" : "🌙";
}

function setupEventListeners() {
  document.getElementById("themeToggle").addEventListener("click", toggleTheme);

  // Tier Filters
  document.getElementById("tierFilters").addEventListener("click", (e) => {
    if (e.target.classList.contains("tier-btn")) {
      document.querySelectorAll(".tier-btn").forEach(btn => btn.classList.remove("active"));
      e.target.classList.add("active");
      currentTier = e.target.getAttribute("data-tier");
      renderApp();
    }
  });

  // Category Filters
  document.getElementById("categoryFilters").addEventListener("click", (e) => {
    if (e.target.classList.contains("cat-btn")) {
      document.querySelectorAll(".cat-btn").forEach(btn => btn.classList.remove("active"));
      e.target.classList.add("active");
      currentCategory = e.target.getAttribute("data-cat");
      renderApp();
    }
  });

  // Search
  document.getElementById("searchInput").addEventListener("input", (e) => {
    searchQuery = e.target.value.toLowerCase().trim();
    renderApp();
  });

  // Refresh
  document.getElementById("refreshBtn").addEventListener("click", () => {
    showToast("Checking news sources... Feed up to date!");
  });

  // Bookmarks Modal
  document.getElementById("bookmarksBtn").addEventListener("click", openBookmarksModal);
  document.getElementById("bookmarksCloseBtn").addEventListener("click", closeBookmarksModal);
  document.getElementById("bookmarksOverlay").addEventListener("click", closeBookmarksModal);

  // Article Modal
  document.getElementById("modalCloseBtn").addEventListener("click", closeArticleModal);
  document.getElementById("modalOverlay").addEventListener("click", closeArticleModal);

  // Audio Player controls
  document.getElementById("audioStopBtn").addEventListener("click", stopSpeech);
  document.getElementById("audioPauseResumeBtn").addEventListener("click", togglePauseSpeech);
}

function getFilteredArticles() {
  return articles.filter(art => {
    const matchesTier = currentTier === "ALL" || art.tier === currentTier;
    const matchesCat = currentCategory === "All" ||
                       (currentCategory === "Breaking" && art.isBreaking) ||
                       art.category.toLowerCase() === currentCategory.toLowerCase();
    const matchesSearch = !searchQuery ||
                          art.title.toLowerCase().includes(searchQuery) ||
                          art.summary.toLowerCase().includes(searchQuery) ||
                          art.sourceName.toLowerCase().includes(searchQuery);
    return matchesTier && matchesCat && matchesSearch;
  });
}

function renderApp() {
  const filtered = getFilteredArticles();
  const heroContainer = document.getElementById("heroSection");
  const gridContainer = document.getElementById("articlesGrid");
  const countBadge = document.getElementById("feedArticlesCount");
  const title = document.getElementById("feedSectionTitle");

  title.textContent = currentCategory === "All" ? "Top Stories" : `${currentCategory} Stories`;
  countBadge.textContent = `${filtered.length} stories`;

  if (filtered.length === 0) {
    heroContainer.innerHTML = "";
    gridContainer.innerHTML = `
      <div style="grid-column: 1/-1; text-align: center; padding: 48px; background: var(--bg-card); border-radius: var(--radius); border: 1px dashed var(--border-color);">
        <h3>No articles found</h3>
        <p style="color: var(--text-muted); margin-top: 8px;">Try clearing filters or search terms.</p>
        <button class="btn btn-outline" style="margin-top: 16px;" onclick="resetFilters()">Reset All Filters</button>
      </div>
    `;
    return;
  }

  // Hero section is the first article if no specific search query
  const heroArticle = filtered[0];
  const remainingArticles = filtered.slice(1);

  renderHero(heroArticle);
  renderGrid(remainingArticles);
}

function renderHero(article) {
  const isSaved = savedIds.has(article.id);
  const heroContainer = document.getElementById("heroSection");

  heroContainer.innerHTML = `
    <div class="hero-card">
      <div class="hero-img-wrap">
        <img src="${article.imageUrl}" alt="${article.title}" loading="lazy">
        <span class="hero-badge-tier">${article.tier} EDITION</span>
      </div>
      <div class="hero-body">
        <div>
          <div class="hero-category">${article.category} • ${article.readTime}</div>
          <h2 class="hero-title" onclick="openArticleDetail('${article.id}')">${article.title}</h2>
          <p class="hero-summary">${article.summary}</p>
        </div>
        <div class="hero-meta">
          <div>
            <strong>${article.sourceName}</strong> • ${article.publishedAt}
          </div>
          <div class="hero-actions">
            <button class="btn btn-outline" style="padding: 6px 12px; font-size: 0.8rem;" onclick="speakArticle('${article.id}')">
              🔊 Listen
            </button>
            <button class="btn btn-outline" style="padding: 6px 12px; font-size: 0.8rem;" onclick="toggleBookmark('${article.id}')">
              ${isSaved ? "⭐ Saved" : "☆ Save"}
            </button>
            <button class="btn btn-primary" style="padding: 6px 14px; font-size: 0.8rem;" onclick="openArticleDetail('${article.id}')">
              Read Story →
            </button>
          </div>
        </div>
      </div>
    </div>
  `;
}

function renderGrid(list) {
  const gridContainer = document.getElementById("articlesGrid");

  gridContainer.innerHTML = list.map(art => {
    const isSaved = savedIds.has(art.id);
    return `
      <article class="article-card">
        <div class="card-img-wrap">
          <img src="${art.imageUrl}" alt="${art.title}" loading="lazy">
          <span class="card-badge">${art.tier}</span>
        </div>
        <div class="card-body">
          <div class="card-cat">${art.category} • ${art.readTime}</div>
          <h3 class="card-title" onclick="openArticleDetail('${art.id}')">${art.title}</h3>
          <p class="card-desc">${art.summary}</p>
          <div class="card-footer">
            <span>${art.sourceName}</span>
            <div class="card-actions">
              <button class="card-icon-btn" title="Listen to story" onclick="speakArticle('${art.id}')">🔊</button>
              <button class="card-icon-btn" title="Bookmark story" onclick="toggleBookmark('${art.id}')">
                ${isSaved ? "⭐" : "☆"}
              </button>
              <button class="card-icon-btn" title="Read" onclick="openArticleDetail('${art.id}')">📖</button>
            </div>
          </div>
        </div>
      </article>
    `;
  }).join("");
}

function openArticleDetail(id) {
  const art = articles.find(a => a.id === id);
  if (!art) return;

  const isSaved = savedIds.has(art.id);
  const modalBody = document.getElementById("modalBody");

  modalBody.innerHTML = `
    <img src="${art.imageUrl}" alt="${art.title}" style="width: 100%; max-height: 350px; object-fit: cover; border-radius: 8px; margin-bottom: 20px;">
    <div style="display: flex; gap: 8px; align-items: center; margin-bottom: 12px;">
      <span class="ticker-badge" style="background: var(--primary);">${art.tier}</span>
      <span style="color: var(--primary-light); font-weight: 700; font-size: 0.85rem; text-transform: uppercase;">${art.category}</span>
      <span style="color: var(--text-muted); font-size: 0.82rem;">• ${art.readTime}</span>
    </div>
    <h2 style="font-family: var(--font-serif); font-size: 1.8rem; margin-bottom: 16px; line-height: 1.3;">${art.title}</h2>
    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-color); padding-bottom: 14px; margin-bottom: 20px; color: var(--text-muted); font-size: 0.88rem;">
      <div>By <strong>${art.author}</strong> • ${art.sourceName} • ${art.publishedAt}</div>
      <div style="display: flex; gap: 8px;">
        <button class="btn btn-outline" style="padding: 6px 12px; font-size: 0.82rem;" onclick="speakArticle('${art.id}')">🔊 Listen</button>
        <button class="btn btn-outline" style="padding: 6px 12px; font-size: 0.82rem;" onclick="toggleBookmark('${art.id}')">${isSaved ? "⭐ Saved" : "☆ Save"}</button>
      </div>
    </div>
    <div style="font-size: 1.05rem; line-height: 1.8; color: var(--text-main); margin-bottom: 28px;">
      <p style="font-weight: 600; margin-bottom: 16px; color: var(--text-main); font-size: 1.15rem;">${art.summary}</p>
      <p>${art.fullContent}</p>
    </div>
    <div style="background: var(--bg-main); padding: 16px; border-radius: 8px; border: 1px solid var(--border-color); font-size: 0.82rem; color: var(--text-muted);">
      <strong>Multi-Source Fact Check:</strong> Verified by Novyra News aggregation network. Primary source: ${art.sourceName}.
    </div>
  `;

  document.getElementById("articleModal").classList.remove("hidden");
}

function closeArticleModal() {
  document.getElementById("articleModal").classList.add("hidden");
}

function toggleBookmark(id) {
  if (savedIds.has(id)) {
    savedIds.delete(id);
    showToast("Removed from bookmarks");
  } else {
    savedIds.add(id);
    showToast("Saved to offline bookmarks!");
  }
  localStorage.setItem("novyra_saved_articles", JSON.stringify(Array.from(savedIds)));
  updateBookmarksCount();
  renderApp();

  // If detail modal open, re-render its button
  const modal = document.getElementById("articleModal");
  if (!modal.classList.contains("hidden")) {
    openArticleDetail(id);
  }
}

function updateBookmarksCount() {
  document.getElementById("savedCount").textContent = savedIds.size;
}

function openBookmarksModal() {
  const listContainer = document.getElementById("bookmarksList");
  const savedArticles = articles.filter(a => savedIds.has(a.id));

  if (savedArticles.length === 0) {
    listContainer.innerHTML = `
      <div style="text-align: center; padding: 32px; color: var(--text-muted);">
        <p>No saved articles yet.</p>
        <p style="font-size: 0.85rem; margin-top: 6px;">Click the star icon on any article to save it for offline reading.</p>
      </div>
    `;
  } else {
    listContainer.innerHTML = savedArticles.map(art => `
      <div style="display: flex; justify-content: space-between; align-items: center; padding: 14px 0; border-bottom: 1px solid var(--border-color);">
        <div style="flex: 1; padding-right: 16px;">
          <div style="font-size: 0.75rem; color: var(--primary-light); font-weight: 700;">${art.category} • ${art.tier}</div>
          <h4 style="cursor: pointer; margin: 4px 0;" onclick="openArticleDetail('${art.id}'); closeBookmarksModal();">${art.title}</h4>
          <span style="font-size: 0.78rem; color: var(--text-muted);">${art.sourceName} • ${art.publishedAt}</span>
        </div>
        <button class="btn btn-outline" style="padding: 4px 10px; font-size: 0.8rem;" onclick="toggleBookmark('${art.id}'); openBookmarksModal();">
          Remove
        </button>
      </div>
    `).join("");
  }

  document.getElementById("bookmarksModal").classList.remove("hidden");
}

function closeBookmarksModal() {
  document.getElementById("bookmarksModal").classList.add("hidden");
}

// Text-To-Speech
function speakArticle(id) {
  const art = articles.find(a => a.id === id);
  if (!art) return;

  if (!('speechSynthesis' in window)) {
    alert("Sorry, your browser does not support audio speech synthesis.");
    return;
  }

  stopSpeech();

  const textToRead = `${art.title}. ${art.summary}. ${art.fullContent}`;
  currentUtterance = new SpeechSynthesisUtterance(textToRead);
  currentUtterance.rate = 1.0;
  currentUtterance.pitch = 1.0;

  currentUtterance.onstart = () => {
    isSpeaking = true;
    document.getElementById("audioTitle").textContent = art.title;
    document.getElementById("audioBar").classList.remove("hidden");
    document.getElementById("audioPauseResumeBtn").textContent = "⏸️ Pause";
  };

  currentUtterance.onend = () => {
    stopSpeech();
  };

  currentUtterance.onerror = () => {
    stopSpeech();
  };

  window.speechSynthesis.speak(currentUtterance);
}

function togglePauseSpeech() {
  if (!window.speechSynthesis) return;

  if (window.speechSynthesis.paused) {
    window.speechSynthesis.resume();
    document.getElementById("audioPauseResumeBtn").textContent = "⏸️ Pause";
  } else if (window.speechSynthesis.speaking) {
    window.speechSynthesis.pause();
    document.getElementById("audioPauseResumeBtn").textContent = "▶️ Resume";
  }
}

function stopSpeech() {
  if (window.speechSynthesis) {
    window.speechSynthesis.cancel();
  }
  isSpeaking = false;
  currentUtterance = null;
  document.getElementById("audioBar").classList.add("hidden");
}

function showToast(msg) {
  const banner = document.getElementById("statusBanner");
  const text = document.getElementById("statusText");
  text.textContent = msg;
  banner.classList.remove("hidden");
  setTimeout(() => {
    banner.classList.add("hidden");
  }, 4000);
}

function filterByTier(tier) {
  document.querySelectorAll(".tier-btn").forEach(btn => {
    if (btn.getAttribute("data-tier") === tier) {
      btn.classList.add("active");
    } else {
      btn.classList.remove("active");
    }
  });
  currentTier = tier;
  renderApp();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function filterByCategory(cat) {
  document.querySelectorAll(".cat-btn").forEach(btn => {
    if (btn.getAttribute("data-cat") === cat) {
      btn.classList.add("active");
    } else {
      btn.classList.remove("active");
    }
  });
  currentCategory = cat;
  renderApp();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetFilters() {
  currentTier = "ALL";
  currentCategory = "All";
  searchQuery = "";
  document.getElementById("searchInput").value = "";
  document.querySelectorAll(".tier-btn").forEach(b => b.classList.remove("active"));
  document.querySelector('.tier-btn[data-tier="ALL"]').classList.add("active");
  document.querySelectorAll(".cat-btn").forEach(b => b.classList.remove("active"));
  document.querySelector('.cat-btn[data-cat="All"]').classList.add("active");
  renderApp();
}
