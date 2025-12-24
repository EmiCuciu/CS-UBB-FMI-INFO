# 🎓 LABORATOR 5 - GHID COMPLET DE ÎNȚELEGERE

## 📖 CE REZOLVĂ APLICAȚIA?

**Problema:** 500 de studenți au trimis 10 proiecte fiecare. Trebuie să calculăm **nota finală** (suma celor 10 note) pentru fiecare student și să salvăm rezultatele **sortate descrescător**.

**Provocarea:** Fiecare fișier `proiectX.txt` conține note în **ordine aleatoare** (ordinea de trimitere), și nu toți studenții au trimis toate proiectele.

---

## 🔄 EVOLUȚIA: LAB 4 → LAB 5

### Lab 4 (Simplu, dar limitat)
```
📁 Fișiere → 🔵 Readers → 📦 Queue (unlimited) → 🟢 Workers → 📋 Lista (coarse-lock) → 💾 rezultate.txt
```
**Probleme:**
- ❌ Lista blocată complet la fiecare operație (1 worker la un moment dat)
- ❌ Queue nelimitată (risc de out-of-memory)
- ❌ Rezultate nesortate

### Lab 5 (Avansat, scalabil)
```
📁 Fișiere → 🏊 Thread Pool → 📦 Bounded Queue → 🟢 Workers → 🔗 Fine-Grained List → 
                                                                    ↓
                                              Workers sortează → 📊 Sorted List → 💾 rezultate.txt (DESC)
```
**Îmbunătățiri:**
- ✅ **Fine-grained locking** - workers lucrează simultan pe noduri diferite
- ✅ **Bounded queue** - controlează memoria (max 100 elemente)
- ✅ **Thread pool** - eficiență (4 thread-uri pentru 10 task-uri)
- ✅ **Sortare distribuită** - toți workers ajută la sortare
- ✅ **Rezultate sortate descrescător** după nota totală

---

## 🏗️ ARHITECTURA APLICAȚIEI

```
┌─────────────────────────────────────────────────────────────────┐
│                    COMPONENTE PRINCIPALE                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1️⃣  Thread Pool (ExecutorService)                             │
│      ├─ 4 thread-uri reutilizabile                             │
│      └─ 10 task-uri (câte unul per fișier)                     │
│                                                                 │
│  2️⃣  BoundedQueue (capacity=100)                               │
│      ├─ Producer-Consumer cu Condition Variables               │
│      ├─ notFull.await() / notEmpty.await()                     │
│      └─ activeProducers counter                                │
│                                                                 │
│  3️⃣  FineGrainedLinkedList                                     │
│      ├─ Fiecare nod are ReentrantLock                          │
│      ├─ Hand-over-hand locking (lock pe 2 noduri consecutive)  │
│      └─ Santinele: head(MIN_VALUE) și tail(MAX_VALUE)          │
│                                                                 │
│  4️⃣  ConsumerLab5 (Workers)                                    │
│      ├─ Preia din queue                                        │
│      └─ Adaugă în FineGrainedLinkedList                        │
│                                                                 │
│  5️⃣  SortedLinkedList                                          │
│      ├─ Inserare sortată descrescător                          │
│      ├─ Fine-grained locking și aici                           │
│      └─ Workers inserează concurent                            │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 FLOW COMPLET - EXECUȚIE PAS CU PAS

### 🎬 **FAZA 0: INIȚIALIZARE**

```java
// main() în ParallelLab5.java

// 1. Creăm structurile de date
FineGrainedLinkedList linkedList = new FineGrainedLinkedList();
BoundedQueue queue = new BoundedQueue(100);

// 2. IMPORTANT: Înregistrăm TOȚI producătorii ÎNAINTE de pornire
for (int i = 1; i <= 10; i++) {
    queue.registerProducer();  // activeProducers = 10
}

// 3. Creăm thread pool
ExecutorService executorService = Executors.newFixedThreadPool(4);
```

**De ce înregistrăm înainte?** Pentru a evita deadlock-ul când consumatorii văd `activeProducers=0` și ies prematur!

---

### 🎬 **FAZA 1: CITIRE PARALELĂ (Thread Pool + Producer-Consumer)**

```
T=0ms: Start
       ┌──────────────────────────────────────────────┐
       │  Thread Pool (4 thread-uri)                  │
       │  ┌────────┐  ┌────────┐  ┌────────┐  ┌────┐│
       │  │Thread-1│  │Thread-2│  │Thread-3│  │ T4 ││
       │  └───┬────┘  └───┬────┘  └───┬────┘  └─┬──┘│
       └──────┼───────────┼───────────┼─────────┼────┘
              │           │           │         │
              ↓           ↓           ↓         ↓
         Task1(p1)   Task2(p2)   Task3(p3)  Task4(p4)
              │           │           │         │
              └───────────┴─────┬─────┴─────────┘
                                ↓
                    📦 BoundedQueue (100)
                    ┌─────────────────────┐
                    │ [(15,8), (23,6), ...]│
                    └──────────┬───────────┘
                               ↓
                    🟢 Workers (2 thread-uri)
                    ┌──────────────────────┐
                    │ Worker-1 | Worker-2  │
                    └──────┬───────┬────────┘
                           │       │
                           └───┬───┘
                               ↓
                    🔗 FineGrainedLinkedList
