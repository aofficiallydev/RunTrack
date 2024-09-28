package com.aofficially.runtrack.ui.runner.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.databinding.ItemRunnerBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.visible
import com.aofficially.runtrack.ui.home.domain.RunnerStatus

class RunnerViewHolder(val binding: ItemRunnerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: RunnerEntity, onLongClickItemListener: ((RunnerEntity) -> Unit)?) {
        binding.apply {
            if (item.runStatus == RunnerStatus.IN_RACE.status) {
                tvBib.setTextColor(Color.BLACK)
            } else {
                tvBib.setTextColor(Color.RED)
            }

            tvBib.text = item.runBid
            tvName.text = "${item.runFirstname} ${item.runLastname}"
            tvTime.text = item.timeIn
            tvDate.text = item.dateIn
            tvGroup.text = "${item.runSex} ${item.runDistance}"

            if (item.isUpLoaded) {
                icUpload.visible()
            } else {
                icUpload.gone()
            }
        }

        binding.root.setOnLongClickListener {
            onLongClickItemListener?.invoke(item)
            true
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup): RunnerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemRunnerBinding.inflate(inflater, parent, false)
            return RunnerViewHolder(binding)
        }
    }
}