import { useEffect, useState } from 'react';
import { Network } from '@capacitor/network';
import { PluginListenerHandle } from "@capacitor/core";

const getLogger: (tag: string) => (...args: unknown[]) => void =
    tag => (...args) => console.log(tag, ...args);

const log = getLogger('useNetwork');

export const useNetwork = () => {
    const [networkStatus, setNetworkStatus] = useState({
        connected: true,
        connectionType: 'wifi'
    });

    useEffect(() => {
        let handler: PluginListenerHandle | undefined;
        
        const init = async () => {
            // Get initial status
            const status = await Network.getStatus();
            log('Initial network status:', status);
            setNetworkStatus({
                connected: status.connected,
                connectionType: status.connectionType
            });

            // Listen for network changes
            handler = await Network.addListener('networkStatusChange', (status) => {
                log('Network status changed:', status);
                setNetworkStatus({
                    connected: status.connected,
                    connectionType: status.connectionType
                });
            });
        };

        init();

        return () => {
            if (handler) {
                handler.remove();
            }
        };
    }, []);

    return networkStatus;
};
