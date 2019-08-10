package com.example.ducius

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.ContextCompat

class PasswordEditText @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    style: Int = androidx.appcompat.R.attr.editTextStyle
) : EditText(context, attributes, style) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val colorId: Int
                if ((text?.length ?: 0) <= 3) {
                    colorId = R.color.red
                } else if ((text?.length ?: 0) <= 7) {
                    colorId = R.color.orange
                } else {
                    colorId = R.color.green
                }
                setTextColor(ContextCompat.getColor(context, colorId))
            }
        })
    }
}