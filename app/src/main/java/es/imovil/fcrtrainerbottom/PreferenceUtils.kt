package es.imovil.fcrtrainerbottom

import android.R
import android.content.Context

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.R.string


class PreferenceUtils {
    companion object{
        fun getLevel(context: Context): Level? {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val levelPreferenceKey: String = context.getString(es.imovil.fcrtrainerbottom.R.string.pref_level_key)
            val defaultLevel: String = context.getString(es.imovil.fcrtrainerbottom.R.string.pref_level1_name)
            val levelAsString = prefs.getString(levelPreferenceKey, defaultLevel)
            return Level.fromString(context, levelAsString!!)
        }
    }
}
