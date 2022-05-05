package es.imovil.fcrtrainerbottom

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel

import es.uniovi.imovil.fcrtrainer.digitalinformation.BinaryConverter
import java.security.AccessController.getContext
import java.util.*


class SignedMagnitudeExerciseViewModel : ViewModel() {


    var mRandomGenerator = Random()
    private var bits : Int = 4

    var numberOfBitsMagnitude: Int = 0
    var maxMagnitude = 0
    var randomNumber: Int = 0
    var magnitudeDecimal = ""
    var sign: Int = 0
    var signAsString = "0"
    var magnitudeBinary: String = ""


    fun setDatosNumber(){
        numberOfBitsMagnitude = bits - 1
        maxMagnitude = (Math.pow(2.0, numberOfBitsMagnitude.toDouble()) - 1).toInt()
        randomNumber = mRandomGenerator.nextInt(maxMagnitude)
        magnitudeDecimal = randomNumber.toString()
        sign = mRandomGenerator.nextInt(2) // it can be 0 or 1
        signAsString = if (sign == 0) "0" else "1"
        magnitudeBinary = BinaryConverter.binaryToStringWithNbits(
            randomNumber, numberOfBitsMagnitude
        )

    }
    fun getDecimal() : String{
        return if (sign == 0) {
            magnitudeDecimal
        } else {
            "-$magnitudeDecimal"
        }
    }
   fun getBinary() : String{

        return signAsString + magnitudeBinary
    }
    fun setBits(number : Int){
        bits = number
    }
}