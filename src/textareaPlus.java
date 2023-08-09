import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class textareaPlus extends JTextArea {
    Image image;
    Image pending;
    Cursor cursor;
    JPanel panel;
    private Component component;
    public textareaPlus(Component component){
        super();
        this.component=component;
        cursor=new Cursor(Cursor.HAND_CURSOR);
        setLayout(null);
        panel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(pending!=null){
                    g.drawImage(pending,10,10,this);
                }
            }
        };
        panel.setLayout(null);
        JLabel label=new JLabel("Copy");
        label.setForeground(Color.WHITE);
        label.setCursor(cursor);
        label.setBounds(750,10,29,29);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text=component.sendText();
                SendToClipboard(text);
            }
        });
        panel.add(label);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(0, 0, 800, 40); // Set the position and size of the panel

        add(panel);
//        JTextArea textArea=new JTextArea();
//        textArea.setBackground(Color.BLACK);
//        add(textArea,BorderLayout.CENTER);
    }

    private void SendToClipboard(String text) {
        Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection=new StringSelection(text);
        clipboard.setContents(selection,null);
        String notification = "Text copied to clipboard";
        JOptionPane.showMessageDialog(null, notification, "Clipboard Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    public textareaPlus(String text){
        super(text);
    }
    public void setImage(ImageIcon icon){
        this.image=icon.getImage();
        setOpaque(false);
        repaint();
    }
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
         //Draw the image
        if (image != null) {
            g.drawImage(image, 10, 50, this);
        }
        super.paint(g);
    }

    public void setPending(ImageIcon imageIcon) {
        this.pending=imageIcon.getImage();
        panel.repaint();
    }
}
