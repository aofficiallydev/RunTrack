package com.aofficially.runtrack.ui.runingevents.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aofficially.runtrack.databinding.ItemRunningEventsBinding
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import com.bumptech.glide.Glide

class RunningEventsViewHolder(private val binding: ItemRunningEventsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: RunningEventsModel, onItemClick: ((RunningEventsModel) -> Unit)?) = with(binding){
        tvEventName.text = item.raceName
        tvEventDetail.text = item.raceDetail

        Glide.with(itemView.context)
            .load(item.raceImage)
            .centerCrop()
            .into(imgEventBlur)

        Glide.with(itemView.context)
            .load(item.raceImage)
            .into(imgEvent)

        itemView.setOnClickWithDebounce {
            onItemClick?.invoke(item)
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup): RunningEventsViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = ItemRunningEventsBinding.inflate(inflate, parent, false)
            return RunningEventsViewHolder(binding)
        }
    }
}