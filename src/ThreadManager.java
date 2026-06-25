import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================
 * Clase: ThreadManager
 * ============================================================
 *
 * Responsabilidad:
 * Administrar la actualización de tareas utilizando
 * múltiples hilos de ejecución.
 *
 * Modos:
 * • Partículas  — simulación de fuegos artificiales
 * • Matriz      — multiplicación de matrices (CPU intensivo)
 * • Archivo     — escritura de archivos pesados (I/O intensivo)
 *
 * ============================================================
 */

public class ThreadManager {

    //---------------------------------------------------------
    // Constructor privado
    //---------------------------------------------------------

    private ThreadManager() {}

    // =========================================================
    // MODO PARTÍCULAS
    // =========================================================

    public static void updateParticles(
            List<Particle> particles,
            int threadCount
    ) {

        if (particles.isEmpty()) return;

        if (threadCount <= 1) {
            for (Particle p : particles) {
                p.update();
            }
            return;
        }

        int totalParticles = particles.size();
        int chunkSize = (int) Math.ceil((double) totalParticles / threadCount);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {

            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, totalParticles);

            if (start >= end) break;

            PhysicsWorker worker = new PhysicsWorker(particles.subList(start, end));
            Thread thread = new Thread(worker);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // =========================================================
    // MODO MATRIZ
    // =========================================================

    //---------------------------------------------------------
    // Tamaño de la matriz
    //---------------------------------------------------------

    private static final int MATRIX_SIZE = 512;

    //---------------------------------------------------------
    // Matrices globales compartidas
    //---------------------------------------------------------

    private static final double[][] MAT_A = generateMatrix(MATRIX_SIZE);
    private static final double[][] MAT_B = generateMatrix(MATRIX_SIZE);

    //---------------------------------------------------------
    // Generar matriz aleatoria
    //---------------------------------------------------------

    private static double[][] generateMatrix(int size) {
        double[][] mat = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                mat[i][j] = Math.random();
        return mat;
    }

    //---------------------------------------------------------
    // Multiplicar matrices con N hilos
    //---------------------------------------------------------

    public static long multiplyMatrices(int threadCount) {

        // Matriz donde se almacenará el resultado

        double[][] result = new double[MATRIX_SIZE][MATRIX_SIZE];
           
        // Tiempo inicial de la prueba
        long start = System.nanoTime();
            
          // Modo secuencial

        if (threadCount <= 1) {

            for (int i = 0; i < MATRIX_SIZE; i++)
                for (int j = 0; j < MATRIX_SIZE; j++)
                    for (int k = 0; k < MATRIX_SIZE; k++)
                        result[i][j] += MAT_A[i][k] * MAT_B[k][j];

        } else {
                 // División de filas entre los hilos disponibles
            int chunkSize = (int) Math.ceil((double) MATRIX_SIZE / threadCount);
            List<Thread> threads = new ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                      // Determinar el rango de filas asignado al hilo
                int rowStart = t * chunkSize;
                int rowEnd = Math.min(rowStart + chunkSize, MATRIX_SIZE);

                if (rowStart >= rowEnd) break;

                final int rs = rowStart;
                final int re = rowEnd;

                Thread thread = new Thread(() -> {

                    // Multiplicación de las filas asignadas
                    for (int i = rs; i < re; i++)
                        for (int j = 0; j < MATRIX_SIZE; j++)
                            for (int k = 0; k < MATRIX_SIZE; k++)
                                result[i][j] += MAT_A[i][k] * MAT_B[k][j];
                });

                threads.add(thread);
                thread.start();
            }


            // Espera a que todos los hilos completen su trabajovantes de continuar. 
            // Esto garantiza que los cálculos
            // o escrituras hayan finalizado y que la medición del
            // tiempo de ejecución sea correcta.
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        long end = System.nanoTime();
        long elapsed = (end - start) / 1_000_000;

        writeLog("MATRIX", threadCount, elapsed);

        return elapsed;
    }

    // =========================================================
    // MODO ARCHIVO
    // =========================================================

    //---------------------------------------------------------
    // Tamaño total a escribir por prueba (3 GB)
    //---------------------------------------------------------

private static final long FILE_SIZE_BYTES = 3L * 1024 * 1024 * 1024; // 3 GB

    //---------------------------------------------------------
    /** Escribe archivos temporales utilizando múltiples hilos.
 
   Cada hilo genera un archivo independiente y escribe
   una porción del tamaño total configurado.
 
     El método elimina los archivos temporales al finalizar
    la prueba y registra el tiempo obtenido.
 
 * //@ param threadCount Número de hilos a utilizar.
 * //@ return Tiempo de ejecución en milisegundos.
 */
    public static long writeFiles(int threadCount) {
                
        // Cantidad de bytes asignados a cada hilo

        long chunkBytes = FILE_SIZE_BYTES / threadCount;

        long start = System.nanoTime();

        if (threadCount <= 1) {
                        
            // Escritura secuencial

            writeChunk("output_thread_0.tmp", chunkBytes);

        } else {

            List<Thread> threads = new ArrayList<>();

            for (int t = 0; t < threadCount; t++) {

                final String fileName = "output_thread_" + t + ".tmp";
                final long bytes = chunkBytes;

                Thread thread = new Thread(() -> writeChunk(fileName, bytes));
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        long end = System.nanoTime();
        long elapsed = (end - start) / 1_000_000;

        // Limpiar archivos temporales
        for (int t = 0; t < threadCount; t++) {
            new File("output_thread_" + t + ".tmp").delete();
        }

        writeLog("FILE", threadCount, elapsed);

        return elapsed;
    }

    //---------------------------------------------------------
    // Escribir un bloque de bytes en un archivo
    //---------------------------------------------------------

    // =========================================================
    // LOG
    // =========================================================

    private static final String LOG_FILE = "benchmark_log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void writeLog(String mode, int threadCount, long elapsedMs) {

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = String.format("[%s] Mode: %-8s | Threads: %d | Tiempo: %d ms%n",
                timestamp, mode, threadCount, elapsedMs);

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
        } catch (IOException e) {
            System.err.println("Error escribiendo log: " + e.getMessage());
        }
    }

    //---------------------------------------------------------
    // Escribir un bloque de bytes en un archivo
    //---------------------------------------------------------

    private static void writeChunk(String fileName, long bytes) {

        byte[] buffer = new byte[8192];

        try (FileOutputStream fos = new FileOutputStream(fileName, false);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            long written = 0;

            while (written < bytes) {
                long remaining = bytes - written;
                int toWrite = (int) Math.min(buffer.length, remaining);
                bos.write(buffer, 0, toWrite);
                written += toWrite;
            }

        } catch (IOException e) {
            System.err.println("Error escribiendo archivo: " + e.getMessage());
        }
    }
}