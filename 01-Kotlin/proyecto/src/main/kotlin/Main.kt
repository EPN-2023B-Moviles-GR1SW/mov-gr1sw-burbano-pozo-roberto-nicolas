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
}
