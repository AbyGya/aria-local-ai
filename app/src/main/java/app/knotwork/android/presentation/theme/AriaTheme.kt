package app.knotwork.android.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import app.knotwork.design.a11y.KnotworkA11y
import app.knotwork.design.a11y.LocalKnotworkA11y
import app.knotwork.design.tokens.AriaDark
import app.knotwork.design.tokens.AriaLight
import app.knotwork.design.tokens.AriaPalette
import app.knotwork.design.tokens.DefaultKnotworkElevation
import app.knotwork.design.tokens.DefaultKnotworkMotion
import app.knotwork.design.tokens.DefaultKnotworkShapes
import app.knotwork.design.tokens.DefaultKnotworkSpacing
import app.knotwork.design.tokens.KnotworkElevation
import app.knotwork.design.tokens.KnotworkExtendedColors
import app.knotwork.design.tokens.KnotworkMotion
import app.knotwork.design.tokens.KnotworkShapes
import app.knotwork.design.tokens.KnotworkSpacing
import app.knotwork.design.tokens.LocalKnotworkElevation
import app.knotwork.design.tokens.LocalKnotworkExtendedColors
import app.knotwork.design.tokens.LocalKnotworkMotion
import app.knotwork.design.tokens.LocalKnotworkShapes
import app.knotwork.design.tokens.LocalKnotworkSpacing
import app.knotwork.design.tokens.MaterialKnotworkShapes
import app.knotwork.design.tokens.ariaDarkColorScheme
import app.knotwork.design.tokens.ariaLightColorScheme
import app.knotwork.design.tokens.knotworkTypography

/**
 * Minimalist Organic theme for Aria, wired into the Knotwork design-system
 * slots so every existing screen keeps working while the palette changes.
 */
@Composable
fun AriaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) ariaDarkColorScheme() else ariaLightColorScheme()
    val extended = if (darkTheme) ariaExtendedColorsDark() else ariaExtendedColorsLight()
    CompositionLocalProvider(
        LocalKnotworkExtendedColors provides extended,
        LocalKnotworkSpacing provides DefaultKnotworkSpacing,
        LocalKnotworkShapes provides DefaultKnotworkShapes,
        LocalKnotworkElevation provides DefaultKnotworkElevation,
        LocalKnotworkMotion provides DefaultKnotworkMotion,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = knotworkTypography(),
            shapes = MaterialKnotworkShapes,
            content = content,
        )
    }
}

fun ariaExtendedColorsLight(): KnotworkExtendedColors = KnotworkExtendedColors(
    surface1 = AriaLight.Surface1,
    surface2 = AriaLight.Surface2,
    surface3 = AriaLight.Surface3,
    surface4 = AriaLight.Surface4,
    outlineStrong = AriaLight.OutlineStrong,
    divider = AriaLight.Divider,
    onSurface2 = AriaLight.OnSurface2,
    onSurfaceMuted = AriaLight.OnSurfaceMuted,
    onSurfaceDim = AriaLight.OnSurfaceDim,
    chatUserBg = AriaLight.ChatUserBg,
    chatUserFg = AriaLight.ChatUserFg,
    chatAgentBg = AriaLight.ChatAgentBg,
    chatAgentFg = AriaLight.ChatAgentFg,
    chatToolBg = AriaLight.ChatToolBg,
    chatToolFg = AriaLight.ChatToolFg,
    riskReadonly = AriaLight.RiskReadonly,
    riskSensitive = AriaLight.RiskSensitive,
    riskDestructive = AriaLight.RiskDestructive,
    signalSuccess = AriaPalette.SignalSuccess,
    signalWarn = AriaPalette.SignalWarn,
    signalError = AriaPalette.SignalError,
    memAutoBg = AriaLight.MemAutoBg,
    memAutoFg = AriaLight.MemAutoFg,
    memAutoRail = AriaLight.MemAutoRail,
    memManualBg = AriaLight.MemManualBg,
    memManualFg = AriaLight.MemManualFg,
    memManualRail = AriaLight.MemManualRail,
    memCompactBg = AriaLight.MemCompactBg,
    memCompactFg = AriaLight.MemCompactFg,
    memCompactRail = AriaLight.MemCompactRail,
    consoleBg = AriaLight.ConsoleBg,
    consoleFg = AriaLight.ConsoleFg,
    consoleTag = AriaLight.ConsoleTag,
    nodeInput = AriaPalette.NodeInput,
    nodeIntentRouter = AriaPalette.NodeIntentRouter,
    nodeIfCondition = AriaPalette.NodeIfCondition,
    nodeClarification = AriaPalette.NodeClarification,
    nodeLiteRt = AriaPalette.NodeLiteRt,
    nodeCloud = AriaPalette.NodeCloud,
    nodeTool = AriaPalette.NodeTool,
    nodeDecomposition = AriaPalette.NodeDecomposition,
    nodeQueueProcessor = AriaPalette.NodeQueueProcessor,
    nodeEvaluation = AriaPalette.NodeEvaluation,
    nodeSummary = AriaPalette.NodeSummary,
    nodeOutput = AriaPalette.NodeOutput,
    nodePipeline = AriaPalette.NodePipeline,
)

