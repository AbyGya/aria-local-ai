@file:Suppress("MagicNumber")

package app.knotwork.design.tokens

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AriaPalette {
    val Sage50 = Color(0xFFF4F7F2)
    val Sage100 = Color(0xFFE4EBE0)
    val Sage200 = Color(0xFFC9D7BF)
    val Sage300 = Color(0xFFA8C09A)
    val Sage400 = Color(0xFF87A96B)
    val Sage500 = Color(0xFF6B8F52)
    val Sage600 = Color(0xFF557340)
    val Sage700 = Color(0xFF415932)
    val Sage800 = Color(0xFF2E3F23)
    val Sage900 = Color(0xFF1B2615)

    val Terracotta50 = Color(0xFFFBF3EF)
    val Terracotta100 = Color(0xFFF5E0D5)
    val Terracotta200 = Color(0xFFEAC0AA)
    val Terracotta300 = Color(0xFFDDA080)
    val Terracotta400 = Color(0xFFCE8056)
    val Terracotta500 = Color(0xFFC67B5C)
    val Terracotta600 = Color(0xFFA85E42)
    val Terracotta700 = Color(0xFF844833)
    val Terracotta800 = Color(0xFF5C3224)
    val Terracotta900 = Color(0xFF381F15)

    val DustyBlue50 = Color(0xFFF2F6F8)
    val DustyBlue100 = Color(0xFFE0EAEF)
    val DustyBlue200 = Color(0xFFC1D5DF)
    val DustyBlue300 = Color(0xFF9DBDCC)
    val DustyBlue400 = Color(0xFF7C9EB2)
    val DustyBlue500 = Color(0xFF5F8296)
    val DustyBlue600 = Color(0xFF4A6879)
    val DustyBlue700 = Color(0xFF38505E)
    val DustyBlue800 = Color(0xFF263742)
    val DustyBlue900 = Color(0xFF152129)

    val Wheat50 = Color(0xFFFBF7F0)
    val Wheat100 = Color(0xFFF5EBDA)
    val Wheat200 = Color(0xFFEBD7B5)
    val Wheat300 = Color(0xFFDFC08A)
    val Wheat400 = Color(0xFFD4A574)
    val Wheat500 = Color(0xFFC48E54)
    val Wheat600 = Color(0xFFA6713D)
    val Wheat700 = Color(0xFF82562E)
    val Wheat800 = Color(0xFF5C3D20)
    val Wheat900 = Color(0xFF382614)

    val SignalSuccess = Color(0xFF6B8F52)
    val SignalWarn = Color(0xFFD4A574)
    val SignalError = Color(0xFFC67B5C)

    val NodeInput = Color(0xFF87A96B)
    val NodeIntentRouter = Color(0xFF9DBDCC)
    val NodeIfCondition = Color(0xFF6B8F52)
    val NodeClarification = Color(0xFFD4A574)

    val NodeLiteRt = Color(0xFF557340)
    val NodeCloud = Color(0xFF5F8296)
    val NodeTool = Color(0xFFC67B5C)
    val NodeDecomposition = Color(0xFF7C9EB2)
    val NodeQueueProcessor = Color(0xFFA85E42)
    val NodeEvaluation = Color(0xFFCE8056)
    val NodeSummary = Color(0xFF4A6879)
    val NodeOutput = Color(0xFF415932)
    val NodePipeline = Color(0xFF38505E)
}

object AriaLight {
    val Surface0 = Color(0xFFFAF9F6)
    val Surface1 = Color(0xFFF5F4F0)
    val Surface2 = Color(0xFFEEEDE8)
    val Surface3 = Color(0xFFE4E3DD)
    val Surface4 = Color(0xFFD9D8D2)
    val SurfaceInv = Color(0xFF1A1A1A)

    val OnSurface = Color(0xFF1A1A1A)
    val OnSurface2 = Color(0xFF3D3D3D)
    val OnSurfaceMuted = Color(0xFF6B6B6B)
    val OnSurfaceDim = Color(0xFF959595)

