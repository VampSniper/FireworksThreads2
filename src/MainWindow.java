import javax.swing.JFrame;

/**
 * ============================================================
 * 𝙲𝚕𝚊𝚜𝚎: 𝙼𝚊𝚒𝚗𝚆𝚒𝚗𝚍𝚘𝚠
 * ============================================================
 *
 * 𝚁𝚎𝚜𝚙𝚘𝚗𝚜𝚊𝚋𝚒𝚕𝚒𝚍𝚊𝚍:
 * 𝙲𝚛𝚎𝚊𝚛 𝚢 𝚌𝚘𝚗𝚏𝚒𝚐𝚞𝚛𝚊𝚛 𝚕𝚊 𝚟𝚎𝚗𝚝𝚊𝚗𝚊 𝚙𝚛𝚒𝚗𝚌𝚒𝚙𝚊𝚕 𝚍𝚎 𝚕𝚊 𝚊𝚙𝚕𝚒𝚌𝚊𝚌𝚒ó𝚗.
 *
 * ============================================================
 */

public class MainWindow extends JFrame {

    //---------------------------------------------------------
    // 𝙲𝚘𝚗𝚜𝚝𝚛𝚞𝚌𝚝𝚘𝚛
    //---------------------------------------------------------

    public MainWindow() {

        setTitle("Fireworks Threads Simulator");

        setSize(1200, 820);

        setLocationRelativeTo(null);

        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new FireworkPanel());

        setVisible(true);

    }

}