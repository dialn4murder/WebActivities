package com.example.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.activities.databinding.ActivityMainBinding
import com.example.activities.databinding.ActivityWebSearchBinding

class WebSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebSearchBinding
    private val backPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent()
            intent.putExtra("url",binding.webView.url)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWebSearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.webView.settings.javaScriptEnabled = true

        // Check for phone rotating
        val url = savedInstanceState?.getString("url") ?: intent.getStringExtra("url")

        binding.webView.loadUrl(url!!)

        binding.webView.webViewClient = WebViewClient() // Prevents opening in browser

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", binding.webView.url)
    }

}