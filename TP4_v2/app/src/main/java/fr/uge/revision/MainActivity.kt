package fr.uge.revision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.uge.revision.ui.theme.RevisionTheme
import kotlinx.serialization.json.Json
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RevisionTheme {

            }
        }
    }
}

// return the countries loaded from the given url
fun loadCountries(url: String): List<Country>{
    val content = URL(url).readText()
    return Json.decodeFromString<List<Country>>(content)
}