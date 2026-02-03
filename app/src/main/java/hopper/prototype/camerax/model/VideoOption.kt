package hopper.prototype.camerax.model

import androidx.annotation.DrawableRes

sealed class VideoOption {
    abstract val label: String
    @get:DrawableRes open val iconId: Int? = null

    data class VideoSize(val resolution: VideoResolution, val fps: Int) : VideoOption() {
        override val label = "$resolution\n$fps"
    }
}
