package com.example

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EnergyHubRepository
import com.example.model.NavDestination
import com.example.ui.components.GlobalSearchDialog
import com.example.ui.components.NotificationSheetDialog
import com.example.ui.components.ToastBanner
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnergyHubApp(
    repository: EnergyHubRepository = remember { EnergyHubRepository() }
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var currentDestination by remember { mutableStateOf(NavDestination.OVERVIEW) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    val devices by repository.devices.collectAsState()
    val recommendations by repository.recommendations.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val solarMetrics by repository.solarMetrics.collectAsState()
    val settings by repository.settings.collectAsState()
    val toastMessage by repository.toast.collectAsState()

    val unreadCount = remember(notifications) { notifications.count { !it.isRead } }

    // Dynamic current date formatted
    val currentDateStr = remember {
        val sdf = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())
        sdf.format(Date())
    }

    if (showSearchDialog) {
        GlobalSearchDialog(
            devices = devices,
            recommendations = recommendations,
            onDismiss = { showSearchDialog = false },
            onNavigateTo = { dest ->
                currentDestination = dest
                showSearchDialog = false
            },
            onToggleDevice = { id -> repository.toggleDevice(id) }
        )
    }

    if (showNotificationsSheet) {
        NotificationSheetDialog(
            notifications = notifications,
            onDismiss = { showNotificationsSheet = false },
            onMarkAsRead = { repository.markNotificationAsRead(it) },
            onMarkAllRead = { repository.markAllNotificationsRead() }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = EnergyPanel,
                drawerContentColor = EnergyText,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header with Logo
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EnergyCyan.copy(alpha = 0.2f))
                                .border(1.dp, EnergyCyan, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "EnergyHub Logo",
                                tint = EnergyCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "EnergyHub",
                                color = EnergyText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Smart Energy Intelligence",
                                color = EnergyCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = EnergyBorder, thickness = 1.dp)
                }

                // Nav Items
                NavDestination.values().forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = getNavIcon(destination),
                                contentDescription = destination.label,
                                tint = if (isSelected) EnergyCyan else EnergyTextMuted
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                color = if (isSelected) EnergyCyan else EnergyText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            currentDestination = destination
                            coroutineScope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = EnergyPanelSecondary,
                            unselectedContainerColor = EnergyPanel
                        ),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                            .testTag("nav_drawer_${destination.name.lowercase()}")
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom User Profile in Drawer
                HorizontalDivider(color = EnergyBorder, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            currentDestination = NavDestination.SETTINGS
                            coroutineScope.launch { drawerState.close() }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EnergyCyan.copy(alpha = 0.2f))
                                .border(1.dp, EnergyCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SS", color = EnergyCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(settings.userName, color = EnergyText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(EnergyLime)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Live Simulation", color = EnergyLime, fontSize = 10.sp)
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = EnergyTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Surface(
                    color = EnergyPanel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { coroutineScope.launch { drawerState.open() } },
                                    modifier = Modifier.testTag("button_open_drawer")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu",
                                        tint = EnergyCyan
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                Column {
                                    Text(
                                        text = currentDestination.label,
                                        color = EnergyText,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = currentDateStr,
                                        color = EnergyTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Global Search button
                                IconButton(
                                    onClick = { showSearchDialog = true },
                                    modifier = Modifier.testTag("button_open_search")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = EnergyTextMuted
                                    )
                                }

                                // Notifications button with badge
                                Box {
                                    IconButton(
                                        onClick = { showNotificationsSheet = true },
                                        modifier = Modifier.testTag("button_open_notifications")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "Notifications",
                                            tint = if (unreadCount > 0) EnergyOrange else EnergyTextMuted
                                        )
                                    }
                                    if (unreadCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(top = 6.dp, end = 6.dp)
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(EnergyOrange)
                                        )
                                    }
                                }
                            }
                        }

                        // Subtitle
                        Text(
                            text = currentDestination.subtitle,
                            color = EnergyTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
                        )
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = EnergyPanel,
                    contentColor = EnergyText,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    val bottomItems = listOf(
                        NavDestination.OVERVIEW,
                        NavDestination.ANALYTICS,
                        NavDestination.DEVICES,
                        NavDestination.RECOMMENDATIONS,
                        NavDestination.SOLAR
                    )
                    bottomItems.forEach { destination ->
                        val isSelected = currentDestination == destination
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = getNavIcon(destination),
                                    contentDescription = destination.label
                                )
                            },
                            label = {
                                Text(
                                    text = destination.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            onClick = { currentDestination = destination },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EnergyCyan,
                                selectedTextColor = EnergyCyan,
                                indicatorColor = EnergyPanelSecondary,
                                unselectedIconColor = EnergyTextMuted,
                                unselectedTextColor = EnergyTextMuted
                            ),
                            modifier = Modifier.testTag("nav_bottom_${destination.name.lowercase()}")
                        )
                    }
                }
            },
            containerColor = EnergyBg
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    NavDestination.OVERVIEW -> OverviewScreen(
                        repository = repository,
                        devices = devices,
                        onNavigate = { currentDestination = it }
                    )
                    NavDestination.ANALYTICS -> AnalyticsScreen(
                        repository = repository,
                        devices = devices
                    )
                    NavDestination.DEVICES -> DevicesScreen(
                        repository = repository,
                        devices = devices
                    )
                    NavDestination.RECOMMENDATIONS -> RecommendationsScreen(
                        repository = repository,
                        recommendations = recommendations
                    )
                    NavDestination.SOLAR -> SolarScreen(
                        repository = repository,
                        solarMetrics = solarMetrics
                    )
                    NavDestination.SETTINGS -> SettingsScreen(
                        repository = repository,
                        currentSettings = settings
                    )
                }

                // Toast notification overlay
                AnimatedVisibility(
                    visible = toastMessage != null,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    toastMessage?.let { toast ->
                        ToastBanner(
                            message = toast.message,
                            onDismiss = { repository.clearToast() }
                        )
                    }
                }
            }
        }
    }
}

fun getNavIcon(dest: NavDestination): ImageVector {
    return when (dest) {
        NavDestination.OVERVIEW -> Icons.Default.Dashboard
        NavDestination.ANALYTICS -> Icons.Default.BarChart
        NavDestination.DEVICES -> Icons.Default.Devices
        NavDestination.RECOMMENDATIONS -> Icons.Default.Lightbulb
        NavDestination.SOLAR -> Icons.Default.SolarPower
        NavDestination.SETTINGS -> Icons.Default.Settings
    }
}
