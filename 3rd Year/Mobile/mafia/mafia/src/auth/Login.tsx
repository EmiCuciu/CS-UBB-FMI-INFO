import React, { useContext, useState } from 'react';
import { Redirect } from 'react-router-dom';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonInput,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
  IonLabel,
  IonItem,
} from '@ionic/react';
import { AuthContext } from './AuthProvider';
import { getLogger } from '../core';

const log = getLogger('Login');

export const Login: React.FC = () => {
  const { isAuthenticated, isAuthenticating, login, authenticationError } =
    useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  log('render');

  if (isAuthenticated) {
    return <Redirect to={{ pathname: '/' }} />;
  }

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Login</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent className="ion-padding">
        <IonItem>
          <IonLabel position="floating">Username</IonLabel>
          <IonInput
            value={username}
            onIonInput={(e) => setUsername(e.detail.value || '')}
          />
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Password</IonLabel>
          <IonInput
            type="password"
            value={password}
            onIonInput={(e) => setPassword(e.detail.value || '')}
          />
        </IonItem>
        <IonButton expand="block" onClick={handleLogin}>
          Login
        </IonButton>
        {authenticationError && (
          <div style={{ color: 'red' }}>Failed to authenticate</div>
        )}
        <IonLoading isOpen={isAuthenticating} />
      </IonContent>
    </IonPage>
  );

  function handleLogin() {
    log('handleLogin...');
    login?.({ username, password });
  }
};
