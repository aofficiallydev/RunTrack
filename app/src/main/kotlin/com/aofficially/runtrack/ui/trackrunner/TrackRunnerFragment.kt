package com.aofficially.runtrack.ui.trackrunner

import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aofficially.runtrack.R
import com.aofficially.runtrack.base.BaseFragment
import com.aofficially.runtrack.databinding.FragmentTrackRunnerBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.extensions.visible
import com.aofficially.runtrack.ui.home.presentation.MainShareViewModel
import com.aofficially.runtrack.ui.trackrunner.adapter.TrackRunnerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackRunnerFragment :
    BaseFragment<FragmentTrackRunnerBinding>(
        R.layout.fragment_track_runner,
        FragmentTrackRunnerBinding::inflate
    ) {

    private val viewModel: TrackRunnerViewModel by viewModels()
    private val shareViewModel: MainShareViewModel by activityViewModels()
    private val trackRunnerAdapter: TrackRunnerAdapter by lazy { TrackRunnerAdapter() }

    override fun initView() {
        initValue()
        setupView()
    }

    private fun initValue() {
        arguments?.getBoolean(ARG_TYPE)?.let { isRace ->
            viewModel.setRace(isRace)
            if (isRace) {
                binding.tvTitle.text =
                    "${getString(R.string.tab_menu_in_race)} : ${shareViewModel.getStationName()}"
                binding.root.setBackgroundResource(R.drawable.bg_gradient_white_to_green)
            } else {
                binding.tvTitle.text =
                    "${getString(R.string.tab_menu_dnf)} : ${shareViewModel.getStationName()}"
                binding.root.setBackgroundResource(R.drawable.bg_gradient_white_to_red)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberList(requireContext())
    }

    private fun setupView() = with(binding) {
        rvTrackRunner.adapter = trackRunnerAdapter
        trackRunnerAdapter.onLongClickItemListener = {
            showResetDialog(it.runBid)
        }
    }

    override fun observeViewModel() {
        viewModel.displayRunners.observe(viewLifecycleOwner) {
            trackRunnerAdapter.submitList(it)
            Handler(Looper.myLooper()!!).postDelayed({
                trackRunnerAdapter.notifyDataSetChanged()
            }, 300)
        }

        shareViewModel.fetchRunnerList.observe(viewLifecycleOwner) {
            viewModel.getMemberList(requireContext())
        }
    }

    override fun setupListener() {
        binding.editTextLayout.apply {
            edtSearch.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isNotEmpty()) {
                    imgClear.visible()
                } else {
                    imgClear.gone()
                }

                viewModel.findRunner(requireContext(), text.toString())
            }

            imgClear.setOnClickWithDebounce {
                edtSearch.setText("")
            }
        }
    }

    private fun showResetDialog(runBid: String) {
        dialogUtility.showAlertDialog(
            context = requireContext(),
            title = getString(R.string.reset_runner_title),
            message = getString(R.string.reset_runner_des),
            positiveText = getString(R.string.reset),
            onPositive = {
                viewModel.resetRunner(requireContext(), runBid)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {}
        )
    }

    companion object {
        private const val ARG_TYPE = "ARG_TYPE"
        fun newInstance(isRaceType: Boolean): TrackRunnerFragment {
            val fragment = TrackRunnerFragment()
            val bundle = bundleOf(ARG_TYPE to isRaceType)
            fragment.arguments = bundle
            return fragment
        }
    }
}