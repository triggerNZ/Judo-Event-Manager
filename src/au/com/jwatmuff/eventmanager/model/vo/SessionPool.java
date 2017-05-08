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

package au.com.jwatmuff.eventmanager.model.vo;

import au.com.jwatmuff.genericdb.distributed.DistributableObject;
import java.io.Serializable;

/**
 *
 * @author James
 */
public class SessionPool extends DistributableObject<SessionPool.Key> {
    // immutable
    public static class Key implements Serializable {
        public final int sessionID;
        public final int poolID;
        
        public Key(int sessionID, int poolID) {
            this.sessionID = sessionID;
            this.poolID = poolID;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (this.sessionID != other.sessionID) {
                return false;
            }
            if (this.poolID != other.poolID) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + this.sessionID;
            hash = 59 * hash + this.poolID;
            return hash;
        }
    }
    
    /** Creates a new instance of SessionPool */
    public SessionPool() {
        setID(new Key(0,0));
    }
    
    public int getSessionID() {
        return getID().sessionID;
    }
    
    public int getPoolID() {
        return getID().poolID;
    }
}