fun ariaExtendedColorsDark(): KnotworkExtendedColors = KnotworkExtendedColors(
    surface1 = AriaDark.Surface1,
    surface2 = AriaDark.Surface2,
    surface3 = AriaDark.Surface3,
    surface4 = AriaDark.Surface4,
    outlineStrong = AriaDark.OutlineStrong,
    divider = AriaDark.Divider,
    onSurface2 = AriaDark.OnSurface2,
    onSurfaceMuted = AriaDark.OnSurfaceMuted,
    onSurfaceDim = AriaDark.OnSurfaceDim,
    chatUserBg = AriaDark.ChatUserBg,
    chatUserFg = AriaDark.ChatUserFg,
    chatAgentBg = AriaDark.ChatAgentBg,
    chatAgentFg = AriaDark.ChatAgentFg,
    chatToolBg = AriaDark.ChatToolBg,
    chatToolFg = AriaDark.ChatToolFg,
    riskReadonly = AriaDark.RiskReadonly,
    riskSensitive = AriaDark.RiskSensitive,
    riskDestructive = AriaDark.RiskDestructive,
    signalSuccess = AriaPalette.SignalSuccess,
    signalWarn = AriaPalette.SignalWarn,
    signalError = AriaPalette.SignalError,
    memAutoBg = AriaDark.MemAutoBg,
    memAutoFg = AriaDark.MemAutoFg,
    memAutoRail = AriaDark.MemAutoRail,
    memManualBg = AriaDark.MemManualBg,
    memManualFg = AriaDark.MemManualFg,
    memManualRail = AriaDark.MemManualRail,
    memCompactBg = AriaDark.MemCompactBg,
    memCompactFg = AriaDark.MemCompactFg,
    memCompactRail = AriaDark.MemCompactRail,
    consoleBg = AriaDark.ConsoleBg,
    consoleFg = AriaDark.ConsoleFg,
    consoleTag = AriaDark.ConsoleTag,
    nodeInput = AriaPalette.NodeInput,
    nodeIntentRouter = AriaPalette.NodeIntentRouter,
    nodeIfCondition = AriaPalette.NodeIfCondition,
    nodeClarification = AriaPalette.NodeClarification,
    nodeLiteRt = AriaPalette.NodeLiteRt,
    nodeCloud = AriaPalette.NodeCloud,
    nodeTool = AriaPalette.NodeTool,
    nodeDecomposition = AriaPalette.NodeDecomposition,
    nodeQueueProcessor = AriaPalette.NodeQueueProcessor,
    nodeEvaluation = AriaPalette.NodeEvaluation,
    nodeSummary = AriaPalette.NodeSummary,
    nodeOutput = AriaPalette.NodeOutput,
    nodePipeline = AriaPalette.NodePipeline,
)

object AriaTheme {
    val extended: KnotworkExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkExtendedColors.current

    val spacing: KnotworkSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkSpacing.current

    val shapes: KnotworkShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkShapes.current

    val elevation: KnotworkElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkElevation.current

    val motion: KnotworkMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkMotion.current

    val a11y: KnotworkA11y
        @Composable
        @ReadOnlyComposable
        get() = LocalKnotworkA11y.current
}
