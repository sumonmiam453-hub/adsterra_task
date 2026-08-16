package com.example.util

import com.example.model.AppCurrency
import com.example.model.AppLanguage

object Localization {

    fun formatCurrency(amountBdt: Double, currency: AppCurrency, language: AppLanguage): String {
        val converted = amountBdt / currency.exchangeRateToBdt
        val formatted = if (currency == AppCurrency.USD) {
            String.format(java.util.Locale.US, "%.2f", converted)
        } else {
            String.format(java.util.Locale.US, "%.2f", converted)
        }
        val symbol = currency.symbol
        return if (language == AppLanguage.BANGLA) {
            "$symbol " + convertToBanglaDigits(formatted)
        } else {
            "$symbol $formatted"
        }
    }

    fun formatCurrency(amountBdt: Double, language: AppLanguage): String {
        return formatCurrency(amountBdt, AppCurrency.BDT, language)
    }

    fun formatNumber(number: Int, language: AppLanguage): String {
        return if (language == AppLanguage.BANGLA) {
            convertToBanglaDigits(number.toString())
        } else {
            number.toString()
        }
    }

    private fun convertToBanglaDigits(input: String): String {
        val banglaDigits = mapOf(
            '0' to '০', '1' to '১', '2' to '২', '3' to '৩', '4' to '৪',
            '5' to '৫', '6' to '৬', '7' to '৭', '8' to '৮', '9' to '৯', '.' to '.'
        )
        return input.map { banglaDigits[it] ?: it }.joinToString("")
    }

    // Currency Switcher Labels
    fun currencySettingTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কারেন্সি নির্বাচন (টাকা / রুপি / ডলার)" else "Currency Selection (BDT / INR / USD)"
    fun currencyRateNote(lang: AppLanguage, currency: AppCurrency) = when (currency) {
        AppCurrency.USD -> if (lang == AppLanguage.BANGLA) "১ ডলার ($) = ১২০ টাকা (৳) রেটে কনভার্ট হবে" else "1 USD ($) = 120 BDT (৳) conversion rate"
        AppCurrency.INR -> if (lang == AppLanguage.BANGLA) "১ রুপি (₹) = ১.৪০ টাকা (৳) রেটে কনভার্ট হবে" else "1 INR (₹) = 1.40 BDT (৳) conversion rate"
        AppCurrency.BDT -> if (lang == AppLanguage.BANGLA) "মূল কারেন্সি: বাংলাদেশি টাকা (৳)" else "Default Currency: Bangladeshi Taka (৳)"
    }

