package com.aofficially.runtrack.ui.runingevents.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel

class RunningEventsDiffCallback: DiffUtil.ItemCallback<RunningEventsModel>() {

    override fun areItemsTheSame(
        oldItem: RunningEventsModel,
        newItem: RunningEventsModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: RunningEventsModel,
        newItem: RunningEventsModel
    ): Boolean {
        return oldItem == newItem
    }
}