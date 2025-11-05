import React, { memo } from 'react';
import { IonItem, IonLabel, IonButton, IonIcon } from '@ionic/react';
import { trash } from 'ionicons/icons';
import { MafiotProps } from './MafiotProps';

interface MafiotPropsExt extends MafiotProps {
    onEdit: (id?: string) => void;
    onDelete?: (id?: string) => void;
}

const Mafiot: React.FC<MafiotPropsExt> = ({ id, nume, prenume, balanta, onEdit, onDelete }) => {
    const handleDelete = (e: React.MouseEvent) => {
        e.stopPropagation(); // Prevent triggering onEdit
        if (onDelete && id) {
            onDelete(id);
        }
    };

    return (
        <IonItem button onClick={() => onEdit(id)}>
            <IonLabel className="ion-text-wrap">
                <h2 style={{ margin: '0 0 0.25rem' }}>{nume} {prenume}</h2>
                <ul style={{ margin: 0, paddingLeft: '1rem' }}>
                    <li>Balanță: {balanta} $</li>
                </ul>
            </IonLabel>
            {onDelete && (
                <IonButton 
                    slot="end" 
                    color="danger" 
                    fill="clear"
                    onClick={handleDelete}
                >
                    <IonIcon slot="icon-only" icon={trash} />
                </IonButton>
            )}
        </IonItem>
    );
};

export default memo(Mafiot);
