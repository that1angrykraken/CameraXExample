package hopper.prototype.camerax

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hopper.prototype.camerax.common.model.VideoAspectRatio
import hopper.prototype.camerax.common.model.VideoFlashMode
import hopper.prototype.camerax.common.model.VideoOption
import hopper.prototype.camerax.common.model.VideoOption.AspectRatio
import hopper.prototype.camerax.common.model.VideoOption.Flash
import hopper.prototype.camerax.common.model.VideoOption.VideoSize
import hopper.prototype.camerax.common.model.VideoResolution
import hopper.prototype.camerax.until.hasSameTypeAs
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _videoOptions = MutableStateFlow(
        listOf(
            Flash(VideoFlashMode.OFF),
            AspectRatio(VideoAspectRatio.WIDESCREEN),
            VideoSize(VideoResolution.FHD, 60)
        )
    )
    val videoOptions = _videoOptions.asLiveData()

    fun updateOption(option: VideoOption) {
        Log.i(TAG, "updateOption: $option")
        viewModelScope.launch {
            val index = _videoOptions.value.indexOfFirst { it.hasSameTypeAs(option) }
            if (index == -1) {
                return@launch
            }
            _videoOptions.value = _videoOptions.value.toMutableList().apply {
                this[index] = option
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