```

#### 📖 **Cod: Thread Pool Task**

```java
// Fiecare task citește UN fișier
Callable<Void> task = () -> {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            int nota = Integer.parseInt(parts[1]);
            
            Pair p = new Pair(id, nota);
            queue.add(p);  // ← Poate bloca dacă queue e plină!
        }
    } finally {
        queue.producerFinished();  // activeProducers--
    }
    return null;
};
```

#### 📖 **Cod: BoundedQueue.add() - Condition Variables**

```java
public void add(Pair p) throws InterruptedException {
    lock.lock();
    try {
        // Așteaptă dacă queue e PLINĂ
        while (size >= capacity) {
            notFull.await();  // 😴 Producer doarme
        }
        
        // Adaugă element
        // ... code ...
        size++;
        
        notEmpty.signal();  // 🔔 Trezește un consumer
    } finally {
        lock.unlock();
    }
}
```

#### 📖 **Cod: BoundedQueue.remove() - Consumer**

```java
public Pair remove() throws InterruptedException {
    lock.lock();
    try {
        // Așteaptă dacă queue e GOALĂ și mai sunt producători
        while (head == null && activeProducers > 0) {
            notEmpty.await();  // 😴 Consumer așteaptă
        }
        
        if (head == null) {
            return null;  // Gata, nu mai sunt date
        }
        
        // Extrage element
        // ... code ...
        size--;
        
        notFull.signal();  // 🔔 Trezește un producer
        return result;
    } finally {
        lock.unlock();
    }
}
```

---

### 🎬 **FAZA 1B: PROCESARE CU FINE-GRAINED LOCKING**

```
Worker preia (id=250, nota=8):

Lista înainte:
head(MIN) → [150,67] → [200,45] → [300,23] → tail(MAX)

┌─ Hand-Over-Hand Locking ─────────────────────────────┐
│                                                       │
│  Pas 1: Blocăm head și [150]                         │
│  🔒head → 🔒[150] → [200] → [300] → tail              │
│  Verificăm: 250 > 150? DA, avansăm                   │
│                                                       │
│  Pas 2: Blocăm [150] și [200] (deblocăm head)        │
│  head → 🔒[150] → 🔒[200] → [300] → tail              │
│  Verificăm: 250 > 200? DA, avansăm                   │
│                                                       │
│  Pas 3: Blocăm [200] și [300] (deblocăm [150])       │
│  head → [150] → 🔒[200] → 🔒[300] → tail              │
│  Verificăm: 250 > 300? NU! Inserăm aici              │
│                                                       │
│  Inserăm [250,8] între [200] și [300]                │
│  head → [150] → [200] → [250,8] → [300] → tail        │
│                                    ↑ NOU              │
│                                                       │
│  Deblocăm [200] și [300]                             │
└───────────────────────────────────────────────────────┘
```

#### 📖 **Cod: FineGrainedLinkedList.addNota()**

```java
public void addNota(int id, int nota) {
    head.lock.lock();  // 🔒 Blocăm head
    MyNode pred = head;
    
    try {
        MyNode curr = pred.next;
        curr.lock.lock();  // 🔒 Blocăm primul nod
        
        try {
            while (curr != tail) {
                if (curr.id == id) {
                    // GĂSIT! Actualizăm nota
                    curr.nota += nota;
                    return;
                } else if (curr.id > id) {
                    // Inserăm AICI
                    break;
                }
                
                // ⚙️ HAND-OVER-HAND LOCKING
                MyNode old = pred;
                pred = curr;
                curr = curr.next;
                curr.lock.lock();   // 🔒 Blocăm NOUA poziție
                old.lock.unlock();  // 🔓 Deblocăm VECHIA poziție
            }
            
            // Inserăm nod nou între pred și curr
            MyNode newNode = new MyNode(id, nota, curr);
            pred.next = newNode;
            
        } finally {
            curr.lock.unlock();  // 🔓
        }
    } finally {
        pred.lock.unlock();  // 🔓
    }
}
```

**🔑 Cheia:** Întotdeauna ținem **2 lock-uri consecutive** - niciodată 0!

---

### 🎬 **FAZA 2: SORTARE DISTRIBUITĂ**

După ce toate notele sunt adunate:

```
┌─ Extragere Noduri ────────────────────────────────┐
│ allNodes = linkedList.extractAll();               │
│ // [Student1, Student2, ..., Student500]          │
└────────────────────────────────────────────────────┘
                     ↓
