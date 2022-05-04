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
import es.imovil.fcrtrainerbottom.databinding.FragmentCodesFirstExerciceBinding
import es.imovil.fcrtrainerbottom.databinding.FragmentHexadecimalExerciseBinding
import java.util.*

class HexadecimalExerciseFragment : Fragment() {

    // Variable declaration
    //  View Binding
    private var _binding: FragmentHexadecimalExerciseBinding? = null
    private val binding get() = _binding!!

    //  Associated ViewModel
    private lateinit var viewModel: HexadecimalExerciseViewModel

    //  Layout elements
    private var mAnswerEditText : EditText? = null
    private var mCheckButton : Button? = null
    private var mChangeDirectionButton : Button? = null
    private var mSolutionButton : Button? = null
    private var mNumberToConvertTextSwitcher : TextSwitcher? = null
    private var mTitleTextView : TextSwitcher? = null

    //  Number to convert and conversion direction
    private var mNumberToConvertString : String = ""
    private var mDirectConversion : Boolean = true;

    // Variables used during result animation
    private var mResult: View? = null
    private var mResultImage: ImageView? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  Initialize view model
        viewModel =
            ViewModelProvider(this).get(HexadecimalExerciseViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.hexadecimal)

        // Inflate the layout for this fragment
        _binding = FragmentHexadecimalExerciseBinding.inflate(inflater, container, false)

        // Initialize variables
        with(binding) {
            mAnswerEditText              = textViewAnswer
            mCheckButton                 = checkbutton
            mChangeDirectionButton       = change
            mSolutionButton              = seesolution
            mNumberToConvertTextSwitcher = numbertoconvert
            mTitleTextView               = exercisetitle
        }
        // Observers
        viewModel.question.observe(viewLifecycleOwner) {
            mNumberToConvertString = it
            mNumberToConvertTextSwitcher?.setText(it)
        }
        viewModel.answer.observe(viewLifecycleOwner) {
            mAnswerEditText?.setText(it)
        }

        // Result and ResultImage
        mResult = binding.result
        mResultImage = binding.resultimage

        //  Exercise title and number to convert animations
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
            mDirectConversion = mDirectConversion xor true
            viewModel.setDirectionConversion(mDirectConversion)
            setTitle()
            newQuestion()
        }
        mSolutionButton!!.setOnClickListener {
            showSolution()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        newQuestion()
    }
    private fun obtainSolution() : String {
        return if(mDirectConversion) { // if it's a binary → hex exercise
            Integer.parseInt(mNumberToConvertString, 2).toString(16).uppercase()
        } else {                // if it's a hex → binary exercise
            viewModel.hexToBinary(mNumberToConvertString)
        }
    }

    private fun isCorrect(answer: String) : Boolean {
        return obtainSolution() == answer.uppercase(Locale.US)
    }
    private fun checkSolution(answer: String) {
        if(answer == "" || !isCorrect(answer)) {
            showAnimationAnswer(false)
        } else {
            showAnimationAnswer(true)
            newQuestion()
        }
    }
    private fun setTitle() {
        mTitleTextView!!.setText(titleString())
    }

    private fun titleString(): String {
        var formatStringId : Int
        if(mDirectConversion) { // if it's a binary → hex exercise
            formatStringId = R.string.convert_bin_to_hex
        } else {                // if it's a hex → binary exercise
            formatStringId = R.string.convert_hex_to_bin
        }
        return String.format(
            resources.getString(formatStringId),
            level()!!.numberOfBits()
        )
    }

    private fun level(): Level? {
        return PreferenceUtils.getLevel(this.requireContext())
    }

    private fun newQuestion() {
         mAnswerEditText!!.setText("")
         with(viewModel) {
             setNumberOfBits(level()!!.numberOfBits())
             newQuestion()
         }
    }
    private fun showSolution() {
        with(mAnswerEditText!!) {
            setText(obtainSolution())
            setSelection(mAnswerEditText!!.text.length)
        }
    }

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