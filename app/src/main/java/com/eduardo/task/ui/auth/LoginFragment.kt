package com.eduardo.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.eduardo.task.R
import com.eduardo.task.databinding.FragmentLoginBinding
import com.eduardo.task.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.buttonLogin.setOnClickListener {
            validateDate()
        }

        binding.criarConta.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.recuperarConta.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun validateDate() {
        val email = binding.email.text.toString().trim()
        val senha = binding.Senha.text.toString().trim()
        val confirma = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sucesso! O usuário está logado.
                    val user = FirebaseAuth.getInstance().currentUser
                    // Você pode redirecionar o usuário, mostrar uma mensagem de boas-vindas, etc.
                    println("Login bem-sucedido para: ${user?.email}")
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    // Falha no login.
                    // Aqui você trata os erros específicos.
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            println("Erro: Usuário não encontrado ou desativado.")
                            // Mostrar mensagem ao usuário: "E-mail não registrado ou conta desativada."
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            println("Erro: Credenciais inválidas.")
                            // Mostrar mensagem ao usuário: "Senha incorreta ou e-mail/senha inválidos."
                        }
                        else -> {
                            // Outros tipos de erro ou erro genérico.
                            println("Erro desconhecido durante o login: ${task.exception?.message}")
                            // Mostrar mensagem genérica de erro.
                        }
                    }
                }
            }
    }
}