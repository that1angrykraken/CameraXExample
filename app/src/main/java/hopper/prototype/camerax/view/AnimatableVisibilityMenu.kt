package hopper.prototype.camerax.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible

abstract class AnimatableVisibilityMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    protected var isAnimating = false
    private var onViewDismissCallback: (() -> Unit)? = null

    open fun animateShow() {
        if (isAnimating || isVisible) {
            return
        }

        alpha = 0f
        isVisible = true

        animate()
            .alpha(1f)
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

        animate()
            .alpha(0f)
            .withEndAction {
                isVisible = false
                alpha = 1f
                isAnimating = false
                onViewDismissCallback?.invoke()
            }
            .start()
        isAnimating = true
    }

    /**
     * Set OnMenuDismissCallback. Callback can only be invoke after [animateHide] is finished.
     */
    fun setOnMenuDismissCallback(callback: (() -> Unit)?) {
        onViewDismissCallback = callback
    }
}
