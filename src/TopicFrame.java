    import javax.swing.*;
    import javax.swing.border.Border;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.WindowEvent;
    import java.awt.event.WindowListener;
    import java.sql.*;

    public class TopicFrame extends JFrame {
        public TopicFrame topicFrame;
         JMenuBar menuBar;
        public PreparedStatement insertStatement;
        public JTextArea text;
        public DefaultListModel<String> tableListModel;


        public StringBuilder createTableQuery1;
         Component component;
         StringBuilder createTableQuery2;
         DBManager dbManager;
         JMenu file;
        JMenuItem New;
         JMenuItem DeleteFiles;
         JMenuItem Open;
         JMenuItem Save;
         JMenuItem Saveas;
        public JButton addBtn;
        public JPanel textAreaPanel;
        public String selectedTableName;
        public JList<String> tableList;
        public PreparedStatement selectStatement;

        TopicFrame(String tablename) throws SQLException, ClassNotFoundException {
            this.selectedTableName=tablename;
            topicFrame=this;
            tableListModel = new DefaultListModel<>();
            dbManager = new DBManager();
            setLayout(new BorderLayout());
            menuBar = new JMenuBar();
            file = new JMenu("File");
            New = new JMenuItem("New");
            Open = new JMenuItem("Open");
            Saveas = new JMenuItem("Save As");
            DeleteFiles=new JMenuItem("Delete Files");
            file.add(New);
            file.add(Open);
            file.add(Saveas);
            file.add(DeleteFiles);
            menuBar.add(file);
            addBtn = new JButton("Add");
            addBtn.setFocusable(false);
            setResizable(true);

            JPanel wholePanel = new JPanel();

            textAreaPanel = new JPanel();
            textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
            textAreaPanel.setBackground(new Color(87, 85, 85, 255));
            wholePanel.add(textAreaPanel, BorderLayout.CENTER);
            wholePanel.setBackground(new Color(87, 85, 85, 255));

            JScrollPane newScrollPane = new JScrollPane(wholePanel);
            add(newScrollPane, BorderLayout.CENTER);
            add(addBtn, BorderLayout.SOUTH);
            New.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newTableName=JOptionPane.showInputDialog("Enter file Name");
                    if(newTableName!=null){
                        createTable(newTableName);
                        try {
                            topicFrame=new TopicFrame(newTableName);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
            Saveas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField input=new JTextField();
                    Object[] message={
                            "Enter file name: ",input
                    };
                    int option=JOptionPane.showOptionDialog(
                        topicFrame,
                        message,
                    "Save File",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.CANCEL_OPTION,
                            null,
                            new String[]{"Save","Cancel"},
                            null
                    );
                    if(option==JOptionPane.YES_NO_OPTION){
                        String tablename=input.getText().toString();
                        saveascontents(tablename);
                        tableListModel.addElement(tablename);
                    }
                }
            });
            DeleteFiles.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JList<String> list = new JList<>(tableListModel);
                    JScrollPane openscrollpane = new JScrollPane(list);
                    int result=JOptionPane.showOptionDialog(
                            topicFrame,
                            openscrollpane,
                            "Delete Files",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.CANCEL_OPTION,
                            null,
                            new String[]{"Delete","Cancel"},
                            null
                    );
                    if(result==JOptionPane.YES_NO_OPTION){
                        String seltectedDeletingItem=list.getSelectedValue();
                        deleteTable(seltectedDeletingItem);
                    }
                }
            });
            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    text = new JTextArea();
                    Component component= null;
                    try {
                        component = new Component(selectedTableName,0,topicFrame);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
//                    text.setColumns(100);
//                    text.setRows(3);
//                    text.setBorder(border);
//                    text.setMaximumSize(new Dimension(getWidth() / 2, 100)); // Set maximum size
//                    text.setMinimumSize(new Dimension(50, 100)); // Set maximum size
                    textAreaPanel.add(component);
                    textAreaPanel.add(Box.createVerticalStrut(10)); // Add vertical strut for spacing
                    component.setTextListener();
                    textAreaPanel.revalidate();
                    textAreaPanel.repaint();
                }
            });
            Open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JList<String> list = new JList<>(tableListModel);
                    JScrollPane openscrollpane = new JScrollPane(list);
                    int result = JOptionPane.showOptionDialog(
                            TopicFrame.this,
                            openscrollpane,
                            "Open Files",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null
                    );
                    if (result == JOptionPane.OK_OPTION) {
                        selectedTableName = list.getSelectedValue();
                        if (selectedTableName != null) {
                            System.out.println("Selected table: " + selectedTableName);
                        } else {
                            System.out.println("No table selected");
                        }
                    }
                    fieldContent(selectedTableName);
                    topicFrame.setTitle(selectedTableName);
                    revalidate();
                }
            });
            add(menuBar, BorderLayout.NORTH);
            loadTableNames();
            setSize(1000, 800);
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        public void saveascontents(String tablename) {
            createTable(tablename);
            try{
                System.out.println(tablename);
                Statement saveasStatement;
                saveasStatement=dbManager.conn.createStatement();
                saveasStatement.execute("INSERT INTO " +tablename+ " SELECT * FROM `"+selectedTableName+"`");
                saveasStatement.close();
                topicFrame=new TopicFrame(tablename);
//                selectedTableName=tablename;
//                fieldContent(selectedTableName);
//                topicFrame.setTitle(selectedTableName);
//                revalidate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try{
                Statement clearStatement=dbManager.conn.createStatement();
                clearStatement.execute("DELETE FROM temp");
                clearStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void deleteTable(String seltectedDeletingItem) {
            StringBuilder deleteTableQuery=new StringBuilder();
            deleteTableQuery.append("DROP TABLE ").append(seltectedDeletingItem);
            deleteTableQuery.append(";");
            try{
                Statement statement = dbManager.conn.createStatement();
                // Execute the CREATE TABLE query
                statement.executeUpdate(deleteTableQuery.toString());
                tableListModel.removeElement(seltectedDeletingItem);

                JOptionPane.showMessageDialog(null, "Deleted Table: " + seltectedDeletingItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //            text.getDocument().addDocumentListener(new DocumentListener() {
//                @Override
//                public void insertUpdate(DocumentEvent e) {
//                    if(text.getText()!=null){
//                        try{
//                            System.out.println(selectedTableName);
//                            insertStatement = dbManager.conn.prepareStatement("INSERT INTO `" + selectedTableName + "` (Task) VALUES (?)");
//                            insertStatement.setString(1, text.getText());
//                            insertStatement.executeUpdate();
//                        } catch (SQLException ex) {
//                            throw new RuntimeException(ex);
//                        }
//                        System.out.println(text.getText());
//                    }
//                }
//
//                @Override
//                public void removeUpdate(DocumentEvent e) {
//
//                }
//
//                @Override
//                public void changedUpdate(DocumentEvent e) {
//
//                }
//            });
        public void createTable(String tableName){
            if(tableName!=null) {
                String[] columns = {"ID", "Task","imp","level"};
                createTableQuery1 = new StringBuilder();
                createTableQuery2 = new StringBuilder();
                createTableQuery1.append("CREATE TABLE ").append(tableName).append(" (");
                for (int i = 0; i < columns.length; i++) {
                    String columnName = columns[i];
                    if (i == 0) {
                        createTableQuery1.append(columnName).append(" ").append("INT").append(" ").append("primary key").append(" ").append("AUTO_INCREMENT");
                        createTableQuery1.append(",");
                    } else if(i==1) {
                        createTableQuery1.append(columnName).append(" ").append("VARCHAR(1000)");
                        createTableQuery1.append(",");
                    }
                    else if(i==2){
                        createTableQuery1.append(columnName).append(" ").append("Boolean DEFAULT 0");
                        createTableQuery1.append(",");
                    }
                    else{
                        createTableQuery1.append(columnName).append(" ENUM('0', '1', '2', '3', '4', '5') DEFAULT '0'");
                    }
                }
                createTableQuery1.append(")");
                try {
                    Statement statement = dbManager.conn.createStatement();
                    // Execute the CREATE TABLE query
                    statement.executeUpdate(createTableQuery1.toString());

                    JOptionPane.showMessageDialog(null, "New table created: " + tableName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void fieldContent(String getTableName){
            try{
                selectStatement=dbManager.conn.prepareStatement("SELECT * FROM `"+getTableName+"`");
                ResultSet resultSet=selectStatement.executeQuery();
                textAreaPanel.removeAll();
                while(resultSet.next()){
                    int id=resultSet.getInt("ID");
                    String task=resultSet.getString("Task");
                    component=new Component(selectedTableName,id,this);
                    component.textArea.setText(task);
                    textAreaPanel.add(component);
                    component.setTextListener();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        public void loadTableNames() {
            try {
                DatabaseMetaData metaData = dbManager.conn.getMetaData();
                String catalogName = "topics";
                ResultSet resultSet = metaData.getTables(catalogName, null, "%", new String[]{"Table"});
                while (resultSet.next()) {
                    String tablename = resultSet.getString("TABLE_NAME");
                    tableListModel.addElement(tablename);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public void deleteComponentInFrame(Component component1){
//            System.out.println(component1);
            topicFrame.textAreaPanel.remove(component1);
            topicFrame.textAreaPanel.revalidate();
//            System.out.println("this is the checked");
        }
    }
