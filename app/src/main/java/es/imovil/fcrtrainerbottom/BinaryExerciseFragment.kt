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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentBinaryExerciseBinding
import java.util.*
import kotlin.math.pow


class BinaryExerciseFragment : Fragment() {

    private var _binding: FragmentBinaryExerciseBinding? = null
    private val binding get() = _binding!!

    protected var mIsPlaying = false

    private var mScoreTextView: TextView? = null
    private var mLevelTextView: TextView? = null
    private var mGameInfoPanel: View? = null

    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    private var mResult: View? = null
    private var mResultImage: ImageView? = null

    private val STATE_DIRECT_CONVERSION = "mDirectConversion"
    private val STATE_NUMBER_TO_CONVERT = "mNumberToConvertString"

    private var mAnswerEditText: EditText? = null
    private var mCheckButton: Button? = null
    private var mChangeDirectionButton: Button? = null
    private var mSolutionButton: Button? = null
    private var mNumberToConvertTextSwitcher: TextSwitcher? = null
    private var mTitleTextView: TextSwitcher? = null
    protected var mNumberToConvertString: String? = null

    protected var mRandomGenerator = Random()
    protected var mDirectConversion = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        _binding = FragmentBinaryExerciseBinding.inflate(inflater, container, false)

        val homeViewModel = ViewModelProvider(this).get(CodesFirstExerciceViewModel::class.java)

        mAnswerEditText = binding.textViewAnswer
        mChangeDirectionButton = binding.change
        mSolutionButton = binding.seesolution
        mCheckButton = binding.checkbutton

        mTitleTextView = binding.exercisetitle
        mTitleTextView!!.setInAnimation(activity, R.anim.slide_in_right);
        mTitleTextView!!.setOutAnimation(activity, R.anim.slide_out_left);
        mTitleTextView!!.setText(titleString())

        mNumberToConvertTextSwitcher = binding.numbertoconvert
        mNumberToConvertTextSwitcher!!.setInAnimation(activity, android.R.anim.slide_in_left);
        mNumberToConvertTextSwitcher!!.setOutAnimation(activity, android.R.anim.slide_out_right);

        mAnswerEditText!!.setOnEditorActionListener {v:TextView, actionId: Int, event:KeyEvent ->
            if (EditorInfo.IME_ACTION_DONE == actionId)
                checkSolution(mAnswerEditText!!.editableText.toString().trim().lowercase(Locale.US))
            false

        }

        mCheckButton!!.setOnClickListener{
            checkSolution(mAnswerEditText!!.editableText.toString().trim().lowercase(Locale.US))
        }

        mSolutionButton!!.setOnClickListener{
            mAnswerEditText!!.setText(obtainSolution())
        }

        mChangeDirectionButton!!.setOnClickListener({
            mDirectConversion = mDirectConversion xor true
            mTitleTextView!!.setText(titleString())
            newQuestion()

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mResult = view.findViewById(R.id.result)
        mResultImage = view.findViewById(R.id.resultimage)
        mGameInfoPanel = view.findViewById(R.id.game_info_panel)
        if (mGameInfoPanel != null) {
            mGameInfoPanel!!.visibility = View.GONE
        }

        newQuestion()
    }

    fun createRandomNumber(numberOfBits: Int): Int {
        val square = 2.0
        var x = square.pow(numberOfBits.toDouble())

        var maxNumber = x.toInt()
        return mRandomGenerator.nextInt(maxNumber)
    }

    private fun convertToDecimal(textToDecimal: String): String {
        return textToDecimal.toInt(2).toString()
    }

    private fun convertToBinary(textToBinary: String): String {
        var debug = Integer.toBinaryString(textToBinary.toInt())
        return debug
    }

    private fun deleteStartingZeroesFromBinaryInput(binaryText: String): String {
        if (binaryText.matches(Regex("")) || !binaryText.contains("1")) return "0" //empty string equals "0" in this case.
        var c = "" + binaryText[0]
        var i = 1
        var lastPosition = 0
        while (c == "0") {
            c = "" + binaryText[i]
            if (c == "1") lastPosition = i
            i++
        }
        if (binaryText.substring(lastPosition) == "") return "0"
        else return binaryText.substring(lastPosition)  //Now substring and return
    }

    private fun checkDecimalAnswer(answer: String): Boolean {
        val answerConverted: String = try {
            convertToBinary(answer)
        } catch (e: NumberFormatException) {
            return false
        }
        return answerConverted == mNumberToConvertString
    }

    private fun checkBinaryAnswer(RawAnswer: String): Boolean {
        // get answer without zeros in front to compare it easy.
        var answer = deleteStartingZeroesFromBinaryInput(RawAnswer)

        // Convert Question to binary
        var questionBeforeConvert = mNumberToConvertString.toString()
        var questionConverted = convertToBinary(questionBeforeConvert)
        return RawAnswer == questionConverted
    }

    protected fun isResultNumeric(): Boolean{
        return true;
    }

    protected fun titleString(): String {
        var formatStringId: Int
        if (mDirectConversion)
            formatStringId = R.string.convert_dec_to_bin
        else
            formatStringId = R.string.convert_bin_to_dec

        val formatString: String = getResources().getString(formatStringId)
        return java.lang.String.format(formatString, numberOfBits())
    }

    protected fun generateRandomNumber(): String {
        val number: Int = createRandomNumber(numberOfBits())

        if (mDirectConversion) return number.toString()
        else return convertToBinary(number.toString())
    }

    protected fun obtainSolution(): String {
        if (mDirectConversion)
            return convertToBinary(mNumberToConvertString.toString())
        else
            return convertToDecimal(mNumberToConvertString.toString())
    }

    protected fun isCorrect(answer: String): Boolean {
        if (mDirectConversion) return checkBinaryAnswer(answer)
        else return checkDecimalAnswer(answer)
    }

    companion object {
        fun newInstance(): BinaryExerciseFragment {
            return BinaryExerciseFragment()
        }
    }

    protected open fun showAnimationAnswer(correct: Boolean) {
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

        mResultImage!!.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), drawableId))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mResultImage!!.animate().setDuration(300).setInterpolator(mAntovershoot)
                .scaleX(1.5f).scaleY(1.5f)
                .withEndAction { // Back to its original size after the animation's end
                    mResultImage!!.animate().scaleX(1f).scaleY(1f)
                }
        }
    }

    private fun generateRandomQuestion() {
        mNumberToConvertString = generateRandomNumber()
    }

    private fun clearAnswer() {
        mAnswerEditText!!.setText("")
    }

    protected fun newQuestion() {
        clearAnswer();
        generateRandomQuestion();
        mNumberToConvertTextSwitcher?.setText(mNumberToConvertString);
    }

    protected fun checkSolution(answer: String) {
        if (answer.equals("") || !isCorrect(answer)) {
            showAnimationAnswer(false);
        } else {
            // Correct answer
            showAnimationAnswer(true);
            if (mIsPlaying) {

            }
            newQuestion();
        }
    }

    protected fun level(): Level? {
        return activity?.let { PreferenceUtils.getLevel(it) }
    }

    protected fun numberOfBits(): Int {
        return level()?.numberOfBits() ?: Level.BEGINNER.numberOfBits()
    }
}