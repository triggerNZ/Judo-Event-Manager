/*
 * EnterWeightDialog.java
 *
 * Created on 12 May 2008, 16:07
 */

package au.com.jwatmuff.eventmanager.gui.player;


import au.com.jwatmuff.eventmanager.model.info.PlayerPoolInfo;
import au.com.jwatmuff.eventmanager.model.vo.Player;
import au.com.jwatmuff.eventmanager.permissions.Action;
import au.com.jwatmuff.eventmanager.permissions.PermissionChecker;
import au.com.jwatmuff.eventmanager.util.GUIUtils;
import au.com.jwatmuff.genericdb.transaction.TransactionNotifier;
import au.com.jwatmuff.genericdb.transaction.TransactionalDatabase;
import java.awt.Component;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 * Displays a dialog for entering a players weight.
 * After the dialog is closed, getSuccess() can be used to determine whether
 * a weight was succesfully entered.
 * The value of the weight entered can be obtained via getWeight().
 *
 * @author  James
 */
public class EnterWeightDialog extends javax.swing.JDialog {
    private boolean success;
    private Frame parent;
    private TransactionalDatabase database;
    private TransactionNotifier notifier;
    private double weight;
    private Player player;

    private DefaultListModel poolListModel;
    
    private ListCellRenderer ppiRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object obj, int arg2, boolean arg3, boolean arg4) {
            if(obj instanceof PlayerPoolInfo)
                obj = ((PlayerPoolInfo)obj).getPool().getDescription();
            return super.getListCellRendererComponent(list, obj, arg2, arg3, arg4);
        }
    };
    
    /** Creates new form EnterWeightDialog */
    public EnterWeightDialog(java.awt.Frame parent, boolean modal, TransactionalDatabase database, TransactionNotifier notifier, Player player) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        this.database = database;
        this.notifier = notifier;
        this.player = player;
        
        poolListModel = new DefaultListModel();
        divisionsList.setModel(poolListModel);
        divisionsList.setCellRenderer(ppiRenderer);

        updatePlayerDetails();
        this.getRootPane().setDefaultButton(okButton);
        this.weightTextField.grabFocus();
        this.setLocationRelativeTo(parent);
    }

    private void updatePlayerDetails() {
        this.idTextField.setText(player.getVisibleID());
        this.nameTextField.setText(player.getFirstName() + " " + player.getLastName());
        this.gradeTextField.setText(player.getGrade().toString());
        this.genderTextField.setText(player.getGender().toString());
        if(player.getDob() != null)
            this.dobTextField.setText(new SimpleDateFormat("dd/MM/yyyy").format(player.getDob()));
        this.teamTextField.setText(player.getTeam());

        updatePoolList();
    }

    
    private void updatePoolList() {
        poolListModel.clear();
        
        ArrayList<PlayerPoolInfo> playerPoolsList = new ArrayList<PlayerPoolInfo>(PlayerPoolInfo.getForPlayer(database, player.getID()));
        Collections.sort(playerPoolsList, new Comparator<PlayerPoolInfo>() {
            public int compare(PlayerPoolInfo p1, PlayerPoolInfo p2) {
                return p1.getPool().getDescription().compareTo(p2.getPool().getDescription());
            }
        });

        for(PlayerPoolInfo ppi : playerPoolsList) {
            poolListModel.addElement(ppi);
        }
    }
    
    public boolean getSuccess() {
        return success;
    }
    
    public double getWeight() {
        return weight;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameTextField = new javax.swing.JTextField();
        genderTextField = new javax.swing.JTextField();
        dobTextField = new javax.swing.JTextField();
        gradeTextField = new javax.swing.JTextField();
        weightTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        openPlayer = new javax.swing.JButton();
        teamTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        divisionsList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enter Weight");
        setLocationByPlatform(true);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("Lock Player");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Player ID:");

        jLabel2.setText("Name:");

        jLabel3.setText("Gender:");

        jLabel4.setText("DOB:");

        jLabel5.setText("Grade:");

        jLabel6.setText("Weight:");

        idTextField.setEditable(false);

        nameTextField.setEditable(false);

        genderTextField.setEditable(false);

        dobTextField.setEditable(false);

        gradeTextField.setEditable(false);

        jLabel7.setText("kg");

        openPlayer.setText("Open Player");
        openPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openPlayerActionPerformed(evt);
            }
        });

        teamTextField.setEditable(false);

        jLabel8.setText("Team:");

        divisionsList.setBackground(new java.awt.Color(240, 240, 240));
        divisionsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "<none>" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        divisionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divisionsList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        divisionsList.setSelectionBackground(new java.awt.Color(240, 240, 240));
        divisionsList.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(divisionsList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(dobTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(genderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(openPlayer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                    .addComponent(gradeTextField)
                                    .addComponent(teamTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(222, 222, 222))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dobTextField, genderTextField, gradeTextField, idTextField, nameTextField, teamTextField});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dobTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gradeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teamTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(openPlayer))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dobTextField, genderTextField, gradeTextField, idTextField, nameTextField});

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            weight = Double.parseDouble(weightTextField.getText());
            int response = JOptionPane.showConfirmDialog(
                    this,
                    weight + " kg. Are you sure this weight is correct?",
                    "Confirm Weight",
                    JOptionPane.WARNING_MESSAGE);
            if(response == JOptionPane.OK_OPTION) {
                this.success = true;
                this.dispose();
            }
        } catch(NumberFormatException e) {
            GUIUtils.displayError(parent, "Please enter a valid weight.");
        }    
}//GEN-LAST:event_okButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

private void openPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openPlayerActionPerformed
        new PlayerDetailsDialog(parent, true, database, notifier, player).setVisible(true);
        updatePlayerDetails();
}//GEN-LAST:event_openPlayerActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JList divisionsList;
    private javax.swing.JTextField dobTextField;
    private javax.swing.JTextField genderTextField;
    private javax.swing.JTextField gradeTextField;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JButton openPlayer;
    private javax.swing.JTextField teamTextField;
    private javax.swing.JTextField weightTextField;
    // End of variables declaration//GEN-END:variables
    
}
