package es.imovil.fcrtrainerbottom

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import es.imovil.fcrtrainerbottom.databinding.FragmentCidrBinding

class CIDRFragment : Fragment(), View.OnClickListener {

    //Vinculacion de vistas
    private var _binding: FragmentCidrBinding? = null
    private val binding get() = _binding!!

    //Viewmodel del fragmento
    val CIDRviewModel: CIDRViewModel by viewModels()

    val delChar: String="◀";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentCidrBinding.inflate(inflater, container, false)

        //Generar numero al iniciar
        CIDRviewModel.newQuestion()
        CIDRviewModel.ip.observe(viewLifecycleOwner){ result ->
            binding.textViewQuestion.text=result
        }

        //Al pulsa comprobar, comprobar
        binding.buttonCheckAnswer.setOnClickListener {
            var correct: Boolean=CIDRviewModel.checkAnswer(binding.textViewAnswer.text.toString())

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

}