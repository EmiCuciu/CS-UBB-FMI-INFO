import { Geolocation } from '@capacitor/geolocation';
import { getLogger } from '../core';

const log = getLogger('useGeolocation');

export interface GeolocationPosition {
    latitude: number;
    longitude: number;
}

export const useGeolocation = () => {
    const getCurrentPosition = async (): Promise<GeolocationPosition | null> => {
        try {
            log('Getting current position...');
            const position = await Geolocation.getCurrentPosition({
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 0
            });

            log('Position obtained:', position);
            return {
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            };
        } catch (error) {
            log('Error getting position:', error);
            return null;
        }
    };

    const checkPermissions = async () => {
        try {
            const status = await Geolocation.checkPermissions();
            log('Permission status:', status);
            return status;
        } catch (error) {
            log('Error checking permissions:', error);
            return null;
        }
    };

    const requestPermissions = async () => {
        try {
            const status = await Geolocation.requestPermissions();
            log('Permission request result:', status);
            return status;
        } catch (error) {
            log('Error requesting permissions:', error);
            return null;
        }
    };

    return {
        getCurrentPosition,
        checkPermissions,
        requestPermissions
    };
};

