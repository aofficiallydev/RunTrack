package com.aofficially.runtrack.ui.trackrunner.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aofficially.runtrack.R
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.databinding.ItemRunnerBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.visible
import com.aofficially.runtrack.ui.home.domain.RunnerStatus

class TrackRunnerViewHolder(val binding: ItemRunnerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(
        item: RunnerEntity,
        onLongClickItemListener: ((RunnerEntity) -> Unit)?,
        position: Int
    ) {
        binding.apply {
            if (position == 0) {
                itemHeaderLayout.visible()
                itemRunnerLayout.gone()

                if (item.runStatus == RunnerStatus.IN_RACE.status) {
                    tvBibHeader.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green_runnder_header
                        )
                    )
                    tvTimeHeader.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green_runnder_header
                        )
                    )
                } else {
                    tvBibHeader.setTextColor(Color.RED)
                    tvTimeHeader.setTextColor(Color.RED)
                }

                if (item.isUpLoaded) {
                    icUploadHeader.visible()
                } else {
                    icUploadHeader.gone()
                }
            } else {
                itemHeaderLayout.gone()
                itemRunnerLayout.visible()
                tvBib.setTextColor(Color.BLACK)

                if (item.isUpLoaded) {
                    icUpload.visible()
                } else {
                    icUpload.gone()
                }
            }

            tvBib.text = item.runBid
            tvBibHeader.text = item.runBid

            tvName.text = "${item.runFirstname} ${item.runLastname}"
            tvNameHeader.text = "${item.runFirstname} ${item.runLastname}"

            tvTime.text = item.timeInt
            tvTimeHeader.text = item.timeInt

            tvDate.text = item.dateIn

            tvGroup.text = "${item.runSex} ${item.runDistance}"
            tvGroupHeader.text = "${item.runSex} ${item.runDistance}"
        }

        binding.root.setOnLongClickListener {
            onLongClickItemListener?.invoke(item)
            true
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup): TrackRunnerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemRunnerBinding.inflate(inflater, parent, false)
            return TrackRunnerViewHolder(binding)
        }
    }
}