package com.capstone.wastetotaste.cv

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.capstone.wastetotaste.R

class ConfirmPassCV : AppCompatEditText, View.OnFocusChangeListener {

    var confirmPassMatched = false
    var isPasswordVisible = false

    init {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.rounded_gray_bg)
        transformationMethod = PasswordTransformationMethod.getInstance()

        onFocusChangeListener = this

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkConfirmPasswordValidity()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            transformationMethod = PasswordTransformationMethod.getInstance()
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            transformationMethod = null
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        isPasswordVisible = !isPasswordVisible
        setSelection(text?.length ?: 0)
    }

    override fun onFocusChange(v: View?, isInFocus: Boolean) {
        if (!isInFocus) {
            checkConfirmPasswordValidity()
        }
    }

    private fun checkConfirmPasswordValidity() {
        val pass = text.toString().trim()
        val confirmPass =
            (parent as ViewGroup).findViewById<PassCV>(R.id.edt_password_signup).text.toString()
                .trim()

        confirmPassMatched = pass.length >= 8 && pass == confirmPass
        error = if (!confirmPassMatched) {
            resources.getString(R.string.mismatchPass)
        } else {
            null
        }
    }
}
