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

package au.com.jwatmuff.genericp2p;

import au.com.jwatmuff.genericp2p.jmdns.JmDNSDiscoveryService;
import au.com.jwatmuff.genericp2p.jmdns.JmDNSRegistrationService;
import au.com.jwatmuff.genericp2p.rmi.RMIPeerManager;
import au.com.jwatmuff.genericp2p.windows.WindowsNetDiscoveryService;
import java.io.File;
import java.util.Collection;
import java.util.UUID;
import org.apache.log4j.Logger;

/**
 * Convenience class to instantiate and configure an RMI based PeerManager
 * using JmDNS to discover peers on the network.
 * 
 * @author James
 */
public class JmDNSRMIPeerManager implements PeerManager {
    private static final Logger log = Logger.getLogger(JmDNSRMIPeerManager.class);

    private final JmDNSRegistrationService registrar;
    private final JmDNSDiscoveryService discoverer;
    
    private final WindowsNetDiscoveryService winDiscoverer;

    private final RMIPeerManager manager;
    
    public JmDNSRMIPeerManager(int port, File idFile) {
        registrar = new JmDNSRegistrationService(port);
        discoverer = new JmDNSDiscoveryService();
        winDiscoverer = new WindowsNetDiscoveryService();

        manager = new RMIPeerManager(port, idFile);
        
        log.info("Starting Peer Registration Service");

        registrar.register();
        manager.setName(registrar.getOurName());

        discoverer.setListener(manager);
        winDiscoverer.setListener(manager);

        log.info("Starting RMI Peer Manager");
        manager.start();

        log.info("Starting Peer Discovery Service");

        discoverer.start();
        winDiscoverer.start();
    }

    @Override
    public boolean initialisedOk() {
        return registrar.initialisedOk() && discoverer.initialisedOk();
    }
    
    public void stop() {
        discoverer.stop();
//        registrar.unregister();
        manager.stop();
//        manager = null;
    }

    @Override
    public void addConnectionListener(PeerConnectionListener listener) {
        manager.addConnectionListener(listener);
    }

    @Override
    public void removeConnectionListener(PeerConnectionListener listener) {
        manager.removeConnectionListener(listener);
    }

    
    @Override
    public Collection<Peer> getPeers() {
        return manager.getPeers();
    }

    @Override
    public UUID getUUID() {
        return manager.getUUID();
    }

    @Override
    public <T> void registerService(String serviceName, Class<T> serviceClass, T implementation) {
        manager.registerService(serviceName, serviceClass, implementation);
    }
    
    @Override
    public <T> void registerService(Class<T> serviceClass, T implementation) {
        manager.registerService(serviceClass, implementation);
    }

    @Override
    public void refreshServices() {
        manager.refreshServices();
        discoverer.stop();
        discoverer.start();
    }
    
    @Override
    public void unregisterService(String serviceName) {
        manager.unregisterService(serviceName);
    }

    public boolean isRegistered(String serviceName) {
        return manager.isRegistered(serviceName);
    }
    
    public void addDiscoveryService(PeerDiscoveryService discoverer) {
        discoverer.setListener(manager);
    }
}
