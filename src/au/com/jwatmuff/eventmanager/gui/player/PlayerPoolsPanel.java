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

import au.com.jwatmuff.eventmanager.db.FightDAO;
import au.com.jwatmuff.eventmanager.db.PoolDAO;
import au.com.jwatmuff.eventmanager.db.ResultDAO;
import au.com.jwatmuff.eventmanager.db.SessionDAO;
import au.com.jwatmuff.eventmanager.gui.divisions.DivisionsDetailsDialog;
import au.com.jwatmuff.eventmanager.model.info.PlayerPoolInfo;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser.FightPlayer;
import au.com.jwatmuff.eventmanager.model.vo.Fight;
import au.com.jwatmuff.eventmanager.model.vo.PlayerPool;
import au.com.jwatmuff.eventmanager.model.vo.Pool;
import au.com.jwatmuff.eventmanager.model.vo.Result;
import au.com.jwatmuff.eventmanager.model.vo.Session;
import au.com.jwatmuff.eventmanager.model.vo.SessionLink;
import au.com.jwatmuff.eventmanager.util.BeanMapper;
import au.com.jwatmuff.eventmanager.util.BeanMapperTableModel;
import au.com.jwatmuff.eventmanager.util.GUIUtils;
import au.com.jwatmuff.genericdb.distributed.DataEvent;
import au.com.jwatmuff.genericdb.transaction.TransactionListener;
import au.com.jwatmuff.genericdb.transaction.TransactionNotifier;
import au.com.jwatmuff.genericdb.transaction.TransactionalDatabase;
import java.awt.Component;
import java.awt.Frame;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.apache.log4j.Logger;

/**
 *
 * @author  James
 */
public class PlayerPoolsPanel extends javax.swing.JPanel implements TransactionListener {
    private static final Logger log = Logger.getLogger(PlayerPoolsPanel.class);

    private TransactionalDatabase database;
    private int playerID;
    
    private DefaultListModel<PlayerPoolInfo> poolListModel;
    private DefaultComboBoxModel<Pool> poolComboBoxModel;

    private Frame parent;

    private FightTableModel fightTableModel;

