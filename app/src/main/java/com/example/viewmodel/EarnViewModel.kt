package com.example.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.AdCampaign
import com.example.model.AdsterraDailyStats
import com.example.model.AdsterraPlacement
import com.example.model.AdsterraStats
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.AuthMode
import com.example.model.AuthUser
import com.example.model.NavTab
import com.example.model.ReferralFriend
import com.example.model.SubScreen
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.model.UserStats
import com.example.util.Localization
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EarnUiState(
    val currentTab: NavTab = NavTab.HOME,
    val activeSubScreen: SubScreen = SubScreen.NONE,
    val language: AppLanguage = AppLanguage.BANGLA,
    val currency: AppCurrency = AppCurrency.BDT,
    val isDarkMode: Boolean = false,
    val isLoggedIn: Boolean = true,
    val authMode: AuthMode = AuthMode.LOGIN,
    val currentUser: AuthUser = AuthUser(
        name = "তানজিম হোসেন",
        phone = "01712345678",
        email = "tanzim@gmail.com",
        password = "password123"
    ),
    val userStats: UserStats = UserStats(),
    val campaigns: List<AdCampaign> = emptyList(),
    val currentAdIndex: Int = 0,
    val timerProgress: Float = 0f,
    val remainingSeconds: Int = 10,
    val elapsedSeconds: Int = 1,
    val isImageClickedInCurrentCycle: Boolean = false,
    val isTimerRunning: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val referralFriends: List<ReferralFriend> = emptyList(),
    val adsterraStats: AdsterraStats = AdsterraStats(
        apiKey = "",
        isConnected = false,
        placements = listOf(
            AdsterraPlacement("Direct Link #1 (High CPM)", "Direct Link", 6800, 420, 3.20, 21.76),
            AdsterraPlacement("Popunder Main Traffic", "Popunder", 5100, 310, 2.10, 10.71),
            AdsterraPlacement("Social Bar Widget", "Social Bar", 3900, 240, 1.85, 7.21),
            AdsterraPlacement("Native Banner 300x250", "Native", 2650, 150, 1.10, 2.92)
        ),
        dailyStats = listOf(
            AdsterraDailyStats("আজ (১৫ আগস্ট)", 4120, 260, 2.55, 10.50),
            AdsterraDailyStats("গতকাল (১৪ আগস্ট)", 5200, 315, 2.40, 12.48),
            AdsterraDailyStats("১৩ আগস্ট", 4600, 280, 2.25, 10.35),
            AdsterraDailyStats("১২ আগস্ট", 4530, 265, 2.05, 9.27)
        )
    ),
    val snackbarMessage: String? = null
)

class EarnViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EarnUiState())
    val uiState: StateFlow<EarnUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        initializeInitialData()
        startAdTimer()
    }

    private fun initializeInitialData() {
        val initialCampaigns = listOf(
            AdCampaign(
                id = "camp_1",
                titleBn = "স্মার্ট গ্যাজেটস ও ব্লুটুথ হেডফোন ৫০% ডিসকাউন্ট",
                titleEn = "Exclusive Smart Tech Gadgets & Earphones 50% Off",
                sponsorBn = "টেক বাজার বিডি",
                sponsorEn = "TechBazar BD",
                imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e",
                localDrawableRes = R.drawable.ad_banner_tech_1786844718709,
                targetUrl = "https://google.com/search?q=smart+gadgets",
                rewardAmount = 5.00,
                categoryBn = "প্রযুক্তি",
                categoryEn = "Gadgets",
                viewsCount = 3840
            ),
            AdCampaign(
                id = "camp_2",
                titleBn = "বাংলায় অ্যান্ড্রয়েড ও কোটলিন প্রোগ্রামিং স্পেশাল কোর্স",
                titleEn = "Learn Android & Kotlin in Bangla Masterclass",
                sponsorBn = "কোডার্স একাডেমি",
                sponsorEn = "Coders Academy",
                imageUrl = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3",
                localDrawableRes = R.drawable.ad_banner_course_1786844729899,
                targetUrl = "https://developer.android.com",
                rewardAmount = 5.00,
                categoryBn = "শিক্ষা",
                categoryEn = "Education",
                viewsCount = 2150
            ),
            AdCampaign(
                id = "camp_3",
                titleBn = "তাজা গ্রোসারি ও ফলমূল ৩০ মিনিটে হোম ডেলিভারি",
                titleEn = "Organic Fresh Groceries 30-Min Fast Home Delivery",
                sponsorBn = "সবুজ বাজার এক্সপ্রেস",
                sponsorEn = "Green Mart Express",
                imageUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e",
                localDrawableRes = R.drawable.ad_banner_shop_1786844744400,
                targetUrl = "https://google.com/search?q=grocery+delivery",
                rewardAmount = 5.00,
                categoryBn = "গ্রোসারি",
                categoryEn = "Grocery",
                viewsCount = 1890
            )
        )

        val initialTransactions = listOf(
            Transaction(
                id = "tx_101",
                type = TransactionType.REFER_BONUS,
                titleBn = "রেফার বোনাস (+৳২০.০০)",
                titleEn = "Referral Bonus (+৳20.00)",
                descriptionBn = "সাব্বির আহমেদ জয়েন করেছেন",
                descriptionEn = "Sabbir Ahmed joined via link",
                amount = 20.00,
                isPositive = true,
                timestamp = "আজ, ১২:৩০ PM",
                statusBn = "সফল",
                statusEn = "Completed"
            ),
            Transaction(
                id = "tx_102",
                type = TransactionType.AD_REWARD,
                titleBn = "বিজ্ঞাপন ভিউ রিওয়ার্ড (+৳৫.০০)",
                titleEn = "Ad View Reward (+৳5.00)",
                descriptionBn = "টেক গ্যাজেট অ্যাড ভিউ সফল",
                descriptionEn = "Tech Gadget ad viewed & clicked",
                amount = 5.00,
                isPositive = true,
                timestamp = "আজ, ১০:১৫ AM",
                statusBn = "সফল",
                statusEn = "Completed"
            ),
            Transaction(
                id = "tx_103",
                type = TransactionType.CASHOUT_BKASH,
                titleBn = "বিকাশ ক্যাশ আউট (-৳১০০.০০)",
                titleEn = "bKash Cash Out (-৳100.00)",
                descriptionBn = "অ্যাকাউন্ট: ০১৭১২-******",
                descriptionEn = "Account: 01712-******",
                amount = 100.00,
                isPositive = false,
                timestamp = "গতকাল, ০৬:৪০ PM",
                statusBn = "সফল",
                statusEn = "Completed"
            ),
            Transaction(
                id = "tx_104",
                type = TransactionType.AD_REWARD,
                titleBn = "বিজ্ঞাপন ভিউ রিওয়ার্ড (+৳৫.০০)",
                titleEn = "Ad View Reward (+৳5.00)",
                descriptionBn = "কোর্স বিজ্ঞাপন ভিউ সফল",
                descriptionEn = "Course ad viewed & clicked",
                amount = 5.00,
                isPositive = true,
                timestamp = "গতকাল, ০৩:২০ PM",
                statusBn = "সফল",
                statusEn = "Completed"
            )
        )

        val initialFriends = listOf(
            ReferralFriend("সাব্বির আহমেদ", "Sabbir Ahmed", "+880 1819-***45", 20.00, "আজ", "সক্রিয়", "Active"),
            ReferralFriend("রফিকুল ইসলাম", "Rafiqul Islam", "+880 1711-***89", 20.00, "গতকাল", "সক্রিয়", "Active"),
            ReferralFriend("নুসরাত জাহান", "Nusrat Jahan", "+880 1914-***12", 20.00, "১২ আগস্ট", "সক্রিয়", "Active"),
            ReferralFriend("তানভীর হাসান", "Tanvir Hasan", "+880 1622-***77", 20.00, "১০ আগস্ট", "সক্রিয়", "Active")
        )

        _uiState.update {
            it.copy(
                campaigns = initialCampaigns,
                transactions = initialTransactions,
                referralFriends = initialFriends
            )
        }
    }

    private fun startAdTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val totalSteps = 100 // 100 steps * 100ms = 10,000ms = 10 seconds
            var currentStep = 0

            while (isActive) {
                if (_uiState.value.isTimerRunning && _uiState.value.currentTab == NavTab.INCOME) {
                    delay(100)
                    currentStep++
                    val progress = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
                    val remaining = ((totalSteps - currentStep) / 10).coerceAtLeast(0)
                    val elapsed = ((currentStep / 10) + 1).coerceIn(1, 10)

                    _uiState.update {
                        it.copy(
                            timerProgress = progress,
                            remainingSeconds = remaining,
                            elapsedSeconds = elapsed
                        )
                    }

                    if (currentStep >= totalSteps) {
                        handleAdCycleCompletion()
                        currentStep = 0
                        _uiState.update {
                            it.copy(
                                timerProgress = 0f,
                                remainingSeconds = 10,
                                elapsedSeconds = 1,
                                isImageClickedInCurrentCycle = false
                            )
                        }
                    }
                } else {
                    delay(200)
                }
            }
        }
    }

    private fun handleAdCycleCompletion() {
        val state = _uiState.value
        if (state.campaigns.isEmpty()) return

        val currentAd = state.campaigns[state.currentAdIndex]
        val clicked = state.isImageClickedInCurrentCycle
        val lang = state.language
        val curr = state.currency

        if (clicked) {
            val reward = currentAd.rewardAmount
            val newBalance = state.userStats.balance + reward
            val newToday = state.userStats.todayEarnings + reward
            val newWatched = state.userStats.totalWatchedAds + 1

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timeStr = timeFormat.format(Date())

            val newTx = Transaction(
                id = "tx_${System.currentTimeMillis()}",
                type = TransactionType.AD_REWARD,
                titleBn = "বিজ্ঞাপন ভিউ রিওয়ার্ড (+${Localization.formatCurrency(reward, curr, AppLanguage.BANGLA)})",
                titleEn = "Ad View Reward (+${Localization.formatCurrency(reward, curr, AppLanguage.ENGLISH)})",
                descriptionBn = "${currentAd.titleBn.take(25)}... ভিউ ও ক্লিক সফল",
                descriptionEn = "${currentAd.titleEn.take(25)}... ad completed",
                amount = reward,
                isPositive = true,
                timestamp = "আজ, $timeStr",
                statusBn = "সফল",
                statusEn = "Completed"
            )

            _uiState.update {
                it.copy(
                    userStats = it.userStats.copy(
                        balance = newBalance,
                        todayEarnings = newToday,
                        totalWatchedAds = newWatched
                    ),
                    transactions = listOf(newTx) + it.transactions,
                    snackbarMessage = Localization.rewardAdded(reward, lang),
                    currentAdIndex = (it.currentAdIndex + 1) % it.campaigns.size
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    snackbarMessage = Localization.noRewardNotClicked(lang),
                    currentAdIndex = (it.currentAdIndex + 1) % it.campaigns.size
                )
            }
        }
    }

    fun onAdImageClicked(context: Context) {
        val state = _uiState.value
        if (state.campaigns.isEmpty()) return

        val currentAd = state.campaigns[state.currentAdIndex]
        _uiState.update { it.copy(isImageClickedInCurrentCycle = true) }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentAd.targetUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Visiting: ${currentAd.targetUrl}", Toast.LENGTH_SHORT).show()
        }
    }

    fun skipAd() {
        _uiState.update {
            it.copy(
                currentAdIndex = (it.currentAdIndex + 1) % it.campaigns.size.coerceAtLeast(1),
                timerProgress = 0f,
                remainingSeconds = 10,
                elapsedSeconds = 1,
                isImageClickedInCurrentCycle = false
            )
        }
    }

    fun toggleTimerPause() {
        _uiState.update { it.copy(isTimerRunning = !it.isTimerRunning) }
    }

    fun selectTab(tab: NavTab) {
        _uiState.update {
            it.copy(
                currentTab = tab,
                // If entering income tab, ensure timer is running
                isTimerRunning = if (tab == NavTab.INCOME) true else it.isTimerRunning
            )
        }
    }

    fun toggleLanguage() {
        _uiState.update {
            val newLang = if (it.language == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
            it.copy(language = newLang)
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun setCurrency(currency: AppCurrency) {
        _uiState.update {
            val message = when (currency) {
                AppCurrency.USD -> if (it.language == AppLanguage.BANGLA) "কারেন্সি পরিবর্তন: ডলার ($) নির্বাচন করা হয়েছে (১২০ ৳ রেট)" else "Currency changed: USD ($) selected (Rate: 120 BDT)"
                AppCurrency.INR -> if (it.language == AppLanguage.BANGLA) "কারেন্সি পরিবর্তন: রুপি (₹) নির্বাচন করা হয়েছে" else "Currency changed: INR (₹) selected"
                AppCurrency.BDT -> if (it.language == AppLanguage.BANGLA) "কারেন্সি পরিবর্তন: টাকা (৳) নির্বাচন করা হয়েছে" else "Currency changed: BDT (৳) selected"
            }
            it.copy(currency = currency, snackbarMessage = message)
        }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun addCampaign(
        title: String,
        targetUrl: String,
        imageUrl: String,
        rewardAmount: Double,
        presetDrawable: Int? = null
    ): Boolean {
        if (title.isBlank() || targetUrl.isBlank()) return false

        val newCampaign = AdCampaign(
            id = "camp_${System.currentTimeMillis()}",
            titleBn = title,
            titleEn = title,
            sponsorBn = _uiState.value.userStats.userNameBn,
            sponsorEn = _uiState.value.userStats.userNameEn,
            imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1460925895917-afdab827c52f" },
            localDrawableRes = presetDrawable ?: R.drawable.ad_banner_tech_1786844718709,
            targetUrl = if (targetUrl.startsWith("http://") || targetUrl.startsWith("https://")) targetUrl else "https://$targetUrl",
            rewardAmount = if (rewardAmount <= 0) 5.00 else rewardAmount,
            categoryBn = "ইউজার ক্যাম্পেইন",
            categoryEn = "User Campaign",
            viewsCount = 0
        )

        _uiState.update {
            it.copy(
                campaigns = listOf(newCampaign) + it.campaigns,
                currentAdIndex = 0,
                timerProgress = 0f,
                remainingSeconds = 10,
                elapsedSeconds = 1,
                isImageClickedInCurrentCycle = false,
                userStats = it.userStats.copy(postedAds = it.userStats.postedAds + 1),
                currentTab = NavTab.INCOME,
                snackbarMessage = Localization.campaignPublished(it.language)
            )
        }
        return true
    }

    fun requestCashout(method: String, accountNumber: String, amount: Double): Boolean {
        val state = _uiState.value
        if (accountNumber.length < 11 || amount < 50 || amount > state.userStats.balance) {
            _uiState.update {
                it.copy(snackbarMessage = Localization.withdrawInsufficient(it.language))
            }
            return false
        }

        val type = when (method.lowercase()) {
            "nagad", "নগদ" -> TransactionType.CASHOUT_NAGAD
            "rocket", "রকেট" -> TransactionType.CASHOUT_ROCKET
            else -> TransactionType.CASHOUT_BKASH
        }

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeStr = timeFormat.format(Date())

        val newTx = Transaction(
            id = "tx_${System.currentTimeMillis()}",
            type = type,
            titleBn = "$method ক্যাশ আউট (-${Localization.formatCurrency(amount, AppLanguage.BANGLA)})",
            titleEn = "$method Cash Out (-৳${String.format("%.2f", amount)})",
            descriptionBn = "অ্যাকাউন্ট: $accountNumber",
            descriptionEn = "Account: $accountNumber",
            amount = amount,
            isPositive = false,
            timestamp = "আজ, $timeStr",
            statusBn = "সফল",
            statusEn = "Completed"
        )

        _uiState.update {
            it.copy(
                userStats = it.userStats.copy(balance = it.userStats.balance - amount),
                transactions = listOf(newTx) + it.transactions,
                snackbarMessage = Localization.withdrawSuccess(amount, method, it.language)
            )
        }
        return true
    }

    fun copyReferralLink(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TakaEarn Referral Link", _uiState.value.userStats.referralLink)
        clipboard.setPrimaryClip(clip)

        _uiState.update {
            it.copy(snackbarMessage = Localization.linkCopied(it.language))
        }
    }

    fun shareReferralLink(context: Context) {
        val state = _uiState.value
        val shareText = if (state.language == AppLanguage.BANGLA) {
            "🔥 TakaEarn অ্যাপে জয়েন করুন এবং বিজ্ঞাপন দেখে প্রতিদিন টাকা আয় করুন! আমার রেফার লিংক: ${state.userStats.referralLink} কোড: ${state.userStats.referralCode}"
        } else {
            "🔥 Join TakaEarn to earn daily income by watching ads! Use my referral link: ${state.userStats.referralLink} Code: ${state.userStats.referralCode}"
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Referral Link")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun navigateToSubScreen(subScreen: SubScreen) {
        _uiState.update { it.copy(activeSubScreen = subScreen) }
    }

    fun navigateBackFromSubScreen() {
        _uiState.update { it.copy(activeSubScreen = SubScreen.NONE) }
    }

    fun setAuthMode(mode: AuthMode) {
        _uiState.update { it.copy(authMode = mode) }
    }

    fun login(identifier: String, pass: String): Boolean {
        if (identifier.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = Localization.enterValidInfo(it.language)) }
            return false
        }

        val name = if (identifier.contains("@")) identifier.substringBefore("@").replaceFirstChar { it.uppercase() } else "User ${identifier.takeLast(4)}"
        val phone = if (identifier.contains("@")) "+880 1712-345678" else identifier
        val email = if (identifier.contains("@")) identifier else "$identifier@takaearn.app"

        val authUser = AuthUser(
            name = name,
            phone = phone,
            email = email,
            password = pass
        )

        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentUser = authUser,
                userStats = it.userStats.copy(
                    userNameBn = name,
                    userNameEn = name,
                    userPhone = phone,
                    userEmail = email
                ),
                snackbarMessage = if (it.language == AppLanguage.BANGLA) "সফলভাবে লগইন হয়েছে! স্বাগতম $name" else "Login Successful! Welcome $name"
            )
        }
        return true
    }

    fun register(name: String, phone: String, email: String, pass: String): Boolean {
        if (name.isBlank() || phone.isBlank() || email.isBlank() || pass.length < 4) {
            _uiState.update { it.copy(snackbarMessage = Localization.enterValidInfo(it.language)) }
            return false
        }

        val authUser = AuthUser(
            name = name,
            phone = phone,
            email = email,
            password = pass
        )

        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentUser = authUser,
                userStats = it.userStats.copy(
                    userNameBn = name,
                    userNameEn = name,
                    userPhone = phone,
                    userEmail = email
                ),
                snackbarMessage = if (it.language == AppLanguage.BANGLA) "অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে! 🎉" else "Account created successfully! 🎉"
            )
        }
        return true
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                snackbarMessage = if (it.language == AppLanguage.BANGLA) "লগআউট সফল হয়েছে" else "Logged out successfully"
            )
        }
    }

    fun connectAdsterra(apiKey: String): Boolean {
        if (apiKey.isBlank()) {
            _uiState.update {
                it.copy(snackbarMessage = if (it.language == AppLanguage.BANGLA) "সঠিক API কি প্রদান করুন" else "Please enter valid API Key")
            }
            return false
        }

        // Calculate dynamic stats based on apiKey hash for realistic demo/live data
        val hash = apiKey.hashCode().let { kotlin.math.abs(it) }
        val randomBalance = 15.0 + (hash % 8500) / 100.0
        val randomToday = 2.0 + (hash % 1200) / 100.0
        val randomImpressions = 8000 + (hash % 35000)
        val randomClicks = (randomImpressions * 0.05).toInt()
        val randomCpm = String.format(Locale.US, "%.2f", (randomBalance / (randomImpressions / 1000.0))).toDoubleOrNull() ?: 2.35

        val updatedAdsterra = _uiState.value.adsterraStats.copy(
            apiKey = apiKey,
            isConnected = true,
            balanceUsd = randomBalance,
            todayRevenueUsd = randomToday,
            totalImpressions = randomImpressions,
            totalClicks = randomClicks,
            averageCpm = randomCpm
        )

        _uiState.update {
            it.copy(
                adsterraStats = updatedAdsterra,
                snackbarMessage = if (it.language == AppLanguage.BANGLA) "অ্যাডস্টেরা অ্যাকাউন্ট সফলভাবে সংযুক্ত হয়েছে! 📈" else "Adsterra account connected successfully! 📈"
            )
        }
        return true
    }

    fun refreshAdsterraStats() {
        val current = _uiState.value.adsterraStats
        if (!current.isConnected) return

        val updated = current.copy(
            balanceUsd = current.balanceUsd + 0.45,
            todayRevenueUsd = current.todayRevenueUsd + 0.45,
            totalImpressions = current.totalImpressions + 250,
            totalClicks = current.totalClicks + 15
        )

        _uiState.update {
            it.copy(
                adsterraStats = updated,
                snackbarMessage = if (it.language == AppLanguage.BANGLA) "অ্যাডস্টেরা আর্নিং ডাটা আপডেট হয়েছে! 🔄" else "Adsterra stats refreshed! 🔄"
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
