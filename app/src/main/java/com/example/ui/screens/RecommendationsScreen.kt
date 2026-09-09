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
import com.example.model.Recommendation
import com.example.model.RecommendationPriority
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.*

@Composable
fun RecommendationsScreen(
    repository: EnergyHubRepository,
    recommendations: List<Recommendation>,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "High", "Medium", "Low")

    val visibleRecommendations = remember(recommendations, selectedFilter) {
        recommendations.filter { !it.isDismissed }.filter { rec ->
            when (selectedFilter) {
                "High" -> rec.priority == RecommendationPriority.HIGH
                "Medium" -> rec.priority == RecommendationPriority.MEDIUM
                "Low" -> rec.priority == RecommendationPriority.LOW
                else -> true
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Energy Recommendations",
                    color = EnergyText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simple actions that can improve your energy efficiency.",
                    color = EnergyTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Priority Filter Tabs
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) EnergyCyan.copy(alpha = 0.2f) else EnergyPanel)
                            .border(1.dp, if (isSelected) EnergyCyan else EnergyBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = if (filter == "All") "All Priorities" else "$filter Priority",
                            color = if (isSelected) EnergyCyan else EnergyTextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (visibleRecommendations.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.CheckCircleOutline,
                    title = "No recommendations available",
                    description = "All current energy optimizations have been addressed.",
                    actionLabel = "Reset Filters",
                    onAction = { selectedFilter = "All" }
                )
            }
        } else {
            items(visibleRecommendations, key = { it.id }) { rec ->
                RecommendationCardItem(
                    recommendation = rec,
                    onApply = {
                        if (rec.deviceId != null) {
                            repository.toggleDevice(rec.deviceId)
                        }
                        repository.completeRecommendation(rec.id)
                    },
                    onComplete = { repository.completeRecommendation(rec.id) },
                    onDismiss = { repository.dismissRecommendation(rec.id) }
                )
            }
        }
    }
}

@Composable
fun RecommendationCardItem(
    recommendation: Recommendation,
    onApply: () -> Unit,
    onComplete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (priorityLabel, priorityColor) = when (recommendation.priority) {
        RecommendationPriority.HIGH -> "HIGH PRIORITY" to EnergyOrange
        RecommendationPriority.MEDIUM -> "MEDIUM PRIORITY" to EnergyCyan
        RecommendationPriority.LOW -> "LOW PRIORITY" to EnergyLime
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, if (recommendation.isCompleted) EnergyLime.copy(alpha = 0.4f) else EnergyBorder, RoundedCornerShape(16.dp))
            .testTag("rec_card_${recommendation.id}"),
        color = EnergyPanel
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(priorityColor.copy(alpha = 0.15f))
                        .border(1.dp, priorityColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = priorityLabel,
                        color = priorityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (recommendation.isCompleted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = "Completed", tint = EnergyLime, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("COMPLETED", color = EnergyLime, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = EnergyTextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = recommendation.title,
                color = EnergyText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recommendation.explanation,
                color = EnergyTextMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Potential Savings pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(EnergyPanelSecondary)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = "Savings",
                    tint = EnergyLime,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = recommendation.potentialSavings,
                    color = EnergyLime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (!recommendation.isCompleted) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onComplete) {
                        Text("Mark Completed", color = EnergyTextMuted, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EnergyCyan,
                            contentColor = EnergyBg
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("button_apply_${recommendation.id}")
                    ) {
                        Text(recommendation.actionLabel, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
