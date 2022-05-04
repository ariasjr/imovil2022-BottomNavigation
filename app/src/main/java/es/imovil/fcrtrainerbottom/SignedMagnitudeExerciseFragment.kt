package es.imovil.fcrtrainerbottom


import android.R
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentSignedMagnitudeExerciseBinding
import es.uniovi.imovil.fcrtrainer.Level
import es.uniovi.imovil.fcrtrainer.PreferenceUtils
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignedMagnitudeExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignedMagnitudeExerciseFragment : Fragment() {
    private var respuestaCorrecta: String? = null

    lateinit var mRandomGenerator : Random
    protected var directConversion = true

    lateinit private var numDecimal : String
    lateinit private var numBinario : String

    lateinit private var labelEnunciado : TextView
    lateinit private var labelPregunta : TextView
    lateinit private var labelRespuesta : TextView
    lateinit private var botonComprobar : Button
    lateinit private var botonSolucion : Button
    lateinit private var botonCambio : Button

    private var result: View? = null
    private var resultImage: ImageView? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    private var _binding: FragmentSignedMagnitudeExerciseBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : SignedMagnitudeExerciseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignedMagnitudeExerciseBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(SignedMagnitudeExerciseViewModel::class.java)


        labelEnunciado = binding.textEnunciado
        labelPregunta = binding.valorEjemplo
        labelRespuesta = binding.textViewAnswer
        botonComprobar = binding.botonComprobar
        botonSolucion = binding.botonSolucion
        botonCambio = binding.botonCambio


        result = binding.result
        resultImage = binding.resultImage


        setEnunciado()
        generarPregunta()


        botonComprobar.setOnClickListener{
            comprobar()
        }

        botonSolucion.setOnClickListener{
            mostrarSolucion()
        }

        botonCambio.setOnClickListener{
            cambioConversion()
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_CORRECT_ANSWER, respuestaCorrecta)
    }

    protected fun generarPregunta(){
        viewModel.setBits(level()!!.numberOfBits())
        viewModel.setDatosNumber()
        numDecimal = viewModel.getDecimal()
        numBinario = viewModel.getBinary()

        if(directConversion){
            binding.valorEjemplo.text = numBinario
            respuestaCorrecta = numDecimal
        }
        else{
            binding.valorEjemplo.text = numDecimal
            respuestaCorrecta = numBinario
        }

    }

    protected fun comprobar(){
        val respuesta = binding.textViewAnswer?.editableText.toString()
        if (isCorrect(respuesta)) {
            //showAnimationAnswer(true)
            generarPregunta()
            binding.textViewAnswer?.setText("")
        } else {
            //showAnimationAnswer(false)
        }
    }

    protected fun mostrarSolucion(){

        binding.textViewAnswer?.setText(respuestaCorrecta)

    }
    protected fun cambioConversion(){
        directConversion = !directConversion
        binding.textViewAnswer?.setText("")
        setEnunciado()
        generarPregunta()
    }
    protected fun isResultNumeric(): Boolean {
        return true
    }



    protected fun setEnunciado() {

        val bits = level()!!.numberOfBits()
        if (directConversion) {
            binding.textEnunciado.text = "Convierte de signo-magnitud a decimal con " + bits+ " bits"
        } else {
            binding.textEnunciado.text = "Convierte de decimal a signo-magnitud con "+bits+" bits"
        }

    }

    protected fun obtainSolution(): String? {
        return respuestaCorrecta
    }

    protected fun isCorrect(answer: String): Boolean {
        return answer == respuestaCorrecta
    }

    companion object {
        private const val STATE_CORRECT_ANSWER = "mCorrectAnswer"
        fun newInstance(): SignedMagnitudeExerciseFragment {
            return SignedMagnitudeExerciseFragment()
        }
    }

    protected fun level(): Level? {
        return PreferenceUtils.getLevel(requireContext())
    }
/*
    protected fun showAnimationAnswer(correct: Boolean) {
        // Fade in - fade out
        result!!.setVisibility(View.VISIBLE)
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 300
        animation.fillBefore = true
        animation.fillAfter = true
        animation.repeatCount = Animation.RESTART
        animation.repeatMode = Animation.REVERSE
        result!!.startAnimation(animation)
        var drawableId: Int = R.drawable.correct

        if (!correct) {
            drawableId = R.drawable.incorrect
        }
        resultImage!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableId))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            resultImage!!.animate().setDuration(300).setInterpolator(mAntovershoot)
                .scaleX(1.5f).scaleY(1.5f)
                .withEndAction(Runnable { // Back to its original size after the animation's end
                    resultImage!!.animate().scaleX(1f).scaleY(1f)
                })
        }
    }
    */

}