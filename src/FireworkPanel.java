import javax.swing.*;
import java.awt.*;

public class FireworkPanel extends JPanel {

    // =========================
    // Simulación
    // =========================
    private final Simulation simulation;

    // =========================
    // UI
    // =========================
    private JButton button1;
    private JButton button2;
    private JButton button4;
    private JButton button8;

    private final BenchmarkPanel benchmark;
    private final FPSCounter fpsCounter;

    private final Timer timer;

    // =========================
    // LAYOUT
    // =========================
    private static final int TOP_MARGIN   = 110;
    private static final int SIM_HEIGHT   = 260;

    public FireworkPanel() {

        setBackground(Assets.BACKGROUND);
        setLayout(null);

        simulation = new Simulation();
        benchmark  = new BenchmarkPanel();
        fpsCounter = new FPSCounter();

        // =========================
        // BOTONES
        // =========================
        button1 = createButton("1 THREAD");
        button2 = createButton("2 THREADS");
        button4 = createButton("4 THREADS");
        button8 = createButton("8 THREADS");

        add(button1);
        add(button2);
        add(button4);
        add(button8);

        // =========================
        // EVENTS
        // =========================
        button1.addActionListener(e -> {
            simulation.setThreadCount(1);
            benchmark.reset();
            updateButtonSelection();
        });

        button2.addActionListener(e -> {
            simulation.setThreadCount(2);
            benchmark.reset();
            updateButtonSelection();
        });

        button4.addActionListener(e -> {
            simulation.setThreadCount(4);
            benchmark.reset();
            updateButtonSelection();
        });

        button8.addActionListener(e -> {
            simulation.setThreadCount(8);
            benchmark.reset();
            updateButtonSelection();
        });

        // =========================
        // TIMER
        // =========================
        timer = new Timer(16, e -> {

            long start = System.nanoTime();

            simulation.update(getWidth(), getHeight());

            long end     = System.nanoTime();
            long elapsed = (end - start) / 1_000_000;

            benchmark.update(simulation.getThreadCount(), elapsed);
            fpsCounter.update();

            layoutButtons();
            repaint();

        });

        timer.start();

    }

    // =========================
    // BOTONES
    // =========================
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Assets.CYAN);
        button.setBorder(BorderFactory.createLineBorder(Assets.PINK));
        button.setFocusPainted(false);
        button.setFont(Assets.TEXT_FONT);

        return button;

    }

    // =========================
    // LAYOUT DINÁMICO
    // =========================
    private void layoutButtons() {

        int buttonY = TOP_MARGIN + SIM_HEIGHT + 50;

        int buttonW  = 150;
        int spacing  = 20;

        int totalWidth = (buttonW * 4) + (spacing * 3);
        int startX     = (getWidth() - totalWidth) / 2;

        button1.setBounds(startX,                          buttonY, buttonW, 40);
        button2.setBounds(startX +   (buttonW + spacing),  buttonY, buttonW, 40);
        button4.setBounds(startX + 2*(buttonW + spacing),  buttonY, buttonW, 40);
        button8.setBounds(startX + 3*(buttonW + spacing),  buttonY, buttonW, 40);

    }

    // =========================
    // SELECCIÓN BOTONES
    // =========================
    private void updateButtonSelection() {

        JButton[] buttons = {button1, button2, button4, button8};

        for (JButton b : buttons) {
            b.setBackground(new Color(25, 25, 25));
            b.setForeground(Assets.CYAN);
        }

        switch (simulation.getThreadCount()) {

            case 1 -> { button1.setBackground(Assets.GREEN); button1.setForeground(Color.BLACK); }
            case 2 -> { button2.setBackground(Assets.GREEN); button2.setForeground(Color.BLACK); }
            case 4 -> { button4.setBackground(Assets.GREEN); button4.setForeground(Color.BLACK); }
            case 8 -> { button8.setBackground(Assets.GREEN); button8.setForeground(Color.BLACK); }

        }

    }

    // =========================
    // RENDER
    // =========================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // =========================
        // TÍTULO
        // =========================
        drawCenteredString(g2, "FIREWORK THREADS SIMULATOR",   55, Assets.PINK,  Assets.TITLE_FONT);
        drawCenteredString(g2, "Multithreaded Particle Simulation", 95, Assets.CYAN, Assets.SUBTITLE_FONT);

        // =========================
        // SIMULACIÓN AREA
        // =========================
        g2.setColor(Assets.PINK);

        g2.drawRoundRect(40, TOP_MARGIN, getWidth() - 80, SIM_HEIGHT, 20, 20);

        for (Particle p : simulation.getParticles()) {
            p.draw(g2);
        }

        // =========================
        // STATS
        // =========================
        int statsY = TOP_MARGIN + SIM_HEIGHT + 110;

        g2.setFont(Assets.TEXT_FONT);
        g2.setColor(Assets.GREEN);

        g2.drawString("FPS: "       + fpsCounter.getFPS(),              60,  statsY);
        g2.drawString("Threads: "   + simulation.getThreadCount(),      260, statsY);
        g2.drawString("Particles: " + simulation.getParticles().size(), 460, statsY);

        // =========================
        // BENCHMARK
        // =========================
        benchmark.draw(g2, 40, statsY + 20, getWidth() - 80);

    }

    // =========================
    // TEXTO CENTRADO
    // =========================
    private void drawCenteredString(Graphics2D g2, String text, int y, Color color, Font font) {

        g2.setFont(font);
        g2.setColor(color);

        FontMetrics metrics = g2.getFontMetrics(font);

        int x = (getWidth() - metrics.stringWidth(text)) / 2;

        g2.drawString(text, x, y);

    }

}