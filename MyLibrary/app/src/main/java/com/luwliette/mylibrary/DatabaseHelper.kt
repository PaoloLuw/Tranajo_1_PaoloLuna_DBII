package com.luwliette.mylibrary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.util.Log
import com.luwliette.mylibrary.adapter.Alumno
import com.luwliette.mylibrary.adapter.Book

class DatabaseHelper : SQLiteOpenHelper {

    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 1

        // Tabla alumnos
        private const val TABLE_ALUMNOS = "alumnos"
        private const val COLUMN_ID_ALUMNO = "id_alumno"
        private const val COLUMN_ID_NOMBRE = "id_nombre"
        private const val COLUMN_ID_APELLIDO = "id_apellido"
        private const val COLUMN_DNI = "dni"
        private const val COLUMN_CODIGO_ESTUDIANTE = "codigo_estudiante"

        // Tabla facultad
        private const val TABLE_FACULTAD = "facultad"
        private const val COLUMN_ID_FACULTAD = "id_facultad"
        private const val COLUMN_NOMBRE_FACULTAD = "nombre_facultad"

        // Tabla libro
        private const val TABLE_LIBRO = "libro"
        private const val COLUMN_ID_LIBRO = "id_libro"
        private const val COLUMN_NOMBRE_LIBRO = "nombre_libro"
        private const val COLUMN_CODIGO_LIBRO = "codigo_libro"
        private const val COLUMN_AUTOR_LIBRO = "autor_libro"
        private const val COLUMN_LIBRO_FACULTAD_ID = "id_facultad"
        private const val COLUMN_ESTADO = "estado"

