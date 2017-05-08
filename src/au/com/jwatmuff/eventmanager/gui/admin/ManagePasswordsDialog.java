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

package au.com.jwatmuff.eventmanager.gui.admin;

import au.com.jwatmuff.eventmanager.permissions.PermissionChecker;
import au.com.jwatmuff.eventmanager.permissions.Action;
import au.com.jwatmuff.eventmanager.permissions.PasswordType;
import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.genericdb.Database;
import au.com.jwatmuff.genericdb.distributed.DataEvent;
import au.com.jwatmuff.genericdb.transaction.TransactionListener;
import au.com.jwatmuff.genericdb.transaction.TransactionNotifier;
import java.awt.Frame;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author  James
 */
public class ManagePasswordsDialog extends javax.swing.JDialog implements TransactionListener {
    private static final Logger log = Logger.getLogger(ManagePasswordsDialog.class);

    private static final String ENABLED = "Enabled";
    private static final String DISABLED = "Disabled";
    private static final String CHANGE = "Change";
    private static final String ENABLE = "Enable";

    private Database database;
    private TransactionNotifier notifier;
    
    /** Creates new form ManagePasswordsDialog */
    public ManagePasswordsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
    }
    
    public void setDatabase(Database database) {
        this.database = database;
        updatePasswordStatus();
    }
    
    public void setNotifier(TransactionNotifier notifier) {
        this.notifier = notifier;
        notifier.addListener(this, CompetitionInfo.class);
    }
    
    private void updatePasswordStatus() {
        final CompetitionInfo ci = database.get(CompetitionInfo.class, null);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                masterPasswordStatusLabel.setText((ci.getPasswordHash()==0)?DISABLED:ENABLED);
                masterPasswordButton.setText((ci.getPasswordHash()==0)?ENABLE:CHANGE);

                weighInPasswordStatusLabel.setText((ci.getWeighInPasswordHash()==0)?DISABLED:ENABLED);
                weighInPasswordButton.setText((ci.getWeighInPasswordHash()==0)?ENABLE:CHANGE);

                personalDetailsPasswordStatusLabel.setText((ci.getPersonalDetailsPasswordHash()==0)?DISABLED:ENABLED);
                personalDetailsPasswordButton.setText((ci.getPersonalDetailsPasswordHash()==0)?ENABLE:CHANGE);

                scoreboardPasswordStatusLabel.setText((ci.getScoreboardPasswordHash()==0)?DISABLED:ENABLED);
                scoreboardPasswordButton.setText((ci.getScoreboardPasswordHash()==0)?ENABLE:CHANGE);
            }
        });
    }
    

    @Override
    public void handleTransactionEvents(List<DataEvent> events, Collection<Class> dataClasses) {
        updatePasswordStatus();
    }    
    private void changePassword(PasswordType passwordType) {
        ChangePasswordDialog cpd = new ChangePasswordDialog((Frame)this.getParent(), true);
        switch(passwordType) {
            case MASTER:
                cpd.setTitle("Master Password");
                break;
            case WEIGH_IN:
                cpd.setTitle("Weigh-in Password");
                break;
            case PERSONAL_DETAILS:
                cpd.setTitle("Personal Details Password");
                break;
            case SCOREBOARD:
                cpd.setTitle("Scoreboard Password");
                break;
        }
        cpd.setVisible(true);
        if(cpd.getSuccess()) {
            final CompetitionInfo ci = database.get(CompetitionInfo.class, null);
            switch(passwordType) {
                case MASTER:
                    ci.setPasswordHash(cpd.getPasswordHash());
                    break;
                case WEIGH_IN:
                    ci.setWeighInPasswordHash(cpd.getPasswordHash());
                    break;
                case PERSONAL_DETAILS:
                    ci.setPersonalDetailsPasswordHash(cpd.getPasswordHash());
                    break;
                case SCOREBOARD:
                    ci.setScoreboardPasswordHash(cpd.getPasswordHash());
                    break;
            }
            database.update(ci);
        }    
    }
    
    @Override
    public void dispose() {
        notifier.removeListener(this);
        super.dispose();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        masterPasswordStatusLabel = new javax.swing.JLabel();
        weighInPasswordStatusLabel = new javax.swing.JLabel();
        personalDetailsPasswordStatusLabel = new javax.swing.JLabel();
        scoreboardPasswordStatusLabel = new javax.swing.JLabel();
        masterPasswordButton = new javax.swing.JButton();
        weighInPasswordButton = new javax.swing.JButton();
        personalDetailsPasswordButton = new javax.swing.JButton();
        scoreboardPasswordButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        okButton = new javax.swing.JButton();

        jButton5.setText("OK");

        jButton6.setText("OK");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manage Passwords");
        setLocationByPlatform(true);
        setResizable(false);

        jLabel1.setText("Master Password:");

        jLabel2.setText("Weigh-in Password:");

        jLabel3.setText("Personal Details Password:");

        jLabel4.setText("Scoreboard Password:");

        masterPasswordStatusLabel.setText("-");

        weighInPasswordStatusLabel.setText("-");

        personalDetailsPasswordStatusLabel.setText("-");

        scoreboardPasswordStatusLabel.setText("-");

        masterPasswordButton.setText("Change..");
        masterPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masterPasswordButtonActionPerformed(evt);
            }
        });

        weighInPasswordButton.setText("Change..");
        weighInPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weighInPasswordButtonActionPerformed(evt);
            }
        });

        personalDetailsPasswordButton.setText("Change..");
        personalDetailsPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personalDetailsPasswordButtonActionPerformed(evt);
            }
        });

        scoreboardPasswordButton.setText("Change..");
        scoreboardPasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreboardPasswordButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(masterPasswordStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(masterPasswordButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(weighInPasswordStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(weighInPasswordButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(personalDetailsPasswordStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(personalDetailsPasswordButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scoreboardPasswordStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(scoreboardPasswordButton))))
                    .addComponent(okButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {masterPasswordButton, okButton, personalDetailsPasswordButton, scoreboardPasswordButton, weighInPasswordButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(masterPasswordStatusLabel)
                    .addComponent(masterPasswordButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(weighInPasswordStatusLabel)
                    .addComponent(weighInPasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(personalDetailsPasswordStatusLabel)
                    .addComponent(personalDetailsPasswordButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(scoreboardPasswordStatusLabel)
                    .addComponent(scoreboardPasswordButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scoreboardPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreboardPasswordButtonActionPerformed
        if(!PermissionChecker.isAllowed(Action.CHANGE_SCOREBOARD_PASSWORD, database)) return;
        changePassword(PasswordType.SCOREBOARD);
    }//GEN-LAST:event_scoreboardPasswordButtonActionPerformed

    private void personalDetailsPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personalDetailsPasswordButtonActionPerformed
        if(!PermissionChecker.isAllowed(Action.CHANGE_PERSONAL_DETAILS_PASSWORD, database)) return;
        changePassword(PasswordType.PERSONAL_DETAILS);
    }//GEN-LAST:event_personalDetailsPasswordButtonActionPerformed

    private void weighInPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weighInPasswordButtonActionPerformed
        if(!PermissionChecker.isAllowed(Action.CHANGE_WEIGH_IN_PASSWORD, database)) return;
        changePassword(PasswordType.WEIGH_IN);
    }//GEN-LAST:event_weighInPasswordButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void masterPasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masterPasswordButtonActionPerformed
        if(!PermissionChecker.isAllowed(Action.CHANGE_MASTER_PASSWORD, database)) return;
        changePassword(PasswordType.MASTER);
    }//GEN-LAST:event_masterPasswordButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton masterPasswordButton;
    private javax.swing.JLabel masterPasswordStatusLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton personalDetailsPasswordButton;
    private javax.swing.JLabel personalDetailsPasswordStatusLabel;
    private javax.swing.JButton scoreboardPasswordButton;
    private javax.swing.JLabel scoreboardPasswordStatusLabel;
    private javax.swing.JButton weighInPasswordButton;
    private javax.swing.JLabel weighInPasswordStatusLabel;
    // End of variables declaration//GEN-END:variables
    
}
