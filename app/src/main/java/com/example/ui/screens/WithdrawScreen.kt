package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.widthIn
import com.example.model.AppCurrency
import com.example.model.AppLanguage
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.ui.components.TransactionItemCard
import com.example.ui.theme.BkashColor
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldGradient
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NagadColor
import com.example.ui.theme.RocketColor
import com.example.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawScreen(
    balance: Double,
    language: AppLanguage,
    currency: AppCurrency = AppCurrency.BDT,
    transactions: List<Transaction>,
    onBack: () -> Unit,
    onSubmitCashout: (method: String, accountNumber: String, amount: Double) -> Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val paymentMethods = listOf(
        PaymentMethodItem("bKash", "বিকাশ", BkashColor, "01XXXXXXXXX"),
        PaymentMethodItem("Nagad", "নগদ", NagadColor, "01XXXXXXXXX"),
        PaymentMethodItem("Rocket", "রকেট", RocketColor, "01XXXXXXXXX-X")
    )
    var selectedMethodIndex by remember { mutableStateOf(0) }
    var accountNumber by remember { mutableStateOf("") }
    var withdrawAmountText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

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
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "উইথড্র / ক্যাশ আউট" else "Withdraw Cash",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_withdraw_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Balance Summary Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = EmeraldGlow),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(EmeraldGradient)
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = Localization.currentBalance(language),
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = Localization.formatCurrency(balance, currency, language),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (language == AppLanguage.BANGLA) "সর্বনিম্ন উইথড্র ৳৫০ • প্রসেসিং টাইম তাৎক্ষণিক" else "Min withdraw ৳50 • Instant Processing",
                                color = EmeraldGlow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

            // 2. Select Payment Method
            Text(
                text = Localization.selectPaymentMethod(language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                paymentMethods.forEachIndexed { index, method ->
                    val isSelected = selectedMethodIndex == index
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) method.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedMethodIndex = index
                                errorMessage = null
                            },
                        color = if (isSelected) method.color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(method.color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = method.nameEn.take(1),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == AppLanguage.BANGLA) method.nameBn else method.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // 3. Inputs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val currentMethod = paymentMethods[selectedMethodIndex]

                    // Account Number
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = {
                            accountNumber = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_page_account_input"),
                        label = { Text("${if (language == AppLanguage.BANGLA) currentMethod.nameBn else currentMethod.nameEn} ${Localization.accountNumber(language)}") },
                        placeholder = { Text(currentMethod.placeholder) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = currentMethod.color)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = currentMethod.color,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Amount Field
                    OutlinedTextField(
                        value = withdrawAmountText,
                        onValueChange = {
                            withdrawAmountText = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_page_amount_input"),
                        label = { Text(Localization.amountToWithdraw(language)) },
                        placeholder = { Text("Min: ৳50.00") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = GoldAccent)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Quick Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("50", "100", "200", "500", "All").forEach { chip ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        withdrawAmountText = if (chip == "All") balance.toInt().toString() else chip
                                        errorMessage = null
                                    }
                            ) {
                                Text(
                                    text = if (chip == "All") (if (language == AppLanguage.BANGLA) "সব" else "All") else "৳$chip",
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (accountNumber.trim().length < 11) {
                                errorMessage = if (language == AppLanguage.BANGLA) "সঠিক ১১ ডিজিটের মোবাইল নম্বর দিন" else "Enter valid 11-digit mobile number"
                                return@Button
                            }
                            val amount = withdrawAmountText.toDoubleOrNull()
                            if (amount == null || amount < 50) {
                                errorMessage = if (language == AppLanguage.BANGLA) "সর্বনিম্ন উইথড্র ৳৫০.০০" else "Minimum withdrawal is ৳50.00"
                                return@Button
                            }
                            if (amount > balance) {
                                errorMessage = if (language == AppLanguage.BANGLA) "অপর্যাপ্ত ব্যালেন্স" else "Insufficient wallet balance"
                                return@Button
                            }

                            val success = onSubmitCashout(currentMethod.nameEn, accountNumber.trim(), amount)
                            if (success) {
                                accountNumber = ""
                                withdrawAmountText = ""
                                isSubmittedSuccess = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_withdraw_page_submit")
                            .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = EmeraldGlow),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = Localization.submitWithdraw(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 4. Instructions Note
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = EmeraldSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (language == AppLanguage.BANGLA)
                            "• টাকা তোলার পর ৫-১০ মিনিটের মধ্যে আপনার মোবাইল অ্যাকাউন্টে যোগ হবে।\n• সঠিক পার্সোনাল নম্বর ব্যবহার করুন (এজেন্ট নম্বর প্রযোজ্য নয়)।"
                        else
                            "• Withdrawal will be processed within 5-10 minutes.\n• Please use personal account number only (Agent accounts not supported).",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            // 5. Withdrawal History
            val withdrawTxList = transactions.filter {
                it.type in listOf(TransactionType.CASHOUT_BKASH, TransactionType.CASHOUT_NAGAD, TransactionType.CASHOUT_ROCKET)
            }

            if (withdrawTxList.isNotEmpty()) {
                Text(
                    text = Localization.withdrawHistory(language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    withdrawTxList.forEach { tx ->
                        TransactionItemCard(
                            transaction = tx,
                            language = language,
                            currency = currency
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
}

private data class PaymentMethodItem(
    val nameEn: String,
    val nameBn: String,
    val color: Color,
    val placeholder: String
)
