package fr.uge.worlddiscover

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.worlddiscover.ui.theme.WorldDiscoverTheme



class IntWrapper(var content: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorldDiscoverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                    HelloWorld("Yassine")
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HelloWorld(name: String) {
    var counter by remember { mutableStateOf(IntWrapper(0)) }
    Log.d("Counter", "La valeur du counter ${counter.content}")

    Column(){
        Column(modifier = Modifier
            .weight(1f)
            .background(color = Color.White)){
            helloWorldMessage(name = name, counter = counter.content)
        }
        Column(modifier = Modifier
            .weight(5f)
            .fillMaxSize()){
            worldMap { counter = if(it) IntWrapper(counter.content + 4) else IntWrapper(counter.content + 1) }
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            DrawMaliFlag()
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            DrawYemenFlag()
        }
    }
}

@Composable
fun helloWorldMessage(name: String, counter: Int){
        Text(
            text = "Hello World $name your counter : $counter",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.Red),
            textAlign = TextAlign.Center
        )
}

@Composable
fun worldMap(mapClick: (Boolean) -> Unit){
    Image(painter = painterResource(id = R.drawable.equirectangular_world_map),
        contentDescription = "World Map",
        modifier = Modifier.pointerInput(Unit){
            detectTapGestures(onTap = {mapClick(false)}, onDoubleTap = {mapClick(true)})
        }
        )
}

@Composable
fun DrawYemenFlag() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }
}

@Composable
fun DrawMaliFlag() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) { // Hauteur du drapeau fixée à 200.dp
        // Bande verte
        Box(
            modifier = Modifier
                .weight(1f) // 1 part
                .fillMaxHeight()
                .background(Color(0xFF009E49)) // Vert du Mali
        )
        // Bande jaune
        Box(
            modifier = Modifier
                .weight(1f) // 1 part
                .fillMaxHeight()
                .background(Color(0xFFFFD100)) // Jaune du Mali
        )
        // Bande rouge
        Box(
            modifier = Modifier
                .weight(1f) // 1 part
                .fillMaxHeight()
                .background(Color(0xFFCE1126)) // Rouge du Mali
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorldDiscoverTheme {
        Greeting("Android")
    }
}