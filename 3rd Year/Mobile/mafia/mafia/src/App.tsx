import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import { MafiotList, MafiotEdit } from './todo';
import { MafiotProvider } from './todo/MafiotProvider';
import { AuthProvider } from './auth/AuthProvider';
import { Login } from './auth/Login';
import { PrivateRoute } from './auth/PrivateRoute';
import { cleanupStorage } from './core/cleanupStorage';

// Make cleanup utility available in console for debugging
if (typeof window !== 'undefined') {
    (window as any).cleanupStorage = cleanupStorage;
}

import '@ionic/react/css/core.css';
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';

import '@ionic/react/css/typography.css';
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

import '@ionic/react/css/palettes/dark.system.css';
import './theme/variables.css';

setupIonicReact();

const App: React.FC = () => (
    <IonApp>
        <AuthProvider>
            <MafiotProvider>
                <IonReactRouter>
                    <IonRouterOutlet>
                        <Route path="/login" component={Login} exact={true} />
                        <PrivateRoute path="/mafiot" exact={true} component={MafiotList} />
                        <PrivateRoute path="/mafiot/:id" component={MafiotEdit} />
                        <Route exact path="/" render={() => <Redirect to="/mafiot" />} />
                    </IonRouterOutlet>
                </IonReactRouter>
            </MafiotProvider>
        </AuthProvider>
    </IonApp>
);

export default App;
