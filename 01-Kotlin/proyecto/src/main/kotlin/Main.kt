
fun main() {
    println("Hola Mundo")

    val inmutable: String = "Nicolas"

    var mutable: String = "Roberto"
    mutable = "Nicolas"
    // Duck typing asigna el tipo de manera automatica
    var varibale = "Roberto"

    // Variable primitiva
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true
    // Clases Java
    val fechaNacimiento: Date = Date()

    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }
        ("S") -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }

    val coqueteo = if (estadoCivilWhen == "S") "Si" else "No"

    fun imprimirNombre(nombre: String): Unit {
        // template strings
        // "Bienvenido: " + nombre + " S
        println("Nombre: ${nombre}")
    }

    fun calcularSueldo(
            sueldo: Double, // Requerido
            tasa: Double = 12.00, // Opcional (defecto)
            bonoEspecial: Double? = null, // Opcion null nullable
    ): Double {
        // Int -> Int? (nullable)
        // String> String? (nullable)
        // Date -> Date? (nullable)
        if (bonoEspecial == null) {
            return sueldo*(100 / tasa)
        } else {
            bonoEspecial.dec()
            return sueldo*(100 / tasa) + bonoEspecial
        }
    }

    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00)
    calcularSueldo(10.00, 12.00, 20.00)
    calcularSueldo(sueldo 10.00, tasa = 12.00, bonoEspecial = 20.00) // Parametros nombrados =
    calcularSueldo(18.08, bonoEspecial = 20.00) // Named Parameters
    calcularSueldo(bonoEspecial= 20.00, sueldo = 18.00, tasa = 14.00) // Parametros nombrados

}


abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int new
    constructor(
    uno: Int,
    dos: Int
    ){ // Bloque de codigo del constructor
    this.numeroUno = uno
    this.numeroDos = dos
    println("Inicializando")
    }
}

abstract class Numeros( // Constructor PRIMARIO
    // Ejemplo:
    // uno: Int, // (Parametro (sin modificador de acceso))
    // private var uno: Int, // Propiedad Publica Clase numeros.uno
    // var uno: Int; // Propiedad de la clase (por defecto es PUBLIC)
    // public var uno: Int,
    // Propiedad de la clase protected numeros.numeroUno
    protected val numeroUno: Int,
    // Propiedad de la clase protected numeros.numeroDos
    protected val numeroDos: Int
){
    // var cedula: string="" (public es por defecto)
    // private valorCalculado: Int = @ (private)
    new
    init {
        this.numeroUno; this.numeroDos; // this es opcional 
        numeroUno; numeroDos; // sin el "this", es lo mismo 
        println("Inicializando")
    }
}

class Suma( // Constructor Primario Suma
    uno: Int, // Parametro
    dos: Int // Parametro
): Numeros(uno, dos) { // <- Constructor del Padre
    new*
    init { // Bloque constructor primario
     this.numeroUno; numeroUno; 
     this.numeroDos; numeroDos;
    }
}