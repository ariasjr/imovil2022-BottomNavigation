package es.imovil.fcrtrainerbottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentCodesFirstExerciceBinding
import es.imovil.fcrtrainerbottom.databinding.FragmentNumericalExerciseBinding

class BinaryExerciseFragment : Fragment() {

    private var _binding: FragmentNumericalExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(CodesFirstExerciceViewModel::class.java)
        // Inflate the layout for this fragment
        _binding = FragmentNumericalExerciseBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textViewAnswer;
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }
}