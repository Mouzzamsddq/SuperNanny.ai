package com.example.suppernanny

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.suppernanny.core.base.BaseActivity
import com.example.suppernanny.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
  ActivityMainBinding::inflate
) {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(
        systemBars.left,
        systemBars.top,
        systemBars.right,
        systemBars.bottom
      )
      insets
    }

    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    val navController = navHostFragment.navController

    val navInflater = navController.navInflater
    val graph = navInflater.inflate(R.navigation.nav_graph)
    val startDestinationId = if (FirebaseAuth.getInstance().currentUser != null) {
      R.id.nav_home_fragment
    } else {
      R.id.nav_auth_fragment
    }
    graph.setStartDestination(startDestinationId)

    navController.graph = graph
  }
}