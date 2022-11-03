package com.example.CountDownTimer

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.CountDownTimer.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val progressKey = "currentProgress"
    private val isRunningKey = "isRunning"
    private var currentProgress = 60
    private val defaultProgress = 60
    private var isRunning = false

    private lateinit var binding: ActivityMainBinding

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val progressBar = binding.progressBar
        val centerText = binding.textViewCenter
        val seekBar = binding.seekBar.apply { progress = defaultProgress }
        val button = binding.button
        val stopToast = Toast(this).apply {
            val view = LayoutInflater.from(applicationContext).inflate(R.layout.stop_toast, null)
            setView(view)
            duration = Toast.LENGTH_SHORT
            }
        val startToast = Toast(this).apply {
            val view = LayoutInflater.from(applicationContext).inflate(R.layout.start_toast, null)
            setView(view)
            duration = Toast.LENGTH_SHORT
        }

        savedInstanceState?.apply {
            currentProgress = savedInstanceState.getInt(progressKey)
            isRunning = savedInstanceState.getBoolean(isRunningKey)
            if (isRunning) {
                button.setText(R.string.stop_button)
                seekBar.isEnabled = false
            }
            centerText.text = currentProgress.toString()

        }

            if (isRunning) scope.launch {
            withContext(Dispatchers.Default) {
                while (currentProgress > 0) {
                    delay(1000)
                    currentProgress--
                    withContext(Dispatchers.Main) {
                        progressBar.progress = currentProgress
                        centerText.text = currentProgress.toString()
                        if (currentProgress == 0) {
                            withContext(Dispatchers.Main) {
                                progressBar.progress = defaultProgress
                                seekBar.progress = defaultProgress
                                button.setText(R.string.start_button)
                                seekBar.isEnabled = true
                                centerText.text = defaultProgress.toString()
                                job.cancelChildren()
                                isRunning = false
                            }
                        }
                    }
                }
            }
        }

        button.setOnClickListener {
            if (isRunning) {
                seekBar.isEnabled = true
                button.setText(R.string.start_button)
                stopToast.apply {
                    setGravity(0, 0, 450)
                    show()
                }
                progressBar.progress = currentProgress
                centerText.text = currentProgress.toString()
                job.cancelChildren()
                isRunning = false

            }
            else {
                isRunning = true
                seekBar.isEnabled = false
                button.setText(R.string.stop_button)
                startToast.apply {
                    setGravity(0, 0, 450)
                    show()
                }
                scope.launch {
                    withContext(Dispatchers.Default) {
                        while (currentProgress > 0) {
                            delay(1000)
                            currentProgress--
                            withContext(Dispatchers.Main) {
                                progressBar.progress = currentProgress
                                centerText.text = currentProgress.toString()
                                if (currentProgress == 0) {
                                    withContext(Dispatchers.Main) {
                                        progressBar.progress = defaultProgress
                                        seekBar.progress = defaultProgress
                                        button.setText(R.string.start_button)
                                        seekBar.isEnabled = true
                                        centerText.text = defaultProgress.toString()
                                        job.cancelChildren()
                                        isRunning = false
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
                if (!seekBar.isEnabled) seekBar.progress = currentProgress
                currentProgress = seekBar.progress
                centerText.text = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt(progressKey, currentProgress)
        savedInstanceState.putBoolean(isRunningKey, isRunning)
        super.onSaveInstanceState(savedInstanceState)

    }
}
