package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EnergyHubRepository {

    private val initialDevices = listOf(
        Device(
            id = "dev_1",
            name = "Living Room AC",
            location = "Living Room",
            category = DeviceCategory.AIR_CONDITIONING,
            powerKw = 1.24f,
            ratedPowerKw = 1.24f,
            todayKwh = 6.2f,
            estimatedCost = 49.60f,
            lastActive = "Active now",
            isOnline = true,
            isOn = true
        ),
        Device(
            id = "dev_2",
            name = "Smart Refrigerator",
            location = "Kitchen",
            category = DeviceCategory.REFRIGERATION,
            powerKw = 0.18f,
            ratedPowerKw = 0.18f,
            todayKwh = 2.7f,
            estimatedCost = 21.60f,
            lastActive = "Active now",
            isOnline = true,
            isOn = true
        ),
        Device(
            id = "dev_3",
            name = "Smart Lights",
            location = "Whole Home",
            category = DeviceCategory.LIGHTING,
            powerKw = 0.09f,
            ratedPowerKw = 0.09f,
            todayKwh = 1.8f,
            estimatedCost = 14.40f,
            lastActive = "Active now",
            isOnline = true,
            isOn = true
        ),
        Device(
            id = "dev_4",
            name = "Television",
            location = "Living Room",
            category = DeviceCategory.ENTERTAINMENT,
            powerKw = 0.00f,
            ratedPowerKw = 0.15f,
            todayKwh = 0.9f,
            estimatedCost = 7.20f,
            lastActive = "2 hours ago",
            isOnline = false,
            isOn = false
        ),
        Device(
            id = "dev_5",
            name = "Ceiling Fan",
            location = "Bedroom",
            category = DeviceCategory.OTHER,
            powerKw = 0.07f,
            ratedPowerKw = 0.07f,
            todayKwh = 0.6f,
            estimatedCost = 4.80f,
            lastActive = "Active now",
            isOnline = true,
            isOn = true
        ),
        Device(
            id = "dev_6",
            name = "Solar Inverter",
            location = "Utility Roof",
            category = DeviceCategory.OTHER,
            powerKw = 0.04f,
            ratedPowerKw = 0.04f,
            todayKwh = 0.4f,
            estimatedCost = 3.20f,
            lastActive = "Active now",
            isOnline = true,
            isOn = true
        )
    )

    private val _devices = MutableStateFlow(initialDevices)
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val initialRecommendations = listOf(
        Recommendation(
            id = "rec_1",
            priority = RecommendationPriority.HIGH,
            title = "High Air Conditioner Consumption",
            explanation = "Your air conditioner is responsible for a large portion of today's consumption (42%). Increasing thermostat to 24°C saves up to 18% cooling energy.",
            potentialSavings = "Save up to 1.8 kWh/day (~₹14.40/day)",
            actionLabel = "Adjust AC Eco Mode",
            deviceId = "dev_1"
        ),
        Recommendation(
            id = "rec_2",
            priority = RecommendationPriority.MEDIUM,
            title = "Extended Lighting Activity Detected",
            explanation = "Several smart lights have remained active for extended periods in unoccupied rooms during daylight hours.",
            potentialSavings = "Save up to 0.4 kWh/day (~₹3.20/day)",
            actionLabel = "Turn Off Idle Lights",
            deviceId = "dev_3"
        ),
        Recommendation(
            id = "rec_3",
            priority = RecommendationPriority.LOW,
            title = "Optimal Solar Generation Utilization",
            explanation = "Your current solar generation is covering 74.8% of daytime usage. Schedule heavy loads like water heaters or EV charging between 11 AM - 3 PM.",
            potentialSavings = "Maximize 100% self-consumption",
            actionLabel = "Schedule Heavy Appliances"
        )
    )

    private val _recommendations = MutableStateFlow(initialRecommendations)
    val recommendations: StateFlow<List<Recommendation>> = _recommendations.asStateFlow()

    private val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Efficiency Achievement",
            message = "Energy usage is 8.4% lower than yesterday. Great job maintaining peak savings!",
            timeAgo = "15m ago",
            type = NotificationType.SUCCESS
        ),
        NotificationItem(
            id = "notif_2",
            title = "Appliance Alert",
            message = "Living Room AC has been running continuously for 3 hours at 1.24 kW.",
            timeAgo = "1h ago",
            type = NotificationType.WARNING
        ),
        NotificationItem(
            id = "notif_3",
            title = "Solar Target Met",
            message = "Solar generation reached today's target of 18.0 kWh at 2:30 PM.",
            timeAgo = "3h ago",
            type = NotificationType.SUCCESS
        ),
        NotificationItem(
            id = "notif_4",
            title = "Device Online",
            message = "Smart Refrigerator is online and operating within optimal defrost range.",
            timeAgo = "5h ago",
            type = NotificationType.INFO
        )
    )

    private val _notifications = MutableStateFlow(initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _solarMetrics = MutableStateFlow(SolarMetrics())
    val solarMetrics: StateFlow<SolarMetrics> = _solarMetrics.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _toast = MutableStateFlow<ToastMessage?>(null)
    val toast: StateFlow<ToastMessage?> = _toast.asStateFlow()

    fun showToast(msg: String) {
        _toast.value = ToastMessage(message = msg)
    }

    fun clearToast() {
        _toast.value = null
    }

    fun toggleDevice(deviceId: String) {
        _devices.update { list ->
            list.map { dev ->
                if (dev.id == deviceId) {
                    val nextOn = !dev.isOn
                    val nextPower = if (nextOn) dev.ratedPowerKw else 0.00f
                    val nextOnline = if (nextOn) true else dev.isOnline
                    showToast("${dev.name} turned ${if (nextOn) "ON" else "OFF"}")
                    dev.copy(isOn = nextOn, powerKw = nextPower, isOnline = nextOnline)
                } else dev
            }
        }
    }

    fun addDevice(name: String, location: String, category: DeviceCategory, powerKw: Float) {
        val newDev = Device(
            id = "dev_${System.currentTimeMillis()}",
            name = name,
            location = location,
            category = category,
            powerKw = powerKw,
            ratedPowerKw = powerKw,
            todayKwh = 0.0f,
            estimatedCost = 0.0f,
            lastActive = "Just added",
            isOnline = true,
            isOn = true
        )
        _devices.update { it + newDev }
        showToast("Device \"$name\" added successfully")
    }

    fun deleteDevice(deviceId: String) {
        _devices.update { list -> list.filterNot { it.id == deviceId } }
        showToast("Device removed")
    }

    fun dismissRecommendation(id: String) {
        _recommendations.update { list ->
            list.map { if (it.id == id) it.copy(isDismissed = true) else it }
        }
        showToast("Recommendation dismissed")
    }

    fun completeRecommendation(id: String) {
        _recommendations.update { list ->
            list.map { if (it.id == id) it.copy(isCompleted = true) else it }
        }
        showToast("Recommendation marked as completed!")
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
        showToast("All notifications marked as read")
    }

    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        showToast("Settings saved successfully")
    }

    fun resetDemoData() {
        _devices.value = initialDevices
        _recommendations.value = initialRecommendations
        _notifications.value = initialNotifications
        _settings.value = AppSettings()
        showToast("Demo data has been reset to defaults")
    }

    fun getEnergyBreakdown(): List<EnergyBreakdownItem> {
        return listOf(
            EnergyBreakdownItem(DeviceCategory.AIR_CONDITIONING, 42, 10.3f, 0xFF57E1DC),
            EnergyBreakdownItem(DeviceCategory.REFRIGERATION, 18, 4.4f, 0xFFB8ED68),
            EnergyBreakdownItem(DeviceCategory.LIGHTING, 12, 3.0f, 0xFFFFB866),
            EnergyBreakdownItem(DeviceCategory.ENTERTAINMENT, 10, 2.5f, 0xFF9D84FD),
            EnergyBreakdownItem(DeviceCategory.KITCHEN, 11, 2.7f, 0xFF4DA6FF),
            EnergyBreakdownItem(DeviceCategory.OTHER, 7, 1.7f, 0xFF8DA5BA)
        )
    }

    fun getEnergyReadings(timeRange: String): List<EnergyDataPoint> {
        return when (timeRange) {
            "Today" -> listOf(
                EnergyDataPoint("00:00", 0.8f, 0.9f),
                EnergyDataPoint("03:00", 0.6f, 0.7f),
                EnergyDataPoint("06:00", 1.2f, 1.4f),
                EnergyDataPoint("09:00", 2.1f, 2.4f),
                EnergyDataPoint("12:00", 2.9f, 3.2f),
                EnergyDataPoint("15:00", 3.4f, 3.6f, isPeak = true),
                EnergyDataPoint("18:00", 3.1f, 3.3f, isPeak = true),
                EnergyDataPoint("21:00", 2.2f, 2.5f)
            )
            "7 Days" -> listOf(
                EnergyDataPoint("Mon", 23.4f, 25.1f),
                EnergyDataPoint("Tue", 26.2f, 28.0f),
                EnergyDataPoint("Wed", 24.6f, 26.8f),
                EnergyDataPoint("Thu", 22.8f, 24.5f),
                EnergyDataPoint("Fri", 27.5f, 29.2f, isPeak = true),
                EnergyDataPoint("Sat", 29.1f, 31.4f, isPeak = true),
                EnergyDataPoint("Sun", 25.3f, 27.0f)
            )
            "30 Days" -> listOf(
                EnergyDataPoint("Week 1", 175.4f, 189.0f),
                EnergyDataPoint("Week 2", 168.2f, 182.5f),
                EnergyDataPoint("Week 3", 182.6f, 194.0f, isPeak = true),
                EnergyDataPoint("Week 4", 162.8f, 178.2f)
            )
            else -> listOf(
                EnergyDataPoint("Jan", 680f, 720f),
                EnergyDataPoint("Feb", 640f, 690f),
                EnergyDataPoint("Mar", 710f, 760f),
                EnergyDataPoint("Apr", 790f, 840f, isPeak = true),
                EnergyDataPoint("May", 890f, 950f, isPeak = true),
                EnergyDataPoint("Jun", 850f, 910f),
                EnergyDataPoint("Jul", 820f, 870f),
                EnergyDataPoint("Aug", 800f, 850f),
                EnergyDataPoint("Sep", 730f, 780f),
                EnergyDataPoint("Oct", 690f, 740f),
                EnergyDataPoint("Nov", 660f, 710f),
                EnergyDataPoint("Dec", 670f, 715f)
            )
        }
    }

    fun exportCsvData(): String {
        val sb = StringBuilder()
        sb.append("Device Name,Location,Category,Power (kW),Today Energy (kWh),Cost (INR),Status,Is On\n")
        _devices.value.forEach { d ->
            sb.append("${d.name},${d.location},${d.category.displayName},${d.powerKw},${d.todayKwh},${d.estimatedCost},${if (d.isOnline) "Online" else "Offline"},${if (d.isOn) "ON" else "OFF"}\n")
        }
        return sb.toString()
    }
}
