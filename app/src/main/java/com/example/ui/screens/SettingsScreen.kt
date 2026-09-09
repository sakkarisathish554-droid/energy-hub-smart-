package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.AppSettings
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    repository: EnergyHubRepository,
    currentSettings: AppSettings,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var name by remember(currentSettings) { mutableStateOf(currentSettings.userName) }
    var email by remember(currentSettings) { mutableStateOf(currentSettings.userEmail) }
    var rateString by remember(currentSettings) { mutableStateOf(currentSettings.electricityRate.toString()) }
    var dailyTargetString by remember(currentSettings) { mutableStateOf(currentSettings.dailyTargetKwh.toString()) }
    var monthlyTargetString by remember(currentSettings) { mutableStateOf(currentSettings.monthlyTargetKwh.toString()) }

    var alertHigh by remember(currentSettings) { mutableStateOf(currentSettings.alertHighConsumption) }
    var alertOffline by remember(currentSettings) { mutableStateOf(currentSettings.alertDeviceOffline) }
    var alertSummary by remember(currentSettings) { mutableStateOf(currentSettings.alertDailySummary) }
    var alertRecs by remember(currentSettings) { mutableStateOf(currentSettings.alertRecommendations) }
    var compactMode by remember(currentSettings) { mutableStateOf(currentSettings.compactMode) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "System Settings",
                    color = EnergyText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Customize user profile, tariffs, and notifications",
                    color = EnergyTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Section 1: User Profile
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("User Profile", color = EnergyText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(EnergyCyan.copy(alpha = 0.2f))
                                .border(1.dp, EnergyCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SS", color = EnergyCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(name, color = EnergyText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(email, color = EnergyTextMuted, fontSize = 12.sp)
                            Text("Pro Energy Subscriber", color = EnergyLime, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name", color = EnergyTextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", color = EnergyTextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Section 2: Energy & Tariff Settings
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Energy Tariffs & Targets", color = EnergyText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = rateString,
                        onValueChange = { rateString = it },
                        label = { Text("Base Electricity Tariff (₹ / kWh)", color = EnergyTextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = dailyTargetString,
                        onValueChange = { dailyTargetString = it },
                        label = { Text("Daily Consumption Target (kWh)", color = EnergyTextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = monthlyTargetString,
                        onValueChange = { monthlyTargetString = it },
                        label = { Text("Monthly Budget Target (kWh)", color = EnergyTextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Section 3: Notifications
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Notification Preferences", color = EnergyText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    SettingToggleRow("High Consumption Alerts", "Trigger when load exceeds 3.0 kW", alertHigh) { alertHigh = it }
                    SettingToggleRow("Device Offline Alerts", "Notify when appliances disconnect", alertOffline) { alertOffline = it }
                    SettingToggleRow("Daily Summary Report", "Receive evening consumption digest", alertSummary) { alertSummary = it }
                    SettingToggleRow("AI Recommendations", "Alert when high saving opportunities arise", alertRecs) { alertRecs = it }
                }
            }
        }

        // Section 4: Appearance & Data Management
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data Management", color = EnergyText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    SettingToggleRow("Compact Dashboard Layout", "Dense layout for smaller screens", compactMode) { compactMode = it }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val csv = repository.exportCsvData()
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, csv)
                                    type = "text/csv"
                                }
                                context.startActivity(Intent.createChooser(intent, "Export Energy Data"))
                                repository.showToast("Data exported successfully")
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EnergyCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EnergyCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export CSV", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { repository.resetDemoData() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EnergyOrange),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EnergyOrange),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    val rate = rateString.toFloatOrNull() ?: currentSettings.electricityRate
                    val daily = dailyTargetString.toFloatOrNull() ?: currentSettings.dailyTargetKwh
                    val monthly = monthlyTargetString.toFloatOrNull() ?: currentSettings.monthlyTargetKwh

                    val updated = currentSettings.copy(
                        userName = name.trim().ifEmpty { currentSettings.userName },
                        userEmail = email.trim().ifEmpty { currentSettings.userEmail },
                        electricityRate = rate,
                        dailyTargetKwh = daily,
                        monthlyTargetKwh = monthly,
                        alertHighConsumption = alertHigh,
                        alertDeviceOffline = alertOffline,
                        alertDailySummary = alertSummary,
                        alertRecommendations = alertRecs,
                        compactMode = compactMode
                    )
                    repository.updateSettings(updated)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnergyCyan,
                    contentColor = EnergyBg
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("button_save_settings")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Changes", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = EnergyText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = EnergyTextMuted, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = EnergyBg,
                checkedTrackColor = EnergyCyan,
                uncheckedThumbColor = EnergyTextMuted,
                uncheckedTrackColor = EnergyPanelSecondary
            )
        )
    }
}
