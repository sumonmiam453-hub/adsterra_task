package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.widthIn
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.components.AppHeader
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldGradient
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.GoldAccent
import com.example.util.Localization

@Composable
fun CampaignScreen(
    language: AppLanguage,
    onSubmitCampaign: (title: String, targetUrl: String, imageUrl: String, rewardAmount: Double, presetRes: Int?) -> Boolean,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var title by remember { mutableStateOf("") }
    var targetUrl by remember { mutableStateOf("") }
    var rewardAmountText by remember { mutableStateOf("5.00") }
    var imageUrlText by remember { mutableStateOf("") }
    var selectedPresetIndex by remember { mutableIntStateOf(0) }
    var pickedImageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val presetImages = listOf(
        Pair("Tech / Gadget", R.drawable.ad_banner_tech_1786844718709),
        Pair("Course / IT", R.drawable.ad_banner_course_1786844729899),
        Pair("Grocery / Shop", R.drawable.ad_banner_shop_1786844744400)
    )

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pickedImageUri = uri
            imageUrlText = uri.toString()
        }
    }

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
                title = Localization.campaignScreenTitle(language),
                subtitle = Localization.campaignScreenSub(language),
                language = language,
                onToggleLanguage = onToggleLanguage
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("campaign_form_card")
                    .shadow(8.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Field 1: Image Picker / URL Input
                    Text(
                        text = Localization.imageFieldLabel(language),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preset Selector Row
                    Text(
                        text = Localization.choosePreset(language),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetImages.forEachIndexed { index, (name, resId) ->
                            val isSelected = selectedPresetIndex == index && pickedImageUri == null && imageUrlText.isBlank()
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(16f / 10f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) EmeraldSecondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        selectedPresetIndex = index
                                        pickedImageUri = null
                                        imageUrlText = ""
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldSecondary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Or Pick From Device / Custom URL Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_pick_image"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) "ডিভাইস থেকে ছবি নিন" else "Pick from Device",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = imageUrlText,
                        onValueChange = { imageUrlText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_campaign_image_url"),
                        label = { Text(if (language == AppLanguage.BANGLA) "অথবা অনলাইন ছবির URL দিন" else "Or enter custom Image URL") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 2: Campaign Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_campaign_title"),
                        label = { Text(Localization.titleFieldLabel(language)) },
                        placeholder = { Text(if (language == AppLanguage.BANGLA) "যেমন: ৫০% ছাড়ে পণ্য কিনুন" else "e.g., Get 50% discount on tech gadgets") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Title, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 3: Target Destination URL
                    OutlinedTextField(
                        value = targetUrl,
                        onValueChange = {
                            targetUrl = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_campaign_target_url"),
                        label = { Text(Localization.targetLinkFieldLabel(language)) },
                        placeholder = { Text("https://yourwebsite.com/offer") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 4: Reward Amount per View
                    OutlinedTextField(
                        value = rewardAmountText,
                        onValueChange = { rewardAmountText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_campaign_reward"),
                        label = { Text(Localization.rewardAmountLabel(language)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.AttachMoney, contentDescription = null, tint = GoldAccent)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Field 5: Submit Button
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = if (language == AppLanguage.BANGLA) "ক্যাম্পেইন শিরোনাম আবশ্যক" else "Campaign title is required"
                                return@Button
                            }
                            if (targetUrl.isBlank()) {
                                errorMessage = if (language == AppLanguage.BANGLA) "টার্গেট লিংক আবশ্যক" else "Target link is required"
                                return@Button
                            }

                            val reward = rewardAmountText.toDoubleOrNull() ?: 5.00
                            val chosenPreset = if (imageUrlText.isBlank()) presetImages[selectedPresetIndex].second else null

                            val success = onSubmitCampaign(
                                title.trim(),
                                targetUrl.trim(),
                                imageUrlText.trim(),
                                reward,
                                chosenPreset
                            )

                            if (success) {
                                title = ""
                                targetUrl = ""
                                imageUrlText = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_submit_campaign")
                            .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = EmeraldGlow),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = Localization.submitCampaign(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
}