    private ListCellRenderer<Object> poolRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object obj, int arg2, boolean arg3, boolean arg4) {
            if(obj instanceof Pool)
                obj = ((Pool)obj).getDescription();
            return super.getListCellRendererComponent(list, obj, arg2, arg3, arg4);
        }
    };
    
    private ListCellRenderer<Object> ppiRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object obj, int arg2, boolean arg3, boolean arg4) {
            if(obj instanceof PlayerPoolInfo) {
                PlayerPoolInfo ppi = (PlayerPoolInfo)obj;
                obj = ppi.getPool().getDescription();
                if(ppi.getPlayerPool().getSeed() > 0) {
                    obj = "[" + ppi.getPlayerPool().getSeed() + "] " + obj;
                }
            }
            return super.getListCellRendererComponent(list, obj, arg2, arg3, arg4);
        }
    };
    
    /** Creates new form PlayerPoolsPanel */
    public PlayerPoolsPanel(Frame parent, TransactionalDatabase database, TransactionNotifier notifier) {
        initComponents();
        this.parent = parent;
        this.database = database;
        
        notifier.addListener(this, PlayerPool.class);
        
        poolComboBoxModel = new DefaultComboBoxModel<Pool>();
        divisionsComboBox.setModel(poolComboBoxModel);
        divisionsComboBox.setRenderer(poolRenderer);
        
        poolListModel = new DefaultListModel<PlayerPoolInfo>();
        divisionsList.setModel(poolListModel);
        divisionsList.setCellRenderer(ppiRenderer);

        fightTableModel = new FightTableModel(notifier);
        fightTable.setModel(fightTableModel);
        
        updatePoolComboBox();
        
        //updatePoolList();
    }
    
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
        
        updatePoolList();
    }
    
    private void updatePoolComboBox() {
        poolComboBoxModel.removeAllElements();

        Collection<Pool> pools = database.findAll(Pool.class, PoolDAO.ALL);
        for(Pool pool : pools)
            poolComboBoxModel.addElement(pool);
    }
    
    private void updatePoolList() {
        poolListModel.clear();
        

        ArrayList<PlayerPoolInfo> playerPoolsList = new ArrayList<PlayerPoolInfo>(PlayerPoolInfo.getForPlayer(database, playerID));
        Collections.sort(playerPoolsList, new Comparator<PlayerPoolInfo>() {
            public int compare(PlayerPoolInfo p1, PlayerPoolInfo p2) {
                return p1.getPool().getDescription().compareTo(p2.getPool().getDescription());
            }
        });


        for(PlayerPoolInfo ppi : playerPoolsList) {
            poolListModel.addElement(ppi);
        }
        
        removeDivisionButton.setEnabled(playerPoolsList.size() > 0);
    }

    @Override
    public void handleTransactionEvents(List<DataEvent> events, Collection<Class> dataClasses) {
        updatePoolList();
    }

    private Pool getSelectedPool() {
        PlayerPoolInfo ppi = (PlayerPoolInfo)divisionsList.getSelectedValue();
        return (ppi != null) ? ppi.getPool() : null;
    }

    private class FightTableModel extends BeanMapperTableModel<Fight> implements TransactionListener {
        private PlayerCodeParser playerCodeParser;

        public FightTableModel(TransactionNotifier notifier) {
            notifier.addListener(this, Fight.class);

            setBeanMapper(new BeanMapper<Fight>() {
                @Override
                public Map<String, Object> mapBean(Fight fight) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("position", fight.getPosition());

                    for(int i = 0; i < 2; i++) {
                        String code = fight.getPlayerCodes()[i];
                        FightPlayer fp = playerCodeParser.parseCode(code);
                        map.put("player" + (i+1), code + ": " + fp.toString());
                    }

                    Session mat = database.find(Session.class, SessionDAO.FOR_FIGHT, fight.getID());
                    while(mat != null && mat.getMat() == null) {
                        Collection<Session> sessions = database.findAll(Session.class, SessionDAO.PRECEDING, mat.getID(), SessionLink.LinkType.MAT);
                        if(sessions.size() > 0)
                            mat = sessions.iterator().next();
                        else
                            mat = null;
                    }
                    map.put("mat", (mat != null) ? mat.getMat() : "");

                    List<Result> results = database.findAll(Result.class, ResultDAO.FOR_FIGHT, fight.getID());
                    if(!results.isEmpty()) {
                        Result result = results.iterator().next();
                        map.put("result", result.getScores()[0] + " --- " + result.getScores()[1]);
                    } else {
                        map.put("result", "");
                    }
                    

                    return map;
                }
            });
            addColumn("Sequence #", "position");
            addColumn("Player 1", "player1");
            addColumn("Player 2", "player2");
            addColumn("Mat", "mat");
            addColumn("Result", "result");
        }

        public void updateFromDatabase() {
            Pool pool = getSelectedPool();
            if(pool != null) {
                playerCodeParser = PlayerCodeParser.getInstance(database, pool.getID());
                Collection<Fight> fights = database.findAll(Fight.class, FightDAO.FOR_POOL, pool.getID());
                setBeans(fights);
            }
            else {
                Collection<Fight> fights = Collections.emptyList();
                setBeans(fights);
            }
        }

        @Override
        public void handleTransactionEvents(List<DataEvent> events, Collection<Class> dataClasses) {
            updateFromDatabase();
        }
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        removeDivisionButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        divisionsList = new javax.swing.JList<PlayerPoolInfo>();
        divisionsComboBox = new javax.swing.JComboBox<Pool>();
        addDivisionButton = new javax.swing.JButton();
        newDivisionButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        fightTable = new javax.swing.JTable();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Requested Divisions"));

        removeDivisionButton.setText("Remove");
        removeDivisionButton.setEnabled(false);
        removeDivisionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDivisionButtonActionPerformed(evt);
            }
        });

        divisionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divisionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                divisionsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(divisionsList);

        addDivisionButton.setText("Add");
        addDivisionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDivisionButtonActionPerformed(evt);
            }
        });

        newDivisionButton.setText("New Division..");
        newDivisionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDivisionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeDivisionButton, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(addDivisionButton, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(newDivisionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(divisionsComboBox, 0, 267, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(removeDivisionButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(addDivisionButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(divisionsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newDivisionButton)))
        );

        jScrollPane2.setViewportView(fightTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addDivisionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDivisionButtonActionPerformed
        Pool pool = ((Pool)divisionsComboBox.getSelectedItem());
        if(pool.getLockedStatus() != Pool.LockedStatus.UNLOCKED) {
            GUIUtils.displayMessage(
                    parent,
                    "This division (" + pool.getDescription() + ") has been locked.\n" +
                    "No more players may be entered into a locked division.",
                    "Division Locked");
            return;
        }
        PlayerPool pp = database.get(PlayerPool.class, new PlayerPool.Key(playerID, pool.getID()));
        if(pp == null || !pp.isValid()) {
            pp = new PlayerPool();
            pp.setPlayerID(playerID);
            pp.setPoolID(pool.getID());
            pp.setApproved(false);
            database.add(pp);
        }
    }//GEN-LAST:event_addDivisionButtonActionPerformed
    
    private void removeDivisionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDivisionButtonActionPerformed
        PlayerPoolInfo ppi = ((PlayerPoolInfo)divisionsList.getSelectedValue());
        if(ppi.getPool().getLockedStatus() != Pool.LockedStatus.UNLOCKED) {
            GUIUtils.displayMessage(
                    parent,
                    "This division (" + ppi.getPool().getDescription() + ") has been locked.\n" +
                    "Players may not be removed from a locked division.",
                    "Division Locked");
            return;
        }
        PlayerPool pp = ppi.getPlayerPool();
        database.delete(pp);
    }//GEN-LAST:event_removeDivisionButtonActionPerformed

private void newDivisionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newDivisionButtonActionPerformed
        DivisionsDetailsDialog pdd = new DivisionsDetailsDialog(parent, true, database, null);
        pdd.setVisible(true);
        updatePoolComboBox();
}//GEN-LAST:event_newDivisionButtonActionPerformed

private void divisionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_divisionsListValueChanged
    fightTableModel.updateFromDatabase();
}//GEN-LAST:event_divisionsListValueChanged
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDivisionButton;
    private javax.swing.JComboBox<Pool> divisionsComboBox;
    private javax.swing.JList<PlayerPoolInfo> divisionsList;
    private javax.swing.JTable fightTable;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton newDivisionButton;
    private javax.swing.JButton removeDivisionButton;
    // End of variables declaration//GEN-END:variables
}
