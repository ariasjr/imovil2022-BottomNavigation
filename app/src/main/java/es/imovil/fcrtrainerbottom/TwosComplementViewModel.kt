package es.imovil.fcrtrainerbottom

import androidx.lifecycle.ViewModel
import java.util.*

class TwosComplementViewModel : ViewModel(){

    private var bits: Int = 0
    private var mRandomGenerator = Random()

    fun setBits(i:Int){
        bits = i
    }

    private var directConversion = true

    // Creamos un numero en Complemento a dos, segun el numero maximo de bits introducido
    // Como es complemento a dos con 4 bits solo convertimos de 8 a -7 etc
    fun createRandomNumber(numberOfBits: Int): Int {
            var min =  -(Math.pow(2.0, (bits - 1).toDouble()).toInt())
            var max = (Math.pow(2.0, (bits - 1).toDouble()).toInt()) - 1

            var mNumberToConvert = mRandomGenerator.nextInt(max - min + 1) + min

            if (directConversion) {
                return mNumberToConvert
            } else {

                return binaryToStringWithNbits(mNumberToConvert, bits)!!.toInt()
            }
    }

    fun binaryToStringWithNbits(number: Int, N: Int): String? {
        val formatString = "%" + N + "s"
        val bits = String.format(formatString, Integer.toBinaryString(number)).replace(' ', '0')
        return if (number >= 0) {
            bits
        } else {
            bits.substring(32 - N)
        }
    }

    fun inversaBinario(twosComplementText: String): String {
        val textInBinaryCharArray = twosComplementText.toCharArray()
        for (i in twosComplementText.indices) { // Hacemos un reverso al binario
            if(textInBinaryCharArray[i] == '0') textInBinaryCharArray[i] = '1'
            else if(textInBinaryCharArray[i] == '1') textInBinaryCharArray[i] = '0'
        }
        println("Esta es la inversa " + String(textInBinaryCharArray))
        return String(textInBinaryCharArray)
    }

    fun sumaUnoBinario(twosComplementText: String): String {
        val textInBinaryCharArray = twosComplementText.toCharArray()
        //Operacion de sumar uno al numero binario
        //Comprobamos si es un numero par y solo cambiamos el ultimo digito
        if(textInBinaryCharArray[textInBinaryCharArray.size-1] == '0'){
            textInBinaryCharArray[textInBinaryCharArray.size-1] = '1'
            println("Este es sumando uno si es par" + textInBinaryCharArray.toString())
        }

        // Como el ultimo numero es 1 lo pasamos a 0 y buscamos el proximo 0 para pasarlo a 1
        else {
            textInBinaryCharArray[textInBinaryCharArray.size - 1] = '0'
            var foundTheZero = false
            var counter = 2
            do {
                if (textInBinaryCharArray[textInBinaryCharArray.size - counter] == '0') {
                    textInBinaryCharArray[textInBinaryCharArray.size - counter] = '1'
                    foundTheZero = true
                }
                else {
                    textInBinaryCharArray[textInBinaryCharArray.size - counter] == '0'
                }
                counter++

            } while (!foundTheZero)
        }
        return String(textInBinaryCharArray)
    }


    //Pasamos de TwosComplement a decimal
    fun convertToDecimal(twosComplementText: String): String {
        // Para pasar Twos Complement a decimal primero miramos si es par
        if(twosComplementText.toCharArray()[0] == '0'){ //Como es par se trata como un binario mas
            return twosComplementText.toInt(2).toString()
        }

        //Si es impar, primero hacemos la inversa
        val inversaBinario = inversaBinario(twosComplementText).toCharArray()
        // Ahora sumamos uno
        val valorSumado = sumaUnoBinario(inversaBinario.toString())

        return "-" + valorSumado.toInt(2).toString()
    }

    fun convertToBinary(textToBinary: String): String {
        return Integer.toBinaryString(textToBinary.toInt())
    }

    //Converts decimal to TwosComplement
    fun convertToTwosComplement(decimal: String): String {

        //Si es negativo
        if(decimal.toCharArray()[0] == '-'){
            // Primero le quito el signo
            var decimalArreglado = decimal.substring(1)
            // Decimal a binario
            var textInBinary = Integer.toBinaryString(decimalArreglado.toInt())

            // El binario lo hago array y le hago la inversa
            val textInBinaryCharArray = inversaBinario(textInBinary).toCharArray()

            //Operacion de sumar uno al numero binario
            val sumadoUno = sumaUnoBinario(textInBinaryCharArray.toString())

            return sumadoUno.toString()
        }

        // Si es positivo
        else{
            // Decimal a binario
            var textInBinary = Integer.toBinaryString(decimal.toInt())

            // El binario lo hago array y le hago la inversa
            val textInBinaryCharArray = inversaBinario(textInBinary).toCharArray()

            //Operacion de sumar uno al numero binario
            val sumadoUno = sumaUnoBinario(textInBinaryCharArray.toString())

            return sumadoUno
        }
    }

    fun setDirectConversion(DirectConversion:Boolean){
        directConversion = DirectConversion
    }

    private fun generateRandomNumber(): String {
        val number: Int = createRandomNumber(bits)

        if (directConversion) return number.toString()
        else return convertToBinary(number.toString())
    }

    fun nuevaPregunta(): String {
        return generateRandomNumber()
    }

}