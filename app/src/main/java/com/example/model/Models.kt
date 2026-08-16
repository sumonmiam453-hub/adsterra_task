package com.example.model

enum class AppLanguage(val displayName: String, val code: String) {
    BANGLA("বাংলা", "bn"),
    ENGLISH("English", "en")
}

enum class AppCurrency(
    val code: String,
    val symbol: String,
    val nameBn: String,
    val nameEn: String,
    val exchangeRateToBdt: Double // Amount of BDT equal to 1 Unit of this currency (BDT: 1, USD: 120, INR: 1.40)
) {
    BDT("BDT", "৳", "টাকা (BDT)", "Taka (BDT)", 1.0),
    INR("INR", "₹", "রুপি (INR)", "Rupee (INR)", 1.40),
    USD("USD", "$", "ডলার (USD)", "Dollar (USD)", 120.0)
}

enum class NavTab {
    HOME,
    INCOME,
    CAMPAIGN,
    REFER,
    ACCOUNT
}

enum class SubScreen {
    NONE,
    WITHDRAW,
    ADSTERRA,
    PRIVACY,
    ABOUT,
    TERMS,
    FAQ
}

enum class AuthMode {
    LOGIN,
    REGISTER
}

enum class TransactionType {
    AD_REWARD,
    REFER_BONUS,
    CASHOUT_BKASH,
    CASHOUT_NAGAD,
    CASHOUT_ROCKET,
    CAMPAIGN_POST
}

data class AuthUser(
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val joinDate: String = "১৫ আগস্ট ২০২৬",
    val referralCode: String = "taka99"
)

data class AdCampaign(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val sponsorBn: String,
    val sponsorEn: String,
    val imageUrl: String,
    val localDrawableRes: Int? = null,
    val targetUrl: String,
    val rewardAmount: Double = 5.00,
    val categoryBn: String = "প্রযুক্তি",
    val categoryEn: String = "Tech",
    val viewsCount: Int = 1240
)

data class Transaction(
    val id: String,
    val type: TransactionType,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val amount: Double,
    val isPositive: Boolean,
    val timestamp: String,
    val statusBn: String = "সফল",
    val statusEn: String = "Completed"
)

data class UserStats(
    val balance: Double = 450.00,
    val todayEarnings: Double = 35.00,
    val totalWatchedAds: Int = 18,
    val totalRefers: Int = 12,
    val referEarnings: Double = 240.00,
    val postedAds: Int = 2,
    val userNameBn: String = "তানজিম হোসেন",
    val userNameEn: String = "Tanzim Hossain",
    val userPhone: String = "+880 1712-345678",
    val userEmail: String = "tanzim@gmail.com",
    val referralCode: String = "tanzim99",
    val referralLink: String = "https://takaearn.app/ref/tanzim99",
    val isPremium: Boolean = true
)

data class ReferralFriend(
    val nameBn: String,
    val nameEn: String,
    val phoneMasked: String,
    val rewardEarned: Double,
    val joinDate: String,
    val statusBn: String,
    val statusEn: String
)

data class AdsterraPlacement(
    val name: String,
    val type: String,
    val impressions: Int,
    val clicks: Int,
    val cpm: Double,
    val revenueUsd: Double
)

data class AdsterraDailyStats(
    val date: String,
    val impressions: Int,
    val clicks: Int,
    val cpm: Double,
    val revenueUsd: Double
)

data class AdsterraStats(
    val apiKey: String = "",
    val isConnected: Boolean = false,
    val publisherName: String = "Adsterra Verified Publisher",
    val balanceUsd: Double = 42.60,
    val todayRevenueUsd: Double = 5.40,
    val exchangeRateBdt: Double = 120.0,
    val totalImpressions: Int = 18450,
    val totalClicks: Int = 1120,
    val averageCpm: Double = 2.31,
    val nextPayoutDate: String = "১-২ সেপ্টেম্বর ২০২৬ (Net15)",
    val payoutThresholdUsd: Double = 5.00,
    val placements: List<AdsterraPlacement> = emptyList(),
    val dailyStats: List<AdsterraDailyStats> = emptyList()
)

