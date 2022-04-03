package com.example.ch07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val resetButton : Button by lazy {
        findViewById(R.id.resetButton)
    }

    private val recordButton : RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    private val requiredPermission = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val recordingFilePath : String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            resetButton.isEnabled = (value == State.AFTER_RECORDING)
                    || (value == State.ON_PLAYING)
            recordButton.updateIconWithState(value)
        }

    private var recorder : MediaRecorder? = null
    private var player : MediaPlayer ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initViews()
        bindViews()
        initVariables()
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    private fun bindViews() {
        recordButton.setOnClickListener {
            when(state) {
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }

        resetButton.setOnClickListener {
            state = State.BEFORE_RECORDING
            stopPlaying()
        }


    }

    private fun initVariables() {
        state = State.BEFORE_RECORDING
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if(!audioRecordPermissionGranted) {
            //No permission
            finish()
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
        state = State.AFTER_RECORDING
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.start()
        state = State.ON_PLAYING
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        state = State.AFTER_RECORDING
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}