package net.ambitious.daigoapp.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

private val LightThemeColors = lightColors(
	primary = md_theme_light_primary,
	onPrimary = md_theme_light_onPrimary,
	secondary = md_theme_light_secondary,
	onSecondary = md_theme_light_onSecondary,
	error = md_theme_light_error,
	onError = md_theme_light_onError,
	background = md_theme_light_background,
	onBackground = md_theme_light_onBackground,
	surface = md_theme_light_surface,
	onSurface = md_theme_light_onSurface,
)

private val DarkThemeColors = darkColors(
	primary = md_theme_dark_primary,
	onPrimary = md_theme_dark_onPrimary,
	secondary = md_theme_dark_secondary,
	onSecondary = md_theme_dark_onSecondary,
	error = md_theme_dark_error,
	onError = md_theme_dark_onError,
	background = md_theme_dark_background,
	onBackground = md_theme_dark_onBackground,
	surface = md_theme_dark_surface,
	onSurface = md_theme_dark_onSurface,
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightThemeColors
    } else {
        DarkThemeColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}

inline fun Modifier.noRippleClickable(
	enabled: Boolean = true,
	onClickLabel: String? = null,
	role: Role? = null,
	crossinline onClick: ()->Unit
): Modifier = composed {
	clickable(
		enabled = enabled,
		indication = null,
		onClickLabel = onClickLabel,
		role = role,
		interactionSource = remember { MutableInteractionSource() }) {
		onClick()
	}
}