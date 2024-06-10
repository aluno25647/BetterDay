import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(DelicateCoroutinesApi::class)
fun loadImageFromUrl(imageUrl: String, imageView: ImageView) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            launch(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the case when image loading fails
        }
    }
}