        // Tabla prestamo
        private const val TABLE_PRESTAMO = "prestamo"
        private const val COLUMN_ID_PRESTAMO = "id_prestamo"
        private const val COLUMN_ALUMNO_ID = "id_alumno"
        private const val COLUMN_LIBRO_ID = "id_libro"
        private const val COLUMN_FECHA_PRESTAMO = "fecha_prestamo"
        private const val COLUMN_FECHA_DEVOLUCION = "fecha_devolucion"

    }

    constructor(context: Context) : super(
        /* context: */ context,
        /* name: */ DATABASE_NAME,
        /* factory: */ null,
        /* version: */ DATABASE_VERSION
    )

    override fun onCreate(db: SQLiteDatabase) {
        CreateTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRESTAMO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIBRO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FACULTAD")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        onCreate(db)
    }

    fun CreateTables(db: SQLiteDatabase){
        // Creación de la tabla alumnos
        val createAlumnosTable = """
            CREATE TABLE $TABLE_ALUMNOS (
                $COLUMN_ID_ALUMNO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ID_NOMBRE TEXT,
                $COLUMN_ID_APELLIDO TEXT,
                $COLUMN_DNI TEXT,
                $COLUMN_CODIGO_ESTUDIANTE TEXT)
            """.trimIndent()
        db.execSQL(createAlumnosTable)

        // Creación de la tabla facultad
        val createFacultadTable = """
            CREATE TABLE $TABLE_FACULTAD (
                $COLUMN_ID_FACULTAD INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE_FACULTAD TEXT)
            """.trimIndent()
        db.execSQL(createFacultadTable)

        // Creación de la tabla libro
        val createLibroTable = """
            CREATE TABLE $TABLE_LIBRO (
                $COLUMN_ID_LIBRO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE_LIBRO TEXT,
                $COLUMN_CODIGO_LIBRO TEXT,
                $COLUMN_AUTOR_LIBRO TEXT,
                $COLUMN_LIBRO_FACULTAD_ID INTEGER,
                $COLUMN_ESTADO TEXT,
                FOREIGN KEY($COLUMN_LIBRO_FACULTAD_ID) REFERENCES $TABLE_FACULTAD($COLUMN_ID_FACULTAD))
            """.trimIndent()
        db.execSQL(createLibroTable)

        // Creación de la tabla prestamo
        val createPrestamoTable = """
                CREATE TABLE $TABLE_PRESTAMO (
                    $COLUMN_ID_PRESTAMO INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_ALUMNO_ID INTEGER,
                    $COLUMN_LIBRO_ID INTEGER,
                    $COLUMN_FECHA_PRESTAMO TEXT, -- Usamos TEXT en lugar de DATE en SQLite
                    $COLUMN_FECHA_DEVOLUCION TEXT, -- Usamos TEXT en lugar de DATE en SQLite
                    FOREIGN KEY($COLUMN_ALUMNO_ID) REFERENCES $TABLE_ALUMNOS($COLUMN_ID_ALUMNO),
                    FOREIGN KEY($COLUMN_LIBRO_ID) REFERENCES $TABLE_LIBRO($COLUMN_ID_LIBRO)
                )
            """.trimIndent()
        db.execSQL(createPrestamoTable)
    }

    fun addAlumno(
        idNombre: String,
        idApellido: String,
        dni: String,
        codigoEstudiante: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_NOMBRE, idNombre)
            put(COLUMN_ID_APELLIDO, idApellido)
            put(COLUMN_DNI, dni)
            put(COLUMN_CODIGO_ESTUDIANTE, codigoEstudiante)
        }
        return db.insert(TABLE_ALUMNOS, null, values)
    }

    fun getAllAlumnos(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_ALUMNOS, null, null, null, null, null, null)
    }

    fun updateAlumno(
        id: Long,
        idNombre: String,
        idApellido: String,
        dni: String,
        codigoEstudiante: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID_NOMBRE, idNombre)
            put(COLUMN_ID_APELLIDO, idApellido)
            put(COLUMN_DNI, dni)
            put(COLUMN_CODIGO_ESTUDIANTE, codigoEstudiante)
        }
        return db.update(TABLE_ALUMNOS, values, "$COLUMN_ID_ALUMNO = ?", arrayOf(id.toString()))
    }

    fun deleteAlumno(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_ALUMNOS, "$COLUMN_ID_ALUMNO = ?", arrayOf(id.toString()))
    }

    // Métodos CRUD para facultad

    fun addFacultad(nombreFacultad: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_FACULTAD, nombreFacultad)
        }
        return db.insert(TABLE_FACULTAD, null, values)
    }

    fun getAllFacultades(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_FACULTAD, null, null, null, null, null, null)
    }

    fun updateFacultad(id: Long, nombreFacultad: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_FACULTAD, nombreFacultad)
        }
        return db.update(TABLE_FACULTAD, values, "$COLUMN_ID_FACULTAD = ?", arrayOf(id.toString()))
    }

    fun deleteFacultad(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_FACULTAD, "$COLUMN_ID_FACULTAD = ?", arrayOf(id.toString()))
    }

    // Métodos CRUD para libro

    fun addLibro(
        nombreLibro: String,
        codigoLibro: String,
        autorLibro: String,
        idFacultad: Long,
        estado: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_LIBRO, nombreLibro)
            put(COLUMN_CODIGO_LIBRO, codigoLibro)
            put(COLUMN_AUTOR_LIBRO, autorLibro)
            put(COLUMN_LIBRO_FACULTAD_ID, idFacultad)
            put(COLUMN_ESTADO, estado)
        }
        return db.insert(TABLE_LIBRO, null, values)
    }

    fun getAllLibros(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_LIBRO, null, null, null, null, null, null)
    }

    fun updateLibro(
        id: Long,
        nombreLibro: String,
        codigoLibro: String,
        autorLibro: String,
        idFacultad: Long,
        estado: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_LIBRO, nombreLibro)
            put(COLUMN_CODIGO_LIBRO, codigoLibro)
            put(COLUMN_AUTOR_LIBRO, autorLibro)
            put(COLUMN_LIBRO_FACULTAD_ID, idFacultad)
            put(COLUMN_ESTADO, estado)
        }
        return db.update(TABLE_LIBRO, values, "$COLUMN_ID_LIBRO = ?", arrayOf(id.toString()))
    }

    fun deleteLibro(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_LIBRO, "$COLUMN_ID_LIBRO = ?", arrayOf(id.toString()))
    }



    fun getBookById(bookId: Long): Book? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LIBRO,
            null,  // Seleccionar todas las columnas
            "$COLUMN_ID_LIBRO = ?",  // WHERE id_libro = ?
            arrayOf(bookId.toString()),
            null,  // No agrupar
            null,  // No tener filtros de agrupación
            null  // No tener orden específico
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID_LIBRO))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE_LIBRO))
            val code = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODIGO_LIBRO))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTOR_LIBRO))
            val facultyId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LIBRO_FACULTAD_ID))
            val estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO))

            cursor.close()
            Book(id, name, code, author, facultyId, estado)  // Retorna un objeto Book
        } else {
            cursor.close()
            null  // Si no se encuentra, retorna null
        }
    }

    fun updateLibroEstado(id: Long, estado: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ESTADO, estado)
        }
        return db.update(TABLE_LIBRO, values, "$COLUMN_ID_LIBRO = ?", arrayOf(id.toString()))
    }




    // Métodos CRUD para prestamo

    fun addPrestamo(
        idAlumno: Long,
        idLibro: Long,
        fechaPrestamo: String,
        fechaDevolucion: String?
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ALUMNO_ID, idAlumno)
            put(COLUMN_LIBRO_ID, idLibro)
            put(COLUMN_FECHA_PRESTAMO, fechaPrestamo)
            put(COLUMN_FECHA_DEVOLUCION, fechaDevolucion)
        }
        return db.insert(TABLE_PRESTAMO, null, values)
    }

    fun getAllPrestamos(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_PRESTAMO, null, null, null, null, null, null)
    }

    fun updatePrestamo(
        id: Long,
        idAlumno: Long,
        idLibro: Long,
        fechaPrestamo: String,
        fechaDevolucion: String?
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ALUMNO_ID, idAlumno)
            put(COLUMN_LIBRO_ID, idLibro)
            put(COLUMN_FECHA_PRESTAMO, fechaPrestamo)
            put(COLUMN_FECHA_DEVOLUCION, fechaDevolucion)
        }
        return db.update(TABLE_PRESTAMO, values, "$COLUMN_ID_PRESTAMO = ?", arrayOf(id.toString()))
    }

    fun deletePrestamo(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_PRESTAMO, "$COLUMN_ID_PRESTAMO = ?", arrayOf(id.toString()))
    }


    fun initializeDatasWhitDatabase() {
        // Insertar alumnos
        val alumnos = listOf(
            Alumno("Juan", "Perez", "12345678", "20230001"),
            Alumno("Ana", "Lopez", "23456789", "20230002"),
            Alumno("Luis", "Gomez", "34567890", "20230003"),
            Alumno("Maria", "Rodriguez", "45678901", "20230004"),
            Alumno("Carlos", "Martinez", "56789012", "20230005")
        )
        for (alumno in alumnos) {
            val alumnoId = addAlumno(alumno.nombre, alumno.apellido, alumno.dni, alumno.codigo)
            Log.d("DatabaseTest", "Alumno añadido con ID: $alumnoId")
        }

        // Insertar facultades
        val facultades = listOf(
            "Ing Sistemas",
            "Facultad de Ciencias",
            "Facultad de Arquitectura"
        )
        for (nombreFacultad in facultades) {
            val facultadId = addFacultad(nombreFacultad)
            Log.d("DatabaseTest", "Facultad añadida con ID: $facultadId")
        }

        // Insertar libros
        val libros = listOf(
            Triple("Re:Zero", "1234567890", "Tappei Nagatsuki"),
            Triple("No Game No Life", "2345678901", "Yuu Kamiya"),
            Triple("Sword Art Online", "3456789012", "Rei Kawahara"),
            Triple("Overlord", "4567890123", "Kugane Maruyama"),
            Triple("The Rising of the Shield Hero", "5678901234", "Aneko Yusagi"),
            Triple("Konosuba", "6789012345", "Natsume Akatsuki"),
            Triple("Log Horizon", "7890123456", "Mamare Touno"),
            Triple("That Time I Got Reincarnated as a Slime", "8901234567", "Fuse"),
            Triple("The Saga of Tanya the Evil", "9012345678", "Carlo Zen"),
            Triple("Grimgar: Ashes and Illusions", "0123456789", "Ao Jyumonji"),
            Triple("In Another World with my Smartphone", "1234567891", "Patora Fuyuhara"),
            Triple("How a Realist Hero Rebuilt the Kingdom", "2345678902", "Dojyomaru"),
            Triple("Cautious Hero", "3456789013", "Light Tuchihi"),
            Triple("Ascendance of a Bookworm", "4567890124", "Miako Kureha"),
            Triple(
                "Arifureta: From Commonplace to World's Strongest",
                "5678901235",
                "Ryo Shirakome"
            )
        )
        for ((nombreLibro, codigoLibro, autorLibro) in libros) {
            val libroId = addLibro(
                nombreLibro,
                codigoLibro,
                autorLibro,
                1,
                "disponible"
            ) // Suponiendo que la facultad con ID 1 existe
            Log.d("DatabaseTest", "Libro añadido con ID: $libroId")
        }

        // Consultar todos los alumnos
        val cursorAlumnos = getAllAlumnos()
        while (cursorAlumnos.moveToNext()) {
            val id = cursorAlumnos.getLong(cursorAlumnos.getColumnIndexOrThrow(COLUMN_ID_ALUMNO))
            val nombre =
                cursorAlumnos.getString(cursorAlumnos.getColumnIndexOrThrow(COLUMN_ID_NOMBRE))
            val apellido =
                cursorAlumnos.getString(cursorAlumnos.getColumnIndexOrThrow(COLUMN_ID_APELLIDO))
            Log.d("DatabaseTest", "Alumno: ID=$id, Nombre=$nombre, Apellido=$apellido")
        }
        cursorAlumnos.close()
    }


    fun initializeDatabase() {
        val db = writableDatabase // Esto crea la base de datos si no existe
        Log.d("DatabaseHelper", "Database created or opened")
    }
}


