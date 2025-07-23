package com.example.suppernanny.presentation.home

import android.os.Bundle
import android.view.View
import com.example.suppernanny.core.base.BaseFragment
import com.example.suppernanny.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(
  FragmentHomeBinding::inflate
) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

  }
}