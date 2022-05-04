package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.imovil.fcrtrainerbottom.databinding.FragmentMainCodesBinding



class MainCodesFragment : Fragment() {

    private var _binding: FragmentMainCodesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainCodesBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_mainCodesFragment_to_codesFirstExerciceFragment)
        }

        binding.binarioButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainCodes_to_binaryExerciseFragment2);
        }
        
        binding.buttonHexadecimal.setOnClickListener {
            findNavController().navigate(R.id.action_mainCodesFragment_to_hexadecimalExerciseFragment)
        }
         
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}