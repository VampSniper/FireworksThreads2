import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * Clase: ThreadManager
 * ============================================================
 *
 * Responsabilidad:
 * Administrar la actualización de partículas utilizando
 * múltiples hilos de ejecución.
 *
 * Funciones:
 *
 * • Dividir la lista de partículas
 * • Crear los hilos necesarios
 * • Esperar a que todos finalicen
 *
 * ============================================================
 */

public class ThreadManager {

    //---------------------------------------------------------
    // Constructor privado
    //---------------------------------------------------------

    private ThreadManager() {

    }

    //---------------------------------------------------------
    // Actualizar partículas
    //---------------------------------------------------------

    public static void updateParticles(

            List<Particle> particles,

            int threadCount

    ) {

        //-----------------------------------------------------
        // Validaciones
        //-----------------------------------------------------

        if (particles.isEmpty()) {

            return;

        }

        if (threadCount <= 1) {

            for (Particle p : particles) {

                p.update();

            }

            return;

        }

        //-----------------------------------------------------
        // Calcular tamaño de cada bloque
        //-----------------------------------------------------

        int totalParticles = particles.size();

        int chunkSize = (int) Math.ceil(
                (double) totalParticles / threadCount
        );

        //-----------------------------------------------------
        // Lista de hilos
        //-----------------------------------------------------

        List<Thread> threads = new ArrayList<>();

        //-----------------------------------------------------
        // Crear hilos
        //-----------------------------------------------------

        for (int i = 0; i < threadCount; i++) {

            int start = i * chunkSize;

            int end = Math.min(start + chunkSize, totalParticles);

            if (start >= end) {

                break;

            }

            PhysicsWorker worker =

                    new PhysicsWorker(

                            particles.subList(start, end)

                    );

            Thread thread = new Thread(worker);

            threads.add(thread);

            thread.start();

        }

        //-----------------------------------------------------
        // Esperar que todos terminen
        //-----------------------------------------------------

        for (Thread thread : threads) {

            try {

                thread.join();

            }

            catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }

        }

    }

}