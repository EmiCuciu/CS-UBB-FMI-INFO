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
    IonToolbar
} from '@ionic/react';
import { getLogger } from '../core';
import { MafiotContext } from './MafiotProvider';
import { RouteComponentProps } from 'react-router';
import { MafiotProps } from './MafiotProps';

const log = getLogger('MafiotEdit');

type MafiotEditProps = RouteComponentProps<{
    id?: string;
}>;

const MafiotEdit: React.FC<MafiotEditProps> = ({ history, match }) => {
    const { mafiots, saving, savingError, saveMafiot } = useContext(MafiotContext);
    const [nume, setNume] = useState('');
    const [prenume, setPrenume] = useState('');
    const [balanta, setBalanta] = useState('');
    const [mafiot, setMafiot] = useState<MafiotProps>();

    useEffect(() => {
        log('useEffect');
        const routeId = match.params.id || '';
        const mafiot = mafiots?.find(it => it.id === routeId);
        setMafiot(mafiot);
        if (mafiot) {
            setNume(mafiot.nume);
            setPrenume(mafiot.prenume);
            setBalanta(mafiot.balanta);
        }
    }, [match.params.id, mafiots]);

    const handleSave = useCallback(() => {
        const editedMafiot = mafiot ?
            { ...mafiot, nume, prenume, balanta } :
            { nume, prenume, balanta };

        if (saveMafiot) {
            saveMafiot(editedMafiot).then(() => history.goBack());
        }
    }, [mafiot, saveMafiot, nume, prenume, balanta, history]);

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
            <IonContent>
                <IonInput
                    label="Nume"
                    value={nume}
                    onIonChange={e => setNume(e.detail.value || '')}
                />
                <IonInput
                    label="Prenume"
                    value={prenume}
                    onIonChange={e => setPrenume(e.detail.value || '')}
                />
                <IonInput
                    label="Balanta"
                    value={balanta}
                    onIonChange={e => setBalanta(e.detail.value || '')}
                />
                <IonLoading isOpen={saving} />
                {savingError && (
                    <div>{savingError.message || 'Failed to save mafiot'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default MafiotEdit;
