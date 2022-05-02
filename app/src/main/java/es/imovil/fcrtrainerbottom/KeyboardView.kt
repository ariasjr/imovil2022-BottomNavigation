package es.imovil.fcrtrainerbottom

import android.content.Context
import android.widget.LinearLayout
import android.widget.EditText
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button

/**
 * Esta vista se utiliza para gestionar una teclado que contiene los números
 * hexadecimales, el punto y una tecla para borrar. Debe incluirse un layout
 * llamado keyboard_panel de esta manera:
 *
 * <es.uniovi.imovil.fcrtrainer.KeyboardView android:id="@+id/keyboard" android:layout_width="wrap_content" android:layout_height="wrap_content" android:layout_gravity="center"></es.uniovi.imovil.fcrtrainer.KeyboardView>
 *
 * Si el layout padre incluye un EditText con identificador "text_view_answer",
 * el teclado se asigna automáticamente a ese identificador. Si no, se debe
 * utilizar la función assignTextView().
 *
 * Si se añade la opción only_binary a true, sólo se generan teclas para binario
 */
class KeyboardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),
    View.OnClickListener {

    lateinit private var anim: Animation
    private var mEditText: EditText? = null // this receives the keystrokes
    private fun getLayoutId(context: Context, attrs: AttributeSet): Int {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.KeyboardView,
            0, 0
        )
        return try {
            val onlyBinary = a.getBoolean(
                R.styleable.KeyboardView_only_binary, false
            )
            if (onlyBinary) {
                R.layout.keyboard_binary_panel
            } else {
                R.layout.keyboard_full_panel
            }
        } finally {
            a.recycle()
        }
    }

    fun assignEditText(textView: EditText?) {
        mEditText = textView
    }

    override fun onClick(view: View) {
        view.startAnimation(anim)
        if (mEditText == null) {
            // No textView assigned. By default, we look for one with id answer
            val parent = parent as View
            mEditText = parent.findViewById(R.id.viewAnswerBinary)
            if (mEditText == null) { // not found
                return
            }
        }
        val button = view as Button
        val keyPressed = button.text
        val start = mEditText!!.selectionStart
        val end = mEditText!!.selectionEnd
        val realStart = Math.min(start, end)
        val realEnd = Math.max(start, end)
        val text = mEditText!!.editableText
        if (button.id == R.id.key_delete) {
            if (text.length <= 0 || realStart - 1 < 0) return
            if (start == end) {
                text.delete(start - 1, start)
            } else {
                text.delete(realStart, realEnd)
            }
        } else {
            text.replace(
                realStart, realEnd,
                keyPressed, 0, keyPressed.length
            )
        }
    }

    companion object {
        private const val delChar = "◀"
    }

    init {
        val layoutId = getLayoutId(context, attrs)
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(layoutId, this, true)
        if (isInEditMode) {

        }
        val allButtons = rootView.touchables
        for (v in allButtons) {
            v.setOnClickListener(this)
        }
        (rootView.findViewById<View>(R.id.key_delete) as Button).text = delChar
        anim = AnimationUtils.loadAnimation(context, R.anim.popup)
    }
}