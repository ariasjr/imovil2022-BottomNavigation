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
import es.imovil.fcrtrainerbottom.databinding.FragmentHexadecimalExerciseBinding
import java.util.*

/**
 * Fragmento para el ejercicio hexadecimal del trabajo en grupo de
 * Informática Móvil 2021-2022, PL2
 * @author Andrés García González UO271210
 */
class HexadecimalExerciseFragment : Fragment() {

    // Declaración de variables
    //  Vinculación de vistas
    private var _binding: FragmentHexadecimalExerciseBinding? = null
    private val binding get() = _binding!!

    //  ViewModel asociado
    private lateinit var viewModel: HexadecimalExerciseViewModel

    //  Elementos del layout
    private var mAnswerEditText : EditText? = null
    private var mCheckButton : Button? = null
    private var mChangeDirectionButton : Button? = null
    private var mSolutionButton : Button? = null
    private var mNumberToConvertTextSwitcher : TextSwitcher? = null
    private var mTitleTextView : TextSwitcher? = null

    //  Número a convertir y tipo de conversión
    private var mNumberToConvertString : String = ""
    private var mDirectConversion : Boolean = true

    // Variables usadas para las animaciones tras comprobar resultado
    private var mResult: View? = null
    private var mResultImage: ImageView? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  Inicialización de ViewModel
        viewModel =
            ViewModelProvider(this).get(HexadecimalExerciseViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.hexadecimal)

        // Inflate the layout for this fragment
        _binding = FragmentHexadecimalExerciseBinding.inflate(inflater, container, false)

        // Inicialización de variables
        with(binding) {
            mAnswerEditText              = textViewAnswer
            mCheckButton                 = checkbutton
            mChangeDirectionButton       = change
            mSolutionButton              = seesolution
            mNumberToConvertTextSwitcher = numbertoconvert
            mTitleTextView               = exercisetitle
        }
        // Observers para la persistencia de datos
        viewModel.question.observe(viewLifecycleOwner) {
            mNumberToConvertString = it
            mNumberToConvertTextSwitcher?.setText(it)
        }
        viewModel.directConversion.observe(viewLifecycleOwner) {
            mDirectConversion = it
            setTitle()
        }

        // Result and ResultImage
        mResult = binding.result
        mResultImage = binding.resultimage

        //  Enunciado del ejercicio y número a convertir
        with(mTitleTextView!!) {
            setText(titleString())
            setInAnimation(activity, R.anim.slide_in_right)
            setOutAnimation(activity, R.anim.slide_out_left)
        }
        with(mNumberToConvertTextSwitcher!!) {
            setInAnimation(activity, android.R.anim.slide_in_left)
            setOutAnimation(activity, android.R.anim.slide_out_right)
        }
        // Event handlers
        mCheckButton!!.setOnClickListener {
            checkSolution(mAnswerEditText!!.editableText.toString().trim().lowercase(Locale.US))
        }
        mChangeDirectionButton!!.setOnClickListener {
            viewModel.changeConversionDirection()
            setTitle()
            newQuestion()
        }
        mSolutionButton!!.setOnClickListener {
            showSolution()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Se asegura que haya un número a convertir en el arranque
        if(viewModel.question.value == "")
            newQuestion()
    }

    /**
     * Devuelve la solución para el ejercicio actual
     * @return Solución en String
     */
    private fun obtainSolution() : String {
        return if(mDirectConversion) { // if it's a binary → hex exercise
            viewModel.binaryToHex(mNumberToConvertString)
        } else {                // if it's a hex → binary exercise
            viewModel.hexToBinary(mNumberToConvertString)
        }
    }

    /**
     * Compara la respuesta introducida con la solución del ejercicio y devuelve
     * un valor booleano
     * @param answer Respuesta introducida
     * @return True si la respuesta es igual que la solución, false si no lo es
     */
    private fun isCorrect(answer: String) : Boolean {
        return obtainSolution() == answer.uppercase(Locale.US)
    }

    /**
     * Comprueba si la solución introducida es correcta. Si la respuesta es
     * correcta se generará una nueva pregunta.
     * @param answer Respuesta introducida
     */
    private fun checkSolution(answer: String) {
        if(answer == "" || !isCorrect(answer)) {
            showAnimationAnswer(false)
        } else {
            showAnimationAnswer(true)
            newQuestion()
        }
    }

    /**
     * Cambia el enunciado del ejercicio según el valor devuelto
     * por titleString()
     * @see titleString
     */
    private fun setTitle() {
        mTitleTextView!!.setText(titleString())
    }

    /**
     * Devuelve el enunciado del ejercicio definido en strings.xml
     * dependiendo del tipo de conversión actual.
     * @return "Convierte de binario con %1$d bits a hexadecimal" si
     * mDirectConversion es true, "Convierte de hexadecimal a binario
     * con %1$d bits" si es falso
     */
    private fun titleString(): String {
        val formatStringId : Int = if(mDirectConversion) { // if it's a binary → hex exercise
            R.string.convert_bin_to_hex
        } else {                // if it's a hex → binary exercise
            R.string.convert_hex_to_bin
        }
        return String.format(
            resources.getString(formatStringId),
            level()!!.numberOfBits()
        )
    }

    /**
     * Devuelve el nivel de dificultad actual
     * @return "APRENDIZ", "INICIADO" o "MAESTRO"
     */
    private fun level(): Level? {
        return PreferenceUtils.getLevel(this.requireContext())
    }

    /**
     * Genera una nueva pregunta para el ejercicio actual. El número de bits
     * de la pregunta generada depende de la dificultad seleccionada.
     * @see level
     */
    private fun newQuestion() {
         mAnswerEditText!!.setText("")
         with(viewModel) {
             setNumberOfBits(level()!!.numberOfBits())
             newQuestion()
         }
    }

    /**
     * Muestra la solución al ejercicio actual en el editText
     * usado para introducir respuestas
     */
    private fun showSolution() {
        with(mAnswerEditText!!) {
            setText(obtainSolution())
            setSelection(mAnswerEditText!!.text.length)
        }
    }

    /**
     * Muestra el icono y animación pertinentes dependiendo del
     * valor de correct
     * @param correct Valor booleano, true indica correcto y false indica
     * incorrecto
     */
    private fun showAnimationAnswer(correct: Boolean) {
        // Fade in - fade out
        val animation = AlphaAnimation(0.0f, 1.0f)
        with(animation) {
            duration = 300
            fillBefore = true
            fillAfter = true
            repeatCount = Animation.RESTART
            repeatMode = Animation.REVERSE
        }
        mResult!!.visibility = View.VISIBLE
        mResult!!.startAnimation(animation)
        var drawableId: Int = R.drawable.correct

        if (!correct)
            drawableId = R.drawable.incorrect

        mResultImage!!.setImageDrawable(
            ContextCompat.getDrawable(
                this.requireContext(),
                drawableId
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mResultImage!!.animate().setDuration(300).setInterpolator(mAntovershoot)
                .scaleX(1.5f).scaleY(1.5f)
                .withEndAction { // Back to its original size after the animation's end
                    mResultImage!!.animate().scaleX(1f).scaleY(1f)
                }
        }
        // Set the cursor at the end so that the user can begin deleting the
        // numbers that are not part of the network address
        mAnswerEditText!!.setSelection(mAnswerEditText!!.text.length)
    }


}