package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import pt.ipt.dama2024.betterday.ui.adapter.MyViewPageAdapter
import pt.ipt.dama2024.betterday.R

class MainActivity : AppCompatActivity() {

    // Auxiliary vars.
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: MyViewPageAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initiate the auxiliary vars
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager2)

        // Initiate the ' fragment adapter'
        viewPagerAdapter = MyViewPageAdapter(this)
        // Link the viewPage2 to the Adapter
        viewPager2.adapter = viewPagerAdapter

        // Find the tab that the user clicked
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager2.currentItem = p0!!.position
            }
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })

        // Format the behave of viewpage with the tabLayout
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.getTabAt(position)?.select()
                }
            }
        )
    }

}