import java.util.List;

/**
 * ============================================================
 * Clase: PhysicsWorker
 * ============================================================
 *
 * Worker encargado de actualizar una parte de la lista
 * de partículas.
 *
 * Cada hilo ejecutará una instancia de esta clase.
 *
 * ============================================================
 */

public class PhysicsWorker implements Runnable {

    //---------------------------------------------------------
    // Partículas asignadas
    //---------------------------------------------------------

    private final List<Particle> particles;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public PhysicsWorker(List<Particle> particles) {

        this.particles = particles;

    }

    //---------------------------------------------------------
    // Ejecutar hilo
    //---------------------------------------------------------

    @Override
    public void run() {

        for (Particle p : particles) {

            p.update();

        }

    }

}