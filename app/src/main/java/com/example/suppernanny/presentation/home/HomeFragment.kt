package com.example.suppernanny.presentation.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suppernanny.core.base.BaseFragment
import com.example.suppernanny.core.base.State
import com.example.suppernanny.databinding.FragmentHomeBinding
import com.example.suppernanny.presentation.home.adapter.ChatMessageAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
  FragmentHomeBinding::inflate
) {

  private val viewModel: HomeViewModel by viewModels()
  private var errorDialog: AlertDialog? = null
  private val chatAdapter: ChatMessageAdapter by lazy {
    ChatMessageAdapter(FirebaseAuth.getInstance().currentUser?.uid ?: "random")
  }
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initChatMessageRV()
    handleClicks()
    viewModel.observeMessages(FirebaseAuth.getInstance().currentUser?.uid ?: "random")
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.sendMessageState.collect { state ->
            when (state) {
              is State.Success -> {
                binding.etMessage.text?.clear()
              }

              is State.Error -> {
                showDialog(state.message, "Error")
              }

              else -> Unit
            }
          }
        }

        launch {
          viewModel.observeMessageState.collectLatest {
            when(it) {
              is State.Success -> {
                chatAdapter.submitList(it.data)
                binding.chatsRv.scrollToPosition(it.data.size - 1)
              }
              else -> Unit
            }
          }
        }
      }
    }

  }

  private fun showDialog(message: String, title: String) {
    errorDialog?.dismiss()
    errorDialog = AlertDialog.Builder(requireContext())
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
      }
      .setCancelable(true)
      .create()

    errorDialog?.show()
  }

  private fun initChatMessageRV() {
    binding.apply {
      chatsRv.adapter = chatAdapter
      val layoutManager =  LinearLayoutManager(requireContext())
      layoutManager.stackFromEnd = true
      layoutManager.reverseLayout = false
      chatsRv.layoutManager = layoutManager
    }

  }

  private fun handleClicks() {
    binding.apply {
      sendIv.setOnClickListener {
        val typedMessage = etMessage.text.toString()
        if(typedMessage.isBlank()) return@setOnClickListener
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "random"
        viewModel.sendTextMessage(senderId, typedMessage)
      }
    }
  }
}