    // Navigation Labels
    fun navHome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "হোম" else "Home"
    fun navIncome(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইনকাম" else "Income"
    fun navCampaign(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যাম্পেইন" else "Campaign"
    fun navRefer(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রেফার" else "Refer"
    fun navAccount(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাকাউন্ট" else "Account"

    // Home Screen
    fun greeting(lang: AppLanguage, nameBn: String, nameEn: String) =
        if (lang == AppLanguage.BANGLA) "স্বাগতম, $nameBn!" else "Welcome, $nameEn!"
    fun currentBalance(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বর্তমান ব্যালেন্স" else "Current Balance"
    fun todayEarnings(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজকের আয়" else "Today's Earnings"
    fun withdrawBtn(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "উইথড্র করুন" else "Withdraw"
    fun quickActions(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "দ্রুত অপশন" else "Quick Actions"
    fun earnByAds(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিজ্ঞাপন দেখে আয়" else "Earn by Watching Ads"
    fun earnByAdsSub(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতি ক্লিকে ৳৫.০০ পান" else "Get ৳5.00 per ad view"
    fun postCampaign(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন বিজ্ঞাপন দিন" else "Create Campaign"
    fun postCampaignSub(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার প্রচার শুরু করুন" else "Promote your links & app"
    fun performanceStats(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পারফরম্যান্স সারাংশ" else "Performance Stats"
    fun watchedAds(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "দেখা বিজ্ঞাপন" else "Watched Ads"
    fun totalRefers(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মোট রেফার" else "Total Refers"
    fun postedAds(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পোস্ট করা বিজ্ঞাপন" else "Posted Ads"
    fun referPromoTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বন্ধুকে রেফার করে আয় করুন!" else "Refer Friends & Earn!"
    fun referPromoDesc(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রতিটি সফল রেফারে পান ৳২০ বোনাস সরাসরি ওয়ালেটে" else "Get ৳20 bonus instantly for every friend you refer"
    fun referNow(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "রেফার করুন" else "Refer Now"

    // Income Screen
    fun adCounter(current: Int, total: Int, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "বিজ্ঞাপন ${formatNumber(current, lang)} এর ${formatNumber(total, lang)}"
        else "Ad $current of $total"
    fun rewardBadge(amount: Double, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "রিওয়ার্ড: ${formatCurrency(amount, lang)}"
        else "Reward: ${formatCurrency(amount, lang)}"
    fun clickToVisitTip(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "💡 ছবিতে ক্লিক করে ওয়েবসাইট ভিজিট করুন এবং ১০ সেকেন্ড অপেক্ষা করুন রিওয়ার্ড পেতে।"
        else "💡 Click the image to visit the link and wait 10 seconds to receive your reward."
    fun clickStatusVisited(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "✓ ছবিতে ক্লিক করা হয়েছে (রিওয়ার্ড রেডি)"
        else "✓ Image Clicked (Reward Ready)"
    fun clickStatusPending(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "⚠️ ছবিতে ক্লিক করা আবশ্যক"
        else "⚠️ Click Image to Qualify"
    fun timerSecondsLeft(sec: Int, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "${formatNumber(sec, lang)} সেকেন্ড বাকি"
        else "$sec seconds left"
    fun skipAd(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "স্কিপ করুন" else "Skip Ad"
    fun pauseTimer(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিরতি" else "Pause"
    fun resumeTimer(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "চালু করুন" else "Resume"
    fun visitLinkDirect(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লিংক ভিজিট করুন" else "Visit Link"

    fun incomeAdInstruction(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ইমেজ এর উপর ক্লিক করে ১০ সেকেন্ড অপেক্ষা করে ব্যাক করুন তাহলে রিওয়ার্ড পেয়ে যাবেন।"
        else "Click on the image, wait 10 seconds, then return to receive your reward."

    fun receiveAmountText(amount: Double, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "পাবেন: ${formatCurrency(amount, lang)}"
        else "Reward: ${formatCurrency(amount, lang)}"

    // Snackbars / Feedback
    fun rewardAdded(amount: Double, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "${formatCurrency(amount, lang)} আপনার ওয়ালেটে যোগ হয়েছে! 🎉"
        else "${formatCurrency(amount, lang)} Added to your wallet! 🎉"
    fun noRewardNotClicked(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "কোনো রিওয়ার্ড নেই! ছবিতে ক্লিক করা হয়নি ❌"
        else "No reward! Image was not clicked ❌"
    fun linkCopied(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "রেফারাল লিংক কপি করা হয়েছে! 📋"
        else "Referral link copied to clipboard! 📋"
    fun campaignPublished(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ক্যাম্পেইন সফলভাবে পোস্ট হয়েছে! 🚀"
        else "Campaign successfully published! 🚀"
    fun withdrawSuccess(amount: Double, method: String, lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "$method এর মাধ্যমে ${formatCurrency(amount, lang)} উইথড্র সফলভাবে সাবমিট হয়েছে!"
        else "Withdraw request for ${formatCurrency(amount, lang)} via $method submitted successfully!"
    fun withdrawInsufficient(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "অপর্যাপ্ত ব্যালেন্স! সর্বনিম্ন উইথড্র ৳৫০"
        else "Insufficient balance! Minimum withdrawal is ৳50"
    fun enterValidInfo(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "অনুগ্রহ করে সঠিক তথ্য প্রদান করুন"
        else "Please enter valid information"

    // Campaign Screen
    fun campaignScreenTitle(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "নতুন ক্যাম্পেইন তৈরি করুন" else "Create New Campaign"
    fun campaignScreenSub(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "আপনার ওয়েবসাইট, ইউটিউব চ্যানেল বা অ্যাপের ভিউ বাড়ান"
        else "Boost views for your website, YouTube channel, or app"
    fun imageFieldLabel(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "বিজ্ঞাপনের ছবির লিংক / প্রিসেট" else "Ad Image URL / Preset"
    fun titleFieldLabel(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ক্যাম্পেইন শিরোনাম" else "Campaign Title"
    fun targetLinkFieldLabel(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "টার্গেট গন্তব্য লিংক (URL)" else "Target Destination Link (URL)"
    fun rewardAmountLabel(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ভিউ প্রতি রিওয়ার্ড (টাকা)" else "Reward Amount per View (BDT)"
    fun choosePreset(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ছবি প্রিসেট নির্বাচন করুন:" else "Choose Image Preset:"
    fun submitCampaign(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "ক্যাম্পেইন চালু করুন" else "Launch Campaign"

    // Refer Screen
    fun referScreenTitle(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "রেফার অ্যান্ড আর্ন প্রোগ্রাম" else "Refer & Earn Program"
    fun referSharePrompt(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "আপনার বন্ধুদের সাথে এই লিংকটি শেয়ার করুন:"
        else "Share your unique referral link with friends:"
    fun copyLink(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কপি করুন" else "Copy Link"
    fun shareLink(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শেয়ার করুন" else "Share Link"
    fun totalReferCount(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "মোট সফল রেফার" else "Total Successful Refers"
    fun totalReferEarnings(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "মোট রেফারাল আয়" else "Total Referral Earnings"
    fun howItWorks(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "রেফার প্রোগ্রাম কীভাবে কাজ করে?" else "How Refer Program Works?"
    fun step1(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "১. লিংক শেয়ার করুন বন্ধুদের সাথে" else "1. Share your link with friends"
    fun step2(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "২. বন্ধু অ্যাপে জয়েন করে ১ম বিজ্ঞাপন দেখবে" else "2. Friend installs & watches 1st ad"
    fun step3(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "৩. তাৎক্ষণিক ৳২০ টাকা আপনার ওয়ালেটে পান" else "3. Instantly earn ৳20 in your wallet"
    fun recentReferrals(lang: AppLanguage) =
        if (lang == AppLanguage.BANGLA) "আমন্ত্রিত বন্ধুদের তালিকা" else "Invited Friends List"

    // Account Screen
    fun accountTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আমার প্রোফাইল" else "My Profile"
    fun premiumUser(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "প্রিমিয়াম মেম্বার" else "Premium Member"
    fun darkMode(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডার্ক মোড" else "Dark Mode"
    fun appLanguage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভাষা (Language)" else "Language (ভাষা)"
    fun withdrawTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যাশ আউট / উইথড্র ফরম" else "Cash Out / Withdraw Form"
    fun selectPaymentMethod(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পেমেন্ট মাধ্যম বেছে নিন" else "Select Payment Method"
    fun accountNumber(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাকাউন্ট নম্বর (মোবাইল)" else "Account Number (Mobile)"
    fun amountToWithdraw(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "উইথড্র পরিমাণ (৳)" else "Withdraw Amount (৳)"
    fun quickAmounts(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "দ্রুত টাকার পরিমাণ:" else "Quick Select:"
    fun submitWithdraw(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "টাকা তুলুন (উইথড্র)" else "Submit Cash Out"
    fun recentTransactions(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সাম্প্রতিক লেনদেন" else "Recent Transactions"
    fun securitySupport(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নিরাপত্তা ও সাপোর্ট" else "Security & Support"
    fun telegramSupport(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "টেলিগ্রাম অফিশিয়াল সাপোর্ট" else "Telegram Official Support"
    fun privacyPolicy(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "গোপনীয়তা ও নিরাপত্তা নীতি" else "Privacy & Security Policy"
    fun termsOfService(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ব্যবহারের শর্তাবলী" else "Terms of Service"
    fun aboutUs(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আমাদের সম্পর্কে" else "About Us"
    fun faq(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সাধারণ জিজ্ঞাসা (FAQ)" else "FAQ"
    fun logout(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগআউট" else "Logout"
    fun login(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "লগইন" else "Login"
    fun register(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাকাউন্ট তৈরি করুন" else "Create Account"
    fun createAccountTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নতুন অ্যাকাউন্ট রেজিস্ট্রেশন" else "Create New Account"
    fun loginTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনার অ্যাকাউন্টে লগইন করুন" else "Login to Your Account"
    fun fullName(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পূর্ণ নাম" else "Full Name"
    fun emailAddress(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইমেইল এড্রেস" else "Email Address"
    fun phoneNumber(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফোন নাম্বার" else "Phone Number"
    fun phoneOrEmail(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফোন নাম্বার অথবা ইমেইল" else "Phone Number or Email"
    fun password(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পাসওয়ার্ড" else "Password"
    fun alreadyHaveAccount(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইতিমধ্যে অ্যাকাউন্ট আছে? লগইন করুন" else "Already have an account? Login"
    fun dontHaveAccount(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাকাউন্ট নেই? নতুন অ্যাকাউন্ট খুলুন" else "Don't have an account? Register"
    fun adsterraEarning(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডস্টেরা আর্নিং (Adsterra)" else "Adsterra Earning"
    fun adsterraApiKey(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অ্যাডস্টেরা এপিআই কি (API Key)" else "Adsterra API Key"
    fun adsterraConnect(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এপিআই কি দিয়ে কানেক্ট করুন" else "Connect with API Key"
    fun adsterraConnected(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সফলভাবে সংযুক্ত হয়েছে" else "Successfully Connected"
    fun adsterraSubmit(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সাবমিট করুন" else "Submit API Key"
    fun adsterraHelp(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "Adsterra পাবলিশার প্যানেল থেকে API Token সংগ্রহ করে এখানে পেস্ট করুন।" else "Get your API Token from Adsterra Publisher Panel and paste it here."
    fun backToAccount(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ফিরে যান" else "Back"
    fun withdrawHistory(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "উইথড্র হিস্ট্রি" else "Withdraw History"
}
