package pt.ipt.dama2024.betterday.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import pt.ipt.dama2024.betterday.R
import loadImageFromUrl

class FragmentTwo : Fragment() {

    private lateinit var catImageView: ImageView
    private lateinit var catTitle: TextView
    private var imageUrl = "https://cataas.com/cat"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catImageView = view.findViewById(R.id.catImageView)
        catTitle = view.findViewById(R.id.catTitle)
    }

    override fun onResume() {
        super.onResume()
        loadImageFromUrl(imageUrl, catImageView)
    }
}
