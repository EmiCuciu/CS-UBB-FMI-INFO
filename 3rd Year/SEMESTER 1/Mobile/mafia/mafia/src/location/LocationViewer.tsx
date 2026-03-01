import React from 'react';
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
import { close } from 'ionicons/icons';
import { SimpleGoogleMap } from './SimpleGoogleMap';

interface LocationViewerProps {
    isOpen: boolean;
    onClose: () => void;
    latitude: number;
    longitude: number;
    title?: string;
    locationName?: string;
}

export const LocationViewer: React.FC<LocationViewerProps> = ({
    isOpen,
    onClose,
    latitude,
    longitude,
    title = 'Location',
    locationName,
}) => {
    return (
        <IonModal isOpen={isOpen} onDidDismiss={onClose}>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>{title}</IonTitle>
                    <IonButtons slot="start">
                        <IonButton onClick={onClose}>
                            <IonIcon icon={close} />
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <div style={{ width: '100%', height: '100%', position: 'relative' }}>
                    <SimpleGoogleMap
                        latitude={latitude}
                        longitude={longitude}
                        zoom={15}
                        marker={{
                            lat: latitude,
                            lng: longitude,
                            draggable: false,
                        }}
                    />
                    {locationName && (
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
                            <div style={{ fontSize: '14px', fontWeight: 'bold' }}>
                                {locationName}
                            </div>
                            <div style={{ fontSize: '12px', color: '#666' }}>
                                {latitude.toFixed(6)}, {longitude.toFixed(6)}
                            </div>
                        </div>
                    )}
                </div>
            </IonContent>
        </IonModal>
    );
};

