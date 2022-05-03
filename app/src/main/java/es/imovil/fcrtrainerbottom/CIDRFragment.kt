package es.imovil.fcrtrainerbottom

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import es.imovil.fcrtrainerbottom.databinding.FragmentCidrBinding

class CIDRFragment : Fragment(), View.OnClickListener {

    //Vinculacion de vistas
    private var _binding: FragmentCidrBinding? = null
    private val binding get() = _binding!!

    //Viewmodel del fragmento
    val CIDRviewModel: CIDRViewModel by viewModels()

    val delChar: String="◀"

    private var mResult: View? = null
    private var mResultImage: ImageView? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentCidrBinding.inflate(inflater, container, false)
        mResult = binding.result
        mResultImage = binding.resultimage


        //Generar numero al iniciar
        CIDRviewModel.newQuestion()
        CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
            binding.textViewQuestion.text=result
        }

        //Al pulsa comprobar, comprobar
        binding.buttonCheckAnswer.setOnClickListener {
            var correct: Boolean=CIDRviewModel.checkAnswer(binding.textViewAnswer.text.toString())

            //Se muestra la animacion
            showAnimationAnswer(correct)

            if(correct){
                CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
                    binding.textViewQuestion.text=result
                }
                binding.textViewAnswer.setText("")
            }

        }

        //Al pulsar boton de solucion, mostrar solucion en input
        binding.buttonShowSolution.setOnClickListener {
            CIDRviewModel.showSolution()
            CIDRviewModel.cidr.observe(viewLifecycleOwner){ result ->
                binding.textViewAnswer.setText(result)

                //Se pone el cursor al final del numero
                binding.textViewAnswer.setSelection(result.length)
            }
        }

        if(savedInstanceState==null){
            CIDRviewModel.newQuestion()
            CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
                binding.textViewQuestion.text=result
            }
        }


        //Configuracion del teclado de la aplicacion
        binding.keyDelete.text=delChar
        binding.b0.setOnClickListener(this)
        binding.b1.setOnClickListener(this)
        binding.b2.setOnClickListener(this)
        binding.b3.setOnClickListener(this)
        binding.b4.setOnClickListener(this)
        binding.b5.setOnClickListener(this)
        binding.b6.setOnClickListener(this)
        binding.b7.setOnClickListener(this)
        binding.b8.setOnClickListener(this)
        binding.b9.setOnClickListener(this)
        binding.keyDelete.setOnClickListener(this)

        return binding.root
    }

    //Se implementan los botones del teclado
    override fun onClick(v: View) {
        when(v.id){
            R.id.b0 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"0")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b1 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"1")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b2 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"2")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b3 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"3")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b4 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"4")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b5 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"5")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b6 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"6")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b7 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"7")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b8 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"8")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.b9 -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.toString()+"9")
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
            R.id.key_delete -> { binding.textViewAnswer.setText(binding.textViewAnswer.text.take(binding.textViewAnswer.text.length-1))
                binding.textViewAnswer.setSelection(binding.textViewAnswer.text.length)}
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