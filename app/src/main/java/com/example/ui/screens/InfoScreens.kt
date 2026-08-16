package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoContentScreen(
    title: String,
    icon: ImageVector,
    language: AppLanguage,
    onBack: () -> Unit,
    items: List<InfoSectionItem>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("btn_info_back")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldPrimary.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSecondary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(EmeraldSecondary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.BANGLA) "TakaEarn অফিসিয়াল নির্দেশিকা ও পলিসি" else "TakaEarn Official Policy & Guidelines",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Items
            items.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item.heading,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.description,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

data class InfoSectionItem(
    val heading: String,
    val description: String
)

fun getPrivacyPolicyItems(language: AppLanguage): List<InfoSectionItem> {
    return if (language == AppLanguage.BANGLA) {
        listOf(
            InfoSectionItem("১. তথ্য সুরক্ষা ও গোপনীয়তা", "TakaEarn ব্যবহারকারীদের ব্যক্তিগত তথ্যের গোপনীয়তা রক্ষা করতে প্রতিশ্রুতিবদ্ধ। আপনার নাম, মোবাইল নম্বর এবং অ্যাকাউন্ট সংক্রান্ত কোনো তথ্য তৃতীয় পক্ষের সাথে শেয়ার করা হয় না।"),
            InfoSectionItem("২. উপার্জিত অর্থ ও নিরাপত্তা", "বিজ্ঞাপন দেখা এবং রেফারের মাধ্যমে অর্জিত সমস্ত রিওয়ার্ড এনক্রিপ্টেড ডাটাবেজে সংরক্ষিত হয়। ভুয়া বা বট ট্রাফিক ব্যবহার সম্পূর্ণ নিষিদ্ধ।"),
            InfoSectionItem("৩. পেমেন্ট প্রক্রিয়া", "বিকাশ, নগদ বা রকেটের মাধ্যমে ক্যাশ আউটের অনুরোধ পাঠানোর পর ম্যানুয়াল ও স্বয়ংক্রিয় ভেরিফিকেশন সম্পন্ন করে সরাসরি পেমেন্ট প্রেরণ করা হয়।"),
            InfoSectionItem("৪. তৃতীয় পক্ষের বিজ্ঞাপন", "অ্যাপে প্রদর্শিত বিজ্ঞাপনগুলো বিশ্বস্ত বিজ্ঞাপন নেটওয়ার্ক ও বিজ্ঞাপনী অংশীদারদের মাধ্যমে পরিবেশন করা হয়।")
        )
    } else {
        listOf(
            InfoSectionItem("1. Data Privacy & Security", "TakaEarn is committed to protecting your private data. Your phone number, credentials, and wallet balance are safely stored and never shared with third parties."),
            InfoSectionItem("2. Earnings & Anti-Fraud", "All rewards earned through genuine ad views and referrals are recorded in real-time. Automated bot clicks and abusive activity are strictly prohibited."),
            InfoSectionItem("3. Payment Handling", "Withdrawal requests via bKash, Nagad, and Rocket are authenticated securely and processed instantly into your nominated account."),
            InfoSectionItem("4. Third-Party Ads", "Ads displayed within the platform come from verified ad partners (including Adsterra and direct community advertisers).")
        )
    }
}

fun getAboutUsItems(language: AppLanguage): List<InfoSectionItem> {
    return if (language == AppLanguage.BANGLA) {
        listOf(
            InfoSectionItem("আমাদের লক্ষ্য", "TakaEarn হলো বাংলাদেশের তরুণ ও স্মার্ট ব্যবহারকারীদের জন্য একটি বিশ্বস্ত মাইক্রো-আর্নিং এবং ডিজিটাল মার্কেটিং প্ল্যাটফর্ম।"),
            InfoSectionItem("অ্যাপ ভার্সন", "TakaEarn Pro v1.2.0 (Build 2026). অ্যান্ড্রয়েড মেটেরিয়াল ৩ ও জেটপ্যাক কম্পোজ দ্বারা নির্মিত দ্রুতগতির অ্যাপ্লিকেশন।"),
            InfoSectionItem("সাপোর্ট ও কমিউনিটি", "আমাদের ডেডিকেটেড টিম ব্যবহারকারীদের যেকোনো সমস্যা সমাধানে ২৪/৭ পাশে রয়েছে। কোনো প্রশ্ন থাকলে টেলিগ্রাম সাপোর্টে মেসেজ করুন।")
        )
    } else {
        listOf(
            InfoSectionItem("Our Mission", "TakaEarn is a premier digital rewards and micro-task advertising network built for modern mobile users."),
            InfoSectionItem("App Details", "TakaEarn Pro v1.2.0 (Build 2026). Engineered with modern Kotlin Jetpack Compose and Material 3 design."),
            InfoSectionItem("Community & Support", "We pride ourselves on lightning-fast support and automated rewards. Reach us anytime via our Telegram Helpdesk.")
        )
    }
}

fun getTermsItems(language: AppLanguage): List<InfoSectionItem> {
    return if (language == AppLanguage.BANGLA) {
        listOf(
            InfoSectionItem("ব্যবহারের নিয়মাবলী", "প্রত্যেক ব্যবহারকারী শুধুমাত্র একটি অ্যাকাউন্টের মাধ্যমে আয় করতে পারবেন। একাধিক অ্যাকাউন্ট তৈরি বা ক্লোন অ্যাপ ব্যবহার করলে অ্যাকাউন্ট স্থগিত হতে পারে।"),
            InfoSectionItem("রিওয়ার্ড ক্লিয়ারিং", "বিজ্ঞাপন দেখার সময় নির্ধারিত ১০ সেকেন্ড অপেক্ষা করা এবং সঠিক লিংকে প্রবেশ করা রিওয়ার্ডের পূর্বশর্ত।"),
            InfoSectionItem("ক্যাশ আউট সীমা", "সর্বনিম্ন উইথড্র পরিমাণ ৳৫০.০০। দৈনিক উইথড্র রিকোয়েস্টের কোনো সীমা নেই।")
        )
    } else {
        listOf(
            InfoSectionItem("Fair Usage Policy", "One account per device/user. Multi-accounting, auto-clickers, and emulator spoofing will lead to permanent account suspension."),
            InfoSectionItem("Reward Qualification", "Users must complete the mandatory 10-second viewing cycle and click the image to qualify for rewards."),
            InfoSectionItem("Withdrawal Limits", "Minimum withdrawal threshold is ৳50.00 with zero platform fees.")
        )
    }
}

fun getFaqItems(language: AppLanguage): List<InfoSectionItem> {
    return if (language == AppLanguage.BANGLA) {
        listOf(
            InfoSectionItem("প্রশ্ন: আমি কীভাবে প্রতিদিন টাকা আয় করব?", "উত্তর: ইনকাম পেজে থাকা বিজ্ঞাপন ছবিতে ক্লিক করে ১০ সেকেন্ড অপেক্ষা করুন। প্রতি বিজ্ঞাপনে পাবেন ৳৫.০০ রিওয়ার্ড। এছাড়া বন্ধুদের রেফার করে প্রতিটি রেফারে পান ৳২০.০০।"),
            InfoSectionItem("প্রশ্ন: কীভাবে টাকা তুলব (উইথড্র)?", "উত্তর: অ্যাকাউন্ট অথবা হোম পেজের 'উইথড্র' অপশনে যান, বিকাশ/নগদ/রকেট বেছে নিয়ে আপনার ১১ ডিজিটের নম্বর ও টাকার পরিমাণ লিখে সাবমিট করুন।"),
            InfoSectionItem("প্রশ্ন: অ্যাডস্টেরা আর্নিং কীভাবে কাজ করে?", "উত্তর: অ্যাকাউন্ট সেকশনে থাকা 'অ্যাডস্টেরা আর্নিং' অপশনে ক্লিক করে আপনার Adsterra API Token পেস্ট করে কানেক্ট করুন। আপনার ওয়েব/পাবলিশার ট্রাফিকের লাইভ ইনকাম দেখতে পাবেন।")
        )
    } else {
        listOf(
            InfoSectionItem("Q: How can I earn money daily?", "A: Visit the Income tab, click the ad image, and stay for 10 seconds to receive ৳5.00 per ad. You also earn ৳20.00 for every friend referred!"),
            InfoSectionItem("Q: How do I withdraw my earnings?", "A: Go to Withdraw from the Account or Home screen, choose bKash, Nagad, or Rocket, enter your account number, and submit."),
            InfoSectionItem("Q: How does Adsterra Earning work?", "A: Click on Adsterra Earning in My Account, enter your Publisher API Key to track your live CPM, impressions, and revenue.")
        )
    }
}
