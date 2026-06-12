/**
 * ============================================================
 * Clase: Vector2D
 * ============================================================
 *
 * Responsabilidad:
 * Representar un vector bidimensional.
 *
 * Se utiliza para almacenar:
 *
 * • Posición
 * • Velocidad
 * • Dirección
 *
 * Esta clase simplifica las operaciones matemáticas utilizadas
 * durante la simulación de partículas.
 *
 * ============================================================
 */

public class Vector2D {

    //---------------------------------------------------------
    // Coordenadas
    //---------------------------------------------------------

    public double x;
    public double y;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Vector2D(double x, double y) {

        this.x = x;
        this.y = y;

    }

    //---------------------------------------------------------
    // Sumar otro vector
    //---------------------------------------------------------

    public void add(Vector2D other) {

        x += other.x;
        y += other.y;

    }

    //---------------------------------------------------------
    // Multiplicar por un escalar
    //---------------------------------------------------------

    public void multiply(double value) {

        x *= value;
        y *= value;

    }

    //---------------------------------------------------------
    // Crear una copia del vector
    //---------------------------------------------------------

    public Vector2D copy() {

        return new Vector2D(x, y);

    }

}