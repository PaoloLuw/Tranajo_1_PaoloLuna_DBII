package com.luwliette.mylibrary

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luwliette.mylibrary.adapter.Book
import com.luwliette.mylibrary.databinding.ActivityInfoBookBinding
import java.util.Calendar

class InfoBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBookBinding
    private lateinit var dbHelper: DatabaseHelper
    private var bookId: Long = -1  // Variable para almacenar el ID del libro
    private var bookEstado: String = "-1"  // Variable para almacenar el ID del libro

    private var Rol: Long = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityInfoBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        bookId = intent.getLongExtra("book_id", -1)
        bookEstado = intent.getStringExtra("book_estado").toString()
        Rol = intent.getLongExtra("rol", -1) //si es 11 es solo lector y si es 22 es editor


        if (bookId != -1L) {
            val book = dbHelper.getBookById(bookId)
            if (book != null) {
                binding.bookName.text = "Nombre: ${book.name}"
                binding.bookAuthor.text = "Autor: ${book.author}"
                binding.bookCode.text = "Código: ${book.code}"
                binding.bookStatus.text = "Estado: ${book.estado}"
            } else {
                Toast.makeText(this, "Libro no encontrado", Toast.LENGTH_SHORT).show()
                finish()  // Cierra la actividad si no se encuentra el libro
            }
        } else {
            Toast.makeText(this, "ID de libro no válido", Toast.LENGTH_SHORT).show()
            finish()  // Cierra la actividad si el ID es inválido
        }

        setupButtons()


        if (Rol.toInt() == 22) {
            binding.buttonDelete.visibility = View.VISIBLE
            binding.buttonEdit.visibility = View.VISIBLE
            binding.buttonLend.visibility = View.VISIBLE
        } else {
            binding.buttonDelete.visibility = View.GONE
            binding.buttonEdit.visibility = View.GONE
            binding.buttonLend.visibility = View.GONE
        }
    }

    private fun setupButtons() {
        binding.buttonDelete.setOnClickListener {

            val rowsDeleted = dbHelper.deleteLibro(bookId)
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Libro eliminado con éxito", Toast.LENGTH_SHORT).show()
                finish()  // Cierra la actividad después de la eliminación
            } else {
                Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show()
            }

        }

        binding.buttonEdit.setOnClickListener {
            Toast.makeText(this, "Editar libro", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditBookActivity::class.java)
            intent.putExtra("book_id", bookId)  // Pasa el ID del libro a la siguiente actividad
            startActivity(intent)
        }

        binding.buttonLend.setOnClickListener {
            Toast.makeText(this, "Prestar libro", Toast.LENGTH_SHORT).show()
            showLendBookDialog()
        }
    }


    private fun showLendBookDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_lend_book, null)
        val etStudentCode = dialogView.findViewById<EditText>(R.id.etStudentCode)
        val etLoanDate = dialogView.findViewById<EditText>(R.id.etLoanDate)
        val etReturnDate = dialogView.findViewById<EditText>(R.id.etReturnDate)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Registrar Préstamo")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar") { _, _ ->

                val studentCode = etStudentCode.text.toString().toLongOrNull()
                val loanDate = etLoanDate.text.toString()
                val returnDate = etReturnDate.text.toString()

                if (studentCode != null && loanDate.isNotEmpty() && returnDate.isNotEmpty()) {
                    val rowsUpdated = dbHelper.addPrestamo(studentCode, bookId, loanDate, returnDate) //one

                    if (rowsUpdated > 0) {

                        if(bookEstado == "disponible"){
                            dbHelper.updateLibroEstado(bookId, "prestado")
                            Toast.makeText(this, "Préstamo registrado exitosamente", Toast.LENGTH_SHORT).show()
                        }else{
                            dbHelper.updateLibroEstado(bookId, "disponible")
                            Toast.makeText(this, "Préstamo devuelto exitosamente", Toast.LENGTH_SHORT).show()
                        }
                        finish()  // Vuelve a ListaBookActivity
                    } else {
                        Toast.makeText(this, "Error al registrar el préstamo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }

        // Configuración de la interfaz dependiendo del estado del libro
        if (bookEstado == "disponible") {
            etReturnDate.isEnabled = false 
            etReturnDate.setText("N/A") 
        } else if (bookEstado == "prestado") {
            etStudentCode.isEnabled = false 
            etStudentCode.setText("Ya registrado")
            etLoanDate.isEnabled = false
            etLoanDate.setText("NULL")
        }

        dialogBuilder.show()

        // Function to show DatePickerDialog
        fun showDatePickerDialog(editText: EditText) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                editText.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // Set onClickListeners for the date fields
        etLoanDate.setOnClickListener { showDatePickerDialog(etLoanDate) }
        etReturnDate.setOnClickListener { showDatePickerDialog(etReturnDate) }
    }
}
