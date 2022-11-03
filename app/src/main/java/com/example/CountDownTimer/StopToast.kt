package com.example.CountDownTimer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.CountDownTimer.databinding.StopToastBinding

class StopToast
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = StopToastBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

}