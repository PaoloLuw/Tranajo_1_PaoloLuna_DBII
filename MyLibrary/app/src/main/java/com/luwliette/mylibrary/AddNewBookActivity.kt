package com.luwliette.mylibrary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luwliette.mylibrary.databinding.ActivityAddNewBookBinding

class AddNewBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewBookBinding

    private lateinit var dbHelper: DatabaseHelper
    private var bookId: Long = -1  // Variable para almacenar el ID del libro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNewBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)
        bookId = intent.getLongExtra("book_id", -1)

        binding.editTextBookFaculty.setText("Escuela: Ing Sistemas")
        binding.editTextBookDescription.setText("Descripcion: not avaible yet")

        funBntInit()
    }

    private fun addingAllBooks( bookName: String, bookCode: String, bookFaculty: String, bookAuthor: String, bookDescription: String) {
        // Insertar libros
        val Addinglibros = listOf(    Triple  (bookName, bookCode, bookAuthor))

        for ((nombreLibro, codigoLibro, autorLibro) in Addinglibros) {
            val libroId = dbHelper.addLibro(
                nombreLibro,
                codigoLibro,
                autorLibro,
                1,
                "disponible"
            ) // Suponiendo que la facultad con ID 1 existe ojo
            Log.d("DatabaseTest", "Libro añadido con ID: $libroId")
        }
    }

    private fun funBntInit() {
        binding.buttonAdd.setOnClickListener {
            val bookName = binding.editTextBookName.text.toString()
            val bookCode = binding.editTextBookCode.text.toString()
            val bookFaculty = binding.editTextBookFaculty.text.toString()
            val bookAuthor = binding.editTextBookAuthor.text.toString()
            val bookDescription = binding.editTextBookDescription.text.toString()

            addingAllBooks( bookName , bookCode , bookFaculty , bookAuthor , bookDescription )

            Toast.makeText(this, "Libro añadido: $bookName", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
