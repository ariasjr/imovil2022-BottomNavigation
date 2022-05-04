package es.imovil.fcrtrainerbottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NetworkAddressExerciseViewModel : ViewModel() {
    private val _question_ip = MutableLiveData<String>().apply {
        value = "52.6.94.107"
    }

    private val _question_mask = MutableLiveData<String>().apply {
        value = "255.255.255.254"
    }

    val question_ip: LiveData<String> = _question_ip
    val question_mask: LiveData<String> = _question_mask

    private var mRandom = Random()

    private var mMask: Int = 0xfffffffffc.toInt()
    private var  mIp: Int = 0xfffffffffc.toInt()

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
    private fun generateRandomIP(): Int {
        var value = 0
        while (value == 0) {
            value = mRandom.nextInt(0x7ffffffe)
        }
        return value
    }

    fun correctAnswer(): Int {
        return mIp and mMask
    }


    fun newQuestion() {
        do {
            mMask = generateRandomMask()
            mIp = generateRandomIP()
        } while (mIp and mMask === 0
            || mIp and mMask === mIp
        ) // A network needs at least two hosts

        printQuestion()
    }

    fun printQuestion() {
        _question_mask.value =  intToIpString(mMask)
        _question_ip.value =  intToIpString(mIp)
    }

    fun setDifficulty(level: Level) {
        if (level != this.level) {
            this.level = level
            newQuestion()
        }
    }
}