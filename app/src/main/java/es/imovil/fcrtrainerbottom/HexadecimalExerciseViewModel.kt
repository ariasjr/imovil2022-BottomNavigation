package es.imovil.fcrtrainerbottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.pow

/**
 * ViewModel asociado al fragmento para el ejercicio hexadecimal del
 * trabajo en grupo de Informática Móvil 2021-2022, PL2
 * @author Andrés García González UO271210
 */
class HexadecimalExerciseViewModel : ViewModel() {
    // Objeto auxiliar
    private var ran: Random = Random()

    //
    private var numberOfBits : Int = 0
    fun setNumberOfBits(number: Int) {
        numberOfBits = number
    }

    // Uso de LiveData para la persistencia de datos
    private val _question = MutableLiveData<String>().apply {
        value = ""
    }
    val question : LiveData<String> = _question
    private val _directConversion = MutableLiveData<Boolean>().apply {
        value = true
    }
    val directConversion : LiveData<Boolean> = _directConversion

    /**
     * Genera un número binario/hexadecimal con numberOfBits bits de longitud
     * dependiendo del tipo de conversión del ejercicio
     */
    fun newQuestion() {
        val maxNumberToConvert = 2.0.pow(numberOfBits).toInt()
        val numberToConvert = ran.nextInt(maxNumberToConvert)
        if (directConversion.value == true) {
            _question.value = binaryToStringWithNbits(
                numberToConvert,
                numberOfBits
            )
        } else {
            _question.value = Integer.toHexString(numberToConvert).uppercase(Locale.US)
        }
    }

    /**
     * Devuelve una string con la representación de number con N bits en complemento
     * a dos. Se asume que N es menor o igual que 32 y que es posible hacer la operación
     * @param number número que se desea representar en complemento a dos
     * @param N número de bits deseados
     * @return String con la epresentación con N bits de number en complemento a dos
     */
    private fun binaryToStringWithNbits(number: Int, N: Int): String {
        val formatString = "%" + N + "s"
        val bits = String.format(
            formatString,
            Integer.toBinaryString(number)).replace(' ', '0')
        return if (number >= 0) {
            bits
        } else {
            bits.substring(32 - N)
        }
    }

    /**
     * Devuelve la representación del número hexadecimal introducido en binario
     * @param hexNumber número hexadecimal que se desea representar en binario
     * @return String con la representación de hexNumber en binario
     */
    fun hexToBinary(hexNumber: String): String {
        return Integer.toBinaryString(Integer.parseInt(hexNumber, 16))
    }

    /**
     * Devuelve la representación del número binario introducido en hexadecimal
     * @param binaryNumber número binario que se desea representar en hexadecimal
     * @return String con la representación de binaryNumber en hexadecimal
     */
    fun binaryToHex(binaryNumber: String): String {
        return Integer.parseInt(binaryNumber, 2).toString(16).uppercase()
    }

    /**
     * Realiza una operación xor sobre el valor actual de _directConversion
     */
    fun changeConversionDirection() {
        _directConversion.value = _directConversion.value?.xor(true)
    }
}