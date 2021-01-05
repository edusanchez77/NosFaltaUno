/*
 * Created by Cba Electronics on 2/10/20 16:38
 * Copyright (c) 2020 . All rights reserved.
 */

package com.cbaelectronics.nosfaltauno.adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cbaelectronics.nosfaltauno.AuthActivity
import com.cbaelectronics.nosfaltauno.LoginTabFragment
import com.cbaelectronics.nosfaltauno.SignupTabFragment

class loginAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    companion object{
        private const val ARG_OBJECT = "object"
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = LoginTabFragment()
        when(position){
            0 -> {
                 fragment = LoginTabFragment()
            }
            1 -> {
                fragment = SignupTabFragment()
            }
        }

        return fragment

    }
}