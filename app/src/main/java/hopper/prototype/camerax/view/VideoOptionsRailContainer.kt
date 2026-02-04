package hopper.prototype.camerax.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import hopper.prototype.camerax.common.model.VideoOption
import hopper.prototype.camerax.databinding.VideoOptionsRailContainerBinding
import hopper.prototype.camerax.databinding.VideoOptionsRailItemBinding

class VideoOptionsRailContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = VideoOptionsRailContainerBinding
        .inflate(LayoutInflater.from(context), this, true)
    private val itemBindingMap = mutableMapOf<String, VideoOptionsRailItemBinding>()
    private val videoOptionMap = mutableMapOf<String, VideoOption>()
    private var isAnimating = false

    init {
        binding.btnVideoOptionsRailToggle.setOnClickListener {
            setOptionsRailVisible(!binding.videoOptionsRailWrapper.isVisible)
        }
    }

    fun updateItems(options: List<VideoOption>) {
        options.forEach { option ->
            updateItem(option)
        }
    }

    fun updateItem(option: VideoOption) {
        val key = checkNotNull(option::class.simpleName)
        if (videoOptionMap[key] == option) {
            return
        }
        videoOptionMap[key] = option

        val itemBinding = itemBindingMap[key]
            ?: VideoOptionsRailItemBinding
                .inflate(LayoutInflater.from(context), binding.videoOptionsRail, true)
                .also { itemBindingMap[key] = it }

        option.iconId?.let {
            itemBinding.imgVideoOptionsRailItem.setImageResource(it)
            itemBinding.imgVideoOptionsRailItem.isVisible = true
            return
        }
        option.label?.let {
            itemBinding.lblVideoOptionsRailItem.text = it
            itemBinding.lblVideoOptionsRailItem.isVisible = true
        }
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
        val railView = binding.videoOptionsRailWrapper
        if (isAnimating || railView.visibility == targetVisibility) {
            return
        }
        val railWidth = railView.width
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
        if (binding.videoOptionsRailWrapper.isVisible) {
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