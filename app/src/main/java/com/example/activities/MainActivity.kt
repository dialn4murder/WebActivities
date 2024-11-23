package com.example.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.activities.databinding.ActivityMainBinding
import java.net.URLEncoder
import android.content.Intent
import android.os.PersistableBundle
import android.text.TextWatcher
import android.view.View
import android.text.Editable


class MainActivity : AppCompatActivity() {
    private var prevUrl: String? = null
    private lateinit var binding: ActivityMainBinding
    private val encodedSearchItem : String
        get() = URLEncoder.encode(binding.etSearch.text.toString(), "UTF-8")
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val url = result.data?.getStringExtra("url")
            if (url != null){
                prevUrl = url
                binding.btnPrevious.visibility= View.VISIBLE
            } else {
                binding.btnPrevious.visibility = View.INVISIBLE
            }
        }
    }
    // Toggles the buttons states
    private fun toggleButtonsState(enabled: Boolean){
        binding.btnAndroid.isEnabled = enabled
        binding.btnKotlin.isEnabled = enabled
        binding.btnStackOverflow.isEnabled = enabled
        binding.btnGoogle.isEnabled = enabled
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //prevUrl = savedInstanceState?.getString("url")

        //prevUrl = savedInstanceState?.getBundle("url").toString()
        if (savedInstanceState != null){
            prevUrl = savedInstanceState.getString("url")
            //binding.etSearch.
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnGoogle.setOnClickListener{
            loadWebActivity("https://google.co.uk/search?q=$encodedSearchItem")
        }
        binding.btnStackOverflow.setOnClickListener{
            loadWebActivity("https://stackoverflow.com/search?q=$encodedSearchItem")
        }
        binding.btnKotlin.setOnClickListener{
            loadWebActivity("https://kotlinlang.org/?q=$encodedSearchItem")
        }
        binding.btnAndroid.setOnClickListener{
            loadWebActivity("https://developer.android.com/s/results/?q=$encodedSearchItem")
        }
        binding.btnPrevious.setOnClickListener{
            loadWebActivity(prevUrl?: "")
        }
        // Detects changes within etSearch
        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?){

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
                toggleButtonsState(binding.etSearch.text.length > 1)
            }
        })

        // Disables buttons
        toggleButtonsState(false)
    }
    private fun loadWebActivity(url: String){
        val intent = Intent(this, WebSearchActivity::class.java)
        intent.putExtra("url", url)
        resultLauncher.launch((intent))
    }

//    override fun onPause() {
//        var savedUrl = prevUrl
//        super.onPause()
//
//
//    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        //outPersistentState.putString("url", prevUrl)
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("url", prevUrl)
        outState.putString("search", binding.etSearch.text.toString())

    }

//    override fun onRestart() {
//
//        super.onRestart()
//    }
}