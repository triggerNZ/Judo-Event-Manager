/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SeedingPanel.java
 *
 * Created on 14/09/2010, 8:25:15 PM
 */

package au.com.jwatmuff.eventmanager.gui.wizard;

import au.com.jwatmuff.eventmanager.gui.wizard.DrawWizardWindow.Context;
import au.com.jwatmuff.eventmanager.model.draw.ConfigurationFile;
import au.com.jwatmuff.eventmanager.model.info.PlayerPoolInfo;
import au.com.jwatmuff.eventmanager.model.misc.CSVImporter;
import au.com.jwatmuff.eventmanager.model.misc.PoolDraw;
import au.com.jwatmuff.eventmanager.model.misc.PoolPlayerSequencer;
import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.eventmanager.model.vo.PlayerPool;
import au.com.jwatmuff.eventmanager.model.vo.Pool;
import au.com.jwatmuff.eventmanager.util.GUIUtils;
import au.com.jwatmuff.genericdb.distributed.DataEvent;
import au.com.jwatmuff.genericdb.transaction.TransactionListener;
import au.com.jwatmuff.genericdb.transaction.TransactionNotifier;
import au.com.jwatmuff.genericdb.transaction.TransactionalDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;

/**
 *
 * @author James
 */
public class SeedingPanel extends javax.swing.JPanel implements DrawWizardWindow.Panel {
    private static final Logger log = Logger.getLogger(SeedingPanel.class);

    private DefaultTableModel model;
    private TransactionalDatabase database;
    private TransactionNotifier notifier;
    private TransactionListener listener;
    private Pool pool;
    private List<PlayerPoolInfo> playerPoolInfoList = new ArrayList<PlayerPoolInfo>();
    private Map<Integer, Integer> seeds = new HashMap<Integer, Integer>();
    private Context context;

    private static final Comparator<PlayerPoolInfo> PLAYERS_COMPARATOR_POSITION = new Comparator<PlayerPoolInfo>() {
        @Override
        public int compare(PlayerPoolInfo pp1, PlayerPoolInfo pp2) {
            String n1 = pp1.getPlayer().getLastName() + pp1.getPlayer().getFirstName();
            String n2 = pp2.getPlayer().getLastName() + pp2.getPlayer().getFirstName();
            return n1.compareTo(n2);
        }
    };

    /** Creates new form SeedingPanel */
    public SeedingPanel(TransactionalDatabase database, TransactionNotifier notifier, Context context) {
        this.database = database;
        this.notifier = notifier;
        this.context = context;

        initComponents();

        model = new DefaultTableModel();
        model.addColumn("Player");
        model.addColumn("Team");
        model.addColumn("Seed");

        model.setColumnIdentifiers(new Object[] { "Player", "Team", "Seed" });

        seedingTable.setModel(model);

        listener = new TransactionListener() {
            @Override
            public void handleTransactionEvents(List<DataEvent> events, Collection<Class> dataClasses) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateFromDatabase();
                    }
                });
            }
        };
    }

    private TableCellEditor getSeedingCellEditor(int numPlayers) {
        Object[] values =  new Object[numPlayers + 1];
        values[0] = "None";
        for(int i = 1; i <= numPlayers; i++) values[i] = "" + i;

        return new DefaultCellEditor(new JComboBox<Object>(values));
    }

    private void updateFromDatabase() {
        playerPoolInfoList = new ArrayList<PlayerPoolInfo>();
        // filter out the null entries
        for(PlayerPoolInfo player : PoolPlayerSequencer.getPlayerSequence(database, pool.getID()))
            if(player != null)
                playerPoolInfoList.add(player);

        Collections.sort(playerPoolInfoList, PLAYERS_COMPARATOR_POSITION);

        seedingTable.getColumn("Seed").setCellEditor(getSeedingCellEditor(playerPoolInfoList.size()));

        // clear table
        while(model.getRowCount() > 0) model.removeRow(0);

        for(PlayerPoolInfo player : playerPoolInfoList) {
            model.addRow(getRowData(player));
        }
    }

    private Object[] getRowData(PlayerPoolInfo player) {
        int playerID = player.getPlayer().getID();
        return new Object[] {
            player.getPlayer().getLastName() + ", " + player.getPlayer().getFirstName(),
            player.getPlayer().getTeam(),
            seeds.get(playerID) == null ? "None" : "" + seeds.get(playerID)
        };
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        seedingTable = new javax.swing.JTable();
        divisionNameLabel = new javax.swing.JLabel();

        seedingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        seedingTable.setGridColor(new java.awt.Color(237, 237, 237));
        seedingTable.setRowHeight(19);
        jScrollPane1.setViewportView(seedingTable);

        divisionNameLabel.setFont(new java.awt.Font("Tahoma", 1, 24));
        divisionNameLabel.setText("Division Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addComponent(divisionNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(divisionNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel divisionNameLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable seedingTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean nextButtonPressed() {
        // TODO: this shouldn't be done here, the seeds map should be updated
        // every time the user changes a seed, otherwise all seeds will be lost
        // if there is a database update.
        // populate seeds map:
        int index = 0;
        for(PlayerPoolInfo player : playerPoolInfoList) {
            if(player != null) {
                String seed = (String)seedingTable.getModel().getValueAt(index, 2);
                try {
                    seeds.put(player.getPlayer().getID(), Integer.parseInt(seed));
                } catch(NumberFormatException e) {
                    // do nothing, just means no seed specified
                }
                index++;
            }
        }
        // end TODO

        CompetitionInfo ci = database.get(CompetitionInfo.class, null);
        ConfigurationFile configurationFile = ConfigurationFile.getConfiguration(ci.getDrawConfiguration());
        if(configurationFile == null) {
            GUIUtils.displayError(this, "Unable to load draw configuration.");
            return false;
        }

        String drawName = configurationFile.getDrawName(playerPoolInfoList.size());
        if(drawName == null) {
            GUIUtils.displayError(this, "The current draw configuration does not support divisions with " + playerPoolInfoList.size() + " players");
            return false;
        }

        File csvFile = new File("resources/draw/" + drawName + ".csv");

        try {
            CSVImporter.importFightDraw(csvFile, database, pool, playerPoolInfoList.size());
        } catch(Exception e) {
            GUIUtils.displayError(this, "Failed to import fight draw (" + drawName + ")");
            log.error("Error importing fight draw", e);
        }


        PoolDraw poolDraw = PoolDraw.getInstance( database, pool.getID(), seeds);
        List<PlayerPoolInfo> orderedPlayers = poolDraw.getOrderedPlayers();

        PoolPlayerSequencer.savePlayerSequence(database, pool.getID(), orderedPlayers);

        return true;
    }

    @Override
    public boolean closedButtonPressed() {
        return true;
    }

    @Override
    public void beforeShow() {
        pool = context.pool;
        divisionNameLabel.setText(pool.getDescription() + ": Seeding");
        updateFromDatabase();
        notifier.addListener(listener, Pool.class, PlayerPool.class);
    }

    @Override
    public void afterHide() {
        notifier.removeListener(listener);
    }
}
