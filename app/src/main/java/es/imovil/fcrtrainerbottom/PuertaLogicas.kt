package es.imovil.fcrtrainerbottom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import es.imovil.fcrtrainerbottom.databinding.FragmentPuertaLogicasBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PuertaLogicas.newInstance] factory method to
 * create an instance of this fragment.
 */
open class PuertaLogicas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentPuertaLogicasBinding? = null
    private val binding get() = _binding!!
    private var correcto=0;
    private  var vistaEstado:View?=null;
    private  var imageviewEstado:ImageView?=null;
    private val vectorImg by lazy{
        resources.obtainTypedArray(R.array.imagenes_puertasLogicas)
    }
    private val vectorEstado by lazy{
        resources.obtainTypedArray(R.array.correctoOIncorrecto)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPuertaLogicasBinding.inflate(inflater, container, false)

        binding.botonComprobarPuertaL.setOnClickListener {
            //todo
            if(binding.spinnerPuertaLogica.selectedItemPosition==correcto){
                var id=0

                val random = Random()
                correcto=random.nextInt(0..7)
                binding.imageView.setImageResource(vectorImg.getResourceId(correcto,0))
                binding.estado.setImageResource(vectorEstado.getResourceId(id,0))
            }
            else {
                var id=1
                binding.estado.setImageResource(vectorEstado.getResourceId(id,0))
            }

        }
        binding.botonSolucionPuertaLogica.setOnClickListener{
            binding.spinnerPuertaLogica.setSelection(correcto)
        }
        return binding.root
    }
    fun Random.nextInt(range: IntRange): Int {
        return range.start + nextInt(range.last - range.start)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PuertaLogicas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PuertaLogicas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    protected fun showAnimationAnswer(correcto: Boolean) {
        vistaEstado?.visibility=View.VISIBLE
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 300
        animation.fillBefore = true
        animation.fillAfter = true
        animation.repeatCount = Animation.RESTART
        animation.repeatMode = Animation.REVERSE
        vistaEstado?.startAnimation(animation)

        var id=R.drawable.correct
        if(correcto!=true){
            id=R.drawable.incorrect
        }
        imageviewEstado?.setImageDrawable(ContextCompat.getDrawable(this as Context,id))
    }
}