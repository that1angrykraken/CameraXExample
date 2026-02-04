package hopper.prototype.camerax.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import hopper.prototype.camerax.common.model.VideoOption.VideoSize
import hopper.prototype.camerax.common.model.VideoResolution
import hopper.prototype.camerax.common.model.fpsOptions
import hopper.prototype.camerax.databinding.VideoSizePopupViewBinding

class VideoSizePopupView(
    private var option: VideoSize,
    context: Context,
) : VideoOptionPopupView(context) {

    private val binding = VideoSizePopupViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        isVisible = false
        binding.btnClosePopup.setOnClickListener {
            animateHide()
        }
        initResolutionOptions()
        initFpsOptions()
    }

    private fun initResolutionOptions() {
        initOptions(
            options = VideoResolution.entries,
            currentOption = option.resolution,
            container = binding.resolutionOptionsContainer
        ) { resolution ->
            option = option.copy(resolution = resolution)
            onOptionUpdateCallback?.onUpdate(option)
        }
    }

    private fun initFpsOptions() {
        initOptions(
            options = fpsOptions,
            currentOption = option.fps,
            container = binding.fpsOptionsContainer
        ) { fps ->
            option = option.copy(fps = fps)
            onOptionUpdateCallback?.onUpdate(option)
        }
    }

    private fun <T> initOptions(
        options: List<T>,
        currentOption: T,
        container: LinearLayout,
        onOptionSelected: (T) -> Unit
    ) {
        var currentIndex: Int? = null
        options.forEachIndexed { index, item ->
            val itemView = TogglableItemView(context).apply {
                applyDefaultLayoutParams()
                text = item.toString()
                isSelectedItem = currentOption == item
                if (isSelectedItem) {
                    currentIndex = index
                }
                setOnClickListener {
                    if (currentIndex == index) {
                        return@setOnClickListener
                    }
                    currentIndex?.let {
                        container.tryRemoveSelectionAt(it)
                    }
                    currentIndex = index
                    isSelectedItem = true
                    onOptionSelected(item)
                }
            }
            container.addView(itemView)
        }
    }

    private fun LinearLayout.tryRemoveSelectionAt(index: Int) {
        val itemView = getChildAt(index) as? TogglableItemView ?: return
        itemView.isSelectedItem = false
    }

    private fun TogglableItemView.applyDefaultLayoutParams() {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1f
        }
    }
}
