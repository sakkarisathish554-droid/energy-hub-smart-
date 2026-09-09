package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.SolarMetrics
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun SolarScreen(
    repository: EnergyHubRepository,
    solarMetrics: SolarMetrics,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Simulation Badge
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Solar Intelligence",
                        color = EnergyText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "PV generation, battery flow, and grid metrics",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EnergyCyan.copy(alpha = 0.15f))
                        .border(1.dp, EnergyCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Simulation Mode", color = EnergyCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section: Primary Solar KPI Metrics
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Solar Generation",
                        value = "${solarMetrics.generationKwh}",
                        unit = "kWh",
                        subtitle = "Today's total PV harvest",
                        trend = "+9.3%",
                        isPositiveTrendGood = true,
                        isTrendingUp = true,
                        accentColor = EnergyCyan,
                        icon = Icons.Default.SolarPower,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Home Consumption",
                        value = "${solarMetrics.consumptionKwh}",
                        unit = "kWh",
                        subtitle = "Total home energy load",
                        trend = "-8.4%",
                        isPositiveTrendGood = false,
                        isTrendingUp = false,
                        accentColor = EnergyOrange,
                        icon = Icons.Default.Home,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Grid Import",
                        value = "${solarMetrics.gridImportKwh}",
                        unit = "kWh",
                        subtitle = "Imported during night/peaks",
                        trend = "-14.2%",
                        isPositiveTrendGood = false,
                        isTrendingUp = false,
                        accentColor = EnergyTextMuted,
                        icon = Icons.Default.Power,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Solar Coverage",
                        value = "${solarMetrics.solarCoveragePct}",
                        unit = "%",
                        subtitle = "Self-sufficiency index",
                        trend = "+5.8%",
                        isPositiveTrendGood = true,
                        isTrendingUp = true,
                        accentColor = EnergyLime,
                        icon = Icons.Default.EnergySavingsLeaf,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: Visual Energy Flow
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
                color = EnergyPanel
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Live Energy Flow",
                                color = EnergyText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Real-time power routing across system",
                                color = EnergyTextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EnergyLime))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Active Flow", color = EnergyLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 4 Nodes Flow: SOLAR PANELS -> SOLAR INVERTER -> HOME -> GRID
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlowCardNode(
                            title = "Panels",
                            power = "${solarMetrics.currentSolarKw} kW",
                            icon = Icons.Default.WbSunny,
                            color = EnergyCyan,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "flow",
                            tint = EnergyCyan,
                            modifier = Modifier.size(16.dp).padding(horizontal = 2.dp)
                        )

                        FlowCardNode(
                            title = "Inverter",
                            power = "${solarMetrics.currentInverterKw} kW",
                            icon = Icons.Default.Transform,
                            color = EnergyLime,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "flow",
                            tint = EnergyLime,
                            modifier = Modifier.size(16.dp).padding(horizontal = 2.dp)
                        )

                        FlowCardNode(
                            title = "Home",
                            power = "${solarMetrics.currentHomeKw} kW",
                            icon = Icons.Default.Home,
                            color = EnergyOrange,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "flow",
                            tint = EnergyTextMuted,
                            modifier = Modifier.size(16.dp).padding(horizontal = 2.dp)
                        )

                        FlowCardNode(
                            title = "Grid",
                            power = "${solarMetrics.currentGridKw} kW",
                            icon = Icons.Default.ElectricMeter,
                            color = EnergyTextMuted,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(EnergyPanelSecondary)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("System Inverter Efficiency", color = EnergyTextMuted, fontSize = 11.sp)
                            Text("${solarMetrics.solarEfficiencyPct}%", color = EnergyLime, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Today's Estimated Savings", color = EnergyTextMuted, fontSize = 11.sp)
                            Text("₹${solarMetrics.estimatedSavingsRupees}", color = EnergyCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Solar Generation Hourly Curve Chart
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
                        text = "Solar Generation Curve (Today)",
                        color = EnergyText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Peak generation: 3.82 kW at 12:30 PM",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val curvePoints = listOf(
                        "06:00" to 0.1f,
                        "08:00" to 1.1f,
                        "10:00" to 2.8f,
                        "12:00" to 3.8f,
                        "14:00" to 3.4f,
                        "16:00" to 2.0f,
                        "18:00" to 0.5f,
                        "20:00" to 0.0f
                    )

                    val cyanColor = EnergyCyan
                    val panelSec = EnergyPanelSecondary

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        val width = size.width
                        val height = size.height - 24f
                        val stepX = width / (curvePoints.size - 1)
                        val maxPower = 4.5f

                        // Grid lines
                        for (i in 0..3) {
                            val y = height * (1f - i / 3f)
                            drawLine(color = panelSec, start = Offset(0f, y), end = Offset(width, y), strokeWidth = 1f)
                        }

                        val path = Path()
                        val area = Path()
                        area.moveTo(0f, height)

                        curvePoints.forEachIndexed { idx, pt ->
                            val x = idx * stepX
                            val y = height * (1f - pt.second / maxPower)
                            if (idx == 0) {
                                path.moveTo(x, y)
                                area.lineTo(x, y)
                            } else {
                                val prevX = (idx - 1) * stepX
                                val prevY = height * (1f - curvePoints[idx - 1].second / maxPower)
                                val cx = prevX + (x - prevX) / 2
                                path.cubicTo(cx, prevY, cx, y, x, y)
                                area.cubicTo(cx, prevY, cx, y, x, y)
                            }
                        }
                        area.lineTo(width, height)
                        area.close()

                        drawPath(
                            path = area,
                            brush = Brush.verticalGradient(
                                colors = listOf(cyanColor.copy(alpha = 0.4f), Color.Transparent),
                                startY = 0f,
                                endY = height
                            )
                        )
                        drawPath(path = path, color = cyanColor, style = Stroke(width = 3.dp.toPx()))

                        // Highlight peak
                        val peakIdx = 3
                        val peakX = peakIdx * stepX
                        val peakY = height * (1f - curvePoints[peakIdx].second / maxPower)
                        drawCircle(color = EnergyLime, radius = 5.dp.toPx(), center = Offset(peakX, peakY))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        curvePoints.forEach { pt ->
                            Text(pt.first, color = EnergyTextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlowCardNode(
    title: String,
    power: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
        color = EnergyPanelSecondary
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = EnergyTextMuted,
                fontSize = 10.sp,
                maxLines = 1
            )
            Text(
                text = power,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
