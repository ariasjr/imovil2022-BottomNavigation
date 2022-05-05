package es.imovil.fcrtrainerbottom

import kotlin.random.Random

class BinaryConverter {
    private val mRandom = Random
    fun createRandomNumber(numberOfBits: Int): Int {
        val x = Math.pow(2.0, numberOfBits.toDouble())
        val maxNumber = x.toInt()
        return mRandom.nextInt(maxNumber)
    }

        fun isStringEmpty(string: String): Boolean {
            if ((string.isEmpty()) || !string.contains("1")) {
                return true
            }
            return false
        }

        fun deleteStartingZeroesFromBinaryInput(binaryText: String): String {
            if (isStringEmpty(binaryText)) return "0" //empty string equals "0" in this case.
            var c = "" + binaryText[0]
            var i = 0
            var lastPosition = 0
            while (c == "0") {
                c = "" + binaryText[i]
                if (c == "1") {
                    lastPosition = i
                }
                i++
            }
            return if (binaryText.substring(lastPosition) == "") "0" else binaryText.substring(
                lastPosition
            )

            //Now substring and return
        }

        fun convertDecimalToBinary(decimalNumber: Int): String {
            return Integer.toBinaryString(decimalNumber)
        }

        fun convertDecimalToBinary(decimalNumber: String): String {
            val number = decimalNumber.toInt()
            return convertDecimalToBinary(number)
        }

        fun convertBinaryToDecimal(binary: String): String {
            return "" + binary.toInt(2)
        }

        /**
         * Returns a string with the representation of number with N bits in two's
         * complement. It assumes N is less or equal than 32 and that it is possible
         * to do it
         */
        fun binaryToStringWithNbits(number: Int, N: Int): String {
            val formatString = "%" + N + "s"
            val bits = String.format(formatString, Integer.toBinaryString(number)).replace(' ', '0')
            return if (number >= 0) {
                bits
            } else {
                bits.substring(32 - N)
            }
        }
}