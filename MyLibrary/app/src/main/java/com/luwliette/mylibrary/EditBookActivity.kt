package com.luwliette.mylibrary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luwliette.mylibrary.databinding.ActivityEditBookBinding

class EditBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBookBinding
    private lateinit var dbHelper: DatabaseHelper
    private var bookId: Long = -1  // Variable para almacenar el ID del libro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Configuración de insets para manejo de márgenes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        bookId = intent.getLongExtra("book_id", -1)

        if (bookId != -1L) {
            val book = dbHelper.getBookById(bookId)
            if (book != null) {
                Toast.makeText(this, "Libro encontrado: ${book.name}", Toast.LENGTH_SHORT).show()
                binding.editTextBookName.setText("${book.name}")
                binding.editTextBookAuthor.setText(" ${book.author}")
                binding.editTextBookCode.setText("${book.code}")
                binding.editTextBookFaculty.setText("Ing Sistemas")
                binding.editTextBookDescription.setText("not available yet")

            } else {
                Toast.makeText(this, "Libro no encontrado en la base de datos", Toast.LENGTH_SHORT).show()
                finish()  // Cierra la actividad si no se encuentra el libro
            }
        } else {
            Toast.makeText(this, "ID de libro no válido", Toast.LENGTH_SHORT).show()
            finish()  // Cierra la actividad si el ID es inválido
        }

        setupButtons()
    }

    private fun setupButtons() {
        binding.buttonEdit.setOnClickListener {

            // Obtén los datos del formulario
            val bookName = binding.editTextBookName.text.toString()
            val bookCode = binding.editTextBookCode.text.toString()
            val bookAuthor = binding.editTextBookAuthor.text.toString()
            val bookFaculty = binding.editTextBookFaculty.text.toString()
            val bookDescription = binding.editTextBookDescription.text.toString()


            if (bookName.isNotBlank() && bookCode.isNotBlank() && bookAuthor.isNotBlank() && bookFaculty.isNotBlank()) {
                val rowsUpdated = dbHelper.updateLibro(
                    id = bookId,
                    nombreLibro = bookName,
                    codigoLibro = bookCode,
                    autorLibro = bookAuthor,
                    idFacultad = 1,
                    estado = "disponible"
                )

                if (rowsUpdated > 0) {
                    finish()
                } else {
                    Toast.makeText(this, "Error al editar el libro", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
