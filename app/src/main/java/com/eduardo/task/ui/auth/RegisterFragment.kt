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
import com.google.firebase.auth.FirebaseAuth


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
        binding.Entrar.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
        }
    }

    private fun validateDate() {
        val email = binding.email.text.toString().trim()
        val senha = binding.Senha.text.toString()
        val confir = binding.confirmaSenha.text.toString()

        if (email.isNotBlank()){
            if (senha.isNotBlank()) {
                if (senha.contentEquals(confir)){
                    Toast.makeText(requireContext(), "Conta criada!\nConfirme seu email", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.email.text.toString().trim(), binding.Senha.text.toString())
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
                }
                else {
                    showBottomSheet(message = getString(R.string.password_differ_register_fragment))
                }
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