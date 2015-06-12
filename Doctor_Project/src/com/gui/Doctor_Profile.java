package com.gui;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class Doctor_Profile extends javax.swing.JFrame {

    public Doctor_Profile() {
        initComponents();
        initCustomComponent();
        isVisible = false;
        visibilty(isVisible);;
    }
    
    public Doctor_Profile(String firstName, String regNo, String password)
    {
        this();
        this.regNo=regNo;
        this.password=password;
        jLabel1.setText("Hi Dr. " + firstName + "!");
        initProfile();
    }

    private void initCustomComponent()
    {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        jPanel2.add(datePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 200, 30));  
        
        jPanel=new JPanel(new AbsoluteLayout());
        jScrollPane=new JScrollPane(jPanel);
        jLabel = new JLabel("No treatment added");
        jLabel.setVisible(false);
        jPanel.add(jLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel1.add(jScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(150,300,670,240));
        
        jLabel43.setVisible(false);
        jScrollPane.setVisible(false);
        jButton1.setVisible(false);        
    }    
    
    private void initProfile()
    {
        try
        {
            mongoClient=new MongoClient(IP.hostName, 27017);
            DB db=mongoClient.getDB("e_case_history");
            DBCollection coll = db.getCollection("doctor");
            DBObject doc = coll.find(new BasicDBObject("regNo", regNo)).next();
            mongoClient.close();
            
            jTextField5.setText(doc.get("regNo").toString());
            jTextField20.setText(((DBObject)doc.get("fullName")).get("firstName").toString());
            jTextField24.setText(((DBObject)doc.get("fullName")).get("middleName").toString());
            jTextField21.setText(((DBObject)doc.get("fullName")).get("lastName").toString());
            
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date)doc.get("birthDate"));
            datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.getModel().setSelected(true);
            
            if(doc.get("gender").equals("Male"))
                groupButton1.setSelected(true);
            else
                groupButton2.setSelected(true);
            
            jComboBox1.setSelectedItem(doc.get("degree"));
            jComboBox2.setSelectedItem(doc.get("speciality"));
            jComboBox3.setSelectedItem(doc.get("area"));
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
        if(!jTextField5.getText().equals("") && !jTextField20.getText().equals("") && !jTextField24.getText().equals("") &&
           !jTextField21.getText().equals("") && datePicker.getModel().isSelected() && getSelectedButtonText(buttonGroup1)!=null && 
           jComboBox1.getSelectedIndex()!=0 && jComboBox2.getSelectedIndex()!=0 && jComboBox3.getSelectedIndex()!=0 && 
           !jTextArea1.getText().equals("") && !jTextField29.getText().equals("") && !jPasswordField2.getText().equals("") &&
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

            BasicDBObject dBObject = new BasicDBObject("regNo",jTextField5.getText())
                                    .append("fullName", new BasicDBObject("firstName", jTextField20.getText())
                                                            .append("middleName", jTextField24.getText())
                                                            .append("lastName", jTextField21.getText()))
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
            DBCollection doctorColl = db.getCollection("doctor");
            doctorColl.update(new BasicDBObject("regNo", regNo), dBObject);
            mongoClient.close();
            jLabel1.setText("Hi Dr. " + jTextField20.getText() + "!");
            regNo = jTextField5.getText();
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
    
    private void visibilty(boolean b)
    {
        jLabel14.setVisible(b);
        jLabel15.setVisible(b);
        jLabel29.setVisible(b);
        jLabel31.setVisible(b);
        jLabel36.setVisible(b);
        jLabel38.setVisible(b);
        jLabel39.setVisible(b);
        jLabel16.setVisible(b);
        jLabel40.setVisible(b);
        jLabel17.setVisible(b);
        jLabel30.setVisible(b);
        jLabel33.setVisible(b);
        jLabel41.setVisible(b);
        jLabel42.setVisible(b);
    }
    
    private void resetLabels()
    {
        jLabel16.setText("");
        jLabel40.setText("");
        jLabel17.setText("");
        jLabel30.setText("");
        jLabel33.setText("");
        jLabel41.setText("");
        jLabel42.setText("");
    }
    
    private void viewData(boolean flag)
    {
        resetLabels();
        visibilty(isVisible=true);
        jLabel43.setVisible(false);
        jScrollPane.setVisible(false);
        jButton1.setVisible(false); 
        
        uid = jTextField18.getText();
        try
        {
            System.out.println(flag);
            mongoClient=new MongoClient(IP.hostName, 27017);
            DB db=mongoClient.getDB("e_case_history");
            DBCollection coll = db.getCollection("patient");
            BasicDBObject obj = (BasicDBObject)coll.find(new BasicDBObject("uid", uid)).next();
            mongoClient.close();
            
            BasicDBObject name = (BasicDBObject)obj.get("fullName");
            jLabel16.setText(name.getString("firstName") + " " + name.getString("middleName") + " " + name.getString("lastName"));
            
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date)obj.get("birthDate"));
            LocalDate today = LocalDate.now();
            LocalDate bDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
            Period p = Period.between(bDate, today);
            jLabel40.setText(p.getYears()+"");
            
            jLabel17.setText(obj.getString("gender"));
            jLabel30.setText(obj.getString("height") + " cm");
            jLabel33.setText(obj.getString("weight") + " kg");
            jLabel41.setText(obj.getString("bloodgroup"));
            jLabel42.setText(obj.getString("contact"));
            
            if(flag)
            {
                jLabel43.setVisible(true);
                jScrollPane.setVisible(true);
                jButton1.setVisible(true);
                
                BasicDBList bList = (BasicDBList)obj.get("treatment");
                if(bList!=null)
                {
                    viewTreatment(bList);
                    /*jLabel.setVisible(false);
                    int size = bList.size();
                    myPanel = new MyPanel[size];
                    for(int i=size-1 , y=10 ; i >=0 ; i-- , y+=230)
                    {
                        BasicDBObject temp = (BasicDBObject)bList.get(i);
                        myPanel[i] = new MyPanel();                    
                        myPanel[i].jLabel1.setText("Treatment No " + (i+1));

                        Calendar c = Calendar.getInstance();
                        c.setTime((Date)temp.get("treatedOn"));
                        myPanel[i].jLabel5.setText(
                                c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));

                        myPanel[i].jTextArea1.setText(temp.getString("diagnosis"));
                        myPanel[i].jTextArea2.setText(temp.getString("medication"));
                        jPanel.add(myPanel[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(10,y,630,210));
                    }*/
                }
                else
                {
                    jLabel.setVisible(true);
                }
            }
                
        }
        catch(Exception e)
        {
                
            mongoClient.close();
            String error = e.getClass().getName();
            System.err.println( error + ": " + e.getMessage() );
            Logger.getLogger(Doctor_Profile.class.getName()).log(Level.SEVERE, null, e);
                
            if(error.equals("com.mongodb.MongoTimeoutException"))
                JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);                
                            
        }        
    }
    
    public void viewTreatment(BasicDBList bList)
    {
        jLabel.setVisible(false);
        jPanel.removeAll();
        int size = bList.size();
        myPanel = new MyPanel[size];
        for (int i = size - 1, y = 10; i >= 0; i--, y += 230)
        {
            BasicDBObject temp = (BasicDBObject) bList.get(i);
            myPanel[i] = new MyPanel();
            myPanel[i].jLabel1.setText("Treatment No " + (i + 1));

            Calendar c = Calendar.getInstance();
            c.setTime((Date) temp.get("treatedOn"));
            myPanel[i].jLabel5.setText(
                    c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));

            myPanel[i].jTextArea1.setText(temp.getString("diagnosis"));
            myPanel[i].jTextArea2.setText(temp.getString("medication"));
            jPanel.add(myPanel[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(10, y, 630, 210));
        }       
    }
    
    private void showDialog()
    {
        new MyDialog(this,uid).setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        groupButton1 = new javax.swing.JRadioButton();
        groupButton2 = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel27 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Hi Dr. ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 13, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 23, 1010, -1));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton4.setText("Sign Out");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1225, 13, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setText("U ID");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 10, -1, -1));
        jPanel1.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 120, -1));

        jButton6.setText("View Data");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, -1));

        jLabel14.setText("Name:");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 45, -1));

        jLabel15.setText("Sex:");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jLabel29.setText("Height:");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        jLabel31.setText("Weight:");
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel36.setText("Age:");
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel38.setText("Bloodgroup:");
        jPanel1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jLabel39.setText("Contact No:");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 300, -1));
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 120, -1));
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 120, -1));
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 120, -1));
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 120, -1));
        jPanel1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 120, -1));
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 120, -1));

        jLabel43.setText("Treatments:");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jButton1.setText("Add a treatment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 550, -1, -1));

        jTabbedPane1.addTab("View Patient Data", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setBackground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Registration No");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, -1));

        jTextField5.setToolTipText("Enter your Registration Number");
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 120, -1));

        jLabel5.setText("Name");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 45, -1));
        jPanel2.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 120, -1));
        jPanel2.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 120, -1));
        jPanel2.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 120, -1));

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

        jLabel10.setText("Degree");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Degree","MBBS", "MD", "BDS", "BAMS" ,"PhD","DGO"}));
        jComboBox1.setToolTipText("Select your UG Degree");
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 199, -1));

        jLabel11.setText("Specialization");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Speciality","Immunologist", "Anaesthesiologist", "Cardiologist", "Dermatologist","Gastroenterologist","Haematologist","Internal Medicine Physician","Nephrologist","Neurologist","Neuro-Surgeon","Obstretrician","Gynaecologist" ,"Occupational Medicine Physician","Opthalmologist","Maxillofacial Surgeon","Orthopaedic","Otolaryngologist","Pathologist","Pediatrician","Plastic Surgeon","Podiatrist","Psychiatrist","Pulmonary Medicine Physician","Radiation Oncologist","Diagnostic Radiologist","Rheumatologist","Urologist"}));
        jComboBox2.setToolTipText("");
        jPanel2.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 199, -1));

        jLabel13.setText("Area");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 45, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Area","Camp", "Koregaon Park", "Aundh", "Bavdhan","Pashan","Deccan","Baner","Kothrud","Salisbury Park","Dhankawadi","Sahakarnagar","Bibvewadi","Lullanagar","Hadapsar","Vimannagar","Pimpri-Chinchwad"}));
        jComboBox3.setToolTipText("Select your Region of Workplace");
        jPanel2.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 199, -1));

        jLabel12.setText("Address");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 380, 120));

        jLabel6.setText("Password");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 91, -1));
        jPanel2.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 120, -1));

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

        jLabel37.setText("Contact No");
        jPanel2.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));
        jPanel2.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, 120, -1));

        jLabel7.setText("Re-Enter Password");
        jLabel7.setToolTipText("");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 110, -1));
        jPanel2.add(jPasswordField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 440, 120, -1));

        jTabbedPane1.addTab("Update Profile", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 1000, 660));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        String uid = jTextField18.getText();
        if(!uid.equals(""))
        {
            try
            {

                mongoClient=new MongoClient(IP.hostName, 27017);
                DB db=mongoClient.getDB("e_case_history");
                DBCollection coll = db.getCollection("patient");
                DBCursor dBCursor = coll.find(new BasicDBObject("uid", uid));

                LocalDate date = LocalDate.now();
                int flag=0;

                if(dBCursor.count()!=0)
                {
                    BasicDBObject obj = (BasicDBObject)dBCursor.next();  
                    mongoClient.close();
                    BasicDBList bList = (BasicDBList)obj.get("passkey");
                    if(bList==null)
                        viewData(false);
                    else
                    {
                        for(int i=0; i<bList.size(); i++)
                        {
                            BasicDBObject temp = (BasicDBObject)bList.get(i);
                            if(temp.containsValue(regNo))
                            {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime((Date)temp.get("date"));
                                date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                                flag=1;
                                break;
                            }
                        }
                        if(flag==1)
                        {
                            Period p = Period.between(date, LocalDate.now());
                            if(p.getDays()<1)
                                viewData(true);
                            else
                                viewData(false);
                        }
                        else
                            viewData(false);
                    }
                }
                else
                {
                    mongoClient.close();
                    visibilty(isVisible=false);
                    jLabel43.setVisible(false);
                    jScrollPane.setVisible(false);
                    jButton1.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Entered U ID is invalid","Message",JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception e)
            {

                mongoClient.close();
                String error = e.getClass().getName();
                System.err.println( error + ": " + e.getMessage() );
                Logger.getLogger(Doctor_Profile.class.getName()).log(Level.SEVERE, null, e);

                if(error.equals("com.mongodb.MongoTimeoutException"))
                    JOptionPane.showMessageDialog(this, "Database Connectivity Error","Message",JOptionPane.ERROR_MESSAGE);                

            }
        }
        else
        {
            visibilty(isVisible=false);          
            JOptionPane.showMessageDialog(this, "Please Enter U ID","Message",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

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
        new Doctor_Login(regNo,password).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showDialog();
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
            java.util.logging.Logger.getLogger(Doctor_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Doctor_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Doctor_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Doctor_Profile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Doctor_Profile().setVisible(true);
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
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private MyPanel myPanel[];
    private JLabel jLabel;    
    private boolean isVisible;    
    private String regNo, password, uid;
    private MongoClient mongoClient;
    private JDatePickerImpl datePicker;
}

class MyPanel extends JPanel
{
    public JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5;   
    public JScrollPane jScrollPane1, jScrollPane2;
    public JTextArea jTextArea1, jTextArea2;
    
    public MyPanel() {
        setLayout(null);
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        
        jLabel1 = new JLabel("Treatment No ");
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        jLabel1.setBounds(10, 10, 100, 20);
        
        jLabel4 = new JLabel("Treated on: ");
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        jLabel4.setBounds(10, 40, 100, 20);        

        jLabel5 = new JLabel("dd/MM/yyyy");
        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        jLabel5.setBounds(120, 40, 100, 20);                
        
        jLabel2 = new JLabel("Diagnosis: ");
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        jLabel2.setBounds(10, 70, 100, 20);
        
        jTextArea1 = new JTextArea();
        jScrollPane1=new JScrollPane(jTextArea1);
        jScrollPane1.setBounds(120, 70, 500, 60);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 13));
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);        
        
        jLabel3 = new JLabel("Medication: ");
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        jLabel3.setBounds(10, 140, 100, 20);
        
        jTextArea2 = new JTextArea();
        jScrollPane2=new JScrollPane(jTextArea2);
        jScrollPane2.setBounds(120, 140, 500, 60);
        jTextArea2.setFont(new java.awt.Font("Tahoma", 0, 13));
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);
        
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        add(jLabel4);
        add(jLabel5);        
        add(jScrollPane1);
        add(jScrollPane2);
    }
    
}

