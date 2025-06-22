package com.eduardo.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.eduardo.task.R
import com.eduardo.task.databinding.FragmentRegisterBinding
import com.eduardo.task.util.initToolbar
import com.eduardo.task.util.showBottomSheet


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolBar)
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.buttonRegister.setOnClickListener {
            validateDate()
        }
    }

    private fun validateDate() {
        val email = binding.email.text.toString().trim()
        val senha = binding.Senha.text.toString().trim()

        if (email.isNotBlank()){
            if (senha.isNotBlank()) {
                Toast.makeText(requireContext(), "Tudo Ok!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
            }
            else {
                showBottomSheet(message = getString(R.string.password_empty_register_fragment))
            }
        }
        else{
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }
}