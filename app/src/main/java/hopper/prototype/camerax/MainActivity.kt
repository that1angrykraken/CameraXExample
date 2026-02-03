package hopper.prototype.camerax

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hopper.prototype.camerax.model.VideoOption
import hopper.prototype.camerax.model.VideoResolution
import hopper.prototype.camerax.view.VideoOptionsRailContainer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val container = findViewById<VideoOptionsRailContainer>(R.id.optionsRailContainer).apply {
            setUp(listOf(VideoOption.VideoSize(VideoResolution.FHD, 60)))
            setOnOptionClickListener {

            }
        }
        val showBtn = findViewById<Button>(R.id.btnShowContainer).apply {
            setOnClickListener {
                container.showContainer()
            }
        }
        val hideBtn = findViewById<Button>(R.id.btnHideContainer).apply {
            setOnClickListener {
                container.hideContainer()
            }
        }
    }
}