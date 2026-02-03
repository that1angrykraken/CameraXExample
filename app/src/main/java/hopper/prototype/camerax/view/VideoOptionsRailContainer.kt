package hopper.prototype.camerax.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.graphics.alpha
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import hopper.prototype.camerax.databinding.VideoOptionsRailContainerBinding
import hopper.prototype.camerax.databinding.VideoOptionsRailItemBinding
import hopper.prototype.camerax.model.VideoOption
import kotlin.math.log

class VideoOptionsRailContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var settled = false
    private val binding = VideoOptionsRailContainerBinding
        .inflate(LayoutInflater.from(context), this, true)
    private val itemBindingMap = mutableMapOf<String, VideoOptionsRailItemBinding>()
    private val videoOptionMap = mutableMapOf<String, VideoOption>()
    private var isAnimating = false

    init {
        binding.btnVideoOptionsRailToggle.setOnClickListener {
            setOptionsRailVisible(!binding.videoOptionsRail.isVisible)
        }
    }

    fun setUp(options: List<VideoOption>) {
        if (settled) {
            Log.i(TAG, "setUp: already settled")
            return
        }
        options.forEach { option ->
            val itemBinding = VideoOptionsRailItemBinding
                .inflate(LayoutInflater.from(context), binding.videoOptionsRail, true)
            val key = checkNotNull(option::class.simpleName)
            itemBindingMap[key] = itemBinding
            updateItem(option)
        }
        settled = true
    }

    fun updateItem(option: VideoOption) {
        val key = checkNotNull(option::class.simpleName)
        if (videoOptionMap[key] == option) {
            return
        }
        videoOptionMap[key] = option
        val itemBinding = itemBindingMap[key] ?: run {
            Log.w(TAG, "updateItem: cannot find binding for $key")
            return
        }
        option.iconId?.let {
            itemBinding.imgVideoOptionsRailItem.setImageResource(it)
            itemBinding.imgVideoOptionsRailItem.isVisible = true
            return
        }
        itemBinding.lblVideoOptionsRailItem.text = option.label
        itemBinding.lblVideoOptionsRailItem.isVisible = true
    }

    fun setOnOptionClickListener(listener: (VideoOption) -> Unit) {
        videoOptionMap.forEach { (key, option) ->
            val itemBinding = itemBindingMap[key] ?: run {
                Log.w(TAG, "setOnOptionClickListener: cannot find binding for $key")
                return
            }
            itemBinding.root.setOnClickListener {
                listener(option)
            }
            Log.i(TAG, "setOnOptionClickListener: $option")
        }
    }

    fun setOptionsRailVisible(visible: Boolean, duration: Long = 200, onEnd: (() -> Unit)? = null) {
        val targetVisibility = if (visible) VISIBLE else INVISIBLE
        val railView = binding.videoOptionsRail
        if (isAnimating || railView.visibility == targetVisibility) {
            return
        }
        val railWidth = binding.videoOptionsRail.width
        val (startW, endW) = if (visible) 1 to railWidth else railWidth to 1
        if (visible) {
            railView.visibility = VISIBLE
        }

        ValueAnimator.ofInt(startW, endW).apply {
            this.duration = duration
            addUpdateListener {
                val currentWidth = it.animatedValue as Int
                railView.updateLayoutParams { width = currentWidth }
                railView.translationX = (railWidth - currentWidth).toFloat()
                railView.alpha = (currentWidth / railWidth.toFloat()).coerceAtMost(1f)
            }
            doOnEnd {
                if (!visible) {
                    railView.visibility = INVISIBLE
                    railView.updateLayoutParams { width = railWidth }
                }
                isAnimating = false
                onEnd?.invoke()
            }
        }.start()
        isAnimating = true
    }

    fun hideContainer() {
        Log.i(TAG, "hideContainer: entry")
        if (isAnimating || !binding.btnVideoOptionsRailToggle.isVisible) {
            return
        }
        val hideToggle = {
            binding.btnVideoOptionsRailToggle.apply {
                animate()
                    .alpha(0f)
                    .setDuration(150)
                    .withEndAction {
                        isVisible = false
                        alpha = 1f
                        isAnimating = false
                    }
                    .start()
            }
            isAnimating = true
        }
        if (binding.videoOptionsRail.isVisible) {
            setOptionsRailVisible(false, 100, hideToggle)
        } else {
            hideToggle()
        }
    }

    fun showContainer() {
        binding.btnVideoOptionsRailToggle.apply {
            if (isAnimating || isVisible) {
                return@apply
            }
            alpha = 0f
            isVisible = true
            animate()
                .alpha(1f)
                .setDuration(200)
                .withEndAction {
                    isAnimating = false
                }
                .start()
        }
    }

    companion object {
        private const val TAG = "VideoOptionsRail"
    }
}