┌─ Distribuție cu AtomicInteger ────────────────────┐
│ nodeIndex = AtomicInteger(0);                     │
│                                                   │
│ Worker-1:                                         │
│   idx = nodeIndex.getAndIncrement() // 0         │
│   sortedList.insertSorted(allNodes[0])           │
│   idx = nodeIndex.getAndIncrement() // 2         │
│   sortedList.insertSorted(allNodes[2])           │
│   ...                                             │
│                                                   │
│ Worker-2:                                         │
│   idx = nodeIndex.getAndIncrement() // 1         │
│   sortedList.insertSorted(allNodes[1])           │
│   idx = nodeIndex.getAndIncrement() // 3         │
│   sortedList.insertSorted(allNodes[3])           │
│   ...                                             │
└────────────────────────────────────────────────────┘
                     ↓
┌─ SortedLinkedList (Descrescător) ─────────────────┐
│ head(MAX) → [192,98] → [285,91] → [48,90] → ...   │
│              ↑ max      ↑ 2nd      ↑ 3rd          │
└────────────────────────────────────────────────────┘
```

#### 📖 **Cod: Sortare Distribuită**

```java
// Extragere
List<MyNode> allNodes = linkedList.extractAll();
SortedLinkedList sortedList = new SortedLinkedList();

// Distribuție
AtomicInteger nodeIndex = new AtomicInteger(0);

// Workers
for (int i = 0; i < p_w; i++) {
    Thread worker = new Thread(() -> {
        while (true) {
            int idx = nodeIndex.getAndIncrement();  // Atomic!
            if (idx >= allNodes.size()) break;
            
            MyNode node = allNodes.get(idx);
            sortedList.insertSorted(node.id, node.nota);
        }
    });
    worker.start();
}
```

#### 📖 **Cod: SortedLinkedList.insertSorted()**

```java
public void insertSorted(int id, int nota) {
    // Similar cu FineGrainedLinkedList, dar SORTARE DESCRESCĂTOARE
    
    while (curr != tail) {
        if (curr.nota < nota) {
            // Nodul curent are notă MAI MICĂ
            // Inserăm ÎNAINTE de curr (descrescător!)
            break;
        }
        // Hand-over-hand...
    }
    
    // Inserare
    pred.next = new MyNode(id, nota, curr);
}
```

---

## 🔧 CONCEPTE TEHNICE ESENȚIALE

### 1️⃣ **Fine-Grained Locking**

```
COARSE (Lab 4):           FINE (Lab 5):
═══════════════           ═══════════════
Worker-1: 🔒[TOATĂ LISTA] Worker-1: 🔒[N1]🔒[N2]
Worker-2: 😴 Așteaptă     Worker-2: 🔒[N99]🔒[N100]
                          ✅ Lucrează SIMULTAN!
```

**Avantaj:** Mai multe thread-uri pot lucra pe listă simultan.  
**Cost:** Overhead de lock/unlock (~25,000 operații pentru 500 studenti).

---

### 2️⃣ **Santinele (Sentinel Nodes)**

```
Lista CU santinele:
head(MIN_VALUE) → [noduri reale] → tail(MAX_VALUE)

✅ Avantaj: NU mai verificăm null!
✅ Simplificare: while (curr != tail) în loc de while (curr != null)
✅ Hand-over-hand: Mereu avem 2 noduri de blocat
```

---

### 3️⃣ **Condition Variables**

```java
// Lab 4 (wait/notify):
synchronized (obj) {
    while (condition) obj.wait();
    obj.notifyAll();
}

// Lab 5 (Condition):
lock.lock();
try {
    while (condition) notEmpty.await();  // Mai expresiv!
    notFull.signal();
} finally {
    lock.unlock();
}
```

**Avantaj:** Două condiții separate (`notFull` / `notEmpty`) = mai clar.

---

### 4️⃣ **Thread Pool**

```
Manual (Lab 4):          Thread Pool (Lab 5):
═══════════             ═══════════════════
10 fișiere              4 thread-uri
→ 10 thread-uri noi     → reutilizate pentru
→ creare/distrugere      toate cele 10 task-uri
→ overhead mare         → overhead minim
```

---

## 🎯 URMĂRIRE COD - EXEMPLU COMPLET

### Exemplu: Student 250 are notele (8, 7, 9) din 3 proiecte

```
┌─ T1: proiect1.txt ────────────────────────────┐
│ Thread-1 citește: "250,8"                     │
│ queue.add(250, 8)                             │
│                                                │
│ Consumer-1 preia (250, 8)                     │
│ linkedList.addNota(250, 8)                    │
│ → Lista: ... → [250,8] → ...                  │
└────────────────────────────────────────────────┘

