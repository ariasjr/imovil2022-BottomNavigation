package es.imovil.fcrtrainerbottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.pow

class HexadecimalExerciseViewModel : ViewModel() {
    private var ran: Random = java.util.Random()

    private var numberOfBits : Int = 0
    fun setNumberOfBits(number: Int) {
        numberOfBits = number
    }

    private var directConversion : Boolean = true
    fun setDirectionConversion(direct : Boolean) {
        directConversion = direct
    }

    private val _question = MutableLiveData<String>().apply {
        value = ""
    }
    val question : LiveData<String> = _question
    private val _answer = MutableLiveData<String>().apply {
        value = ""
    }
    val answer : LiveData<String> = _answer

    fun newQuestion() {
        val maxNumberToConvert = 2.0.pow(numberOfBits).toInt()
        val numberToConvert = ran.nextInt(maxNumberToConvert)
        if (directConversion) {
            _question.value = binaryToStringWithNbits(
                numberToConvert,
                numberOfBits
            )
        } else {
            _question.value = Integer.toHexString(numberToConvert).uppercase(Locale.US)
        }
    }

    fun binaryToStringWithNbits(number: Int, N: Int): String {
        val formatString = "%" + N + "s"
        val bits = String.format(
            formatString,
            Integer.toBinaryString(number)).replace(' ', '0')
        if (number >= 0) {
            return bits;
        } else {
            return bits.substring(32 - N);
        }
    }

    fun hexToBinary(hexNumber: String): String {
        return Integer.toBinaryString(Integer.parseInt(hexNumber, 16))
    }
}