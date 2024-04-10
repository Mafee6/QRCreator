import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class QRCreator{

    static String qrurl = "https://goqr.me";
    static JFrame window;
    static JLabel imglbl;

    private static Image QR(String parameters){
        final String endpoint = "https://api.qrserver.com/v1/create-qr-code";
        qrurl = endpoint + parameters;

        try {
            return ImageIO.read(new URL(endpoint + parameters));
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error while generating the QR Code!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }

    private static String parseParams (String data) {
        Options options = new Options();
        Object[] temp = new Object[] {
                options.color,
                options.bg,
                options.ext,
                options.margin,
                options.size,
                options.padding,
                data.replaceAll("\\s", "%20")
        };

        return (
                java.text.MessageFormat
                        .format("?color={0}&bgcolor={1}&format={2}&margin={3}&size={4}x{4}&qzone={5}&data={6}", temp)
                );
    }

    static Image QRCode;

    private static void refreshQR(String url) {
        QRCode = QR(parseParams(url));
        imglbl.setIcon(new ImageIcon(QRCode));
    }

    public static void main(String[] args){
        window = new JFrame("Mafee QR Creator");
        imglbl = new JLabel();
        window.setLayout(null);

        JLabel help = new JLabel();
        help.setText("<html><b>Mafee QR Code Creator</b> Enter QR Data under QR Code & press Enter to make a new one! Left click on the QR Code to open it in your browser. <b>API: https://goqr.me</b></html>");
        help.setForeground(Color.white);
        help.setBounds(6, 6, 680, 50);
        help.setFont(new Font("sans-serif", Font.PLAIN, 15));
        window.add(help);

        imglbl.setBounds(1, help.getY() + help.getHeight() + 10, new Options().size, new Options().size);
        imglbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imglbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    try {
                        System.out.println("[!] Opening QR Code in Your Browser");
                        Desktop.getDesktop().browse(new URI(qrurl));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        refreshQR("https://goqr.me");

        JTextField data = new JTextField();
        data.setToolTipText("Enter QR Data Here!");
        data.setBounds(10, imglbl.getY() + imglbl.getHeight() + 5, 680, 40);
        data.setBackground(new Color(40, 40, 40));
        data.setForeground(Color.white);
        data.setCaretColor(Color.white);
        data.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, data.getBackground()));

        data.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    if(data.getText() != null) {
                        refreshQR(data.getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "You didn't enter any data :(");
                    }
                }
            }
        });

        window.add(data);
        window.add(imglbl);
        window.getContentPane().setBackground(new Color(34, 34, 34));
        window.setIconImage(new ImageIcon("qrcode-icon.png").getImage());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(720, 480);
        window.setMinimumSize(new Dimension(720, 480));
        window.setMaximumSize(new Dimension(720, 480));
        window.setResizable(false);
        window.setVisible(true);

        System.out.println("[!] Opened Window");

        window.getContentPane().repaint();
        data.grabFocus();
    }
}
