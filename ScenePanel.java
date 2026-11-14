import javax.swing.*;
import java.awt.*;

// Draws the noir mansion scene with a fade-in effect when the room changes
public class ScenePanel extends JPanel {
    private String roomName = "";
    private String suspectLine = "";
    private float fade = 0f;
    private Timer fadeTimer;

    public ScenePanel() {
        setPreferredSize(new Dimension(100, 230));
    }

    public void setRoom(String roomName, String suspectName) {
        this.roomName = roomName;
        this.suspectLine = suspectName == null ? "" : "Suspect here: " + suspectName;
        fade = 1f;
        if (fadeTimer != null) fadeTimer.stop();
        fadeTimer = new Timer(30, e -> {
            fade -= 0.06f;
            if (fade <= 0f) {
                fade = 0f;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        // Night sky
        g2.setPaint(new GradientPaint(0, 0, new Color(30, 30, 55), 0, h, new Color(10, 10, 15)));
        g2.fillRect(0, 0, w, h);

        // Moon
        g2.setColor(new Color(240, 235, 200, 40));
        g2.fillOval(w - 140, 10, 100, 100);
        g2.setColor(new Color(240, 235, 200));
        g2.fillOval(w - 120, 30, 60, 60);

        // City / mansion skyline with lit windows
        int[] heights = {90, 130, 70, 150, 100, 120, 80, 140};
        int bw = Math.max(40, w / heights.length);
        for (int i = 0; i < heights.length; i++) {
            int x = i * bw, y = h - heights[i];
            g2.setColor(new Color(8, 8, 12));
            g2.fillRect(x, y, bw - 4, heights[i]);
            for (int r = 0; r < heights[i] / 22; r++) {
                for (int c = 0; c < 2; c++) {
                    if ((i * 7 + r * 3 + c) % 3 == 0) {
                        g2.setColor(new Color(230, 190, 80, 200));
                        g2.fillRect(x + 8 + c * 16, y + 8 + r * 20, 8, 10);
                    }
                }
            }
        }

        // Room title
        g2.setFont(new Font("Serif", Font.BOLD, 42));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(roomName)) / 2;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(roomName, tx + 3, 83);
        g2.setColor(new Color(212, 175, 55));
        g2.drawString(roomName, tx, 80);

        g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
        fm = g2.getFontMetrics();
        g2.setColor(new Color(200, 200, 200));
        g2.drawString(suspectLine, (w - fm.stringWidth(suspectLine)) / 2, 110);

        // Fade overlay
        if (fade > 0f) {
            g2.setColor(new Color(0, 0, 0, (int) (fade * 255)));
            g2.fillRect(0, 0, w, h);
        }
    }
}
