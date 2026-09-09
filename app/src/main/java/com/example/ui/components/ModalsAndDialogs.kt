package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceDialog(
    onDismiss: () -> Unit,
    onAddDevice: (name: String, location: String, category: DeviceCategory, powerKw: Float) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Living Room") }
    var category by remember { mutableStateOf(DeviceCategory.AIR_CONDITIONING) }
    var powerKwString by remember { mutableStateOf("1.20") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expandedCategory by remember { mutableStateOf(false) }

    val locations = listOf("Living Room", "Kitchen", "Master Bedroom", "Whole Home", "Utility Roof", "Garage")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, EnergyBorder, RoundedCornerShape(20.dp)),
            color = EnergyPanel
        ) {
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Add Smart Device",
                            color = EnergyText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Connect a simulated energy monitor",
                            color = EnergyTextMuted,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = EnergyTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Input
                Text("Device Name", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    placeholder = { Text("e.g. Master Bedroom AC", color = EnergyTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = EnergyText,
                        unfocusedTextColor = EnergyText,
                        focusedBorderColor = EnergyCyan,
                        unfocusedBorderColor = EnergyBorder,
                        focusedContainerColor = EnergyPanelSecondary,
                        unfocusedContainerColor = EnergyPanelSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_device_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category dropdown
                Text("Category", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    OutlinedTextField(
                        value = category.displayName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = EnergyText,
                            unfocusedTextColor = EnergyText,
                            focusedBorderColor = EnergyCyan,
                            unfocusedBorderColor = EnergyBorder,
                            focusedContainerColor = EnergyPanelSecondary,
                            unfocusedContainerColor = EnergyPanelSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false },
                        modifier = Modifier.background(EnergyPanelSecondary)
                    ) {
                        DeviceCategory.values().forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName, color = EnergyText) },
                                onClick = {
                                    category = cat
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Location selector
                Text("Location", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    locations.take(3).forEach { loc ->
                        val isSelected = location == loc
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) EnergyCyan.copy(alpha = 0.2f) else EnergyPanelSecondary)
                                .border(1.dp, if (isSelected) EnergyCyan else EnergyBorder, RoundedCornerShape(8.dp))
                                .clickable { location = loc }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = loc,
                                color = if (isSelected) EnergyCyan else EnergyTextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Power Rating (kW)
                Text("Power Rating (kW)", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = powerKwString,
                    onValueChange = {
                        powerKwString = it
                        errorMessage = null
                    },
                    placeholder = { Text("e.g. 1.25", color = EnergyTextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = EnergyText,
                        unfocusedTextColor = EnergyText,
                        focusedBorderColor = EnergyCyan,
                        unfocusedBorderColor = EnergyBorder,
                        focusedContainerColor = EnergyPanelSecondary,
                        unfocusedContainerColor = EnergyPanelSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_power_rating")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = EnergyRed,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = EnergyTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.trim().isEmpty()) {
                                errorMessage = "Device name cannot be empty"
                                return@Button
                            }
                            val power = powerKwString.toFloatOrNull()
                            if (power == null || power <= 0f) {
                                errorMessage = "Enter a valid power rating (e.g. 0.5 - 5.0 kW)"
                                return@Button
                            }
                            onAddDevice(name.trim(), location, category, power)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EnergyCyan,
                            contentColor = EnergyBg
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("button_submit_device")
                    ) {
                        Text("Add Device", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun GlobalSearchDialog(
    devices: List<Device>,
    recommendations: List<Recommendation>,
    onDismiss: () -> Unit,
    onNavigateTo: (NavDestination) -> Unit,
    onToggleDevice: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredDevices = remember(searchQuery, devices) {
        if (searchQuery.isBlank()) emptyList()
        else devices.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.location.contains(searchQuery, ignoreCase = true) ||
                    it.category.displayName.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredRecommendations = remember(searchQuery, recommendations) {
        if (searchQuery.isBlank()) emptyList()
        else recommendations.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.explanation.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, EnergyBorder, RoundedCornerShape(20.dp)),
            color = EnergyPanel
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search devices, recommendations, solar...", color = EnergyTextMuted, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = EnergyCyan, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = EnergyTextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = EnergyText,
                        unfocusedTextColor = EnergyText,
                        focusedBorderColor = EnergyCyan,
                        unfocusedBorderColor = EnergyBorder,
                        focusedContainerColor = EnergyPanelSecondary,
                        unfocusedContainerColor = EnergyPanelSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("global_search_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (searchQuery.isBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = EnergyTextMuted.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Type to search anything in EnergyHub", color = EnergyTextMuted, fontSize = 12.sp)
                    }
                } else if (filteredDevices.isEmpty() && filteredRecommendations.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No results found for \"$searchQuery\"", color = EnergyTextMuted, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (filteredDevices.isNotEmpty()) {
                            item {
                                Text(
                                    text = "DEVICES (${filteredDevices.size})",
                                    color = EnergyCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(filteredDevices) { dev ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(EnergyPanelSecondary)
                                        .clickable {
                                            onNavigateTo(NavDestination.DEVICES)
                                            onDismiss()
                                        }
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(dev.name, color = EnergyText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("${dev.location} • ${dev.powerKw} kW", color = EnergyTextMuted, fontSize = 11.sp)
                                    }
                                    Switch(
                                        checked = dev.isOn,
                                        onCheckedChange = { onToggleDevice(dev.id) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = EnergyBg,
                                            checkedTrackColor = EnergyCyan
                                        )
                                    )
                                }
                            }
                        }

                        if (filteredRecommendations.isNotEmpty()) {
                            item {
                                Text(
                                    text = "RECOMMENDATIONS (${filteredRecommendations.size})",
                                    color = EnergyOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                                )
                            }
                            items(filteredRecommendations) { rec ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(EnergyPanelSecondary)
                                        .clickable {
                                            onNavigateTo(NavDestination.RECOMMENDATIONS)
                                            onDismiss()
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(rec.title, color = EnergyText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(rec.potentialSavings, color = EnergyLime, fontSize = 11.sp)
                                    }
                                    Icon(Icons.Default.ArrowForward, contentDescription = "View", tint = EnergyTextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationSheetDialog(
    notifications: List<NotificationItem>,
    onDismiss: () -> Unit,
    onMarkAsRead: (String) -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, EnergyBorder, RoundedCornerShape(20.dp)),
            color = EnergyPanel
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Notifications",
                            color = EnergyText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val unreadCount = notifications.count { !it.isRead }
                        if (unreadCount > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EnergyOrange.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("$unreadCount new", color = EnergyOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = EnergyTextMuted)
                    }
                }

                if (notifications.any { !it.isRead }) {
                    TextButton(
                        onClick = onMarkAllRead,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Mark all as read", color = EnergyCyan, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (notifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No notifications", color = EnergyTextMuted, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notifications) { notif ->
                            val typeColor = when (notif.type) {
                                NotificationType.SUCCESS -> EnergyLime
                                NotificationType.WARNING -> EnergyOrange
                                NotificationType.INFO -> EnergyCyan
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (notif.isRead) EnergyPanelSecondary.copy(alpha = 0.5f) else EnergyPanelSecondary)
                                    .border(
                                        1.dp,
                                        if (notif.isRead) EnergyBorder else typeColor.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onMarkAsRead(notif.id) }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (notif.isRead) EnergyBorder else typeColor)
                                        .padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = notif.title,
                                            color = EnergyText,
                                            fontSize = 13.sp,
                                            fontWeight = if (notif.isRead) FontWeight.Medium else FontWeight.Bold
                                        )
                                        Text(
                                            text = notif.timeAgo,
                                            color = EnergyTextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = notif.message,
                                        color = EnergyTextMuted,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
