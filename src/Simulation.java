import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ============================================================
 * Clase: Simulation
 * ============================================================
 *
 * Responsabilidad:
 * Administrar toda la simulación de partículas.
 *
 * Funciones:
 *
 * • Crear explosiones
 * • Actualizar partículas
 * • Eliminar partículas muertas
 * • Controlar la cantidad de hilos utilizados
 *
 * ============================================================
 */

public class Simulation {

    //---------------------------------------------------------
    // Lista de partículas
    //---------------------------------------------------------

    private final ArrayList<Particle> particles;

    //---------------------------------------------------------
    // Contador de frames
    //---------------------------------------------------------

    private int frameCounter;

    //---------------------------------------------------------
    // Número de hilos
    //---------------------------------------------------------

    private int threadCount;

    //---------------------------------------------------------
    // Generador aleatorio
    //---------------------------------------------------------

    private final ThreadLocalRandom random;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Simulation() {

        particles = new ArrayList<>();

        frameCounter = 0;

        threadCount = 1;

        random = ThreadLocalRandom.current();

    }

    //---------------------------------------------------------
    // Obtener partículas
    //---------------------------------------------------------

    public List<Particle> getParticles() {

        return particles;

    }

    //---------------------------------------------------------
    // Configurar número de hilos
    //---------------------------------------------------------

    public void setThreadCount(int threadCount) {

        this.threadCount = threadCount;

    }

    //---------------------------------------------------------
    // Obtener número de hilos
    //---------------------------------------------------------

    public int getThreadCount() {

        return threadCount;

    }

    //---------------------------------------------------------
    // Actualizar simulación
    //---------------------------------------------------------

    public void update(int width, int height) {

        //---------------------------------------------
        // Contador de frames
        //---------------------------------------------

        frameCounter++;

        //---------------------------------------------
        // Crear explosión periódicamente
        //---------------------------------------------

        if (frameCounter >= 45) {

            frameCounter = 0;

            createExplosion(width, height);

        }

        //---------------------------------------------
        // Actualizar partículas usando multihilo
        //---------------------------------------------

        if (!particles.isEmpty()) {

            ThreadManager.updateParticles(
                    particles,
                    threadCount
            );

        }

        //---------------------------------------------
        // Eliminar partículas muertas
        //---------------------------------------------

        particles.removeIf(p -> !p.isAlive());

    }

    //---------------------------------------------------------
    // Crear explosión
    //---------------------------------------------------------

    private void createExplosion(int width, int height) {

        int x = random.nextInt(150, width - 150);

        int y = random.nextInt(100, height / 2);

        Color[] colors = {

                Assets.CYAN,
                Assets.PINK,
                Assets.YELLOW,
                Assets.GREEN,
                Assets.PURPLE

        };

        Color color = colors[random.nextInt(colors.length)];

        //-----------------------------------------------------
        // Crear partículas
        //-----------------------------------------------------

        for (int i = 0; i < 250; i++) {

            particles.add(

                    Particle.createExplosionParticle(

                            x,

                            y,

                            color

                    )

            );

        }

    }

}