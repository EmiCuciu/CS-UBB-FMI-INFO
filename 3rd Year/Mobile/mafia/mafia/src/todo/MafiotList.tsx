import React, {useContext, useEffect, useState} from 'react';
import { RouteComponentProps } from 'react-router';
import {
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
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
    IonSelectOption, IonGrid, IonRow,
    IonCol, IonCard, IonImg, IonCardHeader, IonCardTitle, IonCardContent,
} from '@ionic/react';
import {add, logOut, wifi, cloudOffline, trash, location} from 'ionicons/icons';
import { getLogger } from '../core';
import { MafiotContext } from './MafiotProvider';
import { AuthContext } from '../auth';
import { useNetwork } from '../core';
import { photoStorage } from '../camera/photoStorage';
import { MafiotProps } from './MafiotProps';
import { LocationViewer } from '../location';

const log = getLogger('MafiotList');

// Component to load and display photo from filesystem
const MafiotPhoto: React.FC<{ mafiot: MafiotProps }> = ({ mafiot }) => {
    const [photoData, setPhotoData] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (mafiot.photoPath) {
            photoStorage.loadPhoto(mafiot.photoPath, mafiot.id)
                .then(data => {
                    setPhotoData(data);
                    setLoading(false);
                })
                .catch(() => {
                    setLoading(false);
                });
        } else {
            setLoading(false);
        }
    }, [mafiot.photoPath, mafiot.id]);

    if (loading) {
        return (
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                width: '100%',
                height: '100%',
                fontSize: '24px',
                color: '#999'
            }}>
                Loading...
            </div>
        );
    }

    if (photoData) {
        return (
            <IonImg
                src={`data:image/jpeg;base64,${photoData}`}
                style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'contain'
                }}
            />
        );
    }

    return (
        <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: '100%',
            height: '100%',
            fontSize: '48px',
            color: '#ccc'
        }}>
            📷
        </div>
    );
};

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
    const [showLocationViewer, setShowLocationViewer] = useState(false);
    const [selectedMafiot, setSelectedMafiot] = useState<MafiotProps | null>(null);

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

    const handleDelete = async (e: React.MouseEvent<HTMLIonButtonElement, MouseEvent>, id?: string) => {
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

    const handleShowLocation = (mafiot: MafiotProps) => {
        if (mafiot.latitude && mafiot.longitude) {
            setSelectedMafiot(mafiot);
            setShowLocationViewer(true);
        }
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>
                        Mafia App
                        {username && <IonBadge color="primary" style={{marginLeft: '10px'}}>{username}</IonBadge>}
                    </IonTitle>
                    <IonButtons slot="end">
                        {networkStatus.connected ? (
                            <IonIcon icon={wifi} color="success" style={{marginRight: '10px', fontSize: '24px'}}/>
                        ) : (
                            <IonIcon icon={cloudOffline} color="danger"
                                     style={{marginRight: '10px', fontSize: '24px'}}/>
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
                        backgroundColor: '#ffdddd',
                        color: '#cc0000',
                        textAlign: 'center',
                        fontWeight: 'bold'
                    }}>
                        You are offline. Changes will be synced when online.
                    </div>
                )}

                {pendingOperations && pendingOperations.length > 0 && (
                    <div style={{
                        padding: '10px',
                        backgroundColor: '#fff3cd',
                        color: '#856404',
                        textAlign: 'center'
                    }}>
                        <IonChip>
                            <IonLabel>{pendingOperations.length} pending operation(s)</IonLabel>
                        </IonChip>
                    </div>
                )}

                {mafiots && (
                    <IonGrid>
                        <IonRow>
                            {mafiots.map((mafiot) => (
                                <IonCol size="12" sizeMd="6" sizeLg="4" key={mafiot.id}>
                                    <IonCard
                                        button
                                        onClick={() => history.push(`/mafiot/${mafiot.id}`)}
                                        style={{
                                            height: '100%',
                                            display: 'flex',
                                            flexDirection: 'column',
                                            minHeight: '400px'
                                        }}
                                    >
                                        <div style={{
                                            width: '100%',
                                            height: '300px',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            backgroundColor: '#202020',
                                            overflow: 'hidden'
                                        }}>
                                            <MafiotPhoto mafiot={mafiot} />
                                        </div>
                                        <IonCardHeader>
                                            <IonCardTitle>{mafiot.nume} {mafiot.prenume}</IonCardTitle>
                                        </IonCardHeader>
                                        <IonCardContent style={{flex: 1}}>
                                            <p><strong>Balanță:</strong> {mafiot.balanta} $</p>
                                            {mafiot.latitude && mafiot.longitude && (
                                                <IonButton
                                                    expand="block"
                                                    color="primary"
                                                    onClick={() => handleShowLocation(mafiot)}
                                                    style={{marginTop: '10px'}}
                                                >
                                                    <IonIcon slot="start" icon={location}/>
                                                    View Location
                                                </IonButton>
                                            )}
                                            <IonButton
                                                expand="block"
                                                color="danger"
                                                onClick={(e) => handleDelete(e, mafiot.id)}
                                                style={{marginTop: '10px'}}
                                            >
                                                <IonIcon slot="start" icon={trash}/>
                                                Delete
                                            </IonButton>
                                        </IonCardContent>
                                    </IonCard>
                                </IonCol>
                            ))}
                        </IonRow>
                    </IonGrid>
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

                {selectedMafiot && selectedMafiot.latitude && selectedMafiot.longitude && (
                    <LocationViewer
                        isOpen={showLocationViewer}
                        onClose={() => {
                            setShowLocationViewer(false);
                            setSelectedMafiot(null);
                        }}
                        latitude={selectedMafiot.latitude}
                        longitude={selectedMafiot.longitude}
                        title={`${selectedMafiot.nume} ${selectedMafiot.prenume}`}
                        locationName={selectedMafiot.locationName}
                    />
                )}
            </IonContent>
        </IonPage>
    );
};

export default MafiotList;
