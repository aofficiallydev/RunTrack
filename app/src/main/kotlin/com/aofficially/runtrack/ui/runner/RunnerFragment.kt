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
        fun newInstance() = RunnerFragment()
    }
}