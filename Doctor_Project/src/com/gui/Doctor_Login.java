package com.gui;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.util.Calendar;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class Doctor_Login extends javax.swing.JFrame {

    public Doctor_Login() {
        initComponents(); 
        initCustomComponent();
        isVisible=false;
        visibility(isVisible);
    }
    
    public Doctor_Login(String regNo, String password) {
        this();
        jTextField1.setText(regNo);
        jPasswordField1.setText(password);
    }    
    
    private void initCustomComponent()
    {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        jTextField20.setNextFocusableComponent(datePicker);
        datePicker.setNextFocusableComponent(groupButton1);
        getContentPane().add(datePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, 200, 30));        
    }
    
    private void visibility(boolean b)
    {
        jTextField5.setVisible(b);
        jTextField19.setVisible(b);
        jTextField20.setVisible(b);
        jTextField24.setVisible(b);
        datePicker.setVisible(b);
        jLabel4.setVisible(b);
        jLabel5.setVisible(b);
        jLabel6.setVisible(b);
        jLabel8.setVisible(b);
        jLabel9.setVisible(b);
        jLabel10.setVisible(b);
        jLabel11.setVisible(b);
        jLabel12.setVisible(b);
        jLabel13.setVisible(b);
        jLabel27.setVisible(b);
        jLabel35.setVisible(b);
        jLabel32.setVisible(b);
        jLabel34.setVisible(b);
        jPasswordField2.setVisible(b);
        jSeparator2.setVisible(b);
        jTextArea1.setVisible(b);
        jComboBox1.setVisible(b);
        jComboBox2.setVisible(b);
        jComboBox3.setVisible(b);
        groupButton1.setVisible(b);
        groupButton2.setVisible(b);
        jScrollPane1.setVisible(b);
        jButton3.setVisible(b);
        jButton4.setVisible(b);
        jLabel37.setVisible(b);
        jTextField29.setVisible(b);
        jLabel7.setVisible(b);
        jPasswordField3.setVisible(b);
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
    
    private boolean insertDocument()
    {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getModel().getDay());
            cal.set(Calendar.MONTH, datePicker.getModel().getMonth());
            cal.set(Calendar.YEAR, datePicker.getModel().getYear());
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 00);

            BasicDBObject dBObject = new BasicDBObject("regNo",jTextField5.getText())
                                    .append("fullName", new BasicDBObject("firstName", jTextField19.getText())
                                                            .append("middleName", jTextField24.getText())
                                                            .append("lastName", jTextField20.getText()))
                                    .append("birthDate", cal.getTime())
                                    .append("gender", getSelectedButtonText(buttonGroup1))
                                    .append("degree", jComboBox1.getSelectedItem())
                                    .append("speciality", jComboBox2.getSelectedItem())
                                    .append("area", jComboBox3.getSelectedItem())
                                    .append("address", jTextArea1.getText())
                                    .append("contact", jTextField29.getText())
                                    .append("password", jPasswordField2.getText());
            
            mongoClient=new MongoClient(IP.hostName, 27017);
            DB db=mongoClient.getDB("e_case_history");
            DBCollection coll = db.getCollection("doctor");
            coll.createIndex(new BasicDBObject("regNo", 1), new BasicDBObject("unique", "true"));            
            coll.insert(dBObject);
            mongoClient.close();
            
            return true;
            
        } catch (Exception e) {
            mongoClient.close();
            String error = e.getClass().getName();
            System.err.println( error + ": " + e.getMessage() );
            
            if(error.equals("com.mongodb.MongoTimeoutException"))
                JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);
            else if(error.equals("com.mongodb.MongoException$DuplicateKey"))
                JOptionPane.showMessageDialog(this, "Regestration No is already present in database",
                        "Message",JOptionPane.ERROR_MESSAGE);
            
            return false;
            
        }    
    }
    
    private boolean allFieldEntered()
    {
        if(!jTextField5.getText().equals("") && !jTextField19.getText().equals("") && !jTextField24.getText().equals("") &&
           !jTextField20.getText().equals("") && datePicker.getModel().isSelected() && getSelectedButtonText(buttonGroup1)!=null && 
           jComboBox1.getSelectedIndex()!=0 && jComboBox2.getSelectedIndex()!=0 && jComboBox3.getSelectedIndex()!=0 && 
           !jTextArea1.getText().equals("") && !jPasswordField2.getText().equals("") && !jTextField29.getText().equals("") && 
           !jPasswordField2.getText().equals(""))
            return true;
        return false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        groupButton1 = new javax.swing.JRadioButton();
        groupButton2 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField19 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel37 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login Console");
        setBackground(new java.awt.Color(184, 237, 238));
        setMinimumSize(new java.awt.Dimension(1366, 768));
        setPreferredSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Doctor Login");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 23));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 24, 460, 10));

        jLabel2.setText("Registration No");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 91, -1));
        jLabel2.getAccessibleContext().setAccessibleName("Username");

        jTextField1.setNextFocusableComponent(jPasswordField1);
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 120, -1));

        jLabel3.setText("Password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, 91, -1));

        jPasswordField1.setNextFocusableComponent(jButton1);
        getContentPane().add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 94, 120, -1));

        jButton1.setText("Login");
        jButton1.setNextFocusableComponent(jButton5);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 79, -1));

        jButton2.setText("New User");
        jButton2.setNextFocusableComponent(jTextField1);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 75, -1, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 147, 500, 10));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("Sign Up");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 134, 64, 23));

        jLabel5.setText("Name");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 205, 45, -1));

        buttonGroup1.add(groupButton1);
        groupButton1.setText("Male");
        groupButton1.setNextFocusableComponent(groupButton2);
        getContentPane().add(groupButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, -1, -1));

        buttonGroup1.add(groupButton2);
        groupButton2.setText("Female");
        groupButton2.setNextFocusableComponent(jComboBox1);
        getContentPane().add(groupButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, -1, -1));

        jLabel8.setText("Sex");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jLabel9.setBackground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Registration No");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 174, 100, -1));

        jTextField5.setToolTipText("Enter your Registration Number");
        jTextField5.setNextFocusableComponent(jTextField19);
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 171, 120, -1));

        jLabel10.setText("Degree");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Degree","MBBS", "MD", "BDS", "BAMS" ,"PhD","DGO"}));
        jComboBox1.setToolTipText("Select your UG Degree");
        jComboBox1.setNextFocusableComponent(jComboBox2);
        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 199, -1));

        jLabel11.setText("Specialization");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Speciality","Immunologist", "Anaesthesiologist", "Cardiologist", "Dermatologist","Gastroenterologist","Haematologist","Internal Medicine Physician","Nephrologist","Neurologist","Neuro-Surgeon","Obstretrician","Gynaecologist" ,"Occupational Medicine Physician","Opthalmologist","Maxillofacial Surgeon","Orthopaedic","Otolaryngologist","Pathologist","Pediatrician","Plastic Surgeon","Podiatrist","Psychiatrist","Pulmonary Medicine Physician","Radiation Oncologist","Diagnostic Radiologist","Rheumatologist","Urologist"}));
        jComboBox2.setToolTipText("");
        jComboBox2.setNextFocusableComponent(jComboBox3);
        getContentPane().add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 199, -1));

        jLabel12.setText("Address");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setNextFocusableComponent(jTextField29);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 380, 120));

        jLabel13.setText("Area");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 45, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Area","Camp", "Koregaon Park", "Aundh", "Bavdhan","Pashan","Deccan","Baner","Kothrud","Salisbury Park","Dhankawadi","Sahakarnagar","Bibvewadi","Lullanagar","Hadapsar","Vimannagar","Pimpri-Chinchwad"}));
        jComboBox3.setToolTipText("Select your Region of Workplace");
        jComboBox3.setNextFocusableComponent(jTextArea1);
        getContentPane().add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, 199, -1));

        jButton3.setText("Sign Up");
        jButton3.setNextFocusableComponent(jButton4);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 640, -1, -1));

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 640, 80, -1));

        jButton5.setText("Reset");
        jButton5.setMaximumSize(new java.awt.Dimension(61, 22));
        jButton5.setNextFocusableComponent(jButton2);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 80, -1));

        jTextField19.setNextFocusableComponent(jTextField24);
        getContentPane().add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 120, -1));

        jLabel27.setText("First Name");
        getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, -1, -1));
        getContentPane().add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 200, 120, -1));

        jLabel35.setText("Middle Name");
        getContentPane().add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 230, -1, -1));

        jTextField24.setNextFocusableComponent(jTextField20);
        getContentPane().add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, 120, -1));

        jLabel32.setText("Surname");
        getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 230, -1, -1));

        jLabel34.setText("Birthdate");
        getContentPane().add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel6.setText("Password");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 91, -1));

        jPasswordField2.setNextFocusableComponent(jPasswordField3);
        getContentPane().add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 570, 120, -1));

        jLabel37.setText("Contact No");
        getContentPane().add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, -1, -1));

        jTextField29.setNextFocusableComponent(jPasswordField2);
        getContentPane().add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 540, 120, -1));

        jLabel7.setText("Re-Enter Password");
        jLabel7.setToolTipText("");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 110, -1));

        jPasswordField3.setNextFocusableComponent(jButton3);
        getContentPane().add(jPasswordField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 600, 120, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!jTextField1.getText().equals("") && !jPasswordField1.getText().equals(""))
        {
            try
            {
                mongoClient=new MongoClient(IP.hostName, 27017);
                DB db=mongoClient.getDB("e_case_history");
                DBCollection doctorColl = db.getCollection("doctor");
                DBCursor cursor = doctorColl.find(new BasicDBObject("regNo", jTextField1.getText())
                                                  .append("password", jPasswordField1.getText()));
                if(cursor.count()==0)
                {
                    mongoClient.close();
                    JOptionPane.showMessageDialog(this, "Registration No or Password is incorrect","Message",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    BasicDBObject doc = (BasicDBObject)cursor.next();
                    mongoClient.close();
                    String firstName = ((BasicDBObject)doc.get("fullName")).get("firstName").toString();
                    String password = doc.get("password").toString();
                    this.setVisible(false);
                    new Doctor_Profile(firstName,jTextField1.getText(),password).setVisible(true);
                }
            } catch (Exception e) {
                mongoClient.close();
                String error = e.getClass().getName();
                System.err.println( error + ": " + e.getMessage() ); 
                if(error.equals("com.mongodb.MongoTimeoutException"))
                    JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Please enter both fields","Message",JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTextField5.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
        jTextField24.setText("");
        datePicker.getModel().setSelected(false);
        jPasswordField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        buttonGroup1.clearSelection();
        jTextArea1.setText("");
        jTextField29.setText("");
        jPasswordField3.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        visibility(isVisible=!isVisible);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jPasswordField1.setText("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(allFieldEntered())
        {
            if(jPasswordField2.getText().equals(jPasswordField3.getText()))
            {
                if(insertDocument())
                {
                    jButton4ActionPerformed(evt);
                    visibility(isVisible=!isVisible);
                    JOptionPane.showMessageDialog(this, "Sign Up Successful\nPlease Enter Registration No and Password to Login",
                            "Message",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(this, "Password Re-Entered doesn't match","Message",JOptionPane.ERROR_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(this, "Please Enter All Fields","Message",JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Doctor_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Doctor_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Doctor_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Doctor_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Doctor_Login().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton groupButton1;
    private javax.swing.JRadioButton groupButton2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
    private boolean isVisible;
    private MongoClient mongoClient;
    private JDatePickerImpl datePicker;
}