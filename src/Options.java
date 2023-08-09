import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Options extends JFrame {
    private TopicFrame topicFrame;
    private JPanel southpanel;
    private JButton btn;
    private DefaultListModel<String> menuList;
    private JList<String> menuJList;
    private Statement statement1;
    private DBManager dbManager;
    Options() throws SQLException, ClassNotFoundException {
        setLayout(new BorderLayout());
        setTitle("OPEN FILES");
        menuList=new DefaultListModel<>();
        menuJList=new JList<>(menuList);
        dbManager=new DBManager();
        try{
            statement1=dbManager.conn.createStatement();
            ResultSet getTables=statement1.executeQuery("SHOW TABLES");
            while(getTables.next()){
                menuList.addElement(getTables.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        southpanel=new JPanel();
        btn=new JButton("Create New");
        southpanel.add(btn);
        JScrollPane menuScrollPane=new JScrollPane(menuJList);
        add(menuScrollPane,BorderLayout.CENTER);
        add(southpanel,BorderLayout.SOUTH);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newTableName=JOptionPane.showInputDialog("New Table Name");
                if(newTableName!=null){
                    System.out.println(newTableName);
                    try {
                        topicFrame=new TopicFrame(newTableName);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    topicFrame.createTable(newTableName);
                }
            }
        });
        setSize(500,500);
        setVisible(true);
        setLocationRelativeTo(null);
        menuJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    int index=menuJList.locationToIndex(e.getPoint());
//                    System.out.println(index);
                if(index>=0){
                    String selectedItem=menuList.getElementAt(index);
                    SubMenu(selectedItem,e.getX(),e.getY());
                }
            }
        });
    }

    private void SubMenu(String selectedItem, int x, int y) {
        JPopupMenu jPopupMenu=new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Open");
//        JMenuItem menuItem2 = new JMenuItem("Subitem 2");
//        JMenuItem menuItem3 = new JMenuItem("Subitem 3");

        jPopupMenu.add(menuItem1);
//        jPopupMenu.add(menuItem2);
//        jPopupMenu.add(menuItem3);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(menuJList);
        if (frame != null) {
            JRootPane rootPane = frame.getRootPane();
            jPopupMenu.show(rootPane, x, y);
        }
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    topicFrame=new TopicFrame(selectedItem);
                    topicFrame.setBackground(Color.BLACK);
                    topicFrame.fieldContent(selectedItem);
                    SwingUtilities.getWindowAncestor(menuJList).dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
