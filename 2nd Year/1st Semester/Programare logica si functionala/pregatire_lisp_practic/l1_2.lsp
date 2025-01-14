; a) Definiti o functie care selecteaza al n-lea element al unei liste, sau
; NIL, daca nu exista.
; b) Sa se construiasca o functie care verifica daca un atom e membru al unei
; liste nu neaparat liniara.
; c) Sa se construiasca lista tuturor sublistelor unei liste. Prin sublista se
; intelege fie lista insasi, fie un element de pe orice nivel, care este
; lista. Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10)) =>
; ( (1 2 (3 (4 5) (6 7)) 8 (9 10)) (3 (4 5) (6 7)) (4 5) (6 7) (9 10) ).
; d) Sa se scrie o functie care transforma o lista liniara intr-o multime.

;a)
(defun extrage(l n)
    (cond
        ((null l) nil)
        ((= n 1) (car l))
        (t (extrage (cdr l) (- n 1)))
    )
)

;b)
(defun apartine(l e)
    (cond
        ((null l) nil)
        ((equal (car l) e) t)
        ((listp (car l)) (or (apartine (car l) e) (apartine (cdr l) e)))
        (t (apartine (cdr l) e))        
    )
)

;c)
(defun subliste(l)
    (cond
        ((null l) nil)
        ((listp (car l)) (append (list (car l)) (subliste (car l)) (subliste (cdr l))))
        (t (subliste(cdr l)))
    )
)

;d)
(defun apartine_3(l e)
    (cond
        ((null l) nil)
        ((equal (car l) e) t)
        (t (apartine_3 (cdr l) e))
    )
)

(defun lista-multime(l)
    (cond
        ((null l) nil)
        ((apartine_3 (cdr l) (car l)) (lista-multime(cdr l)))
        (t (append (list (car l)) (lista-multime (cdr l))))
    )
)

(defun aux(l vizitate)
    (cond 
        ((null l) nil)
        ((apartine_3 vizitate (car l)) (aux (cdr l) vizitate))
        (t (cons (car l) (aux (cdr l) (cons (car l) vizitate))))
    )
)

(defun lista-multime_2(l)
    (aux l nil)
)