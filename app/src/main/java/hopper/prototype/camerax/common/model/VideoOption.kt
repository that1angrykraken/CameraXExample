package hopper.prototype.camerax.common.model

import androidx.annotation.DrawableRes

sealed class VideoOption {
    open val label: String? = ""
    @get:DrawableRes
    open val iconId: Int? = null

    class Flash(val mode: VideoFlashMode): VideoOption() {
        override val iconId = mode.iconId
    }

    class AspectRatio(val value: VideoAspectRatio) : VideoOption() {
        override val label = "${value.width}:${value.height}"
    }

    data class VideoSize(val resolution: VideoResolution, val fps: Int) : VideoOption() {
        override val label = "$resolution\n$fps"
    }
}
