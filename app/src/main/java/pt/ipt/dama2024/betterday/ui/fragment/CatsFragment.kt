package pt.ipt.dama2024.betterday.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipt.dama2024.betterday.R
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class CatsFragment : Fragment() {

    private lateinit var catImageView: ImageView
    private lateinit var catTitle: TextView
    private lateinit var catPositivity: TextView
    private var imageUrl = "https://cataas.com/cat"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cats_fragment, container, false)
    }

    /**
     * Called immediately after the view created, to initialize UI components.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialization of the ImageView and TextViews
        catImageView = view.findViewById(R.id.catImageView)
        catTitle = view.findViewById(R.id.catTitle)
        catPositivity = view.findViewById(R.id.catPositivity)
    }

    /**
     * Called when the Fragment is visible to the user
     */
    override fun onResume() {
        super.onResume()
        // Set placeholder image before loading the actual image
        catImageView.setImageResource(R.drawable.baseline_pets_24)
        loadImageFromUrl(imageUrl, catImageView)
    }

    /**
     * Loads an image from a URL and sets it to the given ImageView.
     *
     * @param imageUrl The URL of the image to load.
     * @param imageView The ImageView where the image will be displayed.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun loadImageFromUrl(imageUrl: String, imageView: ImageView) {
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

}
