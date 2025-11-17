import React, { useEffect, useRef } from 'react';
import { waitForGoogleMaps } from './googleMapsLoader';
import './google-maps.d';

interface SimpleMapProps {
    latitude: number;
    longitude: number;
    zoom?: number;
    onMapClick?: (lat: number, lng: number) => void;
    marker?: {
        lat: number;
        lng: number;
        draggable?: boolean;
    };
}

/**
 * Simple Google Maps component using native JavaScript API
 * Works in browser without Capacitor plugin
 */
export const SimpleGoogleMap: React.FC<SimpleMapProps> = ({
    latitude,
    longitude,
    zoom = 13,
    onMapClick,
    marker,
}) => {
    const mapRef = useRef<HTMLDivElement>(null);
    const mapInstanceRef = useRef<google.maps.Map | null>(null);
    const markerInstanceRef = useRef<google.maps.Marker | null>(null);

    useEffect(() => {
        let mounted = true;

        const initMap = async () => {
            if (!mapRef.current) return;

            try {
                // Wait for Google Maps API to load
                await waitForGoogleMaps();

                if (!mounted) return;

                console.log('SimpleGoogleMap: Creating map...');

                // Create map using native Google Maps API
                const map = new google.maps.Map(mapRef.current, {
                    center: { lat: latitude, lng: longitude },
                    zoom: zoom,
                    mapTypeControl: true,
                    streetViewControl: false,
                    fullscreenControl: false,
                });

                mapInstanceRef.current = map;

                // Add marker if provided
                if (marker) {
                    const mapMarker = new google.maps.Marker({
                        position: { lat: marker.lat, lng: marker.lng },
                        map: map,
                        draggable: marker.draggable || false,
                    });

                    markerInstanceRef.current = mapMarker;

                    // Handle marker drag
                    if (marker.draggable && onMapClick) {
                        mapMarker.addListener('dragend', () => {
                            const position = mapMarker.getPosition();
                            if (position) {
                                onMapClick(position.lat(), position.lng());
                            }
                        });
                    }
                }

                // Handle map clicks
                if (onMapClick) {
                    map.addListener('click', (e: google.maps.MapMouseEvent) => {
                        if (e.latLng) {
                            const lat = e.latLng.lat();
                            const lng = e.latLng.lng();
                            onMapClick(lat, lng);

                            // Update or create marker
                            if (markerInstanceRef.current) {
                                markerInstanceRef.current.setPosition({ lat, lng });
                            } else {
                                const newMarker = new google.maps.Marker({
                                    position: { lat, lng },
                                    map: map,
                                    draggable: true,
                                });
                                markerInstanceRef.current = newMarker;

                                newMarker.addListener('dragend', () => {
                                    const position = newMarker.getPosition();
                                    if (position) {
                                        onMapClick(position.lat(), position.lng());
                                    }
                                });
                            }
                        }
                    });
                }

                console.log('SimpleGoogleMap: Map created successfully!');
            } catch (error) {
                console.error('SimpleGoogleMap: Error creating map:', error);
            }
        };

        initMap();

        return () => {
            mounted = false;
            // Cleanup
            if (markerInstanceRef.current) {
                markerInstanceRef.current.setMap(null);
            }
            mapInstanceRef.current = null;
        };
    }, [latitude, longitude, zoom, marker?.lat, marker?.lng, marker?.draggable, onMapClick]);

    // Update marker position when prop changes
    useEffect(() => {
        if (markerInstanceRef.current && marker) {
            markerInstanceRef.current.setPosition({ lat: marker.lat, lng: marker.lng });
            if (mapInstanceRef.current) {
                mapInstanceRef.current.panTo({ lat: marker.lat, lng: marker.lng });
            }
        }
    }, [marker?.lat, marker?.lng]);

    return (
        <div
            ref={mapRef}
            style={{
                width: '100%',
                height: '100%',
                minHeight: '400px',
            }}
        />
    );
};

