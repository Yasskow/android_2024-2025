package fr.uge.colorflags

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.colorflags.model.Country
import fr.uge.colorflags.ui.theme.ColorFlagsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorFlagsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountryGalleryRoot()
                }
            }
        }
    }
}


suspend fun loadCountries(url: String): List<Country> {
    return withContext(Dispatchers.IO){
        val content = URL(url).readText()
        Json.decodeFromString<List<Country>>(content)
    }
}

@Composable
fun CountryGallery(countries: List<Country>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(countries) { country ->
            CountryCard(country)
        }
    }
}

@Composable
fun CountryGalleryRoot(){
    val COUNTRY_SERVER = "http://localhost:8081/"
    var countryCall by remember{ mutableStateOf(listOf<Country>()) }
    LaunchedEffect(true) {
        countryCall = loadCountries(COUNTRY_SERVER + "countries.json")
    }
    CountryGallery(countryCall)
}

fun loadBitmap(url: String) : Bitmap {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.doInput = true
    connection.connect()
    val inputStream = connection.inputStream
    return BitmapFactory.decodeStream(inputStream)

}

@Composable
fun FlagDisplayer(country: Country, modifier: Modifier = Modifier){
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(country.code) {
        bitmap = withContext(Dispatchers.IO) {
            loadBitmap("http://localhost:8081/flags/${country.code}.png")
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "${country.name} flag",
            modifier = modifier
                .size(128.dp)
        )
    } else {
        Box(
            modifier = modifier
                .size(128.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun CountryCard(country: Country) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                FlagDisplayer(country)
            Text(
                text = country.name,
                style = MaterialTheme.typography.bodyLarge
            )
            }
        }
    }
}
