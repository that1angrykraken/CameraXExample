package hopper.prototype.camerax.view

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import hopper.prototype.camerax.R
import hopper.prototype.camerax.databinding.TogglableItemViewBinding

class TogglableItemView(context: Context) : FrameLayout(context) {

    private val binding = TogglableItemViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    private val toggledContentColor = context.getColor(R.color.toggled_item_content_color)

    var text = ""
        set(value) {
            if (field == value) {
                return
            }
            field = value
            binding.txtTogglableItem.apply {
                this.text = value
                isVisible = true
            }
        }

    var isSelectedItem = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            val (contentColor, bgId) = when {
                value -> toggledContentColor to R.drawable.overlay_background_dark
                else -> Color.WHITE to 0
            }
            binding.txtTogglableItem.setTextColor(contentColor)
            binding.togglableItem.setBackgroundResource(bgId)
        }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.togglableItem.setOnClickListener(l)
    }
}
