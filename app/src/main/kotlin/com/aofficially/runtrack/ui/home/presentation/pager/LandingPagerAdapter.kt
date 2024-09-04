package com.aofficially.runtrack.ui.home.presentation.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aofficially.runtrack.ui.runner.RunnerFragment
import com.aofficially.runtrack.ui.trackrunner.TrackRunnerFragment

class LandingPagerAdapter constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> TrackRunnerFragment.newInstance(isRaceType = true)
        1 -> TrackRunnerFragment.newInstance(isRaceType = false)
        else -> RunnerFragment.newInstance()
    }

    override fun getItemCount(): Int = 3
}