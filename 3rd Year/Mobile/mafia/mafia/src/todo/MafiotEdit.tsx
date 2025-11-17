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
import { camera, close, trash } from 'ionicons/icons';
import { getLogger } from '../core';
import { MafiotContext } from './MafiotProvider';
import { RouteComponentProps } from 'react-router';
import { MafiotProps } from './MafiotProps';
import { useCamera } from "../camera/useCamera";
import { useFilesystem } from "../camera/useFilesystem";

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

    const { getPhoto } = useCamera();
    const { readFile, writeFile, deleteFile } = useFilesystem();

    useEffect(() => {
        log('useEffect');
        const routeId = match.params.id || '';
        const mafiot = mafiots?.find(it => it.id === routeId);
        setMafiot(mafiot);
        if (mafiot) {
            setNume(mafiot.nume);
            setPrenume(mafiot.prenume);
            setBalanta(mafiot.balanta);
            if (mafiot.photo) {
                const loadPhoto = async () => {
                    try {
                        const data = await readFile(mafiot.photo!);
                        setPhotoPreview(`data:image/jpeg;base64,${data}`);
                        setPhoto(mafiot.photo);
                    } catch (error) {
                        log('Error loading photo: ', error);
                    }
                };
                loadPhoto();
            }
        }
    }, [match.params.id, mafiots, readFile]);

    const handleSave = useCallback(() => {
        const editedMafiot = {
            ...mafiot,
            nume,
            prenume,
            balanta,
            photo: photo || mafiot?.photo
        };

        if (saveMafiot) {
            saveMafiot(editedMafiot).then(() => history.goBack());
        }
    }, [mafiot, saveMafiot, nume, prenume, balanta, photo, history]);

    const handleTakePhoto = async () => {
        try {
            const cameraPhoto = await getPhoto();

            if (cameraPhoto?.base64String) {
                const fileName = new Date().getTime() + '.jpeg';
                const base64Data = cameraPhoto.base64String;
                await writeFile(fileName, base64Data);
                const webviewPath = `data:image/jpeg;base64,${base64Data}`;
                setPhoto(fileName);
                setPhotoPreview(webviewPath);
                setShowActionSheet(false);
            }
        } catch (error) {
            log('Error taking photo:', error);
        }
    };

    const handleDeletePhoto = async () => {
        if (photo) {
            try {
                await deleteFile(photo);
                setPhoto(undefined);
                setPhotoPreview(undefined);
                setShowActionSheet(false);
            } catch (error) {
                log('Error deleting photo:', error);
            }
        }
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
