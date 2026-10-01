package app.knotwork.android.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * The app's theme entry point, delegating to [AriaTheme] so every existing
 * call site picks up the Minimalist Organic palette without touching them.
 *
 * Material You / dynamic colour stays disabled here, as it was before the
 * rename: the wallpaper-derived `primary` is whatever the device picks, which
 * would override the documented design tokens on some devices and not others.
 *
 * @param darkTheme `true` to use the dark palette; defaults to the system
 *   theme via [isSystemInDarkTheme].
 * @param content composable tree wrapped by the theme.
 */
@Composable
fun KnotworkAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    AriaTheme(darkTheme = darkTheme, content = content)
}
