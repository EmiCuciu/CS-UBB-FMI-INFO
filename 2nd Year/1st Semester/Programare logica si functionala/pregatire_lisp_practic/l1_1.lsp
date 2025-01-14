; 1.
; a) Sa se insereze intr-o lista liniara un atom a dat dupa al 2-lea, al 4-lea,
; al 6-lea,....element.
; b) Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
; care apar, pe orice nivel, dar in ordine inversa. De exemplu: (((A B) C)
; (D E)) --> (E D C B A)
; c) Definiti o functie care intoarce cel mai mare divizor comun al numerelor
; dintr-o lista neliniara.
; d) Sa se scrie o functie care determina numarul de aparitii ale unui atom dat
; intr-o lista neliniara.

;a)
(defun insert(l a poz)
    (cond 
        ((null l) nil)
        ((equal (mod poz 2) 0) (cons (car l) (cons a (insert (cdr l) a (+ poz 1)))))
        (t (cons (car l) (insert (cdr l) a (+ poz 1))))
    )
)

;b)
(defun invers(l)
    (cond
        ((null l) nil)
        ((atom (car l)) (append (invers (cdr l)) (list (car l))))
        (t (append (invers (cdr l)) (invers (car l))))
    )
)

;c)
(defun cmmdc(a b)
    (cond 
        ((= b 0) a)
        (t (cmmdc b (mod a b)))
    )
)

(defun cmmdc_lista(l)
    (cond
        ((null l) 0)
        ((numberp (car l)) (cmmdc (car l) (cmmdc_lista (cdr l))))
        (t (cmmdc (cmmdc_lista (car l)) (cmmdc_lista (cdr l))))
    )
)

;d)
(defun aparitii(l e)
    (cond 
        ((null l) 0)
        ((and (atom l) (equal l e)) 1)
        ((atom l) 0)
        (t (apply #'+ (mapcar #'(lambda (x)
                            (aparitii x e)
                            )
                            l
                            )))
    )
)