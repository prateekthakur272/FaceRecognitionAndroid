import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FaceEnrollViewModel : ViewModel() {
    var selectedImageBitmap by mutableStateOf<Bitmap?>(null)

    fun setImage(bitmap: Bitmap?) {
        selectedImageBitmap = bitmap
    }
}