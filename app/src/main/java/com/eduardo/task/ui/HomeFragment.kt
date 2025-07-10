package com.eduardo.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eduardo.task.R
import com.eduardo.task.databinding.FragmentHomeBinding
// Importações necessárias para o Realtime Database
import com.google.firebase.database.*

// Data class para mapear os dados do Firebase.
// Os nomes das variáveis devem ser EXATAMENTE IGUAIS às chaves no seu Realtime Database.
data class SensorData(
    val temp: Double? = null, // Mudamos para 'temp' e para 'Double'
    val turb: Int? = null,    // Mudamos para 'turb'
    val local: String? = null // 'local' ainda não está nos seus dados, mas pode ficar aqui
)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Variáveis para o Firebase Realtime Database
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var sensorDataListener: ValueEventListener // Listener para os dados do sensor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener() // Seus listeners de navegação existentes
        initFirebaseData() // <-- Chamada para iniciar a leitura dos dados do Firebase
    }

    private fun initListener() {
        binding.imgHist.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_todoFragment)
        }
        binding.textviewHist.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_todoFragment)
        }
        binding.voltar.setOnClickListener {
            findNavController().popBackStack()
        }
        // Se você tiver um imgHome e textviewHome no layout do HomeFragment
        // e eles tiverem alguma função (tipo, atualizar a tela ou ir para outro lugar)
        // você pode adicioná-los aqui.
        // Por exemplo, se imgHome e textviewHome estão no rodapé e representam a tela "Início",
        // talvez não precisem de um listener aqui, pois você já está na tela de Início.
        // Ou eles poderiam ser usados para forçar uma atualização ou redefinir algo na tela.
    }

    // NOVO MÉTODO: Configuração e leitura dos dados do Firebase
    private fun initFirebaseData() {
        // Obtenha a instância do Firebase Realtime Database, especificando a URL completa
        database = FirebaseDatabase.getInstance("https://hydrocontrol-126eb-default-rtdb.firebaseio.com")

        // Define a referência para o local dos seus dados no Realtime Database.
        // EXTREMAMENTE IMPORTANTE: AJUSTE ESTE CAMINHO para onde seus dados de sensor estão!
        // Por exemplo, se seus dados estiverem em:
        // { "dadosSensores": { "temperatura": 20, "turbidez": 15, "local": "Praia da Costa" } }
        // Use database.getReference("dadosSensores")
        // Se for aninhado como em:
        // { "locais": { "praia_da_costa": { "temperatura": 20, "turbidez": 15, "local": "Praia da Costa" } } }
        // Use database.getReference("locais/praia_da_costa")
        myRef = database.getReference("users/RKi1XjFz5rNJpFXuPjEJWpHz0PD3")// <-- Mude aqui!

        // Cria o ValueEventListener para ouvir as mudanças nos dados
        sensorDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Este método é chamado sempre que os dados no 'myRef' mudam
                if (snapshot.exists()) {
                    // Tenta converter o DataSnapshot para a nossa data class SensorData
                    val sensorData = snapshot.getValue(SensorData::class.java)

                    if (sensorData != null) {
                        println("Dados do Sensor Recebidos: Temp=${sensorData.temp}, Turb=${sensorData.turb}")

                        // Atualiza os TextViews no seu layout (fragment_home.xml)
                        // valTemp e valTurb são os IDs dos TextViews que você tem no seu XML
                        binding.valTemp.text = sensorData.temp?.toString() ?: "N/A"
                        binding.valTurb.text = sensorData.turb?.toString() ?: "N/A"
                        binding.cardTitle.text = sensorData.local ?: "Local Desconhecido" // Atualiza o título do card

                    } else {
                        // Caso o snapshot exista, mas o mapeamento para SensorData falhe (dados malformados)
                        println("Dados encontrados no Firebase, mas falha ao converter para SensorData.")
                        binding.valTemp.text = "Erro!"
                        binding.valTurb.text = "Erro!"
                        binding.cardTitle.text = "Dados Inválidos"
                    }
                } else {
                    // Caso não haja dados no caminho especificado
                    println("Nenhum dado encontrado no caminho: ${myRef.path}")
                    binding.valTemp.text = "---"
                    binding.valTurb.text = "---"
                    binding.cardTitle.text = "Sem Dados"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Este método é chamado em caso de erro (ex: regras de segurança negando acesso)
                println("Falha ao ler dados do sensor: ${error.message}")
                binding.valTemp.text = "Erro de Conexão"
                binding.valTurb.text = "Erro de Conexão"
                binding.cardTitle.text = "Falha na Leitura"
            }
        }

        // Anexa o listener à referência do banco de dados para começar a receber atualizações
        myRef.addValueEventListener(sensorDataListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // MUITO IMPORTANTE: Remova o listener do Firebase quando a View do Fragment for destruída.
        // Isso evita vazamentos de memória e leituras desnecessárias de dados após o Fragment não estar mais ativo.
        if (::myRef.isInitialized && ::sensorDataListener.isInitialized) {
            myRef.removeEventListener(sensorDataListener)
        }
        _binding = null
    }
}
