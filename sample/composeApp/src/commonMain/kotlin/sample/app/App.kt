package sample.app

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.IO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectivityTesterApp() {
    val sites = listOf(
        "https://www.google.com",
        "https://www.github.com",
        "https://www.amazon.com"
    )

    val results = remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Connectivity Tester") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val client = HttpClient(CIO)
                        val newResults = sites.associateWith { site ->
                            try {
                                val response: HttpResponse = client.get(site)
                                response.status.value in 200..299
                            } catch (e: Exception) {
                                false
                            }
                        }
                        results.value = newResults
                        client.close()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Test Connectivity")
            }

            Spacer(modifier = Modifier.height(16.dp))

            results.value.forEach { (site, isConnected) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = site, modifier = Modifier.weight(1f))
                    val statusText = if (isConnected) "Online" else "Offline"
                    val color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    Text(text = statusText, color = color)
                }
            }
        }
    }
}



@Composable
fun App() {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            ConnectivityTesterApp()
        }
    }
}