/*
Copyright 2014 Profesores y alumnos de la asignatura Informática Móvil de la EPI de Gijón
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package es.uniovi.imovil.fcrtrainer.digitalinformation

import java.util.*

class BinaryConverter {
    private val mRandom = Random()
    fun createRandomNumber(numberOfBits: Int): Int {
        val x = Math.pow(2.0, numberOfBits.toDouble())
        val maxNumber = x.toInt()
        return mRandom.nextInt(maxNumber)
    }

    companion object {
        fun isStringEmpty(string: String): Boolean {
            return string.equals("") || !string.contains("1")
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
}