//1. onCreate(db: SQLiteDatabase)
//2. onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
//3. CreateTables(db: SQLiteDatabase)
//4. addAlumno(idNombre: String, idApellido: String, dni: String, codigoEstudiante: String): Long
//5. getAllAlumnos(): Cursor
//6. updateAlumno(id: Long, idNombre: String, idApellido: String, dni: String, codigoEstudiante: String): Int
//7. deleteAlumno(id: Long): Int
//8. addFacultad(nombreFacultad: String): Long
//9. getAllFacultades(): Cursor
//10. updateFacultad(id: Long, nombreFacultad: String): Int
//11. deleteFacultad(id: Long): Int
//12. addLibro(nombreLibro: String, codigoLibro: String, autorLibro: String, idFacultad: Long, estado: String): Long
//13. getAllLibros(): Cursor
//14. updateLibro(id: Long, nombreLibro: String, codigoLibro: String, autorLibro: String, idFacultad: Long, estado: String): Int
//15. deleteLibro(id: Long): Int
//16. addPrestamo(idAlumno: Long, idLibro: Long, fechaPrestamo: String, fechaDevolucion: String?): Long
//17. getAllPrestamos(): Cursor
//18. updatePrestamo(id: Long, idAlumno: Long, idLibro: Long, fechaPrestamo: String, fechaDevolucion: String?): Int
//19. deletePrestamo(id: Long): Int
//20. initializeDatasWhitDatabase()
//21. initializeDatabase()
