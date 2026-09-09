package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EnergyHubColorScheme = darkColorScheme(
  primary = EnergyCyan,
  onPrimary = EnergyBg,
  primaryContainer = EnergyPanelSecondary,
  onPrimaryContainer = EnergyCyan,
  secondary = EnergyLime,
  onSecondary = EnergyBg,
  secondaryContainer = EnergyPanelSecondary,
  onSecondaryContainer = EnergyLime,
  tertiary = EnergyOrange,
  onTertiary = EnergyBg,
  background = EnergyBg,
  onBackground = EnergyText,
  surface = EnergyPanel,
  onSurface = EnergyText,
  surfaceVariant = EnergyPanelSecondary,
  onSurfaceVariant = EnergyTextMuted,
  outline = EnergyBorder,
  error = EnergyRed,
  onError = EnergyBg
)

private val LightEnergyHubColorScheme = lightColorScheme(
  primary = EnergyCyan,
  onPrimary = EnergyBg,
  background = EnergyBg,
  onBackground = EnergyText,
  surface = EnergyPanel,
  onSurface = EnergyText,
  outline = EnergyBorder
)

@Composable
fun EnergyHubTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = EnergyHubColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  EnergyHubTheme(darkTheme = darkTheme, content = content)
}

