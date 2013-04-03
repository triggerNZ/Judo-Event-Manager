/*
 * LoadWindow.java
 *
 * Created on 11 October 2008, 02:22
 */

package au.com.jwatmuff.eventmanager.gui.main;

import javax.swing.DefaultListModel;

/**
 *
 * @author  James
 */
public class LoadWindow extends javax.swing.JFrame {
    DefaultListModel<String> model = new DefaultListModel<String>();
    
    /** Creates new form LoadWindow */
    public LoadWindow() {
        initComponents();
        messageList.setModel(model);
        // center window on screen
        setLocationRelativeTo(null);
        setIconImage(Icons.MAIN_WINDOW.getImage());
    }
    
    public void addMessage(String message) {
        model.addElement(message);
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
        messageList = new javax.swing.JList<String>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Loading EventManager");
        setLocationByPlatform(true);

        jScrollPane1.setViewportView(messageList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> messageList;
    // End of variables declaration//GEN-END:variables

}
