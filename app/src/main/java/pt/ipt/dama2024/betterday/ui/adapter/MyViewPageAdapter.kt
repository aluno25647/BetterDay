package pt.ipt.dama2024.betterday.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pt.ipt.dama2024.betterday.ui.fragment.FragmentOne
import pt.ipt.dama2024.betterday.ui.fragment.FragmentThree
import pt.ipt.dama2024.betterday.ui.fragment.FragmentTwo
import pt.ipt.dama2024.betterday.ui.fragment.SettingsFragment

/**
 * This class is used to choose the fragment to be presented at 'main_activity.xml'
 *
 * @constructor
 *
 * @param fragmentActivity
 */
class MyViewPageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    /**
     * Gets the number of fragments
     *
     * @return 4
     */
    override fun getItemCount(): Int {
        return 4
    }

    /**
     * Choose the fragment to be used
     *
     * @param position
     * @return fragment
     */
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentOne()
            1-> FragmentTwo()
            2-> FragmentThree()
            3-> SettingsFragment()
            else -> FragmentOne()
        }
    }
}