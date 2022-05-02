package es.imovil.fcrtrainerbottom

import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.max

class NetworkMaskExerciseViewModel : ViewModel() {
    private val _question = MutableLiveData<String>().apply {
        value = "2"
    }

    val question: LiveData<String> = _question

    private var mRandom = Random()

    private var mMask: Int = 0xfffffffffc.toInt()

    var level : Level = Level.BEGINNER

    private fun generateRandomMask(): Int {
        var maxOffset = 8

        when(level){
            Level.BEGINNER -> {
                maxOffset = 4
            }
            Level.INTERMEDIATE -> {
                maxOffset = 16
            }
            Level.PROFICIENCY -> {
                maxOffset = 26
            }
        }

        // Add 1 because 0 is not a valid mask
        val offset = mRandom.nextInt(maxOffset) + 1
        return -0x1 shl offset
    }

    fun intToIpString(ipAddress: Int): String? {
        val bytes = intArrayOf(
            ipAddress shr 24 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress and 0xff
        )
        return (Integer.toString(bytes[0])
                + "." + Integer.toString(bytes[1])
                + "." + Integer.toString(bytes[2])
                + "." + Integer.toString(bytes[3]))
    }

    fun correctAnswer(): Int {
        return mMask
    }

    protected fun maxHost(): Int {
        // 1 is subtracted for removing the value with all 0, that
        // corresponds to the network address and is not a valid
        // host address
        return (mMask xor -0x1) - 1
    }

    fun newQuestion() {
        do {
            mMask = generateRandomMask()
        } while (maxHost() < 2) // A network needs at least two hosts

        printQuestion()
    }

    fun printQuestion() {
        _question.value = Integer.toString(maxHost())
    }

    fun setDifficulty(level: Level) {
        if (level != this.level) {
            this.level = level
            newQuestion()
        }
    }
}