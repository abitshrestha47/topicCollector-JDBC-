import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Component extends JPanel {
    private int width=800;
    private JMenuItem important;
    private JPopupMenu importantpopup;
    ImageIcon icon;
    private int getemp;
    private int getLevel;
    private int height=50;
    private PreparedStatement updateLevelStatement;
    private JButton btn;
    public textareaPlus textArea;
    public DBManager dbManager;
    private PreparedStatement insertStatement;
    private PreparedStatement selectStatement;
    private PreparedStatement selectStatement_COLORS;
    private PreparedStatement deleteStatement;
    private PreparedStatement updateStatement;
    private String selectedTableName;
    private String logoImagePath;
    public int id;
    public Component component1;
    private TopicFrame topicFrame;
    Component(String selectedTableName,int id,TopicFrame topicFrame1) throws SQLException, ClassNotFoundException {
        component1=this;
        this.topicFrame=topicFrame1;
        this.selectedTableName=selectedTableName;
        dbManager=new DBManager();
        deleteStatement=dbManager.conn.prepareStatement("DELETE FROM `"+selectedTableName+"` WHERE id=?");
        selectStatement=dbManager.conn.prepareStatement("SELECT * FROM `"+selectedTableName+"` WHERE id=?");
        selectStatement_COLORS=dbManager.conn.prepareStatement("SELECT * FROM `"+selectedTableName+"` WHERE id=?");
        setLayout(new FlowLayout());
        this.id=id;
        setBackground(new Color(87, 85, 85, 255));
        textArea=new textareaPlus(this);
        logoImagePath = "Q:/JAVA_PROJECTS/Topics/images/alert1.png";
         icon=new ImageIcon(logoImagePath);
//        textArea.setImage(icon);
//        File imageFile = new File(logoImagePath);
//        if (imageFile.exists()) {
//            try {
//                Desktop.getDesktop().open(imageFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Image file not found: " + logoImagePath);
//        }
//        ImageIcon imageIcon = new ImageIcon(logoImagePath);
//        JLabel label = new JLabel(imageIcon);
//        add(label,BorderLayout.EAST);
//        textArea.setPreferredSize(new Dimension(width, height));
        btn=new JButton("X");
        textArea.setColumns(60);
        textArea.setRows(3);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteComponent();
            }
        });
        selectStatement.setInt(1,id);
        ResultSet tocheckIMP=selectStatement.executeQuery();
        selectStatement_COLORS.setInt(1,id);
        ResultSet resultSet=selectStatement_COLORS.executeQuery();
        int getLevel=0;
        getemp=0;
        while(resultSet.next()){
            getLevel=resultSet.getInt("level");
        }
        while(tocheckIMP.next()){
            getemp=tocheckIMP.getInt("imp");
        }
        System.out.println(getemp);
        if(getemp==0){
            textArea.setImage(new ImageIcon());
        }
        else{
            textArea.setImage(icon);
        }
        System.out.println(getLevel);
        switch(getLevel){
            case 0:
                textArea.setForeground(new Color(196, 27, 27));
                String path="Q:/JAVA_PROJECTS/Topics/images/waiting-list.png";
                ImageIcon imageIcon=new ImageIcon(path);
                textArea.setPending(imageIcon);
                break;
            case 1:
                textArea.setForeground(new Color(238, 32, 193));
                String path1="Q:/JAVA_PROJECTS/Topics/images/shield.png";
                ImageIcon imageIcon1=new ImageIcon(path1);
                textArea.setPending(imageIcon1);
                break;
            case 2:
                textArea.setForeground(new Color(225, 126, 8));
                path="Q:/JAVA_PROJECTS/Topics/images/evolving.png";
                imageIcon=new ImageIcon(path);
                textArea.setPending(imageIcon);
                break;
            case 3:
                textArea.setForeground(new Color(44, 117, 236));
                path="Q:/JAVA_PROJECTS/Topics/images/inter.png";
                imageIcon=new ImageIcon(path);
                textArea.setPending(imageIcon);
                break;
            case 4:
                textArea.setForeground(new Color(0,120,0));
                path="Q:/JAVA_PROJECTS/Topics/images/proficient.png";
                imageIcon=new ImageIcon(path);
                textArea.setPending(imageIcon);
                break;
            case 5:
                textArea.setForeground(new Color(159, 79, 255));
                path="Q:/JAVA_PROJECTS/Topics/images/piercing.png";
                imageIcon=new ImageIcon(path);
                textArea.setPending(imageIcon);
                break;
        }
        add(textArea);
        add(btn);
        Font currentFont=textArea.getFont();
        int fontSize=currentFont.getSize();
        Font biggerFont=currentFont.deriveFont(fontSize+5f);
        textArea.setFont(biggerFont);
        textArea.setBorder(new EmptyBorder(70, 20, 0, 0));
        textArea.setBackground(Color.BLACK);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