    val Outline = Color(0xFFD4D3CE)
    val OutlineStrong = Color(0xFFAFADA8)
    val Divider = Color(0xFFE7E6E2)

    val OnPrimary = Color(0xFFFAF9F6)
    val PrimaryContainer = AriaPalette.Sage100
    val OnPrimaryContainer = AriaPalette.Sage800

    val TertiaryContainer = Color(0xFFE0EAEF)
    val OnTertiaryContainer = Color(0xFF263742)

    val RiskReadonly = Color(0xFF7C9EB2)
    val RiskSensitive = AriaPalette.SignalWarn
    val RiskDestructive = AriaPalette.SignalError

    val ChatUserBg = AriaPalette.Sage100
    val ChatUserFg = AriaPalette.Sage800
    val ChatAgentBg = Surface1
    val ChatAgentFg = OnSurface
    val ChatToolBg = Surface2
    val ChatToolFg = OnSurface2

    val ConsoleBg = Color(0xFF1A1A1A)
    val ConsoleFg = Color(0xFFE7E6E2)
    val ConsoleTag = AriaPalette.Wheat300

    val MemAutoBg = Color(0xFFE0EAEF)
    val MemAutoFg = Color(0xFF263742)
    val MemAutoRail = Color(0xFF5F8296)
    val MemManualBg = AriaPalette.Sage100
    val MemManualFg = AriaPalette.Sage800
    val MemManualRail = AriaPalette.Sage500
    val MemCompactBg = Color(0xFFF5EBDA)
    val MemCompactFg = Color(0xFF5C3D20)
    val MemCompactRail = Color(0xFFC48E54)
}

object AriaDark {
    val Surface0 = Color(0xFF1A1A1A)
    val Surface1 = Color(0xFF222222)
    val Surface2 = Color(0xFF2C2C2C)
    val Surface3 = Color(0xFF363636)
    val Surface4 = Color(0xFF404040)
    val SurfaceInv = Color(0xFFF5F4F0)

    val OnSurface = Color(0xFFF5F4F0)
    val OnSurface2 = Color(0xFFC7C7C7)
    val OnSurfaceMuted = Color(0xFF969696)
    val OnSurfaceDim = Color(0xFF676767)

    val Outline = Color(0xFF414141)
    val OutlineStrong = Color(0xFF636363)
    val Divider = Color(0xFF2C2C2C)

    val OnPrimary = Color(0xFF1A1A1A)
    val PrimaryContainer = Color(0xFF2E3F23)
    val OnPrimaryContainer = AriaPalette.Sage200

    val TertiaryContainer = Color(0xFF263742)
    val OnTertiaryContainer = Color(0xFFC1D5DF)

    val RiskReadonly = Color(0xFF9DBDCC)
    val RiskSensitive = Color(0xFFEBD7B5)
    val RiskDestructive = Color(0xFFEAC0AA)

    val ChatUserBg = Color(0xFF2E3F23)
    val ChatUserFg = AriaPalette.Sage100
    val ChatAgentBg = Surface2
    val ChatAgentFg = OnSurface
    val ChatToolBg = Surface3
    val ChatToolFg = OnSurface2

    val ConsoleBg = Color(0xFF0A0A0A)
    val ConsoleFg = Color(0xFFDADADA)
    val ConsoleTag = AriaPalette.Wheat300

    val MemAutoBg = Color(0xFF263742)
    val MemAutoFg = Color(0xFF9DBDCC)
    val MemAutoRail = Color(0xFF5F8296)
    val MemManualBg = Color(0xFF2E3F23)
    val MemManualFg = AriaPalette.Sage100
    val MemManualRail = AriaPalette.Sage300
    val MemCompactBg = Color(0xFF382614)
    val MemCompactFg = Color(0xFFEBD7B5)
    val MemCompactRail = Color(0xFFC48E54)
}

