package com.aofficially.runtrack.ui.login.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityLoginBinding
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.home.presentation.MainActivity
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity :
    BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        initLoadingView(viewModel.loadingState)
        intent.extras?.getParcelable<RunningEventsModel>(BUNDLE_RUNNING_EVENT)?.let {
            viewModel.setupData(it)
        }
    }

    override fun observeViewModel() {
        viewModel.displayEvent.observe(this) { event ->
            binding.tvEventName.text = event.raceName
            Glide.with(this)
                .load(event.raceImage)
                .into(binding.imgEvent)
        }

        viewModel.navigateToHomePage.observe(this) {
            MainActivity.navigate(this)
        }
    }

    override fun setupListener() {
        binding.btnLogin.setOnClickWithDebounce {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            viewModel.login(username, password)
        }
    }

    companion object {

        private const val BUNDLE_RUNNING_EVENT = "BUNDLE_RUNNING_EVENT"
        fun navigate(context: Context, event: RunningEventsModel) {
            val intent = Intent(context, LoginActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_RUNNING_EVENT, event)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}