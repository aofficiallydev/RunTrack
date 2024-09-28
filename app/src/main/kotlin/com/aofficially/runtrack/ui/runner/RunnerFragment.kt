package com.aofficially.runtrack.ui.runner

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aofficially.runtrack.R
import com.aofficially.runtrack.base.BaseFragment
import com.aofficially.runtrack.databinding.FragmentMemberBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.extensions.visible
import com.aofficially.runtrack.ui.home.presentation.MainShareViewModel
import com.aofficially.runtrack.ui.runner.adapter.RunnerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunnerFragment :
    BaseFragment<FragmentMemberBinding>(R.layout.fragment_member, FragmentMemberBinding::inflate) {

    private val viewModel: RunnerViewModel by viewModels()
    private val shareViewModel: MainShareViewModel by activityViewModels()
    private val runnerAdapter: RunnerAdapter by lazy { RunnerAdapter() }
    override fun initView() {
        setupView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberList(requireContext())
        fetchRunnerDisplayed()
    }

    private fun setupView() = with(binding) {
        tvTitle.text =
            "${getString(R.string.tab_menu_all)} : ${shareViewModel.getStationName()}"
        rvMember.apply {
            adapter = runnerAdapter
            runnerAdapter.onLongClickItemListener = {
                showResetDialog(it.runBid)
            }
        }
    }

    override fun observeViewModel() {
        viewModel.displayRunners.observe(viewLifecycleOwner) {
            runnerAdapter.submitList(it)
        }

        viewModel.displayRunnerSize.observe(viewLifecycleOwner) {
            binding.tvRunnerStatus.text = getString(R.string.runner_status_all, it.all, it.inRace, it.dnf)
        }

        viewModel.displayRunnerAdded.observe(viewLifecycleOwner) {
            binding.tvRunnerAdded.text = it
        }

        shareViewModel.fetchRunnerList.observe(viewLifecycleOwner) {
            viewModel.getMemberList(requireContext())
            fetchRunnerDisplayed()
        }
    }

    override fun setupListener() {
        binding.editTextLayout.apply {
            edtSearch.doOnTextChanged { text, start, _, _ ->
                if (text.toString().isNotEmpty()) {
                    imgClear.visible()

                    if (text.toString().length >= 2) {
                        viewModel.findRunner(requireContext(), text.toString())
                    }
                } else {
                    imgClear.gone()
                    runnerAdapter.submitList(listOf())
                }

            }

            imgClear.setOnClickWithDebounce {
                edtSearch.setText("")
            }
        }
    }

    private fun fetchRunnerDisplayed() {
        if (binding.editTextLayout.edtSearch.text.toString().isNotEmpty()) {
            viewModel.findRunner(
                requireContext(),
                binding.editTextLayout.edtSearch.text.toString()
            )
        }
    }

    private fun showResetDialog(runBid: String) {
        dialogUtility.showAlertDialog(
            context = requireContext(),
            title = getString(R.string.reset_runner_title),
            message = getString(R.string.reset_runner_des),
            positiveText = getString(R.string.reset),
            onPositive = {
                binding.editTextLayout.edtSearch.setText("")
                viewModel.resetRunner(requireContext(), runBid)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {}
        )
    }

    companion object {
        fun newInstance() = RunnerFragment()
    }
}