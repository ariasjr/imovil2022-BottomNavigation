package es.imovil.fcrtrainerbottom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import androidx.lifecycle.ViewModelProvider
import es.imovil.fcrtrainerbottom.databinding.FragmentNetworkMaskExerciseBinding

class NetworkMaskExerciseFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: NetworkMaskExerciseViewModel

    private var _binding: FragmentNetworkMaskExerciseBinding? = null
    private val binding get() = _binding!!

    private var mExerciseTitle: TextView? = null
    private var mAnswer: EditText? = null
    private var mButtonShowSolution: Button? = null

    private var mResult: View? = null
    private var mResultImage: ImageView? = null

    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(exerciseId())

        viewModel = ViewModelProvider(this).get(NetworkMaskExerciseViewModel::class.java)

        // Inflate the layout for this fragment
        _binding = FragmentNetworkMaskExerciseBinding.inflate(inflater, container, false)

        mExerciseTitle = binding.textViewExerciseTitle
        val mQuestion = binding.textViewQuestion
        mAnswer = binding.textViewAnswer
        mAnswer!!.setText("255.255.255.255")

        mExerciseTitle!!.text = titleString()

        binding.buttonCheckAnswer.setOnClickListener(this)

        mButtonShowSolution = binding.buttonShowSolution
        mButtonShowSolution!!.setOnClickListener(this)

        mResult = binding.result
        mResultImage = binding.resultimage

        viewModel.question.observe(viewLifecycleOwner){
            mQuestion.text = it
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val level = level()
        if (level != null) {
            viewModel.setDifficulty(level)
        }
    }

    private fun level(): Level? {
        return PreferenceUtils.getLevel(this.requireContext())
    }

    private fun titleString(): String {
        return getString(R.string.network_mask_title)
    }

    private fun newQuestion() {
        viewModel.newQuestion()
        printQuestion()
    }

    private fun checkAnswer() {
        val answer = mAnswer!!.text.toString()
        if (answer == viewModel.intToIpString(viewModel.correctAnswer())) {
            showAnimationAnswer(true)
            newQuestion()
        } else {
            showAnimationAnswer(false)
        }
    }

    private fun showSolution() {
        mAnswer!!.setText(viewModel.intToIpString(viewModel.correctAnswer()))

        // Set the cursor at the end
        mAnswer!!.setSelection(mAnswer!!.text.length)
    }

    private fun exerciseId(): Int {
        return R.string.network_mask
    }

    private fun printQuestion() {
        viewModel.printQuestion()

        mAnswer!!.setText("255.255.255.255")

        // Set the focus on the edit text and show the keyboard
        if (mAnswer!!.requestFocus()) {
            requireActivity().window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            )
        }

        // Set the cursor at the end so that the user can begin deleting the
        // numbers that are not part of the network address
        mAnswer!!.setSelection(mAnswer!!.text.length)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonCheckAnswer -> {
                checkAnswer()
            }
            binding.buttonShowSolution -> {
                showSolution()
            }
        }
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
        mResultImage!!.animate().setDuration(300).setInterpolator(mAntovershoot)
            .scaleX(1.5f).scaleY(1.5f)
            .withEndAction { // Back to its original size after the animation's end
                mResultImage!!.animate().scaleX(1f).scaleY(1f)
            }
    }

}