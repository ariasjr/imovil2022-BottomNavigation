package es.imovil.fcrtrainerbottom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.properties.Delegates

class DigitalsOperacionLogicaViewModel : ViewModel() {
    private val BASE_BINARIA = 2
    private val MAX_NUMBER_OF_OPERATIONS = 3
    private var numberOfBits: Int = 0
    fun setNumberOfBits(number: Int) {
        numberOfBits = number
    }
    private var mSolucion: String? = null

    private var mRandom: Random = Random()

    private val _entrada1 = MutableLiveData<String>().apply {
        value = ""
    }
    val entrada1: LiveData<String> = _entrada1
    private val _entrada2 = MutableLiveData<String>().apply {
        value = ""
    }
    val entrada2: LiveData<String> = _entrada2
    private val _operacion = MutableLiveData<String>().apply {
        value = ""
    }
    val operacion: LiveData<String> = _operacion
    private val _solucion = MutableLiveData<String>().apply {
        value = ""
    }
    val solucion: LiveData<String> = _solucion

    fun cargarPregunta(){
        val e1 = binarioAleatorio()
        _entrada1.value = e1
        val e2 = binarioAleatorio()
        _entrada2.value = e2
        val op = operacionAleatoria()
        _operacion.value = op
        val entrada1 = e1!!.toInt(BASE_BINARIA)
        val entrada2 = e2!!.toInt(BASE_BINARIA)
        var result = 0
        if (op == "AND") result = entrada1 and entrada2 else if (op == "OR") result =
            entrada1 or entrada2 else if (op == "XOR") result = entrada1 xor entrada2
        mSolucion = Integer.toBinaryString(result)
        _solucion.value = completaNumeroBits(mSolucion)

    }
    private fun binarioAleatorio(): String? {
        val maxNumber = Math.pow(
            BASE_BINARIA.toDouble(),
            numberOfBits.toDouble()
        ).toInt()
        val entero = mRandom.nextInt(maxNumber)
        var binario = Integer.toBinaryString(entero)
        binario = completaNumeroBits(binario)
        return binario
    }
    private fun completaNumeroBits(binario: String?): String? {
        var binario = binario
        var i = binario!!.length
        while (i < numberOfBits) {
            binario = "0$binario"
            i = binario.length
        }
        return binario
    }
    private fun operacionAleatoria(): String {
        val entero = mRandom.nextInt(MAX_NUMBER_OF_OPERATIONS)
        val operacion: String
        return when (entero) {
            0 -> {
                operacion = "AND"
                operacion
            }
            1 -> {
                operacion = "OR"
                operacion
            }
            else -> {
                operacion = "XOR"
                operacion
            }
        }
    }
}