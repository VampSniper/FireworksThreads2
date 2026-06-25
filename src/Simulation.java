import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ============================================================
 * Clase: Simulation
 * ============================================================
 *
 * Modos:
 * • PARTICLES — simulación de fuegos artificiales
 * • MATRIX    — multiplicación de matrices (CPU intensivo)
 * • FILE      — escritura de archivos (I/O intensivo)
 *
 * ============================================================
 */

public class Simulation {

    // =========================================================
    // Modos
    // =========================================================

    public enum Mode {
        PARTICLES,
        MATRIX,
        FILE
    }

    //---------------------------------------------------------
    // Modo actual
    //---------------------------------------------------------

    private Mode mode = Mode.PARTICLES;

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
    // Último tiempo medido (matriz / archivo)
    //---------------------------------------------------------

    private long lastElapsed = 0;

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
    // Getters / Setters
    //---------------------------------------------------------

    public List<Particle> getParticles() { return particles; }

    public void setThreadCount(int threadCount) { this.threadCount = threadCount; }

    public int getThreadCount() { return threadCount; }

    public Mode getMode() { return mode; }

    public void setMode(Mode mode) {
        this.mode = mode;
        particles.clear();
        frameCounter = 0;
        lastElapsed = 0;
    }

    public long getLastElapsed() { return lastElapsed; }

    //---------------------------------------------------------
    // Reset completo — limpia partículas y contadores
    //---------------------------------------------------------

    public void reset() {
        particles.clear();
        frameCounter = 0;
        lastElapsed = 0;
    }

    //---------------------------------------------------------
    // Actualizar simulación
    //---------------------------------------------------------

    public void update(int width, int height) {

        switch (mode) {
            case PARTICLES -> updateParticles(width, height);
            case MATRIX    -> updateMatrix();
            case FILE      -> updateFile();
        }

    }

    //---------------------------------------------------------
    // Modo PARTICLES
    //---------------------------------------------------------

    private void updateParticles(int width, int height) {

        frameCounter++;

        if (frameCounter >= 45) {
            frameCounter = 0;
            createExplosion(width, height);
        }

        if (!particles.isEmpty()) {
            ThreadManager.updateParticles(particles, threadCount);
        }

        particles.removeIf(p -> !p.isAlive());
    }

    //---------------------------------------------------------
    // Modo MATRIX
    //---------------------------------------------------------

    private void updateMatrix() {

        frameCounter++;

        if (frameCounter >= 60) {
            frameCounter = 0;
            lastElapsed = ThreadManager.multiplyMatrices(threadCount);
        }

    }

    //---------------------------------------------------------
    // Modo FILE
    //---------------------------------------------------------

    private void updateFile() {

        frameCounter++;

        if (frameCounter >= 120) {
            frameCounter = 0;
            lastElapsed = ThreadManager.writeFiles(threadCount);
        }

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

        for (int i = 0; i < 2000; i++) {
            particles.add(Particle.createExplosionParticle(x, y, color));
        }
    }
}