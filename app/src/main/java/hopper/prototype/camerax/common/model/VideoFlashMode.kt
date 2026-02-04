package hopper.prototype.camerax.common.model

import androidx.annotation.DrawableRes
import hopper.prototype.camerax.R
import hopper.prototype.camerax.common.i.ITogglable

enum class VideoFlashMode(
    @get:DrawableRes val iconId: Int
): ITogglable<VideoFlashMode> {
    OFF(R.drawable.ic_flash_off),
    AUTO(R.drawable.ic_flash_auto),
    ON(R.drawable.ic_flash_on);

    override val nextValue get() = entries[(ordinal + 1) % entries.size]
}