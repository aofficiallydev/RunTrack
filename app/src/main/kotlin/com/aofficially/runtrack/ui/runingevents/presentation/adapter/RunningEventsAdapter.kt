package com.aofficially.runtrack.ui.runingevents.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel

class RunningEventsAdapter :
    ListAdapter<RunningEventsModel, RunningEventsViewHolder>(RunningEventsDiffCallback()) {

    var onItemClick : ((RunningEventsModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningEventsViewHolder {
        return RunningEventsViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RunningEventsViewHolder, position: Int) {
        holder.bindView(getItem(position), onItemClick)
    }
}