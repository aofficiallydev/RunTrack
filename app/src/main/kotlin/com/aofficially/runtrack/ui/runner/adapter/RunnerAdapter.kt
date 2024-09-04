package com.aofficially.runtrack.ui.runner.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aofficially.runtrack.database.RunnerEntity

class RunnerAdapter : ListAdapter<RunnerEntity, RunnerViewHolder>(RunnerDiffCallback()) {

    var onLongClickItemListener: ((RunnerEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunnerViewHolder {
        return RunnerViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: RunnerViewHolder, position: Int) {
        holder.bindView(getItem(position), onLongClickItemListener)
    }
}