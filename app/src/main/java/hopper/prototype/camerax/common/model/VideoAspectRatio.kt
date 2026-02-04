package hopper.prototype.camerax.common.model

import hopper.prototype.camerax.common.i.ITogglable

enum class VideoAspectRatio(val width: Int, val height: Int): ITogglable<VideoAspectRatio> {
    SQUARE(1, 1),
    FULLSCREEN(4, 3),
    WIDESCREEN(16, 9),
    CINEMATIC_WIDESCREEN(21, 9);

    override val nextValue
        get() = entries[(ordinal + 1) % entries.size]
}
