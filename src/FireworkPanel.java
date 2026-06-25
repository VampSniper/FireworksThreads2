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

    private JButton btnParticles;
    private JButton btnMatrix;
    private JButton btnFile;
    private JButton btnReset;

    private final BenchmarkPanel benchmark;
    private final FPSCounter fpsCounter;

    private final Timer timer;

    // =========================
    // LAYOUT
    // =========================
    private static final int TOP_MARGIN = 110;
    private static final int SIM_HEIGHT = 260;

    public FireworkPanel() {

        setBackground(Assets.BACKGROUND);
        setLayout(null);

        simulation = new Simulation();
        benchmark  = new BenchmarkPanel();
        fpsCounter = new FPSCounter();

        // =========================
        // BOTONES HILOS
        // =========================
        button1 = createButton("1 HILO");
        button2 = createButton("2 HILOS");
        button4 = createButton("4 HILOS");
        button8 = createButton("8 HILOS");

        add(button1);
        add(button2);
        add(button4);
        add(button8);

        // =========================
        // BOTONES MODO
        // =========================
        btnParticles = createModeButton("PARTICULAS");
        btnMatrix    = createModeButton("MATRICES");
        btnFile      = createModeButton("ARCHIVO");
        btnReset     = createResetButton("RESET");

        add(btnParticles);
        add(btnMatrix);
        add(btnFile);
        add(btnReset);

        // =========================
        // EVENTS HILOS
        // =========================
  button1.addActionListener(e -> {
    simulation.setThreadCount(1);
    benchmark.reset(1);
    if (simulation.getMode() != Simulation.Mode.PARTICLES) simulation.reset();
    updateButtonSelection();
});

button2.addActionListener(e -> {
    simulation.setThreadCount(2);
    benchmark.reset(2);
    if (simulation.getMode() != Simulation.Mode.PARTICLES) simulation.reset();
    updateButtonSelection();
});

button4.addActionListener(e -> {
    simulation.setThreadCount(4);
    benchmark.reset(4);
    if (simulation.getMode() != Simulation.Mode.PARTICLES) simulation.reset();
    updateButtonSelection();
});

button8.addActionListener(e -> {
    simulation.setThreadCount(8);
    benchmark.reset(8);
    if (simulation.getMode() != Simulation.Mode.PARTICLES) simulation.reset();
    updateButtonSelection();
        });

        // =========================
        // EVENTS MODO
        // =========================
        btnParticles.addActionListener(e -> {
            simulation.setMode(Simulation.Mode.PARTICLES);
            benchmark.resetAll();
            updateModeSelection();
        });
        btnMatrix.addActionListener(e -> {
            simulation.setMode(Simulation.Mode.MATRIX);
            benchmark.resetAll();
            updateModeSelection();
        });
        btnFile.addActionListener(e -> {
            simulation.setMode(Simulation.Mode.FILE);
            benchmark.resetAll();
            updateModeSelection();
        });
        btnReset.addActionListener(e -> benchmark.resetAll());

        // =========================
        // TIMER
        // =========================
        timer = new Timer(16, e -> {

            long start   = System.nanoTime();
            simulation.update(getWidth(), getHeight());
            long elapsed = (System.nanoTime() - start) / 1_000_000;

            if (simulation.getMode() == Simulation.Mode.PARTICLES) {
                benchmark.update(simulation.getThreadCount(), elapsed);
            } else if (simulation.getLastElapsed() > 0) {
                benchmark.update(simulation.getThreadCount(), simulation.getLastElapsed());
            }

            fpsCounter.update();
            layoutButtons();
            repaint();

        });

        timer.start();

        updateButtonSelection();
        updateModeSelection();
    }

    // =========================
    // BOTONES HILO
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
    // BOTONES MODO
    // =========================
    private JButton createModeButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(25, 25, 25));
        button.setForeground(Assets.YELLOW);
        button.setBorder(BorderFactory.createLineBorder(Assets.YELLOW));
        button.setFocusPainted(false);
        button.setFont(Assets.TEXT_FONT);
        return button;
    }

    // =========================
    // BOTÓN RESET
    // =========================
    private JButton createResetButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(180, 30, 30));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Assets.PINK));
        button.setFocusPainted(false);
        button.setFont(Assets.TEXT_FONT);
        return button;
    }

    // =========================
    // LAYOUT DINÁMICO
    // =========================
    private void layoutButtons() {

        int buttonW = 140;
        int spacing = 15;

        // Fila modos: PARTICULAS | MATRICES | ARCHIVO | RESET
        int modeY      = TOP_MARGIN + SIM_HEIGHT + 20;
        int totalModeW = (buttonW * 4) + (spacing * 3);
        int modeStartX = (getWidth() - totalModeW) / 2;

        btnParticles.setBounds(modeStartX,                       modeY, buttonW, 35);
        btnMatrix.setBounds   (modeStartX +  (buttonW+spacing),  modeY, buttonW, 35);
        btnFile.setBounds     (modeStartX + 2*(buttonW+spacing), modeY, buttonW, 35);
        btnReset.setBounds    (modeStartX + 3*(buttonW+spacing), modeY, buttonW, 35);

        // Fila hilos: 1 | 2 | 4 | 8
        int threadY      = modeY + 50;
        int totalThreadW = (buttonW * 4) + (spacing * 3);
        int threadStartX = (getWidth() - totalThreadW) / 2;

        button1.setBounds(threadStartX,                          threadY, buttonW, 35);
        button2.setBounds(threadStartX +  (buttonW + spacing),   threadY, buttonW, 35);
        button4.setBounds(threadStartX + 2*(buttonW + spacing),  threadY, buttonW, 35);
        button8.setBounds(threadStartX + 3*(buttonW + spacing),  threadY, buttonW, 35);
    }

    // =========================
    // SELECCIÓN HILOS
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
    // SELECCIÓN MODO
    // =========================
    private void updateModeSelection() {
        JButton[] modeBtns = {btnParticles, btnMatrix, btnFile};
        for (JButton b : modeBtns) {
            b.setBackground(new Color(25, 25, 25));
            b.setForeground(Assets.YELLOW);
        }
        switch (simulation.getMode()) {
            case PARTICLES -> { btnParticles.setBackground(Assets.YELLOW); btnParticles.setForeground(Color.BLACK); }
            case MATRIX    -> { btnMatrix.setBackground(Assets.YELLOW);    btnMatrix.setForeground(Color.BLACK); }
            case FILE      -> { btnFile.setBackground(Assets.YELLOW);      btnFile.setForeground(Color.BLACK); }
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

        // Título
        drawCenteredString(g2, "THREADS SIMULATOR",        55, Assets.PINK, Assets.TITLE_FONT);
        drawCenteredString(g2, "Multithreaded Simulation", 95, Assets.CYAN, Assets.SUBTITLE_FONT);

        // Área simulación
        g2.setColor(Assets.PINK);
        g2.drawRoundRect(40, TOP_MARGIN, getWidth() - 80, SIM_HEIGHT, 20, 20);

        // Contenido según modo
        switch (simulation.getMode()) {
            case PARTICLES -> {
                for (Particle p : simulation.getParticles()) {
                    p.draw(g2);
                }
            }
            case MATRIX -> {
                drawCenteredString(g2, "MODO MATRICES",
                        TOP_MARGIN + SIM_HEIGHT / 2 - 20, Assets.YELLOW, Assets.SUBTITLE_FONT);
                drawCenteredString(g2, "Multiplicacion de matrices 512x512",
                        TOP_MARGIN + SIM_HEIGHT / 2 + 20, Assets.CYAN, Assets.TEXT_FONT);
                String matMsg = simulation.getLastElapsed() > 0
                        ? "Ultima ejecucion: " + simulation.getLastElapsed() + " ms"
                        : "Esperando primera ejecucion...";
                drawCenteredString(g2, matMsg,
                        TOP_MARGIN + SIM_HEIGHT / 2 + 50, Assets.GREEN, Assets.TEXT_FONT);
            }
            case FILE -> {
                drawCenteredString(g2, "MODO ARCHIVO",
                        TOP_MARGIN + SIM_HEIGHT / 2 - 20, Assets.YELLOW, Assets.SUBTITLE_FONT);
                drawCenteredString(g2, "Escritura de 3 GB dividida entre hilos",
                        TOP_MARGIN + SIM_HEIGHT / 2 + 20, Assets.CYAN, Assets.TEXT_FONT);
                String fileMsg = simulation.getLastElapsed() > 0
                        ? "Ultima ejecucion: " + simulation.getLastElapsed() + " ms"
                        : "Esperando primera ejecucion...";
                drawCenteredString(g2, fileMsg,
                        TOP_MARGIN + SIM_HEIGHT / 2 + 50, Assets.GREEN, Assets.TEXT_FONT);
            }
        }

        // Stats y benchmark debajo de los botones
        int statsY = TOP_MARGIN + SIM_HEIGHT + 130;

        g2.setFont(Assets.TEXT_FONT);
        g2.setColor(Assets.GREEN);
        g2.drawString("FPS: "       + fpsCounter.getFPS(),              60,  statsY);
        g2.drawString("Hilos: "   + simulation.getThreadCount(),      260, statsY);
        g2.drawString("Modo: "      + simulation.getMode(),             460, statsY);

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