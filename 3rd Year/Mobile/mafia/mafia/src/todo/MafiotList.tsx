import React, {useContext} from 'react';
import {RouteComponentProps} from 'react-router';
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList, IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {add} from 'ionicons/icons';
import Mafiot from './Mafiot';
import {getLogger} from '../core';
import {MafiotContext} from './MafiotProvider';

const log = getLogger('ItemList');

const MafiotList: React.FC<RouteComponentProps> = ({history}) => {
    const {mafiots, fetching, fetchingError} = useContext(MafiotContext);
    log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Mafia App</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching mafiots"/>
                {mafiots && (
                    <IonList>
                        {mafiots.map(({id, nume, prenume, balanta}) =>
                            <Mafiot key={id} id={id} nume={nume} prenume={prenume} balanta={balanta}
                                    onEdit={id => history.push(`/mafiot/${id}`)}/>)}
                    </IonList>
                )}
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch mafiots'}</div>
                )}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/mafiot/new')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default MafiotList;
