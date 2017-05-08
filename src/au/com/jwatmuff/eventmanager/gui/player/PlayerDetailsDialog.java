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

package au.com.jwatmuff.eventmanager.gui.player;

import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.eventmanager.model.vo.Player;
import au.com.jwatmuff.eventmanager.model.vo.PlayerDetails;
import au.com.jwatmuff.eventmanager.permissions.Action;
import au.com.jwatmuff.eventmanager.permissions.PermissionChecker;
import au.com.jwatmuff.genericdb.transaction.TransactionNotifier;
import au.com.jwatmuff.genericdb.transaction.TransactionalDatabase;
import java.awt.Frame;
import java.sql.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author  James
 */
public class PlayerDetailsDialog extends javax.swing.JDialog {
    private static final Logger log = Logger.getLogger(PlayerDetailsDialog.class);

    private TransactionalDatabase database;
    private TransactionNotifier notifier;
    private Player player;
    private PlayerDetails details;
    
    private final static String PERSONAL_DETAILS = "Personal Details";
    private final static String PLAYER_POOLS = "Divisions";
    private final static String MEDICAL_DETAILS = "Medical Details";
    private final static String CONTACT_DETAILS = "Contact Details";
    
    private PersonalDetailsPanel personalDetailsPanel;
    private PlayerPoolsPanel playerPoolsPanel;
    private ContactDetailsPanel contactDetailsPanel;
    private MedicalDetailsPanel medicalDetailsPanel;
    
    private Frame parent;
    
    /** Creates new form PlayerDetailsDialog */
    public PlayerDetailsDialog(Frame parent, boolean modal, TransactionalDatabase database, TransactionNotifier notifier, Player player) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.database = database;
        this.notifier = notifier;

        Date censusDate = null;
        try {
            censusDate = new Date(database.get(CompetitionInfo.class, null).getAgeThresholdDate().getTime());
        } catch(Exception e) {}

        personalDetailsPanel = new PersonalDetailsPanel(censusDate);
        playerPoolsPanel = new PlayerPoolsPanel(parent, database, notifier);
        contactDetailsPanel = new ContactDetailsPanel();
        medicalDetailsPanel = new MedicalDetailsPanel();
        
        this.tabbedPane.add(PERSONAL_DETAILS, personalDetailsPanel);
        this.tabbedPane.add(PLAYER_POOLS, playerPoolsPanel);
        this.tabbedPane.add(CONTACT_DETAILS, contactDetailsPanel);
        this.tabbedPane.add(MEDICAL_DETAILS, medicalDetailsPanel);
        
        pack();
        
        if(player != null)
            existingPlayerMode(player);
        else
            newPlayerMode();
    }
    
    private void newPlayerMode() {
        this.tabbedPane.setEnabledAt(1, false);
        this.tabbedPane.setEnabledAt(2, false);
        this.tabbedPane.setEnabledAt(3, false);
        
        this.addPlayerButton.setEnabled(true);
        this.okButton.setEnabled(false);
    }
    
    private void existingPlayerMode(Player player) {
        this.player = player;
        details = database.get(PlayerDetails.class, player.getDetailsID());
        
        /* create new details entry if one does not exist for this player */
        if(details == null) {
            details = new PlayerDetails();
            player.setDetailsID(details.getID());
            database.add(details);
            database.update(player);
        }
        
        personalDetailsPanel.updateFromPlayer(player);
        personalDetailsPanel.disableIDField();
        playerPoolsPanel.setPlayerID(player.getID());
        notifier.addListener(playerPoolsPanel);

        //log.debug("Updating contact details panel. Club is " + details.getClub() + ", detailsID = " + details.getID() + ", " + player.getDetailsID());

        contactDetailsPanel.updateFromPlayerDetails(details);
        medicalDetailsPanel.updateFromPlayerDetails(details);        

        this.tabbedPane.setEnabledAt(1, true);
        this.tabbedPane.setEnabledAt(2, true);
        this.tabbedPane.setEnabledAt(3, true);

        this.addPlayerButton.setEnabled(false);
        this.okButton.setEnabled(true);

        setTitle(getTitle() + " - " + player.getFirstName() + " " + player.getLastName());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        addPlayerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Player Details");
        setLocationByPlatform(true);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        addPlayerButton.setText("Add Player");
        addPlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(addPlayerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(addPlayerButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlayerButtonActionPerformed
        player = new Player();
        details = new PlayerDetails();
        player.setDetailsID(details.getID());

        if(personalDetailsPanel.validateInput()) {
            personalDetailsPanel.populate(player);
            database.add(details);
            database.add(player);
            existingPlayerMode(player);
        }
    }//GEN-LAST:event_addPlayerButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        boolean editable = (player.getLockedStatus() != Player.LockedStatus.LOCKED);
        if(!editable && !PermissionChecker.isAllowed(Action.EDIT_PLAYER, database)) return;
        if(!personalDetailsPanel.validateInput()) return;
        if(!contactDetailsPanel.validateInput()) return;
        if(!medicalDetailsPanel.validateInput()) return;
        
        personalDetailsPanel.populate(player);
        contactDetailsPanel.populate(details);
        medicalDetailsPanel.populate(details);

        //log.debug("New player club is " + details.getClub() + ", details ID = " + details.getID() + ", " + player.getDetailsID());
        
        database.update(player);
        database.update(details);
        
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPlayerButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
    
}
