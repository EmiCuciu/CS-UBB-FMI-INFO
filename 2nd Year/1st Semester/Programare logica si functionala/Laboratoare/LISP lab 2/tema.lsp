; Functia principala pentru gasirea caii catre nodul x
(defun find-path (tree x)
    (cond 
        ((null tree) nil)                   ; verificăm dacă arborele este vid
        ((equal (car tree) x) (list x))     ; verifică dacă primul element (nodul curent) este cel căutat
        (t (let ((nr-children (cadr tree))  ; nr-children = numărul de copii ai nodului curent
                 (children (cddr tree)))    ; children = lista de copii ai nodului curent
            (find-path-in-children children nr-children x (car tree))))))

; Functia pentru cautarea in copiii nodului curent
(defun find-path-in-children (children nr-children x parent)
    (cond 
        ((= nr-children 0) nil)                             ; nu mai sunt copii de verificat
        ((null children) nil)                               ; lista de copii e vidă
        (t (let ((child-path (find-path children x)))       ; căutăm în copilul curent
            (if child-path                               ; daca este diferit de nil
                (cons parent child-path)                    ; dacă am găsit, construim calea
                (let ((next-child (get-next-child children))) ; altfel, trecem la următorul copil
                    (find-path-in-children next-child (- nr-children 1) x parent)))))))

; Functie pentru a obtine urmatorul copil din lista
(defun get-next-child (tree)
    (cond 
        ((null tree) nil)                          ; arbore vid
        ((null (cdr tree)) nil)                    ; nu mai sunt elemente
        (t (let ((nr-subchildren (cadr tree)))     ; numărul de subcopii
            (skip-subtree (cddr tree) nr-subchildren))))) ; sare peste subarbore

; Functie pentru a sari peste un subarbore
(defun skip-subtree (tree count)
    (cond 
        ((= count 0) tree)                         ; am terminat de sărit
        ((null tree) nil)                          ; arbore vid
        (t (skip-subtree 
            (cddr tree)                            ; trecem peste nod și nr-copii
            (+ count (cadr tree) -1)))))           ; actualizăm contorul

; Exemple de testare:
; (find-path '(A 2 B 0 C 2 D 0 E 0) 'D) => (A C D)
; (find-path '(A 2 B 0 C 2 D 0 E 0) 'E) => (A C E)
; (find-path '(A 2 B 2 C 1 I 0 F 1 G 0 D 2 E 0 H 0) 'E) => (A D E)