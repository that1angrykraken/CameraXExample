package hopper.prototype.camerax

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hopper.prototype.camerax.common.model.VideoOption
import hopper.prototype.camerax.common.model.VideoResolution
import hopper.prototype.camerax.view.VideoOptionPopupViewFactory
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

        val vm by viewModels<MainViewModel>()
        val popupViewHolder = findViewById<FrameLayout>(R.id.videoOptionPopupViewHolder)
        val container = findViewById<VideoOptionsRailContainer>(R.id.optionsRailContainer).apply {
            vm.videoOptions.observe(this@MainActivity) {
                updateItems(it)
                setOnOptionClickListener {
                    val popupView = VideoOptionPopupViewFactory(this@MainActivity, it, vm::updateOption)
                        .create()
                        ?: return@setOnOptionClickListener
                    popupViewHolder.addView(popupView)
                    popupView.setOnMenuDismissCallback {
                        popupViewHolder.removeView(popupView)
                        Log.d(TAG, "onCreate: popup dismissed")
                    }
                    popupView.animateShow()
                }
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

    companion object {
        private const val TAG = "MainActivity"
    }
}