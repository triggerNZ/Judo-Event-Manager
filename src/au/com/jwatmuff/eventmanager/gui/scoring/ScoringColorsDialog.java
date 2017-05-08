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

package au.com.jwatmuff.eventmanager.gui.scoring;

import au.com.jwatmuff.eventmanager.gui.scoring.ScoringColors.Area;

/**
 *
 * @author  James
 */
public class ScoringColorsDialog extends javax.swing.JDialog {
    private ScoringColors colors = new ScoringColors();

    /** Creates new form ScoringColorsDialog */
    public ScoringColorsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setLocationRelativeTo(parent);
        initComponents();
        updateGUI();
    }
    
    public void setColors(ScoringColors colors) {
        this.colors = colors;
        updateGUI();
    }
    
    public ScoringColors getColors() {
        return colors;
    }
    
    private void updateGUI() {
        idleBg.setColor(colors.getColor(Area.IDLE_BACKGROUND));
        idleFg.setColor(colors.getColor(Area.IDLE_FOREGROUND));
        fightingBg.setColor(colors.getColor(Area.FIGHTING_BACKGROUND));
        fightingFg.setColor(colors.getColor(Area.FIGHTING_FOREGROUND));
        holddownBg.setColor(colors.getColor(Area.HOLDDOWN_BACKGROUND));
        holddownFg.setColor(colors.getColor(Area.HOLDDOWN_FOREGROUND));
        if(jToggleButton1.isSelected()) {
            player1Bg.setColor(colors.getColor(Area.PLAYER2_BACKGROUND));
            player1Fg.setColor(colors.getColor(Area.PLAYER2_FOREGROUND));
            player2Bg.setColor(colors.getColor(Area.PLAYER1_BACKGROUND));
            player2Fg.setColor(colors.getColor(Area.PLAYER1_FOREGROUND));
        }else {
            player1Bg.setColor(colors.getColor(Area.PLAYER1_BACKGROUND));
            player1Fg.setColor(colors.getColor(Area.PLAYER1_FOREGROUND));
            player2Bg.setColor(colors.getColor(Area.PLAYER2_BACKGROUND));
            player2Fg.setColor(colors.getColor(Area.PLAYER2_FOREGROUND));
        }
    }
    
    private void updateScoringColors() {
        colors.setColor(Area.IDLE_BACKGROUND, idleBg.getColor());
        colors.setColor(Area.IDLE_FOREGROUND, idleFg.getColor());
        colors.setColor(Area.FIGHTING_BACKGROUND, fightingBg.getColor());
        colors.setColor(Area.FIGHTING_FOREGROUND, fightingFg.getColor());
        colors.setColor(Area.HOLDDOWN_BACKGROUND, holddownBg.getColor());
        colors.setColor(Area.HOLDDOWN_FOREGROUND, holddownFg.getColor());
        colors.setColor(Area.PLAYER1_BACKGROUND, player1Bg.getColor());
        colors.setColor(Area.PLAYER1_FOREGROUND, player1Fg.getColor());
        colors.setColor(Area.PLAYER2_BACKGROUND, player2Bg.getColor());
        colors.setColor(Area.PLAYER2_FOREGROUND, player2Fg.getColor());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idleFg = new net.java.dev.colorchooser.ColorChooser();
        jLabel1 = new javax.swing.JLabel();
        idleBg = new net.java.dev.colorchooser.ColorChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fightingFg = new net.java.dev.colorchooser.ColorChooser();
        fightingBg = new net.java.dev.colorchooser.ColorChooser();
        jLabel5 = new javax.swing.JLabel();
        holddownFg = new net.java.dev.colorchooser.ColorChooser();
        holddownBg = new net.java.dev.colorchooser.ColorChooser();
        jLabel6 = new javax.swing.JLabel();
        player1Fg = new net.java.dev.colorchooser.ColorChooser();
        player1Bg = new net.java.dev.colorchooser.ColorChooser();
        jLabel7 = new javax.swing.JLabel();
        player2Fg = new net.java.dev.colorchooser.ColorChooser();
        player2Bg = new net.java.dev.colorchooser.ColorChooser();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        defaultButton = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Choose Colors");

        javax.swing.GroupLayout idleFgLayout = new javax.swing.GroupLayout(idleFg);
        idleFg.setLayout(idleFgLayout);
        idleFgLayout.setHorizontalGroup(
            idleFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        idleFgLayout.setVerticalGroup(
            idleFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel1.setText("Main Area (Idle)");

        javax.swing.GroupLayout idleBgLayout = new javax.swing.GroupLayout(idleBg);
        idleBg.setLayout(idleBgLayout);
        idleBgLayout.setHorizontalGroup(
            idleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        idleBgLayout.setVerticalGroup(
            idleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel2.setText("Foreground");

        jLabel3.setText("Background");

        jLabel4.setText("Main Area (Fighting)");

        javax.swing.GroupLayout fightingFgLayout = new javax.swing.GroupLayout(fightingFg);
        fightingFg.setLayout(fightingFgLayout);
        fightingFgLayout.setHorizontalGroup(
            fightingFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        fightingFgLayout.setVerticalGroup(
            fightingFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout fightingBgLayout = new javax.swing.GroupLayout(fightingBg);
        fightingBg.setLayout(fightingBgLayout);
        fightingBgLayout.setHorizontalGroup(
            fightingBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        fightingBgLayout.setVerticalGroup(
            fightingBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel5.setText("Main Area (Hold Down)");

        javax.swing.GroupLayout holddownFgLayout = new javax.swing.GroupLayout(holddownFg);
        holddownFg.setLayout(holddownFgLayout);
        holddownFgLayout.setHorizontalGroup(
            holddownFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        holddownFgLayout.setVerticalGroup(
            holddownFgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout holddownBgLayout = new javax.swing.GroupLayout(holddownBg);
        holddownBg.setLayout(holddownBgLayout);
        holddownBgLayout.setHorizontalGroup(
            holddownBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        holddownBgLayout.setVerticalGroup(
            holddownBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel6.setText("Player 1");

        javax.swing.GroupLayout player1FgLayout = new javax.swing.GroupLayout(player1Fg);
        player1Fg.setLayout(player1FgLayout);
        player1FgLayout.setHorizontalGroup(
            player1FgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        player1FgLayout.setVerticalGroup(
            player1FgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout player1BgLayout = new javax.swing.GroupLayout(player1Bg);
        player1Bg.setLayout(player1BgLayout);
        player1BgLayout.setHorizontalGroup(
            player1BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        player1BgLayout.setVerticalGroup(
            player1BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel7.setText("Player 2");

        javax.swing.GroupLayout player2FgLayout = new javax.swing.GroupLayout(player2Fg);
        player2Fg.setLayout(player2FgLayout);
        player2FgLayout.setHorizontalGroup(
            player2FgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        player2FgLayout.setVerticalGroup(
            player2FgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout player2BgLayout = new javax.swing.GroupLayout(player2Bg);
        player2Bg.setLayout(player2BgLayout);
        player2BgLayout.setHorizontalGroup(
            player2BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        player2BgLayout.setVerticalGroup(
            player2BgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        defaultButton.setText("Default");
        defaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Swap Player Colors");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(idleFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fightingFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(holddownFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(player1Fg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(player2Fg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(idleBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fightingBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(holddownBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(player1Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(player2Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(defaultButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel4)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel6)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel7))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(14, 14, 14)
                            .addComponent(idleBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fightingBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(holddownBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(player1Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(player2Bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(14, 14, 14)
                            .addComponent(idleFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fightingFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(holddownFg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(player1Fg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(player2Fg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(defaultButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    updateScoringColors();
    this.dispose();
}//GEN-LAST:event_okButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultButtonActionPerformed
    setColors(new ScoringColors());
}//GEN-LAST:event_defaultButtonActionPerformed

private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
    // TODO add your handling code here:
    updateGUI();
}//GEN-LAST:event_jToggleButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton defaultButton;
    private net.java.dev.colorchooser.ColorChooser fightingBg;
    private net.java.dev.colorchooser.ColorChooser fightingFg;
    private net.java.dev.colorchooser.ColorChooser holddownBg;
    private net.java.dev.colorchooser.ColorChooser holddownFg;
    private net.java.dev.colorchooser.ColorChooser idleBg;
    private net.java.dev.colorchooser.ColorChooser idleFg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton okButton;
    private net.java.dev.colorchooser.ColorChooser player1Bg;
    private net.java.dev.colorchooser.ColorChooser player1Fg;
    private net.java.dev.colorchooser.ColorChooser player2Bg;
    private net.java.dev.colorchooser.ColorChooser player2Fg;
    // End of variables declaration//GEN-END:variables

}
