# Laborator 5 - Prezentare Implementare

## 📋 Obiectiv

Implementare paralelă a sistemului de calcul note studenți cu:

- **Fine-grained synchronization** (lock pe nod, nu pe listă)
- **Bounded queue** (capacitate limitată)
- **Thread pool** pentru citire
- **Sortare descrescătoare** a rezultatelor
- **Variabile condiționale** pentru Producer-Consumer

---

## 🔄 Workflow General

```
[10 Fișiere] → [Thread Pool (p_r)] → [Bounded Queue] → [Workers (p_w)] → [Lista Unsorted]
                   ProducerLab5         (cap=50/100)    ConsumerLab5      FineGrainedLinkedList
                                                                                    ↓
                                                                          [Workers sortează]
                                                                                    ↓
                                                                           [SortedLinkedList]
                                                                                    ↓
                                                                          [rezultate_paralel_lab5.txt]
```

---

## 🎯 FAZA 1: Citire + Procesare

### 1.1 Inițializare (ParallelLab5.java)

```java
// Structuri de date principale
FineGrainedLinkedList linkedList = new FineGrainedLinkedList();  // Lista cu fine-grained locking
BoundedQueue queue = new BoundedQueue(QUEUE_CAPACITY);           // Coadă limitată (50/100)

// ESENȚIAL: Înregistrăm producătorii ÎNAINTE de start
for(
int i = 1;
i <=10;i++){
        queue.

registerProducer();  // activeProducers = 10
}

// Thread pool cu p_r threads (ex: 4)
ExecutorService executorService = Executors.newFixedThreadPool(p_r);
```

**De ce?** Pentru a evita deadlock când consumatorii văd `activeProducers=0` prematur.

### 1.2 Pornire Consumatori (Workers)

```java
for(int i = 0;
i<p_w;i++){
ConsumerLab5 consumer = new ConsumerLab5(queue, linkedList);
Thread t = new Thread(consumer, "Worker-" + i);
    t.

start();
}
```

**Workers** așteaptă date în coadă și le procesează continuu.

### 1.3 Lansare Task-uri de Citire

```java
for(int i = 1;
i <=10;i++){
String filename = "proiect" + i + ".txt";
ProducerLab5 producer = new ProducerLab5(filename, queue);
    readTasks.

add(producer);
}

        executorService.

invokeAll(readTasks);  // Execută toate task-urile
executorService.

shutdown();             // Închide pool-ul
```

**Thread Pool** distribuie cele 10 task-uri pe cele p_r=4 threads.

---

## 🔧 Componente Cheie

### A. ProducerLab5 (Callable)

```java
public Void call() throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line;

    while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0].trim());
        int nota = Integer.parseInt(parts[1].trim());

        queue.put(new Pair(id, nota));  // Blocking dacă coada e plină
    }

    queue.unregisterProducer();  // activeProducers--
    return null;
}
```

**Mecanism:** Dacă coada e plină (100 elemente), `put()` blochează thread-ul până se eliberează spațiu.

### B. BoundedQueue (Variabile Condiționale)

```java
public synchronized void put(Pair item) throws InterruptedException {
    while (queue.size() >= capacity) {
        notFull.await();  // Așteaptă pe condiția "coadă nu e plină"
    }

    queue.add(item);
    notEmpty.signalAll();  // Trezește consumatorii
}

public synchronized Pair take() throws InterruptedException {
    while (queue.isEmpty() && activeProducers > 0) {
        notEmpty.await();  // Așteaptă pe condiția "coadă nu e goală"
    }

    if (queue.isEmpty()) return null;  // Gata, nu mai sunt producători

    Pair item = queue.poll();
    notFull.signalAll();  // Trezește producătorii
    return item;
}
```

**Avantaj:** Sincronizare eficientă fără busy-waiting (Lab 4 folosea `wait()/notify()`).

### C. ConsumerLab5 (Worker)

```java
public void run() {
    try {
        while (true) {
            Pair pair = queue.take();
            if (pair == null) break;  // Nu mai sunt date

            linkedList.addOrUpdate(pair.id, pair.nota);  // Fine-grained locking
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

**Comportament:** Preia continuu perechi și le procesează până când `take()` returnează `null`.

### D. FineGrainedLinkedList (Hand-Over-Hand Locking)

```java
// Structură: HEAD(sentinel) → nod1 → nod2 → TAIL(sentinel)
// Fiecare nod are propriul ReentrantLock

public void addOrUpdate(int id, int nota) {
    MyNode prev = head;
    prev.lock.lock();

    MyNode curr = prev.next;
    curr.lock.lock();

    try {
        while (curr != tail) {
            if (curr.id == id) {
                curr.nota += nota;  // Update
                return;
            }

            prev.lock.unlock();  // Hand-over-hand: eliberez prev
            prev = curr;
            curr = curr.next;
            curr.lock.lock();
        }

        // Insert nou nod înainte de tail
        MyNode newNode = new MyNode(id, nota);
        newNode.next = tail;
        prev.next = newNode;

    } finally {
        prev.lock.unlock();
        curr.lock.unlock();
    }
}
```

**Tehnică:**

- Două lock-uri consecutive ("escaladă")
- Permite paralelism: thread-uri pe părți diferite ale listei
- **Santinele:** HEAD și TAIL simplifică edge cases

**Diferență vs Lab 4:**

- Lab 4: `synchronized` pe întreaga listă → 1 thread la un moment dat
- Lab 5: Lock pe 2 noduri consecutive → multiple thread-uri simultan

---

## 🎯 FAZA 2: Sortare Paralelă

### 2.1 Extragere Noduri

```java
List<MyNode> allNodes = linkedList.extractAll();  // Toate nodurile din listă
```

### 2.2 Distribuție Work

```java
AtomicInteger nodeIndex = new AtomicInteger(0);  // Index partajat thread-safe
SortedLinkedList sortedList = new SortedLinkedList();

