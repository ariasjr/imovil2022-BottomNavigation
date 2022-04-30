package es.imovil.fcrtrainerbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.imovil.fcrtrainerbottom.databinding.FragmentNetworkMainBinding



class NetworkMainFragment : Fragment() {

    private var _binding: FragmentNetworkMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNetworkMainBinding.inflate(inflater, container, false)

        binding.buttonCIDR.setOnClickListener {
            findNavController().navigate(R.id.action_mainNetwork_to_CIDRFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}