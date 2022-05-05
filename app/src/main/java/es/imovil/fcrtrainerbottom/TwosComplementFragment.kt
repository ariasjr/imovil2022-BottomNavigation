package es.imovil.fcrtrainerbottom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentTwosComplementBinding
import java.util.*


class TwosComplementFragment : Fragment() {

    private var _binding: FragmentTwosComplementBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TwosComplementViewModel

    //Layout
    private var mSolucionEditText: EditText? = null
    private var mCheckButton: Button? = null
    private var mCambiaDireccionButton: Button? = null
    private var mSolucionButton: Button? = null
    private var mNumeroAConvertirTextSwitcher: TextSwitcher? = null
    private var mTituloTextView: TextSwitcher? = null

    private var mNumeroAConvertirString: String = ""
    private var mConversionDirecta = true

    //Animaciones tras checkear resultado
    private var mResultado: View? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null
    private var mImagenResultado: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.twoscomplement)

        //Inicializamos el View Model
        viewModel = ViewModelProvider(this)[TwosComplementViewModel::class.java]

        // Inflate para el layout en este fragmento
        _binding = FragmentTwosComplementBinding.inflate(inflater, container, false)

        //Inicializamos las variables
        mCambiaDireccionButton = binding.changeTwosComplement
        mSolucionButton = binding.solutionTwosComplement
        mCheckButton = binding.checkButtonTwosComplement
        mSolucionEditText = binding.textViewAnswer

        //Observers y animaciones del Numero a convertir
        mNumeroAConvertirTextSwitcher = binding.number2ConvertTwosComplement
        mNumeroAConvertirTextSwitcher!!.setInAnimation(activity, android.R.anim.slide_in_left);
        mNumeroAConvertirTextSwitcher!!.setOutAnimation(activity, android.R.anim.slide_out_right);
        mCambiaDireccionButton!!.setOnClickListener {
            mConversionDirecta = mConversionDirecta xor true
            viewModel.setDirectConversion(mConversionDirecta)
            mTituloTextView!!.setText(titleString())
            newQuestion()
        }

        // Numero y enunciado del ejercicio
        mTituloTextView = binding.titleTwosComplement
        mTituloTextView!!.setText(titleString())
        mTituloTextView!!.setInAnimation(activity, R.anim.slide_in_right);
        mTituloTextView!!.setOutAnimation(activity, R.anim.slide_out_left);

        //Control de eventos
        mCheckButton!!.setOnClickListener {
            checkSolution(mSolucionEditText!!.editableText.toString().trim().lowercase(Locale.US))
        }
        mSolucionButton!!.setOnClickListener {
            mSolucionEditText!!.setText(obtainSolution())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //En el arranque
        mResultado = view.findViewById(R.id.result)
        mImagenResultado = view.findViewById(R.id.resultimage)
        viewModel.setBits(level()!!.numberOfBits())
        newQuestion()
    }

    // Obtiene la solucion al problema actual
    private fun obtainSolution(): String {
        if (mConversionDirecta) //Cuando es de decimal a TwosComplement
            return viewModel.convertToTwosComplement(mNumeroAConvertirString.toString())
        else //Cuando es de TwosComplement a decimal
            return viewModel.convertToDecimal(mNumeroAConvertirString.toString())
    }

    //Devuelve el titulo del fragment dependiendo de la conversion actual
    private fun titleString(): String {
        var formatStringId: Int = if (mConversionDirecta) {//Cuando es de decimal a TwosComplement
            R.string.convert_dec_to_twos_complement
        }else { //Cuando es de TwosComplement a decimal
            R.string.convert_twos_complement_to_dec
        }
        return String.format(
            resources.getString(formatStringId), level()!!.numberOfBits()
        )
    }

    private fun showAnimationAnswer(correct: Boolean) {
        // Fade in - fade out
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 300
        animation.fillBefore = true
        animation.fillAfter = true
        animation.repeatCount = Animation.RESTART
        animation.repeatMode = Animation.REVERSE
        mResultado!!.startAnimation(animation)
        mResultado!!.visibility = View.VISIBLE
        var drawableId: Int = R.drawable.correct

        if (!correct)
            drawableId = R.drawable.incorrect

        mImagenResultado!!.setImageDrawable(
            ContextCompat.getDrawable(
                this.requireContext(),
                drawableId
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mImagenResultado!!.animate().setDuration(300).setInterpolator(mAntovershoot)
                .scaleX(1.5f).scaleY(1.5f)
                .withEndAction {
                    mImagenResultado!!.animate().scaleX(1f).scaleY(1f)
                }
        }
    }

    // Genera una nueva pregunta
    private fun newQuestion() {
        viewModel.setDirectConversion(mConversionDirecta)
        var nuevaPregunta = viewModel.nuevaPregunta()
        mNumeroAConvertirTextSwitcher?.setText(nuevaPregunta)
        mNumeroAConvertirString = nuevaPregunta
        mSolucionEditText!!.setText("")
    }

    // Retorna true si la respuesta es igual que la solucion, false en otro caso.
    private fun isCorrect(answer: String): Boolean {
        return obtainSolution() == answer
    }

    //Hace las acciones necesarias dependiendo de si la solucion es correcta o no
    private fun checkSolution(answer: String) {
        if (answer.equals("") || !isCorrect(answer))
            showAnimationAnswer(false);
        else {
            showAnimationAnswer(true);
            newQuestion();
        }
    }

    // Le pide a la clase Level el nivel de dificultad actual
    private fun level(): Level? {
        return activity?.let { PreferenceUtils.getLevel(it) }
    }

}