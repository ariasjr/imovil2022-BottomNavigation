@file:Suppress("RedundantOverride")

package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentCodesFirstExerciceBinding



class CodesFirstExerciceFragment : Fragment() {

    private var _binding: FragmentCodesFirstExerciceBinding? = null
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
        _binding = FragmentCodesFirstExerciceBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textExercice1
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }


}