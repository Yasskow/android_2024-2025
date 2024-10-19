package fr.uge.android.goodcount

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.stream.IntStream
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val buttonArray = intArrayOf(R.id.plak1, R.id.plak2, R.id.plak3, R.id.plak4, R.id.plak5, R.id.plak6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val draw = findViewById<TextView>(R.id.new_draw)
        draw.performClick()


    }

    fun initiateNumbers(v: View){
        val elemPicker = ElementPicker.buildFromResource(this, R.raw.frequencies){s -> s.toInt()}
        val target = Random.nextInt(101,999)
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = target.toString()
        val plaquettes = elemPicker.pickElements(buttonArray.size)
        IntStream.range(0, buttonArray.size).forEach{ p ->
            run {
                val textView = findViewById<TextView>(buttonArray[p])
                textView.text = plaquettes.get(p).toString()
            }
        }
    }
}