package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Device
import com.example.model.DeviceCategory
import com.example.model.EnergyDataPoint
import com.example.ui.theme.*

@Composable
fun StatCard(
    title: String,
    value: String,
    unit: String,
    subtitle: String,
    trend: String,
    isPositiveTrendGood: Boolean,
    isTrendingUp: Boolean,
    accentColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp))
            .testTag("stat_card_${title.lowercase().replace(" ", "_")}"),
        color = EnergyPanel
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = EnergyTextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    color = EnergyText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        color = EnergyTextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subtitle,
                    color = EnergyTextMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                val trendColor = if (isTrendingUp) {
                    if (isPositiveTrendGood) EnergyLime else EnergyOrange
                } else {
                    if (isPositiveTrendGood) EnergyOrange else EnergyLime
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(trendColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = if (isTrendingUp) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = "trend",
                        tint = trendColor,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = trend,
                        color = trendColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun EnergyChartInteractive(
    dataPoints: List<EnergyDataPoint>,
    timeRange: String,
    onTimeRangeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember(dataPoints) { mutableStateOf<Int?>(null) }

    Surface(
        modifier = modifier
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
                        text = "Energy Consumption",
                        color = EnergyText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-time load vs previous period",
                        color = EnergyTextMuted,
                        fontSize = 12.sp
                    )
                }

                // Range Selector Pills
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(EnergyPanelSecondary)
                        .padding(2.dp)
                ) {
                    val ranges = listOf("Today", "7 Days", "30 Days", "12 Months")
                    ranges.forEach { range ->
                        val isSelected = range == timeRange
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) EnergyCyan.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable { onTimeRangeChanged(range) }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .testTag("time_range_$range")
                        ) {
                            Text(
                                text = range,
                                color = if (isSelected) EnergyCyan else EnergyTextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tooltip if a point is tapped
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
            ) {
                if (selectedIndex != null && selectedIndex!! in dataPoints.indices) {
                    val point = dataPoints[selectedIndex!!]
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(6.dp))
                            .background(EnergyPanelSecondary)
                            .border(1.dp, EnergyCyan.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${point.label}: ",
                            color = EnergyTextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${point.currentKwh} kWh",
                            color = EnergyCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "prev: ${point.previousKwh} kWh",
                            color = EnergyTextMuted,
                            fontSize = 10.sp
                        )
                        if (point.isPeak) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PEAK",
                                color = EnergyOrange,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EnergyCyan)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Current", color = EnergyTextMuted, fontSize = 11.sp)

                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EnergyTextMuted.copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous", color = EnergyTextMuted, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Canvas Chart
            val cyanColor = EnergyCyan
            val mutedColor = EnergyTextMuted
            val peakColor = EnergyOrange
            val panelSecColor = EnergyPanelSecondary

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .pointerInput(dataPoints) {
                        detectTapGestures { offset ->
                            if (dataPoints.isNotEmpty()) {
                                val stepX = size.width / (dataPoints.size - 1).coerceAtLeast(1)
                                val idx = ((offset.x + stepX / 2) / stepX).toInt().coerceIn(0, dataPoints.size - 1)
                                selectedIndex = idx
                            }
                        }
                    }
            ) {
                if (dataPoints.isEmpty()) return@Canvas

                val width = size.width
                val height = size.height - 30f // Leave room for X-axis labels
                val maxVal = (dataPoints.maxOfOrNull { maxOf(it.currentKwh, it.previousKwh) } ?: 10f) * 1.15f
                val minVal = 0f
                val count = dataPoints.size
                val stepX = if (count > 1) width / (count - 1) else width

                // Draw Horizontal Grid Lines
                val gridLines = 4
                for (i in 0..gridLines) {
                    val y = height * (1f - i.toFloat() / gridLines)
                    drawLine(
                        color = panelSecColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                // Previous Period Dashed Line
                val prevPath = Path()
                dataPoints.forEachIndexed { index, pt ->
                    val x = index * stepX
                    val y = height * (1f - (pt.previousKwh - minVal) / (maxVal - minVal))
                    if (index == 0) prevPath.moveTo(x, y) else prevPath.lineTo(x, y)
                }
                drawPath(
                    path = prevPath,
                    color = mutedColor.copy(alpha = 0.4f),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )

                // Current Period Area & Line
                val currentPath = Path()
                val currentAreaPath = Path()
                currentAreaPath.moveTo(0f, height)

                dataPoints.forEachIndexed { index, pt ->
                    val x = index * stepX
                    val y = height * (1f - (pt.currentKwh - minVal) / (maxVal - minVal))
                    if (index == 0) {
                        currentPath.moveTo(x, y)
                        currentAreaPath.lineTo(x, y)
                    } else {
                        val prevPt = dataPoints[index - 1]
                        val prevX = (index - 1) * stepX
                        val prevY = height * (1f - (prevPt.currentKwh - minVal) / (maxVal - minVal))
                        val cx1 = prevX + (x - prevX) / 2
                        val cy1 = prevY
                        val cx2 = prevX + (x - prevX) / 2
                        val cy2 = y
                        currentPath.cubicTo(cx1, cy1, cx2, cy2, x, y)
                        currentAreaPath.cubicTo(cx1, cy1, cx2, cy2, x, y)
                    }
                }
                currentAreaPath.lineTo(width, height)
                currentAreaPath.close()

                // Draw gradient under current line
                drawPath(
                    path = currentAreaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(cyanColor.copy(alpha = 0.35f), Color.Transparent),
                        startY = 0f,
                        endY = height
                    )
                )

                // Draw current line
                drawPath(
                    path = currentPath,
                    color = cyanColor,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw points and peak markers
                dataPoints.forEachIndexed { index, pt ->
                    val x = index * stepX
                    val y = height * (1f - (pt.currentKwh - minVal) / (maxVal - minVal))

                    val isSelected = selectedIndex == index

                    if (pt.isPeak) {
                        drawCircle(
                            color = peakColor,
                            radius = if (isSelected) 7.dp.toPx() else 5.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = EnergyPanel,
                            radius = 2.5.dp.toPx(),
                            center = Offset(x, y)
                        )
                    } else if (isSelected) {
                        drawCircle(
                            color = cyanColor,
                            radius = 6.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = EnergyPanel,
                            radius = 3.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dataPoints.forEachIndexed { index, pt ->
                    Text(
                        text = pt.label,
                        color = if (selectedIndex == index) EnergyCyan else EnergyTextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceCardItem(
    device: Device,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, EnergyBorder, RoundedCornerShape(14.dp))
            .testTag("device_card_${device.id}"),
        color = EnergyPanel
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val icon = getDeviceIcon(device.category)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (device.isOn) EnergyCyan.copy(alpha = 0.15f)
                                else EnergyPanelSecondary
                            )
                            .border(
                                1.dp,
                                if (device.isOn) EnergyCyan.copy(alpha = 0.4f) else EnergyBorder,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = device.name,
                            tint = if (device.isOn) EnergyCyan else EnergyTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = device.name,
                            color = EnergyText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = device.location,
                                color = EnergyTextMuted,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (device.isOnline) EnergyLime else EnergyOrange)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (device.isOnline) "Online" else "Offline",
                                color = if (device.isOnline) EnergyLime else EnergyOrange,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Interactive Switch
                Switch(
                    checked = device.isOn,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = EnergyBg,
                        checkedTrackColor = EnergyCyan,
                        uncheckedThumbColor = EnergyTextMuted,
                        uncheckedTrackColor = EnergyPanelSecondary,
                        uncheckedBorderColor = EnergyBorder
                    ),
                    modifier = Modifier.testTag("switch_${device.id}")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Power & Energy stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(EnergyPanelSecondary)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "power",
                        tint = if (device.isOn) EnergyCyan else EnergyTextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${String.format("%.2f", device.powerKw)} kW",
                        color = if (device.isOn) EnergyCyan else EnergyTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Today: ${String.format("%.1f", device.todayKwh)} kWh",
                    color = EnergyTextMuted,
                    fontSize = 11.sp
                )

                Text(
                    text = "₹${String.format("%.2f", device.estimatedCost)}",
                    color = EnergyLime,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

fun getDeviceIcon(category: DeviceCategory): ImageVector {
    return when (category) {
        DeviceCategory.AIR_CONDITIONING -> Icons.Default.AcUnit
        DeviceCategory.REFRIGERATION -> Icons.Default.Kitchen
        DeviceCategory.LIGHTING -> Icons.Default.Lightbulb
        DeviceCategory.ENTERTAINMENT -> Icons.Default.Tv
        DeviceCategory.KITCHEN -> Icons.Default.Kitchen
        DeviceCategory.OTHER -> Icons.Default.Air
    }
}

@Composable
fun ToastBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, EnergyCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        color = EnergyPanelSecondary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = EnergyCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = message,
                    color = EnergyText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = EnergyTextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    description: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, EnergyBorder, RoundedCornerShape(16.dp)),
        color = EnergyPanel
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(EnergyPanelSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = EnergyTextMuted,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                color = EnergyText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = EnergyTextMuted,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = EnergyCyan, contentColor = EnergyBg),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = actionLabel, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
