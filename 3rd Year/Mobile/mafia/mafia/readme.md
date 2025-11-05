# mafia


## Functionalități
- Listare mafioți (master): nume, prenume, balanță.
- Editare/creare mafiot (detail).
- Persistență via REST (`GET/POST/PUT /mafiot`).
- Actualizări în timp real via WebSocket (`ws://localhost:3000`), evenimente `created`/`updated`.

## Rute
- `/mafiot` – listă
- `/mafiot/:id` – editare

## Model de date
```ts
interface MafiotProps {
  id?: string;
  nume: string;
  prenume: string;
  balanta: string;
}
