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

package au.com.jwatmuff.eventmanager.gui.scoreboard.layout;

import au.com.jwatmuff.eventmanager.gui.scoreboard.ScoreboardModel;
import au.com.jwatmuff.eventmanager.gui.scoreboard.ScoreboardModel.Score;
import au.com.jwatmuff.eventmanager.util.gui.ScalableAbsoluteLayout.Point;
import au.com.jwatmuff.eventmanager.util.gui.ScalableAbsoluteLayout.Rect;

/**
 *
 * @author james
 */
public class IJFScoreboardLayout implements ScoreboardLayout {
    @Override
    public Rect getScoreRect(int player, Score score) {
        double y = 0.75 + player * 3.5;

        switch(score) {
            case IPPON: return new Rect(5, y, 3, 4);
            case WAZARI: return new Rect(8, y, 3, 4);
            default: return null;
        }
    }

    @Override
    public Rect getScoreLabelRect(int player, Score score) {
        return null; // No labels
    }

    @Override
    public Rect getPlayerLabelRect(int player) {
        return player == 0 ?
                new Rect(0.25, 0, 16, 1.5) :
                new Rect(0.25, 7.5, 16, 1.5);
    }

    @Override
    public Rect getTeamLabelRect(int player) {
        return player == 0 ?
                new Rect(0.25, 1.5, 5.25, 1) :
                new Rect(0.25, 6.5, 5.25, 1);
    }
    
    @Override
    public Rect getHolddownScoreRect(int player, int index) {
        Point position = (player == 0) ?
                new Point(11, 0.25) :
                new Point(11, 4.75);

        Rect rect = new Rect(0, index * 1.35, 1.25, 1.25);
        return rect.offsetBy(position);
    }

    @Override
    public Rect getShidoRect(int player, int shido, int globalIndex, Score shidoType, ScoreboardModel model) {
        int size = 1;
        double sp = 1.5 / 4; // spacing
        
        double y;
        switch(shidoType) {
            default:
            case SHIDO: y = sp; break;
            case LEG_SHIDO: y = sp * 2 + size; break;
            case HANSAKUMAKE: y = sp * 3 + size * 2; break;
        }
        
        double x = 16 - (shido + 1) * (size + sp);
        
        y = (player == 0) ? 4.5 - y - size : 4.5 + y; // Mirror layout around line between P1 & P2
        
        return new Rect(x, y, size, size);
    }

    @Override
    public Rect getTimerRect() {
        return new Rect(4, 8.5, 8, 4);
    }

    @Override
    public Rect getHolddownRect() {
        return new Rect(11.5, 9, 4, 3);
    }

    @Override
    public Rect getDivisionRect() {
        return new Rect(0, 10.5, 4, 1.5);
    }

    @Override
    public Rect getPlayerBackgroundRect(int player) {
        return new Rect(0, player * 4.5, 16, 4.5);
    }

    @Override
    public Rect getTimerBackgroundRect() {
        return new Rect(0, 9, 16, 4);
    }
    
    @Override
    public Rect getGoldenScoreRect() {
        return new Rect(0, 9, 4, 1.5);
    }

    @Override
    public Rect getGoldenScoreApproveRect() {
        return getGoldenScoreRect();
    }

    @Override
    public Rect getResultRect() {
        return getHolddownRect();
    }
}
