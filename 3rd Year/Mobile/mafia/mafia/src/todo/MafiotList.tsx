import React, { useContext, useState } from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar,
    IonSearchbar,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonChip,
    IonLabel,
    IonButton,
    IonButtons,
    IonBadge,
    IonSelect,
    IonSelectOption,
} from '@ionic/react';
import { add, logOut, wifi, cloudOffline } from 'ionicons/icons';
import Mafiot from './Mafiot';
import { getLogger } from '../core';
import { MafiotContext } from './MafiotProvider';
import { AuthContext } from '../auth';
import { useNetwork } from '../core';

const log = getLogger('MafiotList');

const MafiotList: React.FC<RouteComponentProps> = ({ history }) => {
    const {
        mafiots,
        fetching,
        fetchingError,
        hasMore,
        loadMore,
        pendingOperations,
        searchText,
        setSearchText,
        balanceFilter,
        setBalanceFilter,
        deleteMafiot,
    } = useContext(MafiotContext);
    const { logout, username } = useContext(AuthContext);
    const networkStatus = useNetwork();
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState(false);

    log('render', 'username:', username);

    const handleLoadMore = async (e: CustomEvent<void>) => {
        if (loadMore) {
            await loadMore();
            (e.target as HTMLIonInfiniteScrollElement).complete();

            if (!hasMore) {
                setDisableInfiniteScroll(true);
            }
        }
    };

    const handleLogout = () => {
        if (logout) {
            logout();
            history.push('/login');
        }
    };

    const handleDelete = async (id?: string) => {
        if (!id) return;

        const mafiot = mafiots?.find(m => m.id === id);
        if (!mafiot) return;

        const confirmDelete = window.confirm(
            `Sigur vrei să ștergi mafiotul ${mafiot.nume} ${mafiot.prenume}?`
        );

        if (confirmDelete && deleteMafiot) {
            try {
                await deleteMafiot(id);
                log('Mafiot deleted successfully');
            } catch (error) {
                log('Failed to delete mafiot', error);
                alert('Eroare la ștergerea mafiotului!');
            }
        }
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>
                        Mafia App
                        {username && <div style={{ fontSize: '0.8em', fontWeight: 'normal' }}>Conectat: {username}</div>}
                    </IonTitle>
                    <IonButtons slot="end">
                        {networkStatus.connected ? (
                            <IonChip color="success">
                                <IonIcon icon={wifi} />
                                <IonLabel>Online</IonLabel>
                            </IonChip>
                        ) : (
                            <IonChip color="danger">
                                <IonIcon icon={cloudOffline} />
                                <IonLabel>Offline</IonLabel>
                            </IonChip>
                        )}
                        {pendingOperations && pendingOperations.length > 0 && (
                            <IonBadge color="warning">
                                {pendingOperations.length} pending
                            </IonBadge>
                        )}
                        <IonButton onClick={handleLogout}>
                            <IonIcon slot="icon-only" icon={logOut} />
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
                <IonToolbar>
                    <IonSearchbar
                        value={searchText}
                        onIonInput={(e) => setSearchText?.(e.detail.value || '')}
                        placeholder="Search by name or balance"
                        debounce={300}
                    />
                </IonToolbar>
                <IonToolbar>
                    <IonSelect
                        value={balanceFilter}
                        placeholder="Filter by balance"
                        onIonChange={(e) => setBalanceFilter?.(e.detail.value)}
                        interface="popover"
                        style={{ padding: '10px' }}
                    >
                        <IonSelectOption value="">All Balances</IonSelectOption>
                        <IonSelectOption value="positive">Positive Balance</IonSelectOption>
                        <IonSelectOption value="negative">Negative Balance</IonSelectOption>
                        <IonSelectOption value="zero">Zero Balance</IonSelectOption>
                    </IonSelect>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching mafiots" />
                
                {!networkStatus.connected && (
                    <div style={{ 
                        padding: '10px', 
                        background: '#ffcc00', 
                        textAlign: 'center',
                        fontWeight: 'bold'
                    }}>
                        You are offline. Changes will be synced when online.
                    </div>
                )}
                
                {pendingOperations && pendingOperations.length > 0 && (
                    <div style={{ 
                        padding: '10px', 
                        background: '#ff9800', 
                        textAlign: 'center',
                        color: 'white'
                    }}>
                        {pendingOperations.length} item(s) not sent to server
                    </div>
                )}

                {mafiots && (
                    <IonList>
                        {mafiots.map((mafiot, index) =>
                            <Mafiot 
                                key={mafiot.id || `mafiot-${index}`} 
                                id={mafiot.id} 
                                nume={mafiot.nume} 
                                prenume={mafiot.prenume} 
                                balanta={mafiot.balanta}
                                onEdit={id => history.push(`/mafiot/${id}`)}
                                onDelete={handleDelete}
                            />
                        )}
                    </IonList>
                )}
                
                {fetchingError && (
                    <div style={{ padding: '20px', color: 'red', textAlign: 'center' }}>
                        {fetchingError.message || 'Failed to fetch mafiots'}
                    </div>
                )}

                <IonInfiniteScroll
                    threshold="100px"
                    disabled={disableInfiniteScroll || !hasMore}
                    onIonInfinite={handleLoadMore}
                >
                    <IonInfiniteScrollContent
                        loadingSpinner="bubbles"
                        loadingText="Loading more mafiots..."
                    />
                </IonInfiniteScroll>

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/mafiot/new')}>
                        <IonIcon icon={add} />
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default MafiotList;