┌─ T2: proiect5.txt ────────────────────────────┐
│ Thread-3 citește: "250,7"                     │
│ queue.add(250, 7)                             │
│                                                │
│ Consumer-2 preia (250, 7)                     │
│ linkedList.addNota(250, 7)                    │
│ → Găsește [250,8] și actualizează:           │
│ → Lista: ... → [250,15] → ...                 │
│                      ↑ (8+7)                  │
└────────────────────────────────────────────────┘

┌─ T3: proiect9.txt ────────────────────────────┐
│ Thread-2 citește: "250,9"                     │
│ queue.add(250, 9)                             │
│                                                │
│ Consumer-1 preia (250, 9)                     │
│ linkedList.addNota(250, 9)                    │
│ → Găsește [250,15] și actualizează:          │
│ → Lista: ... → [250,24] → ...                 │
│                      ↑ (15+9)                 │
└────────────────────────────────────────────────┘

┌─ T4: Sortare ─────────────────────────────────┐
│ Worker-1 preia nodul [250,24]                 │
│ sortedList.insertSorted(250, 24)              │
│                                                │
│ Găsește poziția corectă (descrescător):       │
│ head → [192,98] → [48,90] → [250,24] → ...    │
│                              ↑ inserat aici   │
└────────────────────────────────────────────────┘

┌─ Rezultat Final ──────────────────────────────┐
│ rezultate_paralel_lab5.txt:                   │
│ 192,98                                         │
│ 285,91                                         │
│ ...                                            │
│ 250,24  ← Studentul 250 cu nota finală 24     │
│ ...                                            │
└────────────────────────────────────────────────┘
```

---

## ⚡ PERFORMANȚĂ: DE CE E LAB 5 MAI LENT?

```
┌────────────────────────────────────────────────┐
│ REZULTATE (500 studenti):                     │
├────────────────────────────────────────────────┤
│ Secvențial:  ~15ms  ✅ Baseline               │
│ Lab 4:       ~11ms  ✅ 1.36x speedup           │
│ Lab 5:       ~75ms  ❌ 5x slowdown             │
└────────────────────────────────────────────────┘
```

**De ce?**
1. **Fine-grained overhead** → ~40,000 operații lock/unlock
2. **Condition variables** → mai costisitoare
3. **Thread pool** → overhead de creare
4. **Sortarea** → Faza 2 adaugă ~20ms
5. **Date prea mici** → 500 studenti = overhead > beneficii

**LECȚIA:** Lab 5 e conceput pentru **date MARI** (5,000+ studenti). Pentru 500, overhead-ul depășește beneficiile paralelismului.

---

## 📝 CONCLUZIE

### Ce ai învățat în Lab 5:

✅ **Fine-grained synchronization** - sincronizare per nod  
✅ **Hand-over-hand locking** - pattern de traversare sigură  
✅ **Santinele** - simplificare cod (fără null checks)  
✅ **Condition variables** - sincronizare condiționată expresivă  
✅ **Thread pool** - gestionare eficientă a thread-urilor  
✅ **Sortare distribuită** - workers cooperează la sortare  

### Trade-offs importante:

🔄 **Coarse vs Fine-grained:** Simplu vs Scalabil  
🔄 **Overhead vs Paralelism:** Date mici vs Date mari  
🔄 **Complexitate vs Performanță:** Lab 4 e mai simplu, Lab 5 scalează mai bine  

### Când să folosești Lab 5:

- ✅ **> 5,000 studenti** - Fine-grained excelează
- ✅ **Multe core-uri** - Scalabilitate excelentă
- ✅ **Concurență mare** - Workers lucrează simultan

### Când Lab 4 e suficient:

- ✅ **< 1,000 studenti** - Overhead-ul Lab 5 nu se amortizează
- ✅ **Puține thread-uri** - Coarse-grained e suficient
- ✅ **Simplitate** - Mai puțin cod, mai puține bug-uri

---

## 🚀 RULARE RAPIDĂ

```bash
# Generare date
java -cp build/classes/java/main com.example.DataGenerator 500

# Secvențial (baseline)
java -cp build/classes/java/main com.example.Secvential

# Lab 5
java -cp build/classes/java/main com.example.ParallelLab5 4 2 100
#                                                         │ │ │
#                                                         │ │ └─ queue capacity
#                                                         │ └─── p_w (workers)
#                                                         └───── p_r (readers)

# Verificare rezultat sortat
head -20 src/main/java/com/example/data/rezultate_paralel_lab5.txt
```

---

**Autor:** Documentație Lab 5  
**Studenți:** 500  
**Configurație:** p_r=4, p_w=2,4,8, queue=100  
**Status:** ✅ Toate cerințele implementate corect