//                    System.out.println("clicked");
                     importantpopup=new JPopupMenu();
                    important=new JMenuItem("Important");
                    JMenuItem pending=new JMenuItem("Pending");
                    JMenuItem begineer=new JMenuItem("Begineer");
                    JMenuItem evolving=new JMenuItem("Evolving");
                    JMenuItem intermediate=new JMenuItem("Intermediate");
                    JMenuItem proficient=new JMenuItem("Proficient");
                    JMenuItem mastery=new JMenuItem("Mastery");
                    importantpopup.add(important);
                    importantpopup.add(pending);
                    importantpopup.add(begineer);
                    importantpopup.add(evolving);
                    importantpopup.add(intermediate);
                    importantpopup.add(proficient);
                    importantpopup.add(mastery);
                    importantpopup.show(textArea,e.getX(),e.getY());
                    try {
                        updateLevelStatement=dbManager.conn.prepareStatement("UPDATE `"+selectedTableName+"` SET level=? WHERE id=?");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    pending.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"0");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/waiting-list.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(196, 27, 27));
                        }
                    });
                    begineer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"1");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/shield.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(238, 32, 193));
                        }
                    });
                    evolving.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"2");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/evolving.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(225, 126, 8));
                        }
                    });
                    intermediate.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"3");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/inter.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);

                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(44, 117, 236));
                        }
                    });
                    proficient.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"4");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/proficient.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(0,120,0));
                        }
                    });
                    mastery.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                updateLevelStatement.setString(1,"5");
                                updateLevelStatement.setInt(2,Component.this.id);
                                updateLevelStatement.executeUpdate();
                                updateLevelStatement.close();
                                String path="Q:/JAVA_PROJECTS/Topics/images/piercing.png";
                                ImageIcon imageIcon=new ImageIcon(path);
                                textArea.setPending(imageIcon);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println(id);
                            textArea.setForeground(new Color(159, 79, 255));
                        }
                    });
                    important.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                boolean value=false;
                                PreparedStatement booleanStatement=dbManager.conn.prepareStatement("UPDATE `"+selectedTableName+"` SET imp=? WHERE id=?");
                                ResultSet resultSet=selectStatement.executeQuery();
                                while(resultSet.next()){
                                    value=resultSet.getBoolean("imp");
                                }
                                if(!value){
                                    ImageIcon imageIcon = new ImageIcon(logoImagePath);
                                    booleanStatement.setInt(1,1);
                                    booleanStatement.setInt(2,id);
                                    booleanStatement.executeUpdate();
                                    booleanStatement.close();
                                    textArea.setImage(imageIcon);
                                    important.setText("Unimportant");
                                    importantpopup.revalidate();
                                }
                                else{
                                    booleanStatement.setInt(1,0);
                                    booleanStatement.setInt(2,id);
                                    booleanStatement.executeUpdate();
                                    booleanStatement.close();
                                    textArea.setImage(new ImageIcon());
                                    important.setText("Important");
                                    importantpopup.revalidate();
                                }
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    });
                }
            }
        });
    }

    public void deleteComponent() {
        try{
            deleteStatement.setInt(1,id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        topicFrame.deleteComponentInFrame(component1);
    }
    public String sendText(){
        return textArea.getText();
    }

    public void setTextListener(){
                textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(selectedTableName!=null){
                        if(textArea.getText()!=null){
                            if(id==0){
                                try{
                                    System.out.println(selectedTableName);
                                    insertStatement = dbManager.conn.prepareStatement("INSERT INTO `" + selectedTableName + "` (Task) VALUES (?)",PreparedStatement.RETURN_GENERATED_KEYS);
                                    insertStatement.setString(1, textArea.getText());
                                    insertStatement.executeUpdate();

//                                    System.out.println(id);

                                    ResultSet generatedID=insertStatement.getGeneratedKeys();
                                    if(generatedID.next()){
                                        id=generatedID.getInt(1);
//                                        System.out.println(id);
                                    }

                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
//                                System.out.println(textArea.getText());
                            }
                            else{
                                try {
                                    updateStatement=dbManager.conn.prepareStatement("UPDATE `"+selectedTableName+"` SET Task=? WHERE id=?");
                                    updateStatement.setString(1,textArea.getText());
                                    updateStatement.setInt(2,id);
                                    updateStatement.executeUpdate();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                    else{
//                        if(id==0){
//                            try{
//                                insertStatement = dbManager.conn.prepareStatement("INSERT INTO temp(Task) VALUES (?)",PreparedStatement.RETURN_GENERATED_KEYS);
//                                insertStatement.setString(1, textArea.getText());
//                                insertStatement.executeUpdate();
//
//                                ResultSet generatedID=insertStatement.getGeneratedKeys();
//                                if(generatedID.next()){
//                                    id=generatedID.getInt(1);
//                                }
//
//                            } catch (SQLException ex) {
//                                throw new RuntimeException(ex);
//                            }
////                            System.out.println(textArea.getText());
//                        }
//                        else{
//                            try {
//                                updateStatement=dbManager.conn.prepareStatement("UPDATE temp SET Task=? WHERE id=?");
//                                updateStatement.setString(1,textArea.getText());
//                                updateStatement.setInt(2,id);
//                                updateStatement.executeUpdate();
//                            } catch (SQLException ex) {
//                                throw new RuntimeException(ex);
//                            }
//                        }
                    }
//                    System.out.println(textArea.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                if(selectedTableName!=null) {
                    try{
                        updateStatement=dbManager.conn.prepareStatement("UPDATE `"+selectedTableName+"` SET Task=? WHERE id=?");
                        updateStatement.setString(1,textArea.getText());
                        updateStatement.setInt(2,id);
                        updateStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
//                    try{
//                        updateStatement=dbManager.conn.prepareStatement("UPDATE temp SET Task=? WHERE id=?");
//                        updateStatement.setString(1,textArea.getText());
//                        updateStatement.setInt(2,id);
//                        updateStatement.executeUpdate();
//                    } catch (SQLException ex) {
//                        throw new RuntimeException(ex);
//                    }
                }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
    }
}
