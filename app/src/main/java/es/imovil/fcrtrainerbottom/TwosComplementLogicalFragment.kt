package es.imovil.fcrtrainerbottom

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentDigitalsOperacionLogicaBinding
import es.imovil.fcrtrainerbottom.databinding.FragmentTwosComplementLogicalBinding
import java.util.*

class TwosComplementLogicalFragment : Fragment() {

    private var _binding: FragmentTwosComplementLogicalBinding? = null
    private val binding get() = _binding!!

    private var mResult: View? = null
    private var mResultImage: ImageView? = null

    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        crearPregunta()
    }

    private fun showAnimationAnswer(correct: Boolean) {
        // Fade in - fade out
        mResult!!.visibility = View.VISIBLE
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 300
        animation.fillBefore = true
        animation.fillAfter = true
        animation.repeatCount = Animation.RESTART
        animation.repeatMode = Animation.REVERSE
        mResult!!.startAnimation(animation)
        var drawableId: Int = R.drawable.correct
        if (!correct) {
            drawableId = R.drawable.incorrect
        }
        mResultImage!!.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), drawableId))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mResultImage!!.animate().setDuration(300).setInterpolator(mAntovershoot)
                .scaleX(1.5f).scaleY(1.5f)
                .withEndAction { // Back to its original size after the animation's end
                    mResultImage!!.animate().scaleX(1f).scaleY(1f)
                }
        }
    }

    private var mTvEntrada1: TextView? = null
    private var mTvEntrada2: TextView? = null
    private var mTvOperacion: TextView? = null
    private var mEtRespuesta: EditText? = null
    private var mButtonCheck: Button? = null
    private var mButtonSolucion: Button? = null
    lateinit private var mRandom: Random
    private var mSolucion: String? = null

    private lateinit var viewModel: DigitalsOperacionLogicaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DigitalsOperacionLogicaViewModel::class.java)
        _binding =  FragmentTwosComplementLogicalBinding.inflate(inflater, container, false)

        mTvEntrada1 = binding.LOentrada1
        viewModel.entrada1.observe(viewLifecycleOwner) {
            mTvEntrada1?.setText(it)
        }
        mTvEntrada2 = binding.LOentrada2
        viewModel.entrada2.observe(viewLifecycleOwner) {
            mTvEntrada2?.setText(it)
        }
        mTvOperacion = binding.LOoperacion
        viewModel.operacion.observe(viewLifecycleOwner) {
            mTvOperacion?.setText(it)
        }
        mEtRespuesta = binding.textViewAnswer

        mButtonCheck = binding.LObCalcular
        mButtonCheck!!.setOnClickListener{
            checkAnswer()
        }
        mButtonSolucion = binding.LObSolucion
        viewModel.solucion.observe(viewLifecycleOwner) {
            mSolucion = it
        }
        mButtonSolucion!!.setOnClickListener {
            showSolution()
        }
        mRandom = Random()

        mResult = binding.result
        mResultImage = binding.resultimage

        (activity as AppCompatActivity).supportActionBar?.title = "Operación Lógica"
        return binding.root
    }

    private fun crearPregunta() {
        viewModel.setNumberOfBits(level()!!.numberOfBits())
        viewModel.cargarPregunta()

        mEtRespuesta?.setText("")
    }

    protected fun level(): Level? {
        return PreferenceUtils.getLevel(this.requireContext())
    }

    private fun checkAnswer() {
        isCorrect(mEtRespuesta?.getEditableText().toString().trim { it <= ' ' })
    }

    // Determina si la respuesta es correcta
    fun isCorrect(answer: String) {
        if (answer == mSolucion) {
            showAnimationAnswer(true)
            // si se acierta la respuesta, crear y cargar otra pregunta
            crearPregunta()
        } else {
            showAnimationAnswer(false)
        }
    }

    private fun showSolution() {
        mEtRespuesta?.setText(mSolucion)
    }

}