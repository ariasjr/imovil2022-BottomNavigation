package es.imovil.fcrtrainerbottom

import android.R.string
import android.content.Context


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
            val levelNames: Array<String> = context.getResources().getStringArray(
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