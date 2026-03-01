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
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonImg,
    createAnimation,
} from '@ionic/react';
import { close, person, cash, locationSharp } from 'ionicons/icons';
import { MafiotProps } from './MafiotProps';
import './mafiot-photo-frame.css';
import './MafiotInfoModal.css';

interface MafiotInfoModalProps {
    isOpen: boolean;
    onClose: () => void;
    mafiot: MafiotProps | null;
    photoData?: string | null;
}

// Custom enter animation - override default Ionic modal animation
const customEnterAnimation = (baseEl: HTMLElement) => {
    const root = baseEl.shadowRoot;
    const backdrop = root ? root.querySelector('ion-backdrop') : null;
    const wrapper = root ? root.querySelector('.modal-wrapper') : null;

    const backdropAnimation = createAnimation()
        .addElement(backdrop || baseEl)
        .fromTo('opacity', '0.01', 'var(--backdrop-opacity)')
        .duration(400);

    const wrapperAnimation = createAnimation()
        .addElement(wrapper || baseEl)
        .keyframes([
            { offset: 0, opacity: '0', transform: 'scale(0.7) rotateX(45deg)' },
            { offset: 0.5, opacity: '0.8', transform: 'scale(1.05) rotateX(-10deg)' },
            { offset: 1, opacity: '1', transform: 'scale(1) rotateX(0deg)' }
        ])
        .duration(500)
        .easing('cubic-bezier(0.36, 0.66, 0.04, 1)');

    return createAnimation()
        .addAnimation([backdropAnimation, wrapperAnimation]);
};

// Custom leave animation
const customLeaveAnimation = (baseEl: HTMLElement) => {
    return customEnterAnimation(baseEl).direction('reverse');
};

export const MafiotInfoModal: React.FC<MafiotInfoModalProps> = ({
    isOpen,
    onClose,
    mafiot,
    photoData
}) => {
    if (!mafiot) return null;

    return (
        <IonModal
            isOpen={isOpen}
            onDidDismiss={onClose}
            enterAnimation={customEnterAnimation}
            leaveAnimation={customLeaveAnimation}
        >
            <IonHeader>
                <IonToolbar color="dark">
                    <IonTitle>Detalii Mafiot</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={onClose}>
                            <IonIcon icon={close} />
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <IonCard className="modal-card-animated">
                    {photoData && (
                        <div className="mafiot-photo-frame">
                            <IonImg
                                src={`data:image/jpeg;base64,${photoData}`}
                            />
                        </div>
                    )}
                    <IonCardHeader>
                        <IonCardTitle className="modal-title-animated">
                            {mafiot.nume} {mafiot.prenume}
                        </IonCardTitle>
                    </IonCardHeader>
                    <IonCardContent>
                        <div className="info-item-animated">
                            <IonIcon icon={person} className="info-icon" />
                            <div>
                                <div className="info-label">Nume Complet</div>
                                <div className="info-value">{mafiot.nume} {mafiot.prenume}</div>
                            </div>
                        </div>

                        <div className="info-item-animated" style={{ animationDelay: '0.1s' }}>
                            <IonIcon icon={cash} className="info-icon" />
                            <div>
                                <div className="info-label">Balanță</div>
                                <div className={`info-value ${parseFloat(mafiot.balanta) >= 0 ? 'balance-positive' : 'balance-negative'}`}>
                                    {mafiot.balanta} $
                                </div>
                            </div>
                        </div>

                        {mafiot.latitude && mafiot.longitude && (
                            <div className="info-item-animated" style={{ animationDelay: '0.2s' }}>
                                <IonIcon icon={locationSharp} className="info-icon" />
                                <div>
                                    <div className="info-label">Locație</div>
                                    <div className="info-value" style={{ fontSize: '12px' }}>
                                        {mafiot.latitude.toFixed(6)}, {mafiot.longitude.toFixed(6)}
                                    </div>
                                </div>
                            </div>
                        )}

                        <IonButton
                            expand="block"
                            onClick={onClose}
                            style={{ marginTop: '20px' }}
                            className="close-button-animated"
                        >
                            Închide
                        </IonButton>
                    </IonCardContent>
                </IonCard>
            </IonContent>
        </IonModal>
    );
};

