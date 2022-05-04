package es.imovil.fcrtrainerbottom

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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

class CIDRFragment : Fragment() {

    //Vinculacion de vistas
    private var _binding: FragmentCidrBinding? = null
    private val binding get() = _binding!!

    //Viewmodel del fragmento
    val CIDRviewModel: CIDRViewModel by viewModels()

    //Variables para la animacion
    private var mResult: View? = null
    private var mResultImage: ImageView? = null
    private val mAntovershoot: AnticipateOvershootInterpolator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Vinculacion de vistas
        _binding= FragmentCidrBinding.inflate(inflater, container, false)
        mResult = binding.result
        mResultImage = binding.resultimage

        //Generar numero al iniciar
        CIDRviewModel.newQuestion()
        CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
            binding.textViewQuestion.text=result
        }

        //Al pulsar comprobar se comprueba si es correcta la respuesta
        binding.buttonCheckAnswer.setOnClickListener {
            var correct: Boolean=CIDRviewModel.checkAnswer(binding.textViewAnswer.text.toString())

            //Se muestra la animacion correspondiente
            showAnimationAnswer(correct)

            //Si es correcto se muestra la nueva pregunta
            if(correct){
                CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
                    binding.textViewQuestion.text=result
                }
                binding.textViewAnswer.setText("")
            }
        }

        //Al pulsar boton de solucion se muestra la solucion en input
        binding.buttonShowSolution.setOnClickListener {
            CIDRviewModel.showSolution()
            CIDRviewModel.cidr.observe(viewLifecycleOwner){ result ->
                binding.textViewAnswer.setText(result)

                //Se pone el cursor al final del numero
                binding.textViewAnswer.setSelection(result.length)
            }
        }

        //Si hay un error se genera una pregunta nueva
        if(savedInstanceState==null){
            CIDRviewModel.newQuestion()
            CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
                binding.textViewQuestion.text=result
            }
        }
        return binding.root
    }


    //Funcion para mostrar la animacion al comprobar una respuesta
    private fun showAnimationAnswer(correct: Boolean) {
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
            .withEndAction {
                mResultImage!!.animate().scaleX(1f).scaleY(1f)
            }
    }

}