fun ariaLightColorScheme(): ColorScheme = lightColorScheme(
    primary = AriaPalette.Sage500,
    onPrimary = AriaLight.OnPrimary,
    primaryContainer = AriaLight.PrimaryContainer,
    onPrimaryContainer = AriaLight.OnPrimaryContainer,
    secondary = AriaPalette.Terracotta500,
    onSecondary = AriaLight.OnPrimary,
    secondaryContainer = AriaLight.Surface3,
    onSecondaryContainer = AriaLight.OnSurface,
    tertiary = AriaPalette.DustyBlue400,
    onTertiary = AriaLight.OnPrimary,
    tertiaryContainer = AriaLight.TertiaryContainer,
    onTertiaryContainer = AriaLight.OnTertiaryContainer,
    error = AriaPalette.SignalError,
    onError = AriaLight.OnPrimary,
    errorContainer = Color(0xFFF5E0D5),
    onErrorContainer = Color(0xFF5C3224),
    background = AriaLight.Surface0,
    onBackground = AriaLight.OnSurface,
    surface = AriaLight.Surface0,
    onSurface = AriaLight.OnSurface,
    surfaceVariant = AriaLight.Surface2,
    onSurfaceVariant = AriaLight.OnSurface2,
    surfaceTint = AriaPalette.Sage400,
    inverseSurface = AriaLight.SurfaceInv,
    inverseOnSurface = AriaLight.OnPrimary,
    inversePrimary = AriaPalette.Sage300,
    outline = AriaLight.Outline,
    outlineVariant = AriaLight.Divider,
    scrim = Color(0x66000000),
    surfaceBright = AriaLight.Surface0,
    surfaceDim = AriaLight.Surface3,
    surfaceContainerLowest = AriaLight.Surface0,
    surfaceContainerLow = AriaLight.Surface1,
    surfaceContainer = AriaLight.Surface2,
    surfaceContainerHigh = AriaLight.Surface3,
    surfaceContainerHighest = AriaLight.Surface4,
)

fun ariaDarkColorScheme(): ColorScheme = darkColorScheme(
    primary = AriaPalette.Sage300,
    onPrimary = AriaDark.OnPrimary,
    primaryContainer = AriaDark.PrimaryContainer,
    onPrimaryContainer = AriaDark.OnPrimaryContainer,
    secondary = AriaPalette.Terracotta300,
    onSecondary = AriaDark.OnPrimary,
    secondaryContainer = AriaDark.Surface3,
    onSecondaryContainer = AriaDark.OnSurface,
    tertiary = AriaDark.RiskReadonly,
    onTertiary = AriaDark.OnPrimary,
    tertiaryContainer = AriaDark.TertiaryContainer,
    onTertiaryContainer = AriaDark.OnTertiaryContainer,
    error = AriaDark.RiskDestructive,
    onError = AriaDark.OnPrimary,
    errorContainer = Color(0xFF5C3224),
    onErrorContainer = Color(0xFFF5E0D5),
    background = AriaDark.Surface0,
    onBackground = AriaDark.OnSurface,
    surface = AriaDark.Surface0,
    onSurface = AriaDark.OnSurface,
    surfaceVariant = AriaDark.Surface2,
    onSurfaceVariant = AriaDark.OnSurface2,
    surfaceTint = AriaPalette.Sage400,
    inverseSurface = AriaDark.SurfaceInv,
    inverseOnSurface = AriaDark.OnPrimary,
    inversePrimary = AriaPalette.Sage700,
    outline = AriaDark.Outline,
    outlineVariant = AriaDark.Divider,
    scrim = Color(0x99000000),
    surfaceBright = AriaDark.Surface4,
    surfaceDim = AriaDark.Surface0,
    surfaceContainerLowest = AriaDark.Surface0,
    surfaceContainerLow = AriaDark.Surface1,
    surfaceContainer = AriaDark.Surface2,
    surfaceContainerHigh = AriaDark.Surface3,
    surfaceContainerHighest = AriaDark.Surface4,
)
