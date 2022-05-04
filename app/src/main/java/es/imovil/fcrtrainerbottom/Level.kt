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
package es.uniovi.imovil.fcrtrainer


import android.content.Context
import es.imovil.fcrtrainerbottom.R

enum class Level(// for numerical exercises
    private val mNumberOfBits: Int, // for floating point
    private val mNumberOfBitsFractionalPart: Int, private val mScoreMultiplier: Int
) {
    BEGINNER(4, 1, 1), INTERMEDIATE(6, 2, 2), PROFICIENCY(8, 3, 4);

    fun numberOfBits(): Int {
        return mNumberOfBits
    }

    fun numberOfBitsFractionalPart(): Int {
        return mNumberOfBitsFractionalPart
    }

    fun toString(context: Context): String {
        return when (this) {
            BEGINNER -> context.getString(R.string.pref_level1_name)
            INTERMEDIATE -> context.getString(R.string.pref_level2_name)
            PROFICIENCY -> context.getString(R.string.pref_level3_name)
        }
        throw IllegalArgumentException()
    }

    fun scoreMultiplier(): Int {
        return mScoreMultiplier
    }

    companion object {
        fun fromString(context: Context, string: String): Level {
            val levelNames = context.resources.getStringArray(
                R.array.pref_level_values
            )
            for (i in levelNames.indices) {
                if (string.equals(levelNames[i], ignoreCase = true)) {
                    return values()[i]
                }
            }
            throw IllegalArgumentException()
        }
    }
}