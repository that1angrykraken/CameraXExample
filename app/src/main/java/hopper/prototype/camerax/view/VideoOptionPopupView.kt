package hopper.prototype.camerax.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import hopper.prototype.camerax.common.model.VideoOption

abstract class VideoOptionPopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    protected var isAnimating = false
    protected var onViewDismissCallback: OnDismissCallback? = null
    protected var onOptionUpdateCallback: OnOptionUpdateCallback? = null

    open fun animateShow() {
        if (isAnimating || isVisible) {
            return
        }
        Log.d(TAG, "animateShow")

        alpha = 0f
        isVisible = true

        animate()
            .alpha(1f)
            .setDuration(200)
            .withEndAction {
                isAnimating = false
            }
            .start()
        isAnimating = true
    }

    open fun animateHide() {
        if (isAnimating || !isVisible) {
            return
        }
        Log.d(TAG, "animateHide")

        animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                isVisible = false
                alpha = 1f
                isAnimating = false
                onViewDismissCallback?.onDismiss()
            }
            .start()
        isAnimating = true
    }

    /**
     * Set OnMenuDismissCallback. Callback can only be invoke after [animateHide] is finished.
     */
    fun setOnMenuDismissCallback(callback: OnDismissCallback) {
        onViewDismissCallback = callback
    }

    fun setOnUpdateCallback(callback: OnOptionUpdateCallback) {
        onOptionUpdateCallback = callback
    }

    fun interface OnDismissCallback {
        fun onDismiss()
    }

    fun interface OnOptionUpdateCallback {
        fun onUpdate(option: VideoOption)
    }

    companion object {
        private const val TAG = "VideoOptionPopupView"
    }
}
