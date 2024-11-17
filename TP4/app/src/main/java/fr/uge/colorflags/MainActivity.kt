package fr.uge.colorflags

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.colorflags.model.ColoredPath
import fr.uge.colorflags.model.Country
import fr.uge.colorflags.model.Sketch
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

                    var sketch = Sketch.createEmpty() // create an empty sketch
                    sketch += Color.Black // we create a new path with the color black
                    sketch += Offset(2f, 7f) // we create a first point x=2 y=7
                    sketch += Offset(3f, 9f) // we create a second point: a new segment connects (2,7) to (3,9)
                    sketch += Offset(7f, 11f) // we create a third point and a new segment from (3,9) to (7,11)
                    sketch += Color.Blue

                    Drawer(sketch)
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

@Composable
fun Drawer(sketch: Sketch, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        sketch.paths.forEach { coloredPath ->
            for (i in 1 until coloredPath.size) {
                val start = coloredPath[i - 1]
                val end = coloredPath[i]
                Log.i("TEST", "Drawing line from ($start) to ($end)")
                drawLine(
                    color = coloredPath.color,
                    start = coloredPath[i - 1],
                    end = coloredPath[i],
                    strokeWidth = 10f
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewDrawer() {
    var sketch = Sketch.createEmpty() // create an empty sketch
    sketch += Color.Black // we create a new path with the color black
    sketch += Offset(2f, 7f) // we create a first point x=2 y=7
    sketch += Offset(3f, 9f) // we create a second point: a new segment connects (2,7) to (3,9)
    sketch += Offset(7f, 11f)

    Drawer(sketch = sketch, modifier = Modifier.fillMaxSize().background(Color.White))
}