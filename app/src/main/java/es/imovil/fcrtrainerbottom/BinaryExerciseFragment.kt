package es.imovil.fcrtrainerbottom


import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentBinaryExerciseBinding
import java.util.*


class BinaryExerciseFragment : Fragment() {

    //viewModel
    private lateinit var viewModel: BinaryExerciseViewModel

    private var _binding: FragmentBinaryExerciseBinding? = null
    private val binding get() = _binding!!

    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    private var mResult: View? = null
    private var mResultImage: ImageView? = null

    private var mAnswerEditText: EditText? = null
    private var mCheckButton: Button? = null
    private var mChangeDirectionButton: Button? = null
    private var mSolutionButton: Button? = null
    private var mNumberToConvertTextSwitcher: TextSwitcher? = null
    private var mTitleTextView: TextSwitcher? = null
    private var numberToConvertString: String = ""
    private var mDirectConversion = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Binario"

        //Get viewModel
        viewModel = ViewModelProvider(this)[BinaryExerciseViewModel::class.java]

        // Inflate the layout for this fragment
        _binding = FragmentBinaryExerciseBinding.inflate(inflater, container, false)

        mAnswerEditText = binding.textViewAnswer

        mChangeDirectionButton = binding.changeBinary
        mSolutionButton = binding.solutionBinary
        mCheckButton = binding.checkButtonBinary

        mTitleTextView = binding.titleBinary
        mTitleTextView!!.setInAnimation(activity, R.anim.slide_in_right);
        mTitleTextView!!.setOutAnimation(activity, R.anim.slide_out_left);
        mTitleTextView!!.setText(titleString())

        mNumberToConvertTextSwitcher = binding.number2ConvertBinary
        mNumberToConvertTextSwitcher!!.setInAnimation(activity, android.R.anim.slide_in_left);
        mNumberToConvertTextSwitcher!!.setOutAnimation(activity, android.R.anim.slide_out_right);

        mCheckButton!!.setOnClickListener {
            checkSolution(mAnswerEditText!!.editableText.toString().trim().lowercase(Locale.US))
        }

        mSolutionButton!!.setOnClickListener {
            mAnswerEditText!!.setText(obtainSolution())
        }

        mChangeDirectionButton!!.setOnClickListener {
            mDirectConversion = mDirectConversion xor true
            viewModel.setDirectConversion(mDirectConversion)
            mTitleTextView!!.setText(titleString())
            newQuestion()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mResult = view.findViewById(R.id.result)
        mResultImage = view.findViewById(R.id.resultimage)
        newQuestion()
    }

    //Returns title of the fragment (decimal or binary)
    private fun titleString(): String {
        var formatStringId: Int
        if (mDirectConversion)
            formatStringId = R.string.convert_dec_to_bin
        else
            formatStringId = R.string.convert_bin_to_dec

        val formatString: String = resources.getString(formatStringId)
        return java.lang.String.format(formatString, numberOfBits())
    }

    private fun obtainSolution(): String {
        if (mDirectConversion)
            return viewModel.convertToBinary(numberToConvertString.toString())
        else
            return viewModel.convertToDecimal(numberToConvertString.toString())
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
    }

    //Clear previous answer
    private fun clearAnswer() {
        mAnswerEditText!!.setText("")
    }

    private fun newQuestion() {
        clearAnswer();
        viewModel.setDirectConversion(mDirectConversion)
        numberToConvertString = viewModel.generateRandomQuestion()
        mNumberToConvertTextSwitcher?.setText(numberToConvertString)
    }

    //True if a decimal answer is correct.
    private fun checkDecimalAnswer(answer: String): Boolean {
        val answerConverted: String = try {
            viewModel.convertToBinary(answer)
        } catch (e: NumberFormatException) {
            return false
        }
        return answerConverted == numberToConvertString
    }

    //True if a binary answer is correct.
    private fun checkBinaryAnswer(RawAnswer: String): Boolean {
        // get answer without zeros in front to compare it easy.
        var answer = viewModel.deleteStartingZeroesFromBinaryInput(RawAnswer)

        // Convert Question to binary
        var questionBeforeConvert = numberToConvertString
        var questionConverted = viewModel.convertToBinary(questionBeforeConvert)
        return answer == questionConverted
    }

    //Chooses the function needed to check te answer (binary or decimal)
    private fun isCorrect(answer: String): Boolean {
        if (mDirectConversion) return checkBinaryAnswer(answer)
        else return checkDecimalAnswer(answer)
    }

    //Performs needed actions when answer is correct or not
    private fun checkSolution(answer: String) {
        if (answer.equals("") || !isCorrect(answer))
            showAnimationAnswer(false);
        else {
            showAnimationAnswer(true);
            newQuestion();
        }
    }

    private fun level(): Level? {
        return activity?.let { PreferenceUtils.getLevel(it) }
    }

    //Get level
    private fun numberOfBits(): Int {
        var nb = level()!!.numberOfBits()
        viewModel.setBits(nb)
        return nb
    }
}