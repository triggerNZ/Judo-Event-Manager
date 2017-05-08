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

package au.com.jwatmuff.genericdb.p2p;

import java.util.UUID;

/**
 *
 * @author James
 */
public interface DatabaseInfoService {

    /**
     * Gets information about the database
     * 
     * @return Database information
     */
    DatabaseInfo getDatabaseInfo();

    /**
     * Checks a prefix-hash pair to see if it is valid for the database.
     *
     * Important note: This does not actually give a peer any
     * access to the database, since if it did a listening third party could
     * simply take this pair and use it to authenticate themselves. It is just
     * for convenience so that a peer can be confident it has a correct
     * password for subsequent access to the database (which will require proper
     * authentication.)
     *
     * @param prefix
     * @param hash
     * @return true if the prefix-hash pair are valid, false otherwise
     */
    boolean checkAuthentication(byte[] prefix, byte[] hash);

    /**
     * Called by another peer to inform us that their advertised database
     * information has changed.
     *
     * @param peerID
     * @param database
     */
    void handleDatabaseAnnouncement(UUID peerID, DatabaseInfo database);
}