class MyDialog extends JDialog
{
    public JLabel jLabel1, jLabel2;
    public JScrollPane jScrollPane1,jScrollPane2;
    public JTextArea jTextArea1, jTextArea2;
    public JButton button;
    private MongoClient mongoClient;
    private String uid;
    private Doctor_Profile parent;
    
    public MyDialog(Doctor_Profile parent, String uid)
    {
        super(parent, "Add a treatment", true);
        this.parent=parent;
        this.uid=uid;
        setLayout(null);
        setBounds(350, 260, 660, 240);
        
        jLabel1 = new JLabel("Diagnosis: ");
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 13));
        jLabel1.setBounds(10, 10, 100, 20);
        
        jTextArea1 = new JTextArea();
        jScrollPane1=new JScrollPane(jTextArea1);
        jScrollPane1.setBounds(120, 10, 500, 60);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 13));
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);   
        
        jLabel2 = new JLabel("Medication: ");
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13));
        jLabel2.setBounds(10, 80, 100, 20);
        
        jTextArea2 = new JTextArea();
        jScrollPane2=new JScrollPane(jTextArea2);
        jScrollPane2.setBounds(120, 80, 500, 60);
        jTextArea2.setFont(new java.awt.Font("Tahoma", 0, 13));
        jTextArea2.setLineWrap(true);
        jTextArea2.setWrapStyleWord(true);   
        
        button = new JButton("Add");
        button.setFont(new java.awt.Font("Tahoma", 0, 13));
        button.setBounds(200, 150, 100, 30);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addTreatment();
            }
        });
        
        add(jLabel1);
        add(jScrollPane1);
        add(jLabel2);
        add(jScrollPane2);
        add(button);
    }
    
    public void addTreatment()
    {
        if(!jTextArea1.equals("") && !jTextArea2.equals(""))
        {
            try
            {
                BasicDBObject pushDoc = new BasicDBObject("$push", new BasicDBObject("treatment", 
                        new BasicDBObject("treatedOn", new Date())
                                            .append("diagnosis", jTextArea1.getText())
                                            .append("medication", jTextArea2.getText())));
             
                MongoClient mongoClient=new MongoClient(IP.hostName, 27017);
                DB db=mongoClient.getDB("e_case_history");
                DBCollection coll = db.getCollection("patient");
                coll.update(new BasicDBObject("uid", uid), pushDoc);
                
                BasicDBList bList = (BasicDBList)coll.find(new BasicDBObject("uid", uid)).next().get("treatment");
                mongoClient.close();
                parent.viewTreatment(bList);
                parent.repaint();
            }
            catch(Exception e)
            {
                mongoClient.close();
                String error = e.getClass().getName();
                System.err.println( error + ": " + e.getMessage() );
                Logger.getLogger(Doctor_Profile.class.getName()).log(Level.SEVERE, null, e);            
            }
            dispose();
        }
        else
            JOptionPane.showMessageDialog(this, "Please Enter All Fields","Message",JOptionPane.ERROR_MESSAGE);
    }
    
}
