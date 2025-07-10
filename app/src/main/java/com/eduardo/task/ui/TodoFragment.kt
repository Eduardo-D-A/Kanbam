package com.eduardo.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eduardo.task.R
import com.eduardo.task.databinding.FragmentTodoBinding
import com.google.firebase.database.*

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var myDataListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initFirebase()
    }

    private fun initListener() {
        binding.imgHome.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_homeFragment)
        }
        binding.textviewHome.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_homeFragment)
        }
        binding.voltar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance("https://hydrocontrol-126eb-default-rtdb.firebaseio.com")
        myRef = database.getReference("caminho/para/seus/dados")

        myDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val value = snapshot.getValue(String::class.java)
                    println("Valor lido: $value")
                    // Exemplo: binding.textViewResultado.text = value
                } else {
                    println("Nenhum dado encontrado no caminho.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Falha ao ler valor: ${error.message}")
            }
        }

        myRef.addValueEventListener(myDataListener)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Remover listener para evitar vazamento de memória
        if (::myRef.isInitialized && ::myDataListener.isInitialized) {
            myRef.removeEventListener(myDataListener)
        }
        _binding = null
    }
}
