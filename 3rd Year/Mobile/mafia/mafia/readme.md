# Mafia App - Ionic Client

Aplicație Ionic React pentru gestionarea mafioților cu funcționalități complete de online/offline și sincronizare.

## 🎯 Funcționalități Implementate

### Ionic 1 Requirements ✅
- ✅ **Master-Detail UI**: Listă de mafioți + ecran de editare/creare
- ✅ **REST Services**: Integrare completă cu API-ul backend
- ✅ **WebSocket Notifications**: Actualizări în timp real pentru operațiile CRUD

### Ionic 2 Requirements ✅
- ✅ **Local Storage**: Persistență locală a datelor și operațiilor pending
- ✅ **Pagination**: Infinite scrolling pentru liste mari de date
- ✅ **JWT Authentication**: Autentificare securizată cu token
- ✅ **Secured REST**: Toate cererile REST necesită autentificare
- ✅ **Secured WebSockets**: Conexiuni WebSocket securizate cu JWT

### Assessment Criteria (8/8 puncte) ✅

#### 1. Network Status (1p) ✅
- Indicator vizual online/offline în header
- Afișare în timp real a stării conexiunii
- Icoane distinctive (wifi/cloud-offline)

#### 2. User Authentication (1p) ✅
- Login și signup cu JWT
- **Token storage**: Token-ul este salvat în localStorage după login
- **Auto-login**: Aplicația verifică token-ul la pornire și sare peste login dacă este valid
- **Logout**: Funcționalitate de logout care șterge token-ul și datele locale

#### 3. User-linked Resources (1p) ✅
- **REST filtering**: Server-ul returnează doar mafioții utilizatorului autentificat
- **WebSocket filtering**: Notificările sunt trimise doar pentru resursele utilizatorului
- Izolare completă între utilizatori

#### 4. Online/Offline Behavior (2p) ✅
- **Online mode**: Încercarea de a folosi REST services pentru create/update
- **Offline mode**: Salvare locală automată când nu există conexiune
- **Pending operations**: Indicator vizual pentru operațiile care nu au fost trimise la server
- **Local-first approach**: UI-ul se actualizează imediat, sincronizarea se face în fundal

#### 5. Auto-sync (1p) ✅
- Sincronizare automată când aplicația revine online
- Procesare secvențială a operațiilor pending
- Retry logic pentru operațiile eșuate

#### 6. Pagination (2p) ✅
- Infinite scroll cu IonInfiniteScroll
- Încărcare lazy a datelor (10 items pe pagină)
- Indicator de loading pentru paginare

#### 7. Search & Filter (1p) ✅
- Bară de căutare în header
- Filtrare în timp real după nume, prenume sau balanță
- Debounce de 300ms pentru performanță

## 🏗️ Arhitectură

### Componente Principale

#### `MafiotProvider.tsx`
- Context provider pentru state management
- Gestionarea operațiilor CRUD
- Sincronizare cu backend
- Gestionare operații pending
- WebSocket integration

#### `MafiotList.tsx`
- Listă master cu infinite scrolling
- Search bar
- Network status indicator
- Pending operations badge
- Delete functionality

#### `MafiotEdit.tsx`
- Formular de editare/creare
- Validare date
- Save cu error handling

#### `AuthProvider.tsx`
- Autentificare JWT
- Token management
- Auto-login logic
- Logout functionality

#### `storage.tsx`
- Service pentru localStorage
- Salvare/citire mafioți
- Gestionare operații pending

#### `useNetwork.tsx`
- Hook custom pentru status rețea
- Integrare cu Capacitor Network API
- Listeners pentru schimbări de status

## 📡 API Integration

### REST Endpoints
- `GET /api/mafiot?page=1&limit=10` - Listă paginată
- `POST /api/mafiot` - Creare mafiot
- `PUT /api/mafiot/:id` - Actualizare mafiot
- `DELETE /api/mafiot/:id` - Ștergere mafiot
- `POST /api/auth/login` - Autentificare
- `POST /api/auth/signup` - Înregistrare

### WebSocket Events
- `created` - Mafiot nou creat
- `updated` - Mafiot actualizat
- `deleted` - Mafiot șters

## 🔐 Security

- **JWT Authentication**: Token-based authentication pentru toate cererile
- **Authorization header**: `Bearer <token>` pentru REST API
- **WebSocket auth**: Mesaj de autorizare la conectare
- **User isolation**: Datele sunt filtrate pe server în funcție de userId

## 💾 Offline Support

### Local Storage
- Mafioți descărcați din server
- Pending operations queue
- Auth token și username

### Sync Strategy
1. **Create/Update/Delete** -> Salvare locală instant
2. Dacă **online** -> Trimite la server
3. Dacă **offline** sau **error** -> Adaugă în pending operations
4. Când revine **online** -> Procesează automat pending operations

## 🛤️ Rute
- `/login` - Pagina de autentificare
- `/mafiot` - Listă mafioți (master)
- `/mafiot/:id` - Editare mafiot (detail)
- `/mafiot/new` - Creare mafiot nou

## 📊 Model de Date
```ts
interface MafiotProps {
  id?: string;
  nume: string;
  prenume: string;
  balanta: string;
  userId?: string;
}

interface PendingOperation {
  id: string;
  type: 'create' | 'update' | 'delete';
  mafiot: MafiotProps;
  timestamp: number;
}
```

## 🚀 Instalare și Rulare

```bash
# Instalare dependențe
npm install

# Rulare în dev mode
npm run dev

# Build pentru producție
npm run build
```

## 📱 Tehnologii Utilizate

- **Ionic React**: Framework UI
- **React Hooks**: State management
- **Axios**: HTTP client
- **WebSocket**: Real-time updates
- **Capacitor**: Native APIs (Network)
- **TypeScript**: Type safety
- **Vite**: Build tool