for(
int i = 0;
i<p_w;i++){
Thread t = new Thread(() -> {
    while (true) {
        int idx = nodeIndex.getAndIncrement();  // Atomic: fiecare thread preia un index unic

        if (idx >= allNodes.size()) break;

        MyNode node = allNodes.get(idx);
        sortedList.insertSorted(node.id, node.nota);  // Insert în ordine descrescătoare
    }
});
    t.

start();
}
```

**Pattern:** Work-stealing - fiecare worker ia un nod și îl inserează în poziția corectă.

### 2.3 SortedLinkedList

```java
public synchronized void insertSorted(int id, int nota) {
    MyNode current = head;

    // Căutăm poziția: inserăm ÎNAINTE de primul nod cu notă mai mică
    while (current.next != null && current.next.nota > nota) {
        current = current.next;
    }

    MyNode newNode = new MyNode(id, nota);
    newNode.next = current.next;
    current.next = newNode;
}
```

**Rezultat:** Lista finală sortată descrescător (nota 100 → 0).

---

## 📊 Exemplu Execuție (500 studenți, p_r=4, p_w=2)

```
1. Pornire: 2 Workers (Consumer1, Consumer2) → wait pe queue
2. Thread Pool: 4 threads preia 10 fișiere
   - Thread1: proiect1.txt → citește 450 perechi → queue.put()
   - Thread2: proiect2.txt → citește 420 perechi → queue.put()
   - Thread3: proiect3.txt → citește 480 perechi → queue.put()
   - Thread4: proiect4.txt → citește 390 perechi → queue.put()
   
3. Consumatori procesează simultan:
   - Consumer1: take() → (id=123, nota=9) → linkedList.addOrUpdate()
   - Consumer2: take() → (id=456, nota=8) → linkedList.addOrUpdate()
   
4. Thread Pool termină → activeProducers=0 → Consumatori văd null → stop

5. Sortare: 2 workers iau noduri cu AtomicInteger
   - Worker1: nod #0, #2, #4...
   - Worker2: nod #1, #3, #5...
   
6. Scriere: rezultate_paralel_lab5.txt (sortate descrescător)
```

---

## ⚡ Diferențe Majore Lab 4 vs Lab 5

| Aspect                 | Lab 4                                    | Lab 5                                       |
|------------------------|------------------------------------------|---------------------------------------------|
| **Sincronizare listă** | Coarse-grained (`synchronized` pe listă) | Fine-grained (lock pe 2 noduri)             |
| **Coadă**              | Unbounded                                | Bounded (50/100)                            |
| **Producer-Consumer**  | `wait()/notify()`                        | Variabile condiționale (`await()/signal()`) |
| **Citire**             | Thread-uri manuale                       | Thread Pool (ExecutorService)               |
| **Sortare**            | Nu                                       | Da (paralelă în Faza 2)                     |
| **Speedup**            | Bun (0.5x-0.7x)                          | Slab (0.17x-0.21x)                          |

---

## 🐌 De Ce Speedup Slab?

1. **Fine-grained overhead:** Lock/unlock frecvent > sincronizare simplă
2. **Contention:** Workers se "luptă" pe aceleași noduri (ex: 2 ID-uri duplicate)
3. **Queue bounded:** Producătorii blochează când e plină → idle time
4. **Sortare costisitoare:** Faza 2 inserează secvențial în listă sortată (lock!)
5. **Dataset mic:** 500 studenți × 10 fișiere = ~4500 perechi → overhead > beneficiu

**Concluzie:** Fine-grained e util pentru date MARI și operații LUNGI, nu pentru taskuri mici/rapide.

---

## 🎓 Puncte Cheie Pentru Prezentare

1. **"Am folosit ProducerLab5 ca Callable în Thread Pool"** - arată cod
2. **"BoundedQueue cu condition variables (notEmpty/notFull)"** - explică await/signal
3. **"Hand-over-hand locking în FineGrainedLinkedList"** - desenează pe tablă
4. **"Faza 2: workers sortează paralel cu AtomicInteger"** - arată distribuția
5. **"Speedup slab pentru că overhead > beneficiu la date mici"** - analiză critică

---

## 📁 Fișiere Relevante

- **ParallelLab5.java** - main flow (2 faze)
- **ProducerLab5.java** - Callable care citește un fișier
- **ConsumerLab5.java** - Worker care procesează queue
- **BoundedQueue.java** - Coadă cu condition variables
- **FineGrainedLinkedList.java** - Listă cu lock per nod
- **SortedLinkedList.java** - Inserție sortată descrescător

---

**Timp Execuție:**

- Secvențial: ~16ms
- Paralel Lab5 (p_r=4, p_w=2): ~75ms
- **Speedup:** 0.21x (mai lent!)

**Învățăminte:** Paralelizarea nu e întotdeauna mai rapidă - depinde de granularitate și overhead!

