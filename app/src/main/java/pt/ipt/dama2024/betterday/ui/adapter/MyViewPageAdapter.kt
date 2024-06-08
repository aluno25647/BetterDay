package pt.ipt.dama2024.betterday.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pt.ipt.dama2024.betterday.ui.fragment.FragmentFive
import pt.ipt.dama2024.betterday.ui.fragment.FragmentFour
import pt.ipt.dama2024.betterday.ui.fragment.FragmentOne
import pt.ipt.dama2024.betterday.ui.fragment.FragmentThree
import pt.ipt.dama2024.betterday.ui.fragment.FragmentTwo

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
     * @return 5
     */
    override fun getItemCount(): Int {
        return 5
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
            3-> FragmentFour()
            4-> FragmentFive()
            else -> FragmentOne()
        }
    }
}