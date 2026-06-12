
/**
 * ============================================================
 * Clase: FPSCounter
 * ============================================================
 *
 * Calcula los Frames Por Segundo (FPS) en tiempo real.
 *
 * ============================================================
 */

public class FPSCounter {

    //---------------------------------------------------------
    // Variables
    //---------------------------------------------------------

    private long lastTime;

    private int frames;

    private int fps;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public FPSCounter() {

        lastTime = System.nanoTime();

        frames = 0;

        fps = 0;

    }

    //---------------------------------------------------------
    // Actualizar contador
    //---------------------------------------------------------

    public void update() {

        frames++;

        long current = System.nanoTime();

        if (current - lastTime >= 1_000_000_000L) {

            fps = frames;

            frames = 0;

            lastTime = current;

        }

    }

    //---------------------------------------------------------
    // Obtener FPS
    //---------------------------------------------------------

    public int getFPS() {

        return fps;

    }

}
