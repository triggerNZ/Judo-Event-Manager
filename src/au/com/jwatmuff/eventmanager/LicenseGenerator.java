/*
 * EventManager
 * Copyright (c) 2008-2017 James Watmuff & Leonard Hall
 *
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * LicenseGenerator.java
 *
 * Created on 05/07/2009, 2:24:24 AM
 */

package au.com.jwatmuff.eventmanager;

import au.com.jwatmuff.eventmanager.gui.main.Icons;
import au.com.jwatmuff.eventmanager.permissions.License;
import au.com.jwatmuff.eventmanager.permissions.LicenseType;
import au.com.jwatmuff.eventmanager.util.GUIUtils;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author James
 */
public class LicenseGenerator extends javax.swing.JFrame {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /** Creates new form LicenseGenerator */
    public LicenseGenerator() {
        initComponents();
        setLocationRelativeTo(null);

        /* set up license type combo box */
        DefaultComboBoxModel<LicenseType> model = new DefaultComboBoxModel<LicenseType>();
        for(LicenseType type : LicenseType.values())
            if(type != LicenseType.DEFAULT_LICENSE)
                model.addElement(type);
        typeComboBox.setModel(model);

        updateValidity();
    }

    private License createLicense() {
        String name = nameTextField.getText().trim();
        boolean nameValid = !name.isEmpty();
        nameValidLabel.setText(nameValid ? "" : "Name required");
        nameValidLabel.setIcon(nameValid ? Icons.YES : Icons.NO);

        String contact = contactTextField.getText().trim();
        boolean contactValid =
                contact.length() > 6 &&
                contact.matches("[0-9()+\\- ]*");
        contactValidLabel.setText(contactValid ? "" : "Contact phone required");
        contactValidLabel.setIcon(contactValid ? Icons.YES : Icons.NO);

        String expiry = expiryTextField.getText().trim();
        Date expiryDate = null;
        boolean dateValid = false;
        if(expiry.length() == 10) {
            try {
                expiryDate = dateFormat.parse(expiry);
                if(expiryDate != null) dateValid = true;
            } catch(Exception e) {}
        }
        expiryValidLabel.setText(dateValid ? "" : "Date required (DD/MM/YYYY)");
        expiryValidLabel.setIcon(dateValid ? Icons.YES : Icons.NO);

        LicenseType type = (LicenseType)typeComboBox.getSelectedItem();

        if(nameValid && contactValid && dateValid)
            return new License(name, contact, expiryDate, type);
        else
            return null;
    }

    private void updateValidity() {
        License license = createLicense();
        if(license != null) {
            keyTextField.setText(license.getKey());
            exportButton.setEnabled(true);
        } else {
            keyTextField.setText("");
            exportButton.setEnabled(false);
        }
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typeComboBox = new javax.swing.JComboBox<LicenseType>();
        nameValidLabel = new javax.swing.JLabel();
        contactTextField = new javax.swing.JTextField();
        expiryTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        contactValidLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        expiryValidLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();
        keyTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EventManager License Generator");
        setLocationByPlatform(true);
        setResizable(false);

        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        });

        nameValidLabel.setText("jLabel5");
        nameValidLabel.setIconTextGap(8);

        contactTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                contactTextFieldCaretUpdate(evt);
            }
        });

        expiryTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                expiryTextFieldCaretUpdate(evt);
            }
        });

        jLabel8.setText("Generated Key:");

        jLabel1.setText("Licensee Name:");

        contactValidLabel.setText("jLabel6");
        contactValidLabel.setIconTextGap(8);

        jLabel2.setText("Licensee Contact Number:");

        expiryValidLabel.setText("jLabel7");
        expiryValidLabel.setIconTextGap(8);

        jLabel3.setText("Expiry Date:");

        jLabel4.setText("License Type:");

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Export License File..");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        nameTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                nameTextFieldCaretUpdate(evt);
            }
        });

        keyTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameValidLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(contactTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contactValidLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(expiryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expiryValidLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(keyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exportButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 284, Short.MAX_VALUE)
                        .addComponent(exitButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {contactTextField, expiryTextField, nameTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameValidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(contactTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactValidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(expiryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiryValidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(keyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportButton)
                    .addComponent(exitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeComboBoxActionPerformed
        updateValidity();
}//GEN-LAST:event_typeComboBoxActionPerformed

    private void contactTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_contactTextFieldCaretUpdate
        updateValidity();
}//GEN-LAST:event_contactTextFieldCaretUpdate

    private void expiryTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_expiryTextFieldCaretUpdate
        updateValidity();
}//GEN-LAST:event_expiryTextFieldCaretUpdate

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
}//GEN-LAST:event_exitButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("License File", "lic"));
        chooser.setSelectedFile(new File("license.lic"));
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith(".lic")) {
                file = new File(file.getAbsolutePath() + ".lic");
            }
            License license = createLicense();
            try {
                License.saveToFile(license, file);
            } catch(Exception e) {
                GUIUtils.displayError(this, "An error occured while saving license file:\n" + e.getMessage());
            }
        }
}//GEN-LAST:event_exportButtonActionPerformed

    private void nameTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_nameTextFieldCaretUpdate
        updateValidity();
}//GEN-LAST:event_nameTextFieldCaretUpdate

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LicenseGenerator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField contactTextField;
    private javax.swing.JLabel contactValidLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JTextField expiryTextField;
    private javax.swing.JLabel expiryValidLabel;
    private javax.swing.JButton exportButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField keyTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nameValidLabel;
    private javax.swing.JComboBox<LicenseType> typeComboBox;
    // End of variables declaration//GEN-END:variables

}
