package com.luwliette.mylibrary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luwliette.mylibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)
        dbHelper.initializeDatabase()

        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        inicializarDatos(isFirstRun, sharedPreferences)
        funBntInit()
    }

    private fun inicializarDatos(isFirstRun: Boolean, sharedPreferences: SharedPreferences) {
        if (isFirstRun) {
            dbHelper.initializeDatasWhitDatabase()
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
    }
    private fun funBntInit() {
        binding.firstButton.setOnClickListener {
            val intent = Intent(this, ListaBookActivity::class.java)
            intent.putExtra("rol", 11L)
            startActivity(intent)
        }


        binding.secondButton.setOnClickListener {
            val intent = Intent(this, ListaBookActivity::class.java)
            intent.putExtra("rol", 22L)
            startActivity(intent)
        }
    }
}
