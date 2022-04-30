package es.imovil.fcrtrainerbottom

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.properties.Delegates

class CIDRViewModel : ViewModel() {

    //LiveData para la ip
    private val _ip = MutableLiveData<String>()
    val ip: LiveData<String> get() = _ip

    //LiveData para el cidr
    private val _cidr = MutableLiveData<String>()
    val cidr: LiveData<String> get() = _cidr

    fun checkAnswer(answer: String): Boolean{
        if(answer.isEmpty()){
            //Error
            return false
        }

        if(answer.equals(correctAnswer())){

            newQuestion()
            return true
        }else{
            //Error
            return false
        }
    }

    fun correctAnswer():String{
        return cidrSuffixFromMask(mMask).toString()
    }

    fun showSolution(){
        _cidr.value=correctAnswer()
    }

    fun cidrSuffixFromMask(mMask:Int):Int{
        var zeroCount = 0
        var mask = mMask

        while((mask and 0x1) == 0){
            zeroCount++
            mask = mask shr 1
        }
        return 32 - zeroCount
    }

    fun newQuestion() {
        mMask=generateRandomMask()
        printQuestion()
    }

    //Se imprime la pregunta
    fun printQuestion(){
        _ip.value=intToIpString(mMask)
    }



    /*********************************************************************
     *  BaseNetworkMaskExerciseFragment (Funciones y variables)
     ********************************************************************/
    val random= Random()
    var mMask by Delegates.notNull<Int>()

    //Funcion para generar una mascara nueva
    fun generateRandomMask():Int{
        val maxOffset=8

        val offset=random.nextInt(maxOffset) + 1

        return -0x1 shl offset
    }

    //Funcion que convierte un int a ip
    fun intToIpString(ipAddress: Int): String{
        val bytes = intArrayOf(
            ipAddress shr 24 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress and 0xff
        )

        return (Integer.toString(bytes[0])
                + "." + Integer.toString(bytes[1])
                + "." + Integer.toString(bytes[2])
                + "." + Integer.toString(bytes[3]))
    }

}