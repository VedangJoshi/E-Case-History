package com.gui;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Patient_Profile extends javax.swing.JFrame {

    public Patient_Profile() {
        initComponents();
        initCustomComponent();
    }

    public Patient_Profile(String firstName, String uid, String password)
    {
        this();
        this.uid=uid;
        this.password=password;
        jLabel1.setText("Hi " + firstName + "!");
        initProfile();
    }
    
    private void initCustomComponent()
    {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        jPanel2.add(datePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 200, 30));  
                
        /*jPanel=new JPanel(new AbsoluteLayout());
        jScrollPane1=new JScrollPane(jPanel);
        myPanel=new MyPanel[10];
        String str="Line1\nLine2\nLine3\nLine4\nLIne5\nLIne6";
        
        for(int i=0,y=10 ; i<10 ; i++,y+=300)
        {
            myPanel[i]=new MyPanel();
            myPanel[i].jLabel1.setText("Treatment " + (i+1)); 
            myPanel[i].jTextArea1.setText(str);
            myPanel[i].jTextArea1.setCaretPosition(0);
            myPanel[i].jTextArea2.setText(str);
            myPanel[i].jTextArea2.setCaretPosition(0);
            jPanel.add(myPanel[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(10,y,640,270));
        }
        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10,360,700,300));        */
    }
    
    private void initProfile()
    {
        try
        {
            mongoClient=new MongoClient(IP.hostName, 27017);
            DB db=mongoClient.getDB("e_case_history");
            DBCollection coll = db.getCollection("patient");
            DBObject doc = coll.find(new BasicDBObject("uid", uid)).next();
            mongoClient.close();
            
            jTextField5.setText(doc.get("uid").toString());
            jTextField19.setText(((DBObject)doc.get("fullName")).get("firstName").toString());
            jTextField24.setText(((DBObject)doc.get("fullName")).get("middleName").toString());
            jTextField20.setText(((DBObject)doc.get("fullName")).get("lastName").toString());
            
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date)doc.get("birthDate"));
            datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.getModel().setSelected(true);
            
            if(doc.get("gender").equals("Male"))
                groupButton1.setSelected(true);
            else
                groupButton2.setSelected(true);
            
            jTextField22.setText(doc.get("height").toString());
            jTextField23.setText(doc.get("weight").toString());
            jComboBox4.setSelectedItem(doc.get("bloodgroup"));
            jTextArea1.setText(doc.get("address").toString());
            jTextField29.setText(doc.get("contact").toString());
            jPasswordField2.setText(doc.get("password").toString());
            
        } catch(Exception e) {
            String error = e.getClass().getName();
            System.err.println( error + ": " + e.getMessage() );
            if(error.equals("com.mongodb.MongoTimeoutException"))
                JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);            
        }
    }
    
    private boolean allFieldEntered()
    {
        if(!jTextField5.getText().equals("") && !jTextField19.getText().equals("") && !jTextField24.getText().equals("") &&
           !jTextField20.getText().equals("") && datePicker.getModel().isSelected() && getSelectedButtonText(buttonGroup1)!=null && 
           !jTextField22.getText().equals("") && !jTextField23.getText().equals("") && jComboBox4.getSelectedIndex()!=0 && 
           !jTextField29.getText().equals("") && !jTextArea1.getText().equals("") && !jPasswordField2.getText().equals("") && 
           !jPasswordField3.getText().equals(""))
            return true;
        return false;
    }
    
    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();)
        {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
    
    private boolean updateDocument()
    {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getModel().getDay());
            cal.set(Calendar.MONTH, datePicker.getModel().getMonth());
            cal.set(Calendar.YEAR, datePicker.getModel().getYear());
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);

            BasicDBObject dBObject = new BasicDBObject("uid",jTextField5.getText())
                                    .append("fullName", new BasicDBObject("firstName", jTextField19.getText())
                                                            .append("middleName", jTextField24.getText())
                                                            .append("lastName", jTextField20.getText()))
                                    .append("birthDate", cal.getTime())
                                    .append("gender", getSelectedButtonText(buttonGroup1))
                                    .append("height", jTextField22.getText())
                                    .append("weight", jTextField23.getText())
                                    .append("bloodgroup", jComboBox4.getSelectedItem())
                                    .append("contact", jTextField29.getText())
                                    .append("address", jTextArea1.getText())
                                    .append("password", jPasswordField2.getText());
            
            mongoClient=new MongoClient(IP.hostName, 27017);
            DB db=mongoClient.getDB("e_case_history");
            DBCollection coll = db.getCollection("patient");
            coll.update(new BasicDBObject("uid", uid), dBObject);
            mongoClient.close();
            jLabel1.setText("Hi " + jTextField19.getText() + "!");
            uid = jTextField5.getText();
            password = jPasswordField2.getText();
            
            return true;
            
        } catch (Exception e) {
            String error = e.getClass().getName();
            System.err.println( error + ": " + e.getMessage() );
            
            if(error.equals("com.mongodb.MongoTimeoutException"))
                JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);
            
            mongoClient.close();
            
            return false;
            
        }            
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        groupButton1 = new javax.swing.JRadioButton();
        groupButton2 = new javax.swing.JRadioButton();
        jLabel29 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jLabel27 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 768));
        setPreferredSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("HI !");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 23, 1010, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 693, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Case History", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setBackground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("U ID");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, -1));

        jTextField5.setToolTipText("Enter your Registration Number");
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 120, -1));

        jLabel5.setText("Name");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 45, -1));
        jPanel2.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 120, -1));
        jPanel2.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 120, -1));
        jPanel2.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 120, -1));

        jLabel34.setText("Birthdate");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel8.setText("Sex");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        buttonGroup1.add(groupButton1);
        groupButton1.setText("Male");
        jPanel2.add(groupButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, -1, -1));

        buttonGroup1.add(groupButton2);
        groupButton2.setText("Female");
        jPanel2.add(groupButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, -1, -1));

        jLabel29.setText("Height");
        jPanel2.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));
        jPanel2.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 39, -1));

        jLabel30.setText("cm");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, -1, -1));

        jLabel31.setText("Weight");
        jPanel2.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));
        jPanel2.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 39, -1));

        jLabel45.setText("kg");
        jPanel2.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, -1, -1));

        jLabel36.setText("Bloodgroup");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Bloodgroup", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" }));
        jPanel2.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, -1, -1));

        jLabel37.setText("Contact No");
        jPanel2.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));
        jPanel2.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 120, -1));

        jLabel12.setText("Address");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 380, 120));

        jLabel6.setText("Password");
        jLabel6.setToolTipText("");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 110, -1));
        jPanel2.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 120, -1));

        jLabel7.setText("Re-Enter Password");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 120, -1));
        jPanel2.add(jPasswordField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 440, 120, -1));

        jLabel27.setText("First Name");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, -1));

        jLabel35.setText("Middle Name");
        jPanel2.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));

        jLabel32.setText("Surname");
        jPanel2.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 70, -1, -1));

        jButton3.setText("Save & Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 480, -1, -1));

        jTabbedPane1.addTab("Update Profile", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Doctor's Registration No");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 120, -1));

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, -1, -1));

        jTabbedPane1.addTab("Add Pass Key", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 700, 660));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton4.setText("Sign Out");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1225, 13, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(allFieldEntered())
        {
            if(jPasswordField2.getText().equals(jPasswordField3.getText()))
            {
                if(updateDocument())
                    JOptionPane.showMessageDialog(this, "Profile updated successfully","Message",JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this, "Password Re-Entered doesn't match","Message",JOptionPane.ERROR_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(this, "Please Enter All Fields","Message",JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.setVisible(false);
        new Patient_Login(uid,password).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        String regNo = jTextField1.getText();
        if(!regNo.equals(""))
        {
            try {
                
                mongoClient=new MongoClient(IP.hostName, 27017);
                DB db=mongoClient.getDB("e_case_history");
                DBCollection coll = db.getCollection("doctor");
                if(coll.find(new BasicDBObject("regNo", regNo)).count()!=0)
                {
                    DBCollection coll2 = db.getCollection("patient");
                    BasicDBObject obj = (BasicDBObject)coll2.find(new BasicDBObject("uid",uid)).next();
                    BasicDBList bList = (BasicDBList)obj.get("passkey");
                    if(bList==null)
                    {
                        obj.append("passkey", new BasicDBList());
                        coll2.update(new BasicDBObject("uid", uid), obj);
                        bList = (BasicDBList)obj.get("passkey");
                    }
                    
                    int flag=0;
                    for(int i=0; i<bList.size(); i++)
                    {
                        BasicDBObject temp = (BasicDBObject)bList.get(i);
                        if(temp.containsValue(regNo))
                        {
                            temp.replace("date", new Date());
                            bList.set(i, temp);
                            flag=1;
                            break;
                        }
                    }
                    
                    if(flag==0)
                        bList.add(new BasicDBObject("regNo", regNo).append("date", new Date()));
                    
                    coll2.update(new BasicDBObject("uid", uid), new BasicDBObject("$set", new BasicDBObject("passkey", bList)));
                    JOptionPane.showMessageDialog(this, "Doctor added is now allowed to view complete data till tomorrow",
                            "Message",JOptionPane.INFORMATION_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(this, "Entered Registration No is Invalid","Message",JOptionPane.ERROR_MESSAGE);
                mongoClient.close();
                
            } catch (Exception e) {                
                
                mongoClient.close();
                String error = e.getClass().getName();
                System.err.println( error + ": " + e.getMessage() );
                Logger.getLogger(Patient_Profile.class.getName()).log(Level.SEVERE, null, e);
                
                if(error.equals("com.mongodb.MongoTimeoutException"))
                    JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);                
                
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Please Enter Doctor's Registration No","Message",JOptionPane.ERROR_MESSAGE);
            
    }//GEN-LAST:event_jButton1ActionPerformed
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Patient_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Patient_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Patient_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Patient_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Patient_Profile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton groupButton1;
    private javax.swing.JRadioButton groupButton2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
    //private JPanel jPanel;
    //private JScrollPane jScrollPane1;
    //private MyPanel myPanel[];
    private String uid,password;
    private JDatePickerImpl datePicker;
    private MongoClient mongoClient;    
}

class MyPanel extends JPanel
{
    public JLabel jLabel1 = new javax.swing.JLabel();
    public JLabel jLabel2 = new JLabel();
    public JLabel jLabel3 = new JLabel();
    public JScrollPane jScrollPane1;
    public JScrollPane jScrollPane2;
    public JTextArea jTextArea1 = new JTextArea();
    public JTextArea jTextArea2 = new JTextArea();
    
    public MyPanel() {
        setLayout(null);
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        jLabel1.setText("Treatment ");
        jLabel1.setBounds(10, 10, 100, 20);
        
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        jLabel2.setText("Illness: ");
        jLabel2.setBounds(10, 40, 100, 20);
        
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        jLabel3.setText("Medication: ");
        jLabel3.setBounds(10, 150, 100, 20);
        
        jScrollPane1=new JScrollPane(jTextArea1);
        jScrollPane1.setBounds(110, 40, 500, 100);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        
        jScrollPane2=new JScrollPane(jTextArea2);
        jScrollPane2.setBounds(110, 150, 500, 100);
        jTextArea2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);
        
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        add(jScrollPane1);
        add(jScrollPane2);
    }
    
}