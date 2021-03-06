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

package au.com.jwatmuff.eventmanager.p2p.chat;

/**
 *
 * @author James
 */
public class ChatServiceImpl implements ChatService {
    private ChatListener listener;

    /** Creates a new instance of ChatServiceImpl */
    public ChatServiceImpl() {
    }
    
    public void setLocalChatListener(ChatListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void sendMessage(String message, String fromPeer) {
        if(listener != null)
            listener.handleMessage(message, fromPeer);
    }
}
