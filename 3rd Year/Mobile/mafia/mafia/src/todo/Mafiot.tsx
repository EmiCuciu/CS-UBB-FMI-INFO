import React, { memo } from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import { MafiotProps } from './MafiotProps';

interface MafiotPropsExt extends MafiotProps {
    onEdit: (id?: string) => void;
}

const Mafiot: React.FC<MafiotPropsExt> = ({ id, nume, prenume, balanta, onEdit }) => {
    return (
        <IonItem button detail onClick={() => onEdit(id)}>
            <IonLabel className="ion-text-wrap">
                <h2 style={{ margin: '0 0 0.25rem' }}>{nume} {prenume}</h2>
                <ul style={{ margin: 0, paddingLeft: '1rem' }}>
                    <li>Balanță: {balanta} $</li>
                </ul>
            </IonLabel>
        </IonItem>
    );
};

export default memo(Mafiot);
