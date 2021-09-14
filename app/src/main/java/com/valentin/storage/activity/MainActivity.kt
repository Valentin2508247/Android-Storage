package com.valentin.storage.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.valentin.storage.R
import com.valentin.storage.appComponent
import com.valentin.storage.databinding.ActivityMainBinding
import com.valentin.storage.fragment.AddFragment
import com.valentin.storage.fragment.CatsFragment
import com.valentin.storage.fragment.SettingsFragment
import com.valentin.storage.viewmodels.CatsViewModel
import com.valentin.storage.viewmodels.CatsViewModelFactory
import javax.inject.Inject

class MainActivity : ParentActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: CatsViewModelFactory
    private lateinit var viewModel:CatsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CatsViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        initListeners()

        setContentView(binding.root)
    }

    private fun initListeners() {
        binding.apply {
//            fab.setOnClickListener {
//                navigate(R.id.action_listFragment_to_addFragment)
//            }
        }
    }
}