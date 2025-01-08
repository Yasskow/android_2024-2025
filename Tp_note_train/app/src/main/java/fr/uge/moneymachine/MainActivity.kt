package fr.uge.moneymachine

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uge.moneymachine.ui.theme.MoneyMachineTheme
import kotlin.random.Random

val SYMBOLS = arrayOf("🎲", "🏦", "🍒", "🍓", "💰", "🏇", "🥹")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyMachineTheme {
                SlotMachineRolls(symbols = SYMBOLS, 40.sp, centerIndices = (0..6).toList())
            }
        }
    }
}

@Composable
fun SlotMachineRoll(symbols: Array<String>, fontSize: TextUnit, centerIndex: Int) {
    val tmp = (centerIndex+symbols.size/2)%symbols.size + 1
    val newList = symbols.slice(tmp..<symbols.size) + symbols.slice(0..<tmp)

    Column {
        for(elem in newList.indices){
            if(elem == newList.size/2){
                Row(Modifier.background(Color.Yellow)
                    .border(BorderStroke(width = 2.dp, Color.Red)))
                {
                    Text(newList[elem],
                        fontSize = fontSize)
                }
            }else{
                Row {
                    Text(newList[elem], fontSize = fontSize)
                }
            }
        }
    }
}

@Composable
fun SlotMachineRolls(symbols: Array<String>, fontSize: TextUnit, centerIndices: List<Int>){
    Row {
        for(elem in centerIndices.indices){
            if(elem != 0){
                Column(Modifier.fillMaxSize().weight(0.1f).background(Color.Blue)) {
                }
            }
            Column(Modifier.fillMaxSize().weight(1f)) {
                SlotMachineRoll(symbols = symbols, fontSize = fontSize, centerIndices[elem])
            }
        }
    }
}

@Composable
fun SlotMachine(symbols: Array<String>, fontSize: TextUnit, rollNumber: Int, running: Boolean, onDraw: (List<String>) -> Unit){
    var randomValue by remember { mutableStateOf(List(rollNumber){Random.nextInt(0,rollNumber)}) }
    if(running){
        Box(Modifier.background(Color.Green).fillMaxSize()){
            Text("Draw in Progress...", fontSize = fontSize)
        }
        randomValue = List(rollNumber){Random.nextInt(0,rollNumber)}
    }else{
        SlotMachineRolls(symbols, fontSize, randomValue)
    }

    onDraw.invoke(randomValue.map{ on -> symbols[on] }.toList())

}

@Preview
@Composable
fun SlotMachinePreview(){
    var isRunning by remember { mutableStateOf(false) }

    var tmp by remember { mutableStateOf(listOf<String>())}
    Column {
        Column(Modifier.weight(1f).fillMaxSize()){
            SlotMachine(symbols = SYMBOLS, 40.sp, 7, isRunning){
                    a -> tmp = a
            }
        }
        Column(Modifier.weight(1f).fillMaxSize()){
            Button(onClick = {isRunning = !isRunning}, modifier =
                Modifier.align(Alignment.CenterHorizontally))
            {
                Text(if (isRunning) "Stop" else "Start")
            }
            Text("$tmp", Modifier.align(Alignment.CenterHorizontally))
        }
    }
}