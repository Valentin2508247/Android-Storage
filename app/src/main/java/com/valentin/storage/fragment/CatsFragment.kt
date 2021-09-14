package com.valentin.storage.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.valentin.storage.adapter.CatAdapter
import com.valentin.storage.adapter.CatListener
import com.valentin.storage.appComponent
import com.valentin.storage.databinding.FragmentListBinding
import com.valentin.storage.models.Cat
import com.valentin.storage.viewmodels.CatsViewModel
import com.valentin.storage.viewmodels.CatsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatsFragment : Fragment(), CatListener {
    private lateinit var listener: IListFragmentListener
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: CatsViewModelFactory

    @Inject
    lateinit var preferences: SharedPreferences

    private lateinit var viewModel: CatsViewModel
    private val adapter = CatAdapter(this)
    private lateinit var liveData: LiveData<List<Cat>>

    // do not know how to combine different Livedata sources in cat repository, so requery on data
    // source change
    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener {
            pref, key ->
        Log.d(TAG, "Cat fragment preference listener.\nKey: $key")
        when (key) {
            "room" -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    delay(200)
                    liveData.removeObservers(viewLifecycleOwner)
                    liveData = viewModel.cats
                    liveData.observe(viewLifecycleOwner) {
                        Log.d(TAG, "Livedata observe")
                        Log.d(TAG, it.toString())
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireContext().appComponent.inject(this)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CatsViewModel::class.java)

        liveData = viewModel.cats
        liveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Livedata observe")
            Log.d(TAG, it.toString())
            adapter.submitList(it)
        }


        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)


        binding.rvCatList.adapter = adapter
        binding.rvCatList.layoutManager = LinearLayoutManager(requireContext())

        initListeners()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as IListFragmentListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        _binding = null
    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
                listener.goToAddCat()
            }
        }
    }

    override fun deleteCat(cat: Cat) {
        viewModel.deleteCat(cat)
    }

    override fun updateCat(cat: Cat) {
//        cat.age = 15
//        cat.name = "Updated"
//        viewModel.updateCat(cat)
        listener.goToUpdateCat(cat.id)
    }

    interface IListFragmentListener {
        fun goToAddCat()
        fun goToUpdateCat(catId: Int)
    }

    private companion object {
        const val TAG = "CatsFragment"
    }
}