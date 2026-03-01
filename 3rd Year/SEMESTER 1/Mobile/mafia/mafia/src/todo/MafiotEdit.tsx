import React, { useCallback, useContext, useEffect, useState } from 'react';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonInput,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar,
    IonIcon,
    IonImg,
    IonActionSheet,
} from '@ionic/react';
import { camera, close, trash, location } from 'ionicons/icons';
import { getLogger } from '../core';
import { MafiotContext } from './MafiotProvider';
import { RouteComponentProps } from 'react-router';
import { MafiotProps } from './MafiotProps';
import { useCamera } from "../camera/useCamera";
import { photoStorage } from "../camera/photoStorage";
import { compressImage } from "../camera/imageCompression";
import { LocationPicker } from "../location";

const log = getLogger('MafiotEdit');

type MafiotEditProps = RouteComponentProps<{
    id?: string;
}>;

const MafiotEdit: React.FC<MafiotEditProps> = ({ history, match }) => {
    const { mafiots, saving, savingError, saveMafiot } = useContext(MafiotContext);
    const [nume, setNume] = useState('');
    const [prenume, setPrenume] = useState('');
    const [balanta, setBalanta] = useState('');
    const [photo, setPhoto] = useState<string | undefined>(undefined);
    const [photoPreview, setPhotoPreview] = useState<string | undefined>(undefined);
    const [mafiot, setMafiot] = useState<MafiotProps>();
    const [showActionSheet, setShowActionSheet] = useState(false);
    const [showLocationPicker, setShowLocationPicker] = useState(false);
    const [latitude, setLatitude] = useState<number | undefined>(undefined);
    const [longitude, setLongitude] = useState<number | undefined>(undefined);

    const { getPhoto } = useCamera();

    useEffect(() => {
        log('useEffssect');
        const routeId = match.params.id || '';
        const mafiot = mafiots?.find(it => it.id === routeId);
        setMafiot(mafiot);
        if (mafiot) {
            setNume(mafiot.nume);
            setPrenume(mafiot.prenume);
            setBalanta(mafiot.balanta);
            setLatitude(mafiot.latitude);
            setLongitude(mafiot.longitude);

            // Load photo from filesystem if photoPath exists
            if (mafiot.photoPath) {
                photoStorage.loadPhoto(mafiot.photoPath, mafiot.id).then(base64Data => {
                    if (base64Data) {
                        setPhoto(base64Data);
                        setPhotoPreview(`data:image/jpeg;base64,${base64Data}`);
                    }
                }).catch(err => {
                    log('Error loading photo:', err);
                });
            }
        }
    }, [match.params.id, mafiots]);

    const handleSave = useCallback(async () => {
        let photoPath = mafiot?.photoPath;

        // If there's a new photo, save it to filesystem for offline access
        if (photo && photo !== mafiot?.photo) {
            try {
                photoPath = await photoStorage.savePhoto(photo, mafiot?.id);
                log('Photo saved to filesystem:', photoPath);
            } catch (error) {
                log('Error saving photo to filesystem:', error);
            }
        }

        const editedMafiot = {
            ...mafiot,
            nume,
            prenume,
            balanta,
            photo: photo || mafiot?.photo, // Send compressed base64 to server
            photoPath: photoPath, // Store filename for local filesystem
            latitude,
            longitude,
        };

        if (saveMafiot) {
            saveMafiot(editedMafiot).then(() => history.goBack());
        }
    }, [mafiot, saveMafiot, nume, prenume, balanta, photo, latitude, longitude, history]);

    const handleTakePhoto = async () => {
        try {
            const cameraPhoto = await getPhoto();

            if (cameraPhoto?.base64String) {
                // Compress image before storing
                const compressedBase64 = await compressImage(cameraPhoto.base64String, 800, 0.7);
                const webviewPath = `data:image/jpeg;base64,${compressedBase64}`;

                // Store the compressed base64 data - will be sent to server
                setPhoto(compressedBase64);
                setPhotoPreview(webviewPath);
                setShowActionSheet(false);

                // Also save to filesystem for offline access
                if (mafiot?.id) {
                    try {
                        const photoPath = await photoStorage.savePhoto(compressedBase64, mafiot.id);
                        log('Photo saved to filesystem:', photoPath);
                    } catch (error) {
                        log('Error saving photo to filesystem:', error);
                    }
                }
            }
        } catch (error) {
            log('Error taking photo:', error);
        }
    };

    const handleDeletePhoto = async () => {
        // Delete from filesystem if photoPath exists
        if (mafiot?.photoPath) {
            try {
                await photoStorage.deletePhoto(mafiot.photoPath);
            } catch (error) {
                log('Error deleting photo from filesystem:', error);
            }
        }

        setPhoto(undefined);
        setPhotoPreview(undefined);
        setShowActionSheet(false);
    };

    const handleLocationSelect = (lat: number, lng: number) => {
        setLatitude(lat);
        setLongitude(lng);
        log('Location selected:', lat, lng);
    };

    log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit Mafiot</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <IonInput
                    label="Nume"
                    labelPlacement="floating"
                    value={nume}
                    onIonChange={e => setNume(e.detail.value || '')}
                />
                <IonInput
                    label="Prenume"
                    labelPlacement="floating"
                    value={prenume}
                    onIonChange={e => setPrenume(e.detail.value || '')}
                />
                <IonInput
                    label="Balanta"
                    labelPlacement="floating"
                    value={balanta}
                    onIonChange={e => setBalanta(e.detail.value || '')}
                />

                <div style={{ marginTop: '20px' }}>
                    {photoPreview && (
                        <>
                            <IonImg
                                onClick={() => setShowActionSheet(true)}
                                src={photoPreview}
                                style={{
                                    width: '100%',
                                    maxWidth: '400px',
                                    height: 'auto',
                                    borderRadius: '8px',
                                    margin: '0 auto 20px',
                                    cursor: 'pointer',
                                    display: 'block'
                                }}
                            />
                        </>
                    )}

                    <IonButton expand="block" onClick={handleTakePhoto}>
                        <IonIcon slot="start" icon={camera} />
                        Take Photo
                    </IonButton>
                </div>

                <div style={{ marginTop: '20px' }}>
                    <IonButton expand="block" onClick={() => setShowLocationPicker(true)}>
                        <IonIcon slot="start" icon={location} />
                        {latitude && longitude ? 'Update Location' : 'Set Location'}
                    </IonButton>

                    {latitude && longitude && (
                        <div style={{
                            marginTop: '10px',
                            padding: '10px',
                            background: 'var(--ion-color-light)',
                            borderRadius: '8px',
                            textAlign: 'center'
                        }}>
                            <div style={{ fontSize: '12px', color: '#666' }}>Location:</div>
                            <div style={{ fontSize: '14px', fontWeight: 'bold' }}>
                                {latitude.toFixed(6)}, {longitude.toFixed(6)}
                            </div>
                        </div>
                    )}
                </div>

                <LocationPicker
                    isOpen={showLocationPicker}
                    onClose={() => setShowLocationPicker(false)}
                    onLocationSelect={handleLocationSelect}
                    initialLatitude={latitude}
                    initialLongitude={longitude}
                />

                <IonActionSheet
                    isOpen={showActionSheet}
                    buttons={[
                        {
                            text: 'Delete',
                            role: 'destructive',
                            icon: trash,
                            handler: handleDeletePhoto
                        },
                        {
                            text: 'Cancel',
                            icon: close,
                            role: 'cancel'
                        }
                    ]}
                    onDidDismiss={() => setShowActionSheet(false)}
                />

                <IonLoading isOpen={saving} />
                {savingError && (
                    <div style={{ color: 'red', marginTop: '20px', textAlign: 'center' }}>
                        {savingError.message || 'Failed to save mafiot'}
                    </div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default MafiotEdit;
