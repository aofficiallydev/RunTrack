package com.aofficially.runtrack.ui.trackrunner.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.aofficially.runtrack.database.RunnerEntity

class TrackRunnerAdapter : ListAdapter<RunnerEntity, TrackRunnerViewHolder>(TrackRunnerDiffCallback()) {

    var onLongClickItemListener: ((RunnerEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackRunnerViewHolder {
        return TrackRunnerViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: TrackRunnerViewHolder, position: Int) {
        holder.bindView(getItem(position), onLongClickItemListener, position)
    }
}