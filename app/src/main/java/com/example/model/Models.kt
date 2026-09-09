package com.example.model

enum class NavDestination(val label: String, val subtitle: String) {
    OVERVIEW("Energy Overview", "Monitor your home's energy performance in real time."),
    ANALYTICS("Analytics & Insights", "Deep-dive into consumption patterns, costs, and efficiency."),
    DEVICES("Smart Devices", "Real-time monitor and controls for connected appliances."),
    RECOMMENDATIONS("Energy Recommendations", "Simple intelligent actions to reduce energy waste."),
    SOLAR("Solar Intelligence", "Real-time solar generation, storage, and grid flow metrics."),
    SETTINGS("System Settings", "Configure electricity tariffs, energy targets, and alerts.")
}

enum class DeviceCategory(val displayName: String) {
    AIR_CONDITIONING("Air Conditioning"),
    REFRIGERATION("Refrigeration"),
    LIGHTING("Lighting"),
    ENTERTAINMENT("Entertainment"),
    KITCHEN("Kitchen"),
    OTHER("Other")
}

data class Device(
    val id: String,
    val name: String,
    val location: String,
    val category: DeviceCategory,
    val powerKw: Float,
    val ratedPowerKw: Float,
    val todayKwh: Float,
    val estimatedCost: Float,
    val lastActive: String,
    val isOnline: Boolean = true,
    val isOn: Boolean = true
)

data class EnergyBreakdownItem(
    val category: DeviceCategory,
    val percentage: Int,
    val kwh: Float,
    val colorHex: Long
)

data class EnergyDataPoint(
    val label: String,
    val currentKwh: Float,
    val previousKwh: Float,
    val isPeak: Boolean = false
)

enum class RecommendationPriority {
    HIGH,
    MEDIUM,
    LOW
}

data class Recommendation(
    val id: String,
    val priority: RecommendationPriority,
    val title: String,
    val explanation: String,
    val potentialSavings: String,
    val actionLabel: String,
    val deviceId: String? = null,
    val isCompleted: Boolean = false,
    val isDismissed: Boolean = false
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)

enum class NotificationType {
    INFO,
    WARNING,
    SUCCESS
}

data class SolarMetrics(
    val generationKwh: Float = 18.4f,
    val consumptionKwh: Float = 24.6f,
    val gridImportKwh: Float = 6.2f,
    val gridExportKwh: Float = 0.0f,
    val solarCoveragePct: Float = 74.8f,
    val solarEfficiencyPct: Float = 94.2f,
    val estimatedSavingsRupees: Float = 147.20f,
    val currentSolarKw: Float = 3.82f,
    val currentInverterKw: Float = 3.65f,
    val currentHomeKw: Float = 2.45f,
    val currentGridKw: Float = 0.40f
)

data class AppSettings(
    val userName: String = "Sathish S.",
    val userEmail: String = "sakkarisathish554@gmail.com",
    val electricityRate: Float = 8.00f, // ₹8 per kWh
    val dailyTargetKwh: Float = 25.0f,
    val monthlyTargetKwh: Float = 750.0f,
    val alertHighConsumption: Boolean = true,
    val alertDeviceOffline: Boolean = true,
    val alertDailySummary: Boolean = true,
    val alertRecommendations: Boolean = true,
    val compactMode: Boolean = false
)

data class ToastMessage(
    val id: Long = System.currentTimeMillis(),
    val message: String
)
