package hopper.prototype.camerax.view

import android.content.Context
import hopper.prototype.camerax.common.model.VideoOption
import hopper.prototype.camerax.common.model.VideoOption.*
import hopper.prototype.camerax.view.VideoOptionPopupView.OnOptionUpdateCallback

class VideoOptionPopupViewFactory(
    val context: Context,
    val option: VideoOption,
    val onOptionUpdateCallback: OnOptionUpdateCallback
) {

    fun create() = when (option) {
        is VideoSize -> VideoSizePopupView(option, context)
        is AspectRatio -> {
            val newOption = AspectRatio(option.value.nextValue)
            onOptionUpdateCallback.onUpdate(newOption)
            null
        }

        is Flash -> {
            val newOption = Flash(option.mode.nextValue)
            onOptionUpdateCallback.onUpdate(newOption)
            null
        }
    }?.apply {
        setOnUpdateCallback(onOptionUpdateCallback)
    }
}
