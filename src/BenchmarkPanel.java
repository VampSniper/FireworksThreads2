import java.awt.*;
/**
 * ============================================================
 * Clase: BenchmarkPanel
 * ============================================================
 *
 * Responsabilidad:
 * Mostrar en pantalla el rendimiento promedio obtenido
 * durante las pruebas de benchmark para diferentes cantidades
 * de hilos de ejecución.
 *
 * Se almacenan y muestran los tiempos promedio para:
 * • 1 hilo
 * • 2 hilos
 * • 4 hilos
 * • 8 hilos
 *
 * ============================================================
 */
public class BenchmarkPanel {
    //---------------------------------------------------------
    // Tiempo promedio de ejecución por cantidad de hilos
    //---------------------------------------------------------
    private double average1;
    private double average2;
    private double average4;
    private double average8;

    //---------------------------------------------------------
    // Cantidad de pruebas realizadas por configuración
    //---------------------------------------------------------

    private int count1;
    private int count2;
    private int count4;
    private int count8;

     /**
     * Reinicia las estadísticas correspondientes a una
     * cantidad específica de hilos.
     *
     * //@param threads Número de hilos cuyo promedio será reiniciado.
     */

    public void reset(int threads) {
        switch (threads) {
            case 1 -> { average1 = 0; count1 = 0; }
            case 2 -> { average2 = 0; count2 = 0; }
            case 4 -> { average4 = 0; count4 = 0; }
            case 8 -> { average8 = 0; count8 = 0; }
        }
    }

    public void resetAll() {
        average1 = 0; count1 = 0;
        average2 = 0; count2 = 0;
        average4 = 0; count4 = 0;
        average8 = 0; count8 = 0;
    }
         /**
     * Actualiza el tiempo promedio para una determinada
     * cantidad de hilos utilizando un promedio incremental.
     *
     * Esta técnica evita almacenar todos los tiempos
     * obtenidos durante las pruebas.
     *
     * @param threads Cantidad de hilos utilizados.
     * @param time Tiempo de ejecución registrado (ms).
     */

    public void update(int threads, long time) {
        switch (threads) {
            case 1 -> { count1++; average1 += (time - average1) / count1; }
            case 2 -> { count2++; average2 += (time - average2) / count2; }
            case 4 -> { count4++; average4 += (time - average4) / count4; }
            case 8 -> { count8++; average8 += (time - average8) / count8; }
        }
    }
        /**
     * Dibuja el panel completo del benchmark.
     *
     * El panel muestra una barra para cada configuración
     * de hilos junto con su tiempo promedio de ejecución.
     *
     * @param g2 Contexto gráfico.
     * @param x Posición horizontal.
     * @param y Posición vertical.
     * @param width Ancho del panel.
     */

    public void draw(Graphics2D g2, int x, int y, int width) {
                
        // Fondo del panel

        g2.setColor(new Color(15, 15, 20));
        g2.fillRoundRect(x, y, width, 140, 20, 20);
                   
        
        // Borde

        g2.setColor(Assets.CYAN);
        g2.drawRoundRect(x, y, width, 140, 20, 20);

                
        // Título

        g2.setFont(Assets.SUBTITLE_FONT);
        g2.setColor(Assets.YELLOW);
        g2.drawString("REFERENCIA DE RENDIMIENTO", x + 20, y + 28);

               
        // Barras de rendimiento

        drawBar(g2, "1 Hilo",  average1, Assets.PINK,   x + 20, y + 55);
        drawBar(g2, "2 Hilos", average2, Assets.CYAN,   x + 20, y + 80);
        drawBar(g2, "4 Hilos", average4, Assets.PURPLE, x + 20, y + 105);
        drawBar(g2, "8 Hilos", average8, Assets.GREEN,  x + 20, y + 130);
    }
        
     /**
     * Dibuja una barra horizontal que representa el tiempo
     * promedio de ejecución para una cantidad de hilos.
     *
     * El ancho de la barra es proporcional al tiempo
     * registrado, con un límite máximo para evitar
     * desbordamientos visuales.
     *
     * @param g2 Contexto gráfico.
     * @param label Etiqueta de la barra.
     * @param value Tiempo promedio en milisegundos.
     * @param color Color de la barra.
     * @param x Posición horizontal.
     * @param y Posición vertical.
     */

    private void drawBar(Graphics2D g2, String label, double value, Color color, int x, int y) {

                
        // Etiqueta de la barra

        g2.setFont(Assets.TEXT_FONT);
        g2.setColor(Color.WHITE);
        g2.drawString(label, x, y);

                
        // Escala el tiempo para convertirlo en el ancho de la barra

        int barWidth = (int) Math.min(value * 80, 320);

               
        // Barra de rendimiento

        g2.setColor(color);
        g2.fillRoundRect(x + 130, y - 13, barWidth, 14, 10, 10);

                
        // Mostrar el tiempo promedio

        g2.setColor(Color.WHITE);
        g2.drawString(String.format("%.2f ms", value), x + 470, y);
    }
}