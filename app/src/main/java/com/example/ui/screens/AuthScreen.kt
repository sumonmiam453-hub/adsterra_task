package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.widthIn
import com.example.model.AppLanguage
import com.example.model.AuthMode
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldGradient
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.GoldAccent
import com.example.util.Localization

@Composable
fun AuthScreen(
    language: AppLanguage,
    authMode: AuthMode,
    onSetAuthMode: (AuthMode) -> Unit,
    onLogin: (identifier: String, pass: String) -> Boolean,
    onRegister: (name: String, phone: String, email: String, pass: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Form inputs
    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var identifierInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Spacer(modifier = Modifier.height(20.dp))

        // App Logo & Brand Header
        Box(
            modifier = Modifier
                .size(72.dp)
                .shadow(12.dp, CircleShape, ambientColor = EmeraldGlow)
                .clip(CircleShape)
                .background(EmeraldGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = "TakaEarn Logo",
                tint = GoldAccent,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "TakaEarn BD",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = if (language == AppLanguage.BANGLA) "সহজ ইনকাম ও অ্যাড ম্যানেজমেন্ট প্ল্যাটফর্ম" else "Easy Earning & Advertising Platform",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Auth Tabs (Login / Register)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                // Login Tab
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (authMode == AuthMode.LOGIN) EmeraldPrimary else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onSetAuthMode(AuthMode.LOGIN)
                            authError = null
                        }
                ) {
                    Text(
                        text = Localization.login(language),
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (authMode == AuthMode.LOGIN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Register Tab
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (authMode == AuthMode.REGISTER) EmeraldPrimary else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onSetAuthMode(AuthMode.REGISTER)
                            authError = null
                        }
                ) {
                    Text(
                        text = Localization.register(language),
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (authMode == AuthMode.REGISTER) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AnimatedContent(
                    targetState = authMode,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "authFormContent"
                ) { mode ->
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        if (mode == AuthMode.REGISTER) {
                            // 1. Name Field
                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = {
                                    nameInput = it
                                    authError = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_auth_name"),
                                label = { Text(Localization.fullName(language)) },
                                placeholder = { Text(if (language == AppLanguage.BANGLA) "আপনার নাম লিখুন" else "Enter full name") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = EmeraldSecondary)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // 2. Phone Field
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = {
                                    phoneInput = it
                                    authError = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_auth_phone"),
                                label = { Text(Localization.phoneNumber(language)) },
                                placeholder = { Text("017XXXXXXXX") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = EmeraldSecondary)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // 3. Email Field
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = {
                                    emailInput = it
                                    authError = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_auth_email"),
                                label = { Text(Localization.emailAddress(language)) },
                                placeholder = { Text("example@gmail.com") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = EmeraldSecondary)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                        } else {
                            // Login: Phone or Email
                            OutlinedTextField(
                                value = identifierInput,
                                onValueChange = {
                                    identifierInput = it
                                    authError = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_auth_identifier"),
                                label = { Text(Localization.phoneOrEmail(language)) },
                                placeholder = { Text(if (language == AppLanguage.BANGLA) "017XXXXXXXX বা ইমেইল" else "Phone number or email") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = EmeraldSecondary)
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                        }

                        // Password Field
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                authError = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_auth_password"),
                            label = { Text(Localization.password(language)) },
                            placeholder = { Text("••••••••") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = EmeraldSecondary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Password"
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }

                if (authError != null) {
                    Text(
                        text = authError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (authMode == AuthMode.LOGIN) {
                            if (identifierInput.trim().isBlank()) {
                                authError = if (language == AppLanguage.BANGLA) "ফোন নম্বর অথবা ইমেইল লিখুন" else "Enter phone or email"
                                return@Button
                            }
                            if (passwordInput.length < 4) {
                                authError = if (language == AppLanguage.BANGLA) "পাসওয়ার্ড অন্তত ৪ ডিজিটের হতে হবে" else "Password must be at least 4 chars"
                                return@Button
                            }
                            val success = onLogin(identifierInput.trim(), passwordInput)
                            if (!success) {
                                authError = if (language == AppLanguage.BANGLA) "লগইন ব্যর্থ হয়েছে। সঠিক তথ্য দিন।" else "Login failed. Check credentials."
                            }
                        } else {
                            if (nameInput.trim().isBlank()) {
                                authError = if (language == AppLanguage.BANGLA) "আপনার নাম লিখুন" else "Enter full name"
                                return@Button
                            }
                            if (phoneInput.trim().length < 11) {
                                authError = if (language == AppLanguage.BANGLA) "সঠিক ১১ ডিজিটের ফোন নম্বর দিন" else "Enter valid 11-digit phone"
                                return@Button
                            }
                            if (!emailInput.contains("@")) {
                                authError = if (language == AppLanguage.BANGLA) "সঠিক ইমেইল এড্রেস লিখুন" else "Enter valid email"
                                return@Button
                            }
                            if (passwordInput.length < 4) {
                                authError = if (language == AppLanguage.BANGLA) "পাসওয়ার্ড অন্তত ৪ ডিজিটের হতে হবে" else "Password must be at least 4 chars"
                                return@Button
                            }
                            val success = onRegister(nameInput.trim(), phoneInput.trim(), emailInput.trim(), passwordInput)
                            if (!success) {
                                authError = if (language == AppLanguage.BANGLA) "রেজিস্ট্রেশন ব্যর্থ হয়েছে।" else "Registration failed."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_auth_submit")
                        .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = EmeraldGlow),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text(
                        text = if (authMode == AuthMode.LOGIN) Localization.login(language) else Localization.register(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                // Switch prompt
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            val newMode = if (authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                            onSetAuthMode(newMode)
                            authError = null
                        }
                    ) {
                        Text(
                            text = if (authMode == AuthMode.LOGIN) Localization.dontHaveAccount(language) else Localization.alreadyHaveAccount(language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Demo Quick Login for easy testing
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
                .clickable {
                    onLogin("01712345678", "password123")
                }
                .padding(4.dp)
        ) {
            Text(
                text = if (language == AppLanguage.BANGLA) "⚡ ডেমো অ্যাকাউন্ট দিয়ে টেস্ট লগইন করুন" else "⚡ Quick Demo Login",
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
}
