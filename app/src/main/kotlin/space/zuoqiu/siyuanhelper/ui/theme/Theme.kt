// Copyright (C) 2023 Zuoqiu Yingyi
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package space.zuoqiu.siyuanhelper.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = siyuanDarkPrimary,
    onPrimary = siyuanDarkOnPrimary,
    primaryContainer = siyuanDarkPrimaryContainer,
    onPrimaryContainer = siyuanDarkOnPrimaryContainer,
    inversePrimary = siyuanDarkPrimaryInverse,
    secondary = siyuanDarkSecondary,
    onSecondary = siyuanDarkOnSecondary,
    secondaryContainer = siyuanDarkSecondaryContainer,
    onSecondaryContainer = siyuanDarkOnSecondaryContainer,
    tertiary = siyuanDarkTertiary,
    onTertiary = siyuanDarkOnTertiary,
    tertiaryContainer = siyuanDarkTertiaryContainer,
    onTertiaryContainer = siyuanDarkOnTertiaryContainer,
    error = siyuanDarkError,
    onError = siyuanDarkOnError,
    errorContainer = siyuanDarkErrorContainer,
    onErrorContainer = siyuanDarkOnErrorContainer,
    background = siyuanDarkBackground,
    onBackground = siyuanDarkOnBackground,
    surface = siyuanDarkSurface,
    onSurface = siyuanDarkOnSurface,
    inverseSurface = siyuanDarkInverseSurface,
    inverseOnSurface = siyuanDarkInverseOnSurface,
    surfaceVariant = siyuanDarkSurfaceVariant,
    onSurfaceVariant = siyuanDarkOnSurfaceVariant,
    outline = siyuanDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = siyuanLightPrimary,
    onPrimary = siyuanLightOnPrimary,
    primaryContainer = siyuanLightPrimaryContainer,
    onPrimaryContainer = siyuanLightOnPrimaryContainer,
    inversePrimary = siyuanLightPrimaryInverse,
    secondary = siyuanLightSecondary,
    onSecondary = siyuanLightOnSecondary,
    secondaryContainer = siyuanLightSecondaryContainer,
    onSecondaryContainer = siyuanLightOnSecondaryContainer,
    tertiary = siyuanLightTertiary,
    onTertiary = siyuanLightOnTertiary,
    tertiaryContainer = siyuanLightTertiaryContainer,
    onTertiaryContainer = siyuanLightOnTertiaryContainer,
    error = siyuanLightError,
    onError = siyuanLightOnError,
    errorContainer = siyuanLightErrorContainer,
    onErrorContainer = siyuanLightOnErrorContainer,
    background = siyuanLightBackground,
    onBackground = siyuanLightOnBackground,
    surface = siyuanLightSurface,
    onSurface = siyuanLightOnSurface,
    inverseSurface = siyuanLightInverseSurface,
    inverseOnSurface = siyuanLightInverseOnSurface,
    surfaceVariant = siyuanLightSurfaceVariant,
    onSurfaceVariant = siyuanLightOnSurfaceVariant,
    outline = siyuanLightOutline
)

@Composable
fun HelperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
