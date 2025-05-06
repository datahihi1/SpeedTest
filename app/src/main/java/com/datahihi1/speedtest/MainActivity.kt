package com.datahihi1.speedtest

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnStart)
        resultText = findViewById(R.id.tvResult)

        button.setOnClickListener {
            measureDownloadSpeed()
        }
    }

    private fun measureDownloadSpeed() {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://speed.hetzner.de/10MB.bin"
            val request = Request.Builder().url(url).build()

            val startTime = System.currentTimeMillis()
            val response = client.newCall(request).execute()
            val byteCount = response.body?.bytes()?.size ?: 0
            val endTime = System.currentTimeMillis()

            val durationSeconds = (endTime - startTime) / 1000.0
            val speedMbps = (byteCount * 8 / 1_000_000.0) / durationSeconds

            val speedStr = DecimalFormat("#.##").format(speedMbps)

            runOnUiThread {
                resultText.text = "Tốc độ tải: $speedStr Mbps"
            }
        }
    }
}

