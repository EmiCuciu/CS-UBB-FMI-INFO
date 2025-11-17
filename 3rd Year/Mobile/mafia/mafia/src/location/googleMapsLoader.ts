/// <reference types="@types/google.maps" />

/**
 * Helper function to wait for Google Maps API to be loaded
 */
export const waitForGoogleMaps = (): Promise<typeof google.maps> => {
    return new Promise((resolve, reject) => {
        // Check if already loaded
        if (window.google?.maps) {
            resolve(window.google.maps);
            return;
        }

        // Wait for the load event from index.html
        const handleLoaded = () => {
            window.removeEventListener('google-maps-loaded', handleLoaded);
            if (window.google?.maps) {
                resolve(window.google.maps);
            } else {
                reject(new Error('Google Maps failed to load'));
            }
        };

        window.addEventListener('google-maps-loaded', handleLoaded);

        // Timeout after 10 seconds
        setTimeout(() => {
            window.removeEventListener('google-maps-loaded', handleLoaded);
            if (window.google?.maps) {
                resolve(window.google.maps);
            } else {
                reject(new Error('Google Maps loading timeout'));
            }
        }, 10000);
    });
};

/**
 * Check if Google Maps API is currently loaded
 */
export const isGoogleMapsLoaded = (): boolean => {
    return !!window.google?.maps;
};

