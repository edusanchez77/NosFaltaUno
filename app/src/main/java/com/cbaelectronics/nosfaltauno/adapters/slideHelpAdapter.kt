/**
 * Created by Cba Electronics on 12/08/20 18:23
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 12/08/20 18:23
 */

package com.cbaelectronics.nosfaltauno.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cbaelectronics.nosfaltauno.HelpFragment1
import com.cbaelectronics.nosfaltauno.HelpFragment2
import com.cbaelectronics.nosfaltauno.HelpFragment3
import com.cbaelectronics.nosfaltauno.HelpFragment4

class slideHelpAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    private val fragments = listOf(
        HelpFragment1(),
        HelpFragment2(),
        HelpFragment3(),
        HelpFragment4()
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}