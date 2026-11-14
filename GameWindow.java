import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    static final Color BG = new Color(13, 13, 18);
    static final Color PANEL = new Color(22, 22, 30);
    static final Color ACCENT = new Color(150, 40, 35);
    static final Color GOLD = new Color(212, 175, 55);
    static final Color TEXT = new Color(230, 230, 230);

    private final CaseFile caseFile = new CaseFile();
    private final List<Clue> inventory = new ArrayList<>();
    private final ScenePanel scene = new ScenePanel();
    private final JTextArea log = new JTextArea();
    private final DefaultListModel<String> clueModel = new DefaultListModel<>();
    private final JLabel clueTitle = new JLabel("CLUES (0)");
    private Room current;
    private Timer typer;

    public GameWindow() {
        setTitle("The Blackwood Murder - Detective Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("THE BLACKWOOD MURDER", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(GOLD);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // Centre: scene + story text
        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setBackground(PANEL);
        log.setForeground(TEXT);
        log.setFont(new Font("Monospaced", Font.PLAIN, 16));
        log.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JScrollPane logScroll = new JScrollPane(log);
        logScroll.setBorder(BorderFactory.createLineBorder(GOLD.darker()));

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(scene, BorderLayout.NORTH);
        center.add(logScroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // West: rooms
        JPanel west = new JPanel(new GridLayout(0, 1, 0, 8));
        west.setBackground(BG);
        west.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        JLabel roomsTitle = new JLabel("ROOMS");
        roomsTitle.setForeground(GOLD);
        roomsTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        west.add(roomsTitle);
        for (Room r : caseFile.rooms) {
            JButton b = makeButton(r.getName(), PANEL);
            b.addActionListener(e -> goTo(r));
            west.add(b);
        }
        JPanel westWrap = new JPanel(new BorderLayout());
        westWrap.setBackground(BG);
        westWrap.add(west, BorderLayout.NORTH);
        add(westWrap, BorderLayout.WEST);

        // East: clues
        JList<String> clueList = new JList<>(clueModel);
        clueList.setBackground(PANEL);
        clueList.setForeground(TEXT);
        clueList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        clueTitle.setForeground(GOLD);
        clueTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        JPanel east = new JPanel(new BorderLayout(0, 8));
        east.setBackground(BG);
        east.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        east.setPreferredSize(new Dimension(220, 100));
        east.add(clueTitle, BorderLayout.NORTH);
        east.add(new JScrollPane(clueList), BorderLayout.CENTER);
        add(east, BorderLayout.EAST);

        // South: actions
        JButton searchBtn = makeButton("Search Room", PANEL);
        JButton talkBtn = makeButton("Talk to Suspect", PANEL);
        JButton accuseBtn = makeButton("ACCUSE", ACCENT);
        searchBtn.addActionListener(e -> search());
        talkBtn.addActionListener(e -> talk());
        accuseBtn.addActionListener(e -> accuse());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        south.setBackground(BG);
        south.add(searchBtn);
        south.add(talkBtn);
        south.add(accuseBtn);
        add(south, BorderLayout.SOUTH);

        goTo(caseFile.rooms.get(0));
        setVisible(true);
    }

    private JButton makeButton(String text, Color base) {
        JButton b = new JButton(text);
        b.setBackground(base);
        b.setForeground(TEXT);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(base.brighter()); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(base); }
        });
        return b;
    }

    // Typewriter effect
    private void typeText(String text, boolean clear) {
        if (typer != null) typer.stop();
        if (clear) log.setText(""); else log.append("\n\n");
        final int[] i = {0};
        typer = new Timer(15, e -> {
            if (i[0] < text.length()) {
                log.append(String.valueOf(text.charAt(i[0]++)));
                log.setCaretPosition(log.getDocument().getLength());
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        typer.start();
    }

    private void goTo(Room room) {
        current = room;
        Suspect s = room.getSuspect();
        scene.setRoom(room.getName(), s == null ? null : s.getName());
        typeText("You enter the " + room.getName() + ".\n" + room.getDescription(), true);
    }

    private void search() {
        StringBuilder sb = new StringBuilder();
        for (Clue c : current.getClues()) {
            if (!c.isFound()) {
                c.markFound();
                inventory.add(c);
                clueModel.addElement(c.getName());
                sb.append("CLUE FOUND: ").append(c).append("\n");
            }
        }
        clueTitle.setText("CLUES (" + inventory.size() + ")");
        typeText(sb.length() == 0 ? "You search carefully... nothing new here." : sb.toString().trim(), false);
    }

    private void talk() {
        Suspect s = current.getSuspect();
        if (s == null) {
            typeText("Nobody is here.", false);
            return;
        }
        // Polymorphism: Witness and Culprit answer differently
        typeText(s.getName() + " the " + s.getRole() + ":\n" + s.interrogate(), false);
    }

    private void accuse() {
        if (inventory.size() < 3) {
            JOptionPane.showMessageDialog(this, "Not enough evidence! Find at least 3 clues first.",
                    "Accusation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] names = new String[caseFile.suspects.size()];
        for (int i = 0; i < names.length; i++) names[i] = caseFile.suspects.get(i).toString();

        int choice = JOptionPane.showOptionDialog(this, "Who is the killer?", "Accusation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, names, null);
        if (choice < 0) return;

        Suspect accused = caseFile.suspects.get(choice);
        boolean win = accused instanceof Culprit;
        String msg = win
                ? "CORRECT! " + accused.getName() + " confesses. Case closed, detective!"
                : "WRONG! " + accused.getName() + " is innocent. The real killer escapes into the night...";
        int again = JOptionPane.showConfirmDialog(this, msg + "\n\nPlay again?",
                win ? "You Win" : "Game Over", JOptionPane.YES_NO_OPTION,
                win ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        if (again == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(GameWindow::new);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
