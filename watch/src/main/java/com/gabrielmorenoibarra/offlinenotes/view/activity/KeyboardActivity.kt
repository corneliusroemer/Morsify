package com.gabrielmorenoibarra.offlinenotes.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.gabrielmorenoibarra.offlinenotes.R
import com.gabrielmorenoibarra.offlinenotes.framework.extension.afterMeasured
import kotlinx.android.synthetic.main.activity_keyboard.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onLongClick
import org.jetbrains.anko.startActivityForResult
import java.util.*
import android.content.Context
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_main.*
import android.os.VibrationEffect
import android.os.Build
import android.os.Vibrator
import org.jetbrains.anko.sdk27.coroutines.onDrag
import kotlin.concurrent.thread


class KeyboardActivity : WearableActivity() {

    companion object {
        const val REQUEST_CODE = 1000

        const val EXTRA_RESULT_INPUT = "resultInput"
        const val EXTRA_RESULT_EDITING = "resultEditing"
        const val EXTRA_RESULT_POSITION = "resultPosition"

        const val EXTRA_TEXT = "text"
        const val EXTRA_POSITION = "position"

        private const val CHARS_LETTERS = "abcdefghijklmnopqrstuvwxyz."
        private const val CHARS_LETTERS_CAPITALIZED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ."
        private const val CHARS_NUMBERS = "1234567890."

        private const val N_COLUMN_BUTTONS = 4

        var saveLongPressed = false

        fun startForResult(activity: Activity,
                           text: String,
                           position: Int) {
            activity.startActivityForResult<KeyboardActivity>(
                REQUEST_CODE,
                EXTRA_TEXT to text,
                EXTRA_POSITION to position
            )
        }
    }

    private lateinit var activity: KeyboardActivity

    private var position = -1
    private var editing = false

    private var btns = ArrayList<Button>()
    private var btnWidth = 0
    private var btnHeight = 0
    private var btnKeyboardWidth = 0

    private var capitalized = false

    private lateinit var charNumbers: String
    private lateinit var charText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        activity = this@KeyboardActivity

        initStrings()
        initDimens()
        initKeyboard()

        initPositionAndEditing()
        initTv()


        initBtnBackspace()
        initBtnSave()
    }

    private fun initStrings() {
        charNumbers = getString(R.string.char_numbers)
        charText = getString(R.string.char_text)
    }

    private fun initDimens() {
        btnWidth = dip(resources.getDimension(R.dimen.btn_width))
        btnHeight = dip(resources.getDimension(R.dimen.btn_height))
    }

    private fun initKeyboard() {
        rlParent.afterMeasured {
            val windowWidth = resources.displayMetrics.widthPixels - 8
            btnKeyboardWidth = windowWidth / N_COLUMN_BUTTONS
            setKeyboardCharacters(CHARS_LETTERS)
        }
    }

    private fun initPositionAndEditing() {
        position = intent.getIntExtra(EXTRA_POSITION, -1)
        if (position != -1) editing = true
    }

    private fun initTv() {
        val text = intent.getStringExtra(EXTRA_TEXT) ?: ""
        tv.text = text
    }

    private fun initBtnBackspace() {
        ibBackspace.setOnClickListener {
            val s = tv.text.toString()
            if (s.isNotBlank()) {
                tv.text = s.substring(0, s.length - 1)
            }
        }
    }

    private fun initBtnSave() {
        btnSave.onLongClick {
            val input = tv.text.toString()
            setResultPerform(input)
            saveLongPressed = true
        }
    }

    private fun setKeyboardCharacters(characters: String) {
//        for (i in 0 until characters.length) {
//            val btn = Button(activity)
//            btn.isAllCaps = false
//            btn.id = i
//            btns.add(btn)
//            val currentBtn = btns[i]
//            val px = btnKeyboardWidth
//            val lp = RelativeLayout.LayoutParams(px, px)
//            if (i > 0) {
//                if (i % N_COLUMN_BUTTONS != 0) {
//                    if (i < N_COLUMN_BUTTONS) {
//                        lp.addRule(RelativeLayout.BELOW, R.id.tv)
//                    } else {
//                        lp.addRule(RelativeLayout.BELOW, btns[i - N_COLUMN_BUTTONS].id)
//                    }
//                    if (i == 1) {
//                        lp.setMargins(px, lp.topMargin, lp.rightMargin, lp.bottomMargin)
//                    } else {
//                        lp.addRule(RelativeLayout.RIGHT_OF, btns[i - 1].id)
//                    }
//                } else {
//                    lp.addRule(RelativeLayout.BELOW, btns[i - N_COLUMN_BUTTONS + 1].id)
//                }
//            } else {
//                lp.addRule(RelativeLayout.BELOW, R.id.tv)
//            }
//            currentBtn.layoutParams = lp
//            currentBtn.text = characters[i].toString()
//            currentBtn.setOnClickListener {
//                tv.text = "${tv.text}${currentBtn.text}"
//            }
//            addKey(currentBtn)
//        }
        var longClick = false
        var character = ""

        inputButton.onClick {
            if (longClick) {
                longClick = false
            } else {
                tv.text = "${tv.text}${"."}"
                character += "."
            }
        }

        inputButton.onLongClick {
            tv.text = "${tv.text}${"-"}"
            longClick = true
            character += "-"
        }


        val numbersMap = mapOf("" to " ", ".-" to "A", "-..." to "B", "-.-." to "C", "-.." to "D", "." to "E", "..-." to "F", "--." to "G", "...." to "H", ".." to "I", ".---" to "J", "-.-" to "K", ".-.." to "L", "--" to "M", "-." to "N", "---" to "O", ".--." to "P", "--.-" to "Q", ".-." to "R", "..." to "S", "-" to "T", "..-" to "U", "...-" to "V", ".--" to "W", "-..-" to "X", "-.--" to "Y", "--.." to "Z", ".----" to "1", "..---" to "2", "...--" to "3", "....-" to "4", "....." to "5", "-...." to "6", "--..." to "7", "---.." to "8", "----." to "9", "-----" to "0")
        val vibrator = this.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate:Boolean = vibrator.hasVibrator()

        btnSave.onClick {
            if (saveLongPressed)
            {
                saveLongPressed = false
            }
            else
            {
                tv.text = tv.text.substring(0, tv.text.length - character.length)
                tv.text = tv.text.toString() + numbersMap[character];

                for (c in character) {
                    var length = 0L
                    if (c.equals('.')) {
                        length = 40
                    } else {
                        length = 100
                    }
                    if (canVibrate) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // void vibrate (VibrationEffect vibe)
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    length,
                                    // The default vibration strength of the device.
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            // This method was deprecated in API level 26
                            vibrator.vibrate(length)
                        }
                        Thread.sleep(150)
                    }
                }
                character = ""
            }
        }
    }

//    private fun addKey(v: View) {
//        rlKeys.addView(v)
//    }
//
//    private fun removeAllKeys() {
//        rlKeys.removeAllViews()
//    }

    private fun setResultPerform(input: String) {
        val intent = Intent()
        intent.putExtra(EXTRA_RESULT_INPUT, input)
        intent.putExtra(EXTRA_RESULT_EDITING, editing)
        intent.putExtra(EXTRA_RESULT_POSITION, position)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
