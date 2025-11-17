import React, { useState } from 'react';
import {
    IonModal,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButton,
    IonButtons,
    IonIcon,
} from '@ionic/react';
import { close, locate } from 'ionicons/icons';
import { getLogger } from '../core';
import { useGeolocation } from './useGeolocation';
import { SimpleGoogleMap } from './SimpleGoogleMap';

const log = getLogger('LocationPicker');

interface LocationPickerProps {
    isOpen: boolean;
    onClose: () => void;
    onLocationSelect: (latitude: number, longitude: number) => void;
    initialLatitude?: number;
    initialLongitude?: number;
}

export const LocationPicker: React.FC<LocationPickerProps> = ({
    isOpen,
    onClose,
    onLocationSelect,
    initialLatitude,
    initialLongitude,
}) => {
    const defaultLat = initialLatitude || 46.7712;
    const defaultLng = initialLongitude || 23.6236;

    const [selectedPosition, setSelectedPosition] = useState({
        lat: defaultLat,
        lng: defaultLng,
    });

    const { getCurrentPosition } = useGeolocation();

    const handleMapClick = (lat: number, lng: number) => {
        setSelectedPosition({ lat, lng });
    };

    const handleGetCurrentLocation = async () => {
        const currentPos = await getCurrentPosition();
        if (currentPos) {
            setSelectedPosition({
                lat: currentPos.latitude,
                lng: currentPos.longitude,
            });
            log('Current location set:', currentPos);
        }
    };

    const handleConfirm = () => {
        onLocationSelect(selectedPosition.lat, selectedPosition.lng);
        onClose();
    };

    return (
        <IonModal isOpen={isOpen} onDidDismiss={onClose}>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Select Location</IonTitle>
                    <IonButtons slot="start">
                        <IonButton onClick={onClose}>
                            <IonIcon icon={close} />
                        </IonButton>
                    </IonButtons>
                    <IonButtons slot="end">
                        <IonButton onClick={handleGetCurrentLocation}>
                            <IonIcon icon={locate} />
                        </IonButton>
                        <IonButton onClick={handleConfirm} strong>
                            Confirm
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <div style={{ width: '100%', height: '100%', position: 'relative' }}>
                    <SimpleGoogleMap
                        latitude={selectedPosition.lat}
                        longitude={selectedPosition.lng}
                        zoom={13}
                        onMapClick={handleMapClick}
                        marker={{
                            lat: selectedPosition.lat,
                            lng: selectedPosition.lng,
                            draggable: true,
                        }}
                    />
                    <div
                        style={{
                            position: 'absolute',
                            bottom: '20px',
                            left: '50%',
                            transform: 'translateX(-50%)',
                            background: 'white',
                            padding: '10px 20px',
                            borderRadius: '8px',
                            boxShadow: '0 2px 6px rgba(0,0,0,0.3)',
                            zIndex: 1000,
                        }}
                    >
                        <div style={{ fontSize: '12px', color: '#666' }}>
                            Selected Location:
                        </div>
                        <div style={{ fontSize: '14px', fontWeight: 'bold' }}>
                            {selectedPosition.lat.toFixed(6)}, {selectedPosition.lng.toFixed(6)}
                        </div>
                    </div>
                </div>
            </IonContent>
        </IonModal>
    );
};

