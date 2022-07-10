package com.example.m3_components

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val defaultProgress = 60
        var intProgress = 60

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val centerText = findViewById<TextView>(R.id.textViewCenter)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val button = findViewById<Button>(R.id.button)
        val stopToast = Toast.makeText(this, "Stop tracking", Toast.LENGTH_SHORT)
        val startToast = Toast.makeText(this, "Start tracking", Toast.LENGTH_SHORT)

        val scope = CoroutineScope(Dispatchers.Main)

        button.setOnClickListener {
            if (!seekBar.isEnabled) {
                seekBar.isEnabled = true
                button.setText(R.string.start_button)
                stopToast.setGravity(0, 0, 450)
                stopToast.show()
                progressBar.progress = defaultProgress
                centerText.text = intProgress.toString()
                scope.coroutineContext.cancelChildren()

            } else {
                seekBar.isEnabled = false
                button.setText(R.string.stop_button)
                startToast.setGravity(0, 0, 450)
                startToast.show()
                progressBar.progress = intProgress

                scope.launch {
                    withContext(Dispatchers.Default) {
                        var digit = intProgress
                        while (digit > 0) {
                            delay(1000)
                            digit--
                            withContext(Dispatchers.Main) {
                                progressBar.progress = digit
                                centerText.text = digit.toString()
                                if (digit == 0) {
                                    withContext(Dispatchers.Main) {
                                        progressBar.progress = defaultProgress
                                        button.setText(R.string.start_button)
                                        seekBar.isEnabled = true
                                        centerText.text = intProgress.toString()
                                    }
                                }
                            }
                        }
                        }
                    }
                }
            }


        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                intProgress = seekBar.progress
                centerText.text = intProgress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }
}