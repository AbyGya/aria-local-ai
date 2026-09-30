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
import app.knotwork.design.tokens.ariaDarkColorScheme
import app.knotwork.design.tokens.ariaLightColorScheme
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
import app.knotwork.design.tokens.knotworkTypography

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
    chatUserBg = AriaLight.ChatUserBg,
    chatUserFg = AriaLight.ChatUserFg,
    chatAgentBg = AriaLight.ChatAgentBg,
    chatAgentFg = AriaLight.ChatAgentFg,
    chatToolBg = AriaLight.ChatToolBg,
    chatToolFg = AriaLight.ChatToolFg,
    consoleBg = AriaLight.ConsoleBg,
    consoleFg = AriaLight.ConsoleFg,
    consoleTag = AriaLight.ConsoleTag,
    riskReadonly = AriaLight.RiskReadonly,
    riskSensitive = AriaLight.RiskSensitive,
    riskDestructive = AriaLight.RiskDestructive,
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
    memAutoBg = AriaLight.MemAutoBg,
    memAutoFg = AriaLight.MemAutoFg,
    memAutoRail = AriaLight.MemAutoRail,
    memManualBg = AriaLight.MemManualBg,
    memManualFg = AriaLight.MemManualFg,
    memManualRail = AriaLight.MemManualRail,
    memCompactBg = AriaLight.MemCompactBg,
    memCompactFg = AriaLight.MemCompactFg,
    memCompactRail = AriaLight.MemCompactRail,
)

fun ariaExtendedColorsDark(): KnotworkExtendedColors = KnotworkExtendedColors(
    chatUserBg = AriaDark.ChatUserBg,
    chatUserFg = AriaDark.ChatUserFg,
    chatAgentBg = AriaDark.ChatAgentBg,
    chatAgentFg = AriaDark.ChatAgentFg,
    chatToolBg = AriaDark.ChatToolBg,
    chatToolFg = AriaDark.ChatToolFg,
    consoleBg = AriaDark.ConsoleBg,
    consoleFg = AriaDark.ConsoleFg,
    consoleTag = AriaDark.ConsoleTag,
    riskReadonly = AriaDark.RiskReadonly,
    riskSensitive = AriaDark.RiskSensitive,
    riskDestructive = AriaDark.RiskDestructive,
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
    memAutoBg = AriaDark.MemAutoBg,
    memAutoFg = AriaDark.MemAutoFg,
    memAutoRail = AriaDark.MemAutoRail,
    memManualBg = AriaDark.MemManualBg,
    memManualFg = AriaDark.MemManualFg,
    memManualRail = AriaDark.MemManualRail,
    memCompactBg = AriaDark.MemCompactBg,
    memCompactFg = AriaDark.MemCompactFg,
    memCompactRail = AriaDark.MemCompactRail,
)

private object AriaPalette {
    val NodeInput = app.knotwork.design.tokens.AriaPalette.NodeInput
    val NodeIntentRouter = app.knotwork.design.tokens.AriaPalette.NodeIntentRouter
    val NodeIfCondition = app.knotwork.design.tokens.AriaPalette.NodeIfCondition
    val NodeClarification = app.knotwork.design.tokens.AriaPalette.NodeClarification
    val NodeLiteRt = app.knotwork.design.tokens.AriaPalette.NodeLiteRt
    val NodeCloud = app.knotwork.design.tokens.AriaPalette.NodeCloud
    val NodeTool = app.knotwork.design.tokens.AriaPalette.NodeTool
    val NodeDecomposition = app.knotwork.design.tokens.AriaPalette.NodeDecomposition
    val NodeQueueProcessor = app.knotwork.design.tokens.AriaPalette.NodeQueueProcessor
    val NodeEvaluation = app.knotwork.design.tokens.AriaPalette.NodeEvaluation
    val NodeSummary = app.knotwork.design.tokens.AriaPalette.NodeSummary
    val NodeOutput = app.knotwork.design.tokens.AriaPalette.NodeOutput
    val NodePipeline = app.knotwork.design.tokens.AriaPalette.NodePipeline
}

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
