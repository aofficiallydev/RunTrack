package com.aofficially.runtrack.ui.trackrunner.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aofficially.runtrack.database.RunnerEntity

class TrackRunnerDiffCallback : DiffUtil.ItemCallback<RunnerEntity>() {

    override fun areItemsTheSame(oldItem: RunnerEntity, newItem: RunnerEntity): Boolean {
        return  oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RunnerEntity, newItem: RunnerEntity): Boolean {
        return  oldItem == newItem
    }
}