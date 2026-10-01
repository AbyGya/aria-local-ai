package app.knotwork.android.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.knotwork.android.presentation.theme.AriaTheme

/**
 * Launcher activity for Aria.
 *
 * Installs the Minimalist Organic theme around the composable tree and hands
 * off to [AriaApp]. Declared `singleTask` in the manifest so a launcher tap
 * reuses this instance instead of stacking a second one.
 */
class AriaMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AriaTheme {
                AriaApp()
            }
        }
    }
}

@Composable
fun AriaApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        AriaHomeScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
