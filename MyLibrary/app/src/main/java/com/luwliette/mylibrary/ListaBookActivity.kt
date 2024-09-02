package com.luwliette.mylibrary

import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luwliette.mylibrary.adapter.Book
import com.luwliette.mylibrary.adapter.BookAdapter
import com.luwliette.mylibrary.databinding.ActivityListaBookBinding
import kotlin.math.log

class ListaBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBookBinding
    private lateinit var dbHelper: DatabaseHelper
    private var Rol: Long = -1


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListaBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de insets para manejo de margenes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ListaBookActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        Rol = intent.getLongExtra("rol", -1) //si es 11 es solo lector y si es 22 es editor

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        funInitBnt()

        if (Rol.toInt() == 22) {
            Toast.makeText(this, "Rol: Bibliotecario", Toast.LENGTH_SHORT).show()

            binding.fab.visibility = View.VISIBLE // Mostrar el botón
        } else {
            Toast.makeText(this, "Rol: Alumno", Toast.LENGTH_SHORT).show()

            binding.fab.visibility = View.GONE // Ocultar el botón
        }

    }

    override fun onResume(){

        super.onResume()
        val bookList = getBooksFromDatabase()
        //Inicializa la lista de libros
        val adapter = BookAdapter(bookList) { book ->
            val intent = Intent(this, InfoBookActivity::class.java)
            intent.putExtra("book_id", book.id)  // Pasa el ID del libro a la siguiente actividad
            intent.putExtra("book_estado", book.estado)  // Pasa el ID del libro a la siguiente actividad
            intent.putExtra("rol", Rol)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

    }

    private fun funInitBnt() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddNewBookActivity::class.java)
            startActivity(intent)
        }
    }


    private fun getBooksFromDatabase(): List<Book> {
        val bookList = mutableListOf<Book>()
        val cursor = dbHelper.getAllLibros()

        // Itera sobre el cursor para extraer los datos y crear objetos Book
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id_libro"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("nombre_libro"))
            val code = cursor.getString(cursor.getColumnIndexOrThrow("codigo_libro"))
            val author = cursor.getString(cursor.getColumnIndexOrThrow("autor_libro"))
            val facultyId = cursor.getLong(cursor.getColumnIndexOrThrow("id_facultad"))
            val estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))

            bookList.add(Book(id, name, code, author, facultyId, estado))
        }
        cursor.close()
        return bookList
    }
}
