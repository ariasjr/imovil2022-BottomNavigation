package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.imovil.fcrtrainerbottom.databinding.FragmentMainDigitalsBinding



class MainDigitalsFragment : Fragment() {


    private var _binding: FragmentMainDigitalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainDigitalsBinding.inflate(inflater, container, false)
        binding.botonLanzarPuertaLogica.setOnClickListener{
            findNavController().navigate(R.id.action_mainDigital_to_puertaLogicas2)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}