package pt.ipt.dama2024.betterday.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import pt.ipt.dama2024.betterday.MyViewPageAdapter
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.Database

class MainActivity : AppCompatActivity() {

    // Auxiliary vars.
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: MyViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is authenticated
        if (!isAuthenticated()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

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

    private fun isAuthenticated(): Boolean {
        // Retrieve username and password from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("betterday_username", "")
        val password = sharedPreferences.getString("betterday_password", "")

        // Check if username and password are not empty
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            return false
        }

        // Check authentication status by querying the database
        val db = Database(this)
        val authenticated = db.checkUser(username, password)

        return authenticated
    }
}