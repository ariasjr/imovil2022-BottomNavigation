package es.imovil.fcrtrainerbottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CodesFirstExerciceViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is exercice1 Fragment"
    }
    val text: LiveData<String> = _text
}