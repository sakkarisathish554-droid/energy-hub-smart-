package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.Device
import com.example.ui.components.EnergyChartInteractive
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    repository: EnergyHubRepository,
    devices: List<Device>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var timeRange by remember { mutableStateOf("7 Days") }
    val dataPoints = remember(timeRange) { repository.getEnergyReadings(timeRange) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Export button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Analytics & Reports",
                        color = EnergyText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Historical trends & predictive analytics",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        val csv = repository.exportCsvData()
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, csv)
                            putExtra(Intent.EXTRA_TITLE, "EnergyHub-Analytics.csv")
                            type = "text/csv"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Export EnergyHub CSV")
                        context.startActivity(shareIntent)
                        repository.showToast("Exported energy analytics data")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EnergyPanelSecondary,
                        contentColor = EnergyCyan
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EnergyCyan.copy(alpha = 0.4f)),
                    modifier = Modifier.testTag("button_export_csv")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Export",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export CSV", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section A: Interactive Consumption Overview Chart
        item {
            EnergyChartInteractive(
                dataPoints = dataPoints,
                timeRange = timeRange,
                onTimeRangeChanged = { timeRange = it }
            )
        }

        // Section B & C: Cost Analysis & Peak Usage Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cost Analysis Card
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                    color = EnergyPanel
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cost Analysis", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = EnergyOrange, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("₹196.80", color = EnergyText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Estimated for current period", color = EnergyTextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(EnergyPanelSecondary)
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Savings:", color = EnergyTextMuted, fontSize = 10.sp)
                            Text("+₹42.50", color = EnergyLime, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Peak Usage Window Card
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                    color = EnergyPanel
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Peak Usage Window", color = EnergyTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.Timer, contentDescription = null, tint = EnergyCyan, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("2:00 PM - 5:30 PM", color = EnergyOrange, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Highest load period (3.4 kW avg)", color = EnergyTextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(EnergyPanelSecondary)
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Peak Tariff:", color = EnergyTextMuted, fontSize = 10.sp)
                            Text("₹9.20 / kWh", color = EnergyText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section D: Device Comparison Bar Chart
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Device Consumption Comparison",
                        color = EnergyText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Today's consumption across appliances in kWh",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val sortedDevices = devices.sortedByDescending { it.todayKwh }
                    val maxKwh = (sortedDevices.maxOfOrNull { it.todayKwh } ?: 10f).coerceAtLeast(1f)

                    sortedDevices.forEach { dev ->
                        val ratio = dev.todayKwh / maxKwh
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(dev.name, color = EnergyText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("${dev.todayKwh} kWh (₹${String.format("%.2f", dev.estimatedCost)})", color = EnergyCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(EnergyPanelSecondary)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(ratio)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (dev.isOn) EnergyCyan else EnergyTextMuted.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section E: Energy Efficiency Score & Gauge
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Donut Canvas Gauge
                    val limeColor = EnergyLime
                    val bgPanel = EnergyPanelSecondary

                    Box(
                        modifier = Modifier.size(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = bgPanel,
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )
                            drawArc(
                                color = limeColor,
                                startAngle = 135f,
                                sweepAngle = 270f * 0.88f,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("88", color = EnergyText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("/100", color = EnergyTextMuted, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Efficiency Score: Grade A",
                                color = EnergyLime,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your household energy efficiency is in the top 12% among peer smart homes.",
                            color = EnergyTextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "+4 points vs last week",
                            color = EnergyLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
