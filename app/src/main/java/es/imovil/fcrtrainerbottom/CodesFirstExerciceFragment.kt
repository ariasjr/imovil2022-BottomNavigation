@file:Suppress("RedundantOverride")

package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Inflate the layout for this fragment
        _binding = FragmentCodesFirstExerciceBinding.inflate(inflater, container, false)

        return binding.root
    }


}