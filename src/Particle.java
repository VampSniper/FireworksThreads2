import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ============================================================
 * Clase: Particle
 * ============================================================
 *
 * Responsabilidad:
 * Representar una partícula individual dentro de la simulación.
 *
 * Cada partícula administra:
 *
 * • Posición
 * • Velocidad
 * • Color
 * • Tamaño
 * • Tiempo de vida
 *
 * Cada partícula puede actualizarse de forma independiente,
 * lo que facilita el procesamiento paralelo mediante múltiples
 * hilos.
 *
 * ============================================================
 */

public class Particle {

    //---------------------------------------------------------
    // Estado de la partícula
    //---------------------------------------------------------

    private Vector2D position;
    private Vector2D velocity;

    private Color color;

    private int size;

    private int life;

    private int maxLife;

    //---------------------------------------------------------
    // Constantes físicas
    //---------------------------------------------------------

    private static final double GRAVITY = 0.05;

    private static final double FRICTION = 0.99;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Particle(
            double x,
            double y,
            double vx,
            double vy,
            Color color,
            int size,
            int life
    ) {

        position = new Vector2D(x, y);

        velocity = new Vector2D(vx, vy);

        this.color = color;

        this.size = size;

        this.life = life;

        this.maxLife = life;

    }

    //---------------------------------------------------------
    // Actualizar física
    //---------------------------------------------------------

    public void update() {

        //-----------------------------
        // Movimiento
        //-----------------------------

        position.add(velocity);

        //-----------------------------
        // Gravedad
        //-----------------------------

        velocity.y += GRAVITY;

        //-----------------------------
        // Fricción
        //-----------------------------

        velocity.multiply(FRICTION);

        //-----------------------------
        // Reducir vida
        //-----------------------------

        life--;

    }

    //---------------------------------------------------------
    // Dibujar partícula
    //---------------------------------------------------------

    public void draw(Graphics2D g2) {

        float alpha = Math.max(
                0f,
                (float) life / maxLife
        );

        Composite original = g2.getComposite();

        g2.setComposite(

                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        alpha
                )

        );

        g2.setColor(color);

        g2.fillOval(

                (int) position.x,

                (int) position.y,

                size,

                size

        );

        g2.setComposite(original);

    }

    //---------------------------------------------------------
    // Estado
    //---------------------------------------------------------

    public boolean isAlive() {

        return life > 0;

    }

    //---------------------------------------------------------
    // Factory para crear partículas de explosión
    //---------------------------------------------------------

    public static Particle createExplosionParticle(

            double x,

            double y,

            Color color

    ) {

        ThreadLocalRandom random =
                ThreadLocalRandom.current();

        double angle =
                random.nextDouble(0, Math.PI * 2);

        double speed =
                random.nextDouble(2, 7);

        return new Particle(

                x,

                y,

                Math.cos(angle) * speed,

                Math.sin(angle) * speed,

                color,

                random.nextInt(3, 6),

                random.nextInt(90, 150)

        );

    }

}