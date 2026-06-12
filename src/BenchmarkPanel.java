import java.awt.*;

public class BenchmarkPanel {

    //---------------------------------------------------------
    // Promedios
    //---------------------------------------------------------

    private double average1;
    private double average2;
    private double average4;
    private double average8;

    //---------------------------------------------------------
    // Cantidad de muestras
    //---------------------------------------------------------

    private int count1;
    private int count2;
    private int count4;
    private int count8;

    //---------------------------------------------------------
    // Resetear promedios
    //---------------------------------------------------------

    public void reset() {
        average1 = 0; count1 = 0;
        average2 = 0; count2 = 0;
        average4 = 0; count4 = 0;
        average8 = 0; count8 = 0;
    }

    //---------------------------------------------------------
    // Actualizar resultados
    //---------------------------------------------------------

    public void update(int threads, long time) {

        switch (threads) {

            case 1:
                count1++;
                average1 += (time - average1) / count1;
                break;

            case 2:
                count2++;
                average2 += (time - average2) / count2;
                break;

            case 4:
                count4++;
                average4 += (time - average4) / count4;
                break;

            case 8:
                count8++;
                average8 += (time - average8) / count8;
                break;

        }

    }

    //---------------------------------------------------------
    // Dibujar panel
    //---------------------------------------------------------

    public void draw(Graphics2D g2, int x, int y, int width) {

        g2.setColor(new Color(15, 15, 20));

        g2.fillRoundRect(x, y, width, 140, 20, 20);

        g2.setColor(Assets.CYAN);

        g2.drawRoundRect(x, y, width, 140, 20, 20);

        g2.setFont(Assets.SUBTITLE_FONT);

        g2.setColor(Assets.YELLOW);

        g2.drawString("PERFORMANCE BENCHMARK", x + 20, y + 28);

        drawBar(g2, "1 Thread",  average1, Assets.PINK,   x + 20, y + 55);
        drawBar(g2, "2 Threads", average2, Assets.CYAN,   x + 20, y + 80);
        drawBar(g2, "4 Threads", average4, Assets.PURPLE, x + 20, y + 105);
        drawBar(g2, "8 Threads", average8, Assets.GREEN,  x + 20, y + 130);

    }

    //---------------------------------------------------------
    // Dibujar una barra
    //---------------------------------------------------------

    private void drawBar(
            Graphics2D g2,
            String label,
            double value,
            Color color,
            int x,
            int y
    ) {

        g2.setFont(Assets.TEXT_FONT);

        g2.setColor(Color.WHITE);

        g2.drawString(label, x, y);

        int barWidth = (int) Math.min(value * 80, 320);

        g2.setColor(color);

        g2.fillRoundRect(x + 130, y - 13, barWidth, 14, 10, 10);

        g2.setColor(Color.WHITE);

        g2.drawString(String.format("%.2f ms", value), x + 470, y);

    }

}