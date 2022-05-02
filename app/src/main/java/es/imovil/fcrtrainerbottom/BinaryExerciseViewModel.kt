package es.imovil.fcrtrainerbottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.pow

class BinaryExerciseViewModel : ViewModel(){

    private val SQUARE = 2.0
    private var mRandomGenerator = Random()
    private var bits = 0
    //private var directConversion: Boolean = true

    private var directConversion = true

    //Creates random binary number with a maximum number of bits
    fun createRandomNumber(numberOfBits: Int): Int {
        var x = SQUARE.pow(numberOfBits.toDouble())

        var maxNumber = x.toInt()
        return mRandomGenerator.nextInt(maxNumber)
    }

    //Converts binary to decimal
    fun convertToDecimal(textToDecimal: String): String {
        return textToDecimal.toInt(2).toString()
    }

    //Converts decimal to binary
    fun convertToBinary(textToBinary: String): String {
        return Integer.toBinaryString(textToBinary.toInt())
    }

    //Deletes 0s before the first 1 in a binary number
    fun deleteStartingZeroesFromBinaryInput(binaryText: String): String {
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

    fun setBits(nBits:Int){
        bits = nBits
    }

    fun setDirectConversion(DirectConversion:Boolean){
        directConversion = DirectConversion
    }

    private fun generateRandomNumber(): String {
        val number: Int = createRandomNumber(bits)

        if (directConversion) return number.toString()
        else return convertToBinary(number.toString())
    }

    fun generateRandomQuestion(): String {
        return generateRandomNumber()
    }






}