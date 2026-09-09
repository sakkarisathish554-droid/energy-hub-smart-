package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.Device
import com.example.ui.components.AddDeviceDialog
import com.example.ui.components.DeviceCardItem
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.*

@Composable
fun DevicesScreen(
    repository: EnergyHubRepository,
    devices: List<Device>,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }

    val locations = listOf("All", "Living Room", "Kitchen", "Bedroom", "Whole Home", "Utility Roof")

    val filteredDevices = remember(devices, searchQuery, selectedLocation) {
        devices.filter { dev ->
            val matchesSearch = dev.name.contains(searchQuery, ignoreCase = true) ||
                    dev.location.contains(searchQuery, ignoreCase = true) ||
                    dev.category.displayName.contains(searchQuery, ignoreCase = true)
            val matchesLocation = selectedLocation == "All" || dev.location.contains(selectedLocation, ignoreCase = true)
            matchesSearch && matchesLocation
        }
    }

    if (showAddDialog) {
        AddDeviceDialog(
            onDismiss = { showAddDialog = false },
            onAddDevice = { name, location, category, powerKw ->
                repository.addDevice(name, location, category, powerKw)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header & Add Device button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Smart Devices",
                        color = EnergyText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${devices.size} configured devices • ${devices.count { it.isOn }} active",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EnergyCyan,
                        contentColor = EnergyBg
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("button_open_add_device")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Device",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Device", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search appliances by name, location...", color = EnergyTextMuted, fontSize = 13.sp) },
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
                    focusedContainerColor = EnergyPanel,
                    unfocusedContainerColor = EnergyPanel
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("devices_search_bar")
            )
        }

        // Location Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(locations) { loc ->
                    val isSelected = loc == selectedLocation
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) EnergyCyan.copy(alpha = 0.2f) else EnergyPanel)
                            .border(1.dp, if (isSelected) EnergyCyan else EnergyBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedLocation = loc }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = loc,
                            color = if (isSelected) EnergyCyan else EnergyTextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Device List or Empty State
        if (filteredDevices.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.DevicesOther,
                    title = "No devices found",
                    description = "Try adjusting your search query or location filter.",
                    actionLabel = "Reset Filters",
                    onAction = {
                        searchQuery = ""
                        selectedLocation = "All"
                    }
                )
            }
        } else {
            items(filteredDevices, key = { it.id }) { device ->
                DeviceCardItem(
                    device = device,
                    onToggle = { repository.toggleDevice(device.id) }
                )
            }
        }
    }
}
