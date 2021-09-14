package com.valentin.storage.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.valentin.storage.R
import com.valentin.storage.appComponent
import com.valentin.storage.databinding.FragmentAddBinding
import com.valentin.storage.extensions.callError
import com.valentin.storage.extensions.clearError
import com.valentin.storage.models.Cat
import com.valentin.storage.viewmodels.CatsViewModel
import com.valentin.storage.viewmodels.CatsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random


class UpdateFragment : Fragment() {
    private lateinit var listener: IUpdateFragmentListener
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: CatsViewModelFactory
    private lateinit var viewModel: CatsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireContext().appComponent.inject(this)
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        initViews()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CatsViewModel::class.java)
        initListeners()
        return binding.root
    }

    private fun initViews() {
        binding.btAdd.text = "Update"
        lifecycleScope.launch(Dispatchers.IO) {
            val id = args.idCat
            val cat = viewModel.readCat(id)
            withContext(Dispatchers.Main) {
                binding.apply {
                    cat?.let {
                        inputNameEdit.setText(cat.name)
                        inputAgeEdit.setText(cat.age.toString())
                        inputBreedEdit.setText(cat.breed)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            inputNameEdit.doOnTextChanged { text, start, before, count ->
                inputName.clearError()
            }

            inputAgeEdit.doOnTextChanged { text, start, before, count ->
                inputAge.clearError()
            }

            inputBreedEdit.doOnTextChanged { text, start, before, count ->
                inputBreed.clearError()
            }

            btAdd.setOnClickListener {
                if (inputNameEdit.text.isNullOrEmpty()) {
                    inputName.callError("Enter name")
                    return@setOnClickListener
                }
                val name = inputNameEdit.text.toString()

                if (inputAgeEdit.text.isNullOrEmpty()) {
                    inputAge.callError("Enter age")
                    return@setOnClickListener
                }
                val age = inputAgeEdit.text.toString().toInt()

                if (inputBreedEdit.text.isNullOrEmpty()) {
                    inputBreed.callError("Enter breed")
                    return@setOnClickListener
                }
                val breed = inputBreedEdit.text.toString()

                val cat = Cat(args.idCat, age, name, breed)

                viewModel.updateCat(cat)
                listener.goBack()
            }
        }
    }

    interface IUpdateFragmentListener {
        fun goBack()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as IUpdateFragmentListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}