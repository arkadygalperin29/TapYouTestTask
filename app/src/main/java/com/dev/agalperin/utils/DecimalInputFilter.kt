package com.dev.agalperin.utils

import android.text.InputFilter
import android.text.Spanned


/**
 * Exclusive fix for Input Filter for Samsung keyboard with Views, since xml
 * declarations don't work for them. Currently it's just a blockade for non-number input in text field
 */
class DecimalInputFilter : InputFilter {

    companion object {
        private const val INPUT_REGEX = "^[0-9]*$"
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest)
        builder.replace(dstart, dend, source?.subSequence(start, end).toString())

        if (!builder.toString().matches(Regex(INPUT_REGEX))) {
            if (source?.isEmpty() == true)
                return dest?.subSequence(dstart, dend)
            return ""
        }

        return null
    }
}