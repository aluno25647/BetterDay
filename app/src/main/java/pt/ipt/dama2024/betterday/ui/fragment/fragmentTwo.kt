package pt.ipt.dama2024.betterday.ui.fragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.CatApiResponse
import pt.ipt.dama2024.betterday.retrofit.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTwo : Fragment() {

    private lateinit var catImageView: ImageView
    private lateinit var catTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    /**
     * Called immediately after the view created, to initialize UI components.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catImageView = view.findViewById(R.id.catImageView)
        catTitle = view.findViewById(R.id.catTitle)

        fetchCatImage()
    }

    private fun fetchCatImage() {
        val call = ApiClient.catService.getCatImage()
        call.enqueue(object : Callback<CatApiResponse> {
            override fun onResponse(call: Call<CatApiResponse>, response: Response<CatApiResponse>) {
                if (response.isSuccessful) {
                    val catImageResponse = response.body()
                    if (catImageResponse != null) {
                        Picasso.get()
                            .load(catImageResponse.imageUrl)
                            .into(catImageView)
                    }
                }
            }

            override fun onFailure(call: Call<CatApiResponse>, t: Throwable) {
                t.printStackTrace()
                t.message?.let { Log.e("onFailure error", it) }
            }
        })
    }
}