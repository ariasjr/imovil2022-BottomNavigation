package es.imovil.fcrtrainerbottom

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

    //Variables para generar una mascara
    val random= Random()
    var mMask by Delegates.notNull<Int>()

    //Funcion para comprobar una respuesta
    fun checkAnswer(answer: String): Boolean{
        //Si la respuesta es vacia devuelve falso
        if(answer.isEmpty()){
            //Error
            return false
        }

        //Si la respuesta es correcto devuelve verdadero
        if(answer.equals(correctAnswer())){
            newQuestion()   //Se genera una nueva pregunta
            return true
        }else{
            //Error
            return false
        }
    }

    //Se obtiene la respuesta correcta de la mascara
    fun correctAnswer():String{
        return cidrSuffixFromMask(mMask).toString()
    }

    //Funcion para mostrar la solucion en pantalla
    fun showSolution(){
        _cidr.value=correctAnswer()
    }

    //Se obtiene el cidr de una mascara pasada como int
    fun cidrSuffixFromMask(mMask:Int):Int{
        var zeroCount = 0
        var mask = mMask

        while((mask and 0x1) == 0){
            zeroCount++
            mask = mask shr 1
        }
        return 32 - zeroCount
    }

    //Funcion para generar una nueva mascara e imprimirla
    fun newQuestion() {
        mMask=generateRandomMask()
        printQuestion()
    }

    //Se imprime la pregunta
    fun printQuestion(){
        _ip.value=intToIpString(mMask)
    }

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