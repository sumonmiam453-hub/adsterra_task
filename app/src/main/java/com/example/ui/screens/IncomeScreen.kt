package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.AdCampaign
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.ui.components.AppHeader
import com.example.ui.theme.EmeraldDarkBorder
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldGradient
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.GoldAccent
import com.example.util.Localization

@Composable
fun IncomeScreen(
    campaigns: List<AdCampaign>,
    currentAdIndex: Int,
    timerProgress: Float,
    remainingSeconds: Int,
    elapsedSeconds: Int = 1,
    isImageClickedInCurrentCycle: Boolean,
    isTimerRunning: Boolean,
    language: AppLanguage,
    currency: AppCurrency = AppCurrency.BDT,
    onAdImageClicked: (Context) -> Unit,
    onSkipAd: () -> Unit,
    onTogglePause: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val currentAd = if (campaigns.isNotEmpty() && currentAdIndex < campaigns.size) {
        campaigns[currentAdIndex]
    } else {
        null
    }

    val animatedProgress by animateFloatAsState(
        targetValue = timerProgress,
        label = "timerProgressAnimation"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // App Header
            AppHeader(
                title = Localization.navIncome(language),
                subtitle = if (language == AppLanguage.BANGLA) "বিজ্ঞাপন দেখে টাকা আয় করুন" else "Watch ads & earn rewards",
                language = language,
                onToggleLanguage = onToggleLanguage
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                // 1. Top Bar Counter & Controls Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ad Counter Tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = Localization.adCounter(
                                current = if (campaigns.isEmpty()) 0 else currentAdIndex + 1,
                                total = campaigns.size,
                                lang = language
                            ),
                            modifier = Modifier
                                .testTag("ad_counter_text")
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    // Timer & Controls (1 to 10 Seconds Count)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Timer Status Pill: 1 to 10 Seconds count display
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isTimerRunning) EmeraldSecondary.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isTimerRunning) EmeraldSecondary else Color.Gray
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isTimerRunning) EmeraldSecondary else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.BANGLA) {
                                        "${Localization.formatNumber(elapsedSeconds, language)} / ১০ সেকেন্ড"
                                    } else {
                                        "${elapsedSeconds}s / 10s"
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isTimerRunning) EmeraldSecondary else Color.Gray
                                    )
                                )
                            }
                        }

                        // Pause / Resume Toggle
                        IconButton(
                            onClick = onTogglePause,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("btn_timer_pause_resume")
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) Localization.pauseTimer(language) else Localization.resumeTimer(language),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. 1 to 10 Second Visual Linear Progress Indicator
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.BANGLA) "কাউন্টডাউন: ১ থেকে ১০ সেকেন্ড (${Localization.formatNumber(elapsedSeconds, language)}/১০)" else "Timer: 1 to 10 Seconds (${elapsedSeconds}/10s)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .testTag("ad_timer_progress_bar"),
                        color = EmeraldSecondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Clean Ad Card (Seamless image click, No buttons, Title + Image + Amount & Instructions below)
                if (currentAd != null) {
                    val borderColor by animateColorAsState(
                        targetValue = if (isImageClickedInCurrentCycle) EmeraldSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        label = "adCardBorder"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("active_ad_card")
                            .shadow(10.dp, RoundedCornerShape(22.dp), ambientColor = EmeraldGlow),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isImageClickedInCurrentCycle) 2.dp else 1.dp,
                            borderColor
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // 1. Only Title at the top
                            Text(
                                text = if (language == AppLanguage.BANGLA) currentAd.titleBn else currentAd.titleEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // 2. Clean Image (Clickable to silently open target link in browser)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        if (isImageClickedInCurrentCycle) 2.dp else 0.5.dp,
                                        if (isImageClickedInCurrentCycle) EmeraldSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        onAdImageClicked(context)
                                    }
                                    .testTag("ad_clickable_image")
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(currentAd.imageUrl)
                                        .crossfade(true)
                                        .error(currentAd.localDrawableRes ?: R.drawable.ad_banner_tech_1786844718709)
                                        .placeholder(currentAd.localDrawableRes ?: R.drawable.ad_banner_tech_1786844718709)
                                        .build(),
                                    contentDescription = if (language == AppLanguage.BANGLA) currentAd.titleBn else currentAd.titleEn,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3. Under the Image: Reward amount and Description side-by-side
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = if (isImageClickedInCurrentCycle) EmeraldSecondary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isImageClickedInCurrentCycle) EmeraldSecondary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Reward Amount Highlight Box
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isImageClickedInCurrentCycle) EmeraldSecondary else Color(0xFF15803D),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGlow)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = if (language == AppLanguage.BANGLA) "পাবেন" else "Reward",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 10.sp,
                                                    color = Color.White.copy(alpha = 0.9f)
                                                )
                                            )
                                            Text(
                                                text = Localization.formatCurrency(currentAd.rewardAmount, currency, language),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = Color.White
                                                )
                                            )
                                        }
                                    }

                                    // Instruction Description Text
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = Localization.incomeAdInstruction(language),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 12.sp,
                                                lineHeight = 17.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        )

                                        if (isImageClickedInCurrentCycle) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = Localization.clickStatusVisited(language),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EmeraldSecondary
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Actions Row (Skip Ad Button)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = onSkipAd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_skip_ad"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = Localization.skipAd(language),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}
