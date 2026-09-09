package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.Device
import com.example.model.NavDestination
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun OverviewScreen(
    repository: EnergyHubRepository,
    devices: List<Device>,
    onNavigate: (NavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    var timeRange by remember { mutableStateOf("Today") }
    val energyReadings = remember(timeRange) { repository.getEnergyReadings(timeRange) }
    val breakdownItems = remember { repository.getEnergyBreakdown() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: 4 KPI Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Energy",
                        value = "24.6",
                        unit = "kWh",
                        subtitle = "Today's consumption",
                        trend = "-8.4%",
                        isPositiveTrendGood = false,
                        isTrendingUp = false,
                        accentColor = EnergyCyan,
                        icon = Icons.Default.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Estimated Cost",
                        value = "₹196.80",
                        unit = "",
                        subtitle = "Today's cost",
                        trend = "-6.2%",
                        isPositiveTrendGood = false,
                        isTrendingUp = false,
                        accentColor = EnergyOrange,
                        icon = Icons.Default.Savings,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Energy Saved",
                        value = "7.8",
                        unit = "kWh",
                        subtitle = "Saved this week",
                        trend = "+12.5%",
                        isPositiveTrendGood = true,
                        isTrendingUp = true,
                        accentColor = EnergyLime,
                        icon = Icons.Default.EnergySavingsLeaf,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Solar Generated",
                        value = "18.4",
                        unit = "kWh",
                        subtitle = "Today's solar generation",
                        trend = "+9.3%",
                        isPositiveTrendGood = true,
                        isTrendingUp = true,
                        accentColor = EnergyCyan,
                        icon = Icons.Default.WbSunny,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: Main Interactive Energy Chart
        item {
            EnergyChartInteractive(
                dataPoints = energyReadings,
                timeRange = timeRange,
                onTimeRangeChanged = { timeRange = it }
            )
        }

        // Section: Energy Breakdown Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Energy Breakdown",
                                color = EnergyText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Consumption distributed by appliance category",
                                color = EnergyTextMuted,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "24.6 kWh Total",
                            color = EnergyCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    breakdownItems.forEach { item ->
                        val itemColor = Color(item.colorHex)
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(itemColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.category.displayName,
                                        color = EnergyText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${item.kwh} kWh",
                                        color = EnergyTextMuted,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${item.percentage}%",
                                        color = EnergyText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(EnergyPanelSecondary)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(item.percentage / 100f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(itemColor)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Live Device Status Header & Cards
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${devices.count { it.isOn }} of ${devices.size} devices active",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )
                }

                TextButton(onClick = { onNavigate(NavDestination.DEVICES) }) {
                    Text("View All", color = EnergyCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View all",
                        tint = EnergyCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        items(devices.take(6)) { device ->
            DeviceCardItem(
                device = device,
                onToggle = { repository.toggleDevice(device.id) }
            )
        }

        // Quick Solar & Recommendations CTA
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyLime.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { onNavigate(NavDestination.SOLAR) },
                color = EnergyPanelSecondary
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EnergyLime.copy(alpha = 0.15f))
                                .border(1.dp, EnergyLime.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SolarPower,
                                contentDescription = "Solar",
                                tint = EnergyLime,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Solar is covering 74.8% of usage",
                                color = EnergyText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "18.4 kWh generated today • ₹147.20 saved",
                                color = EnergyTextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Explore",
                        tint = EnergyLime,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
