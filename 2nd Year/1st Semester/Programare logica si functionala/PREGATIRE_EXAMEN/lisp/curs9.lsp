;Se dă un arbore binar reprezentat în V1 (a se vedea Secțiunea 1). Să se
;determine lista liniară a nodurilor obținute prin parcurgerea arborelui în inordine.

(defun inordine(l)
    (cond
        ((null l) nil)
        (t (append (inordine (cadr l)) (list (car l)) (inordine (caddr l))))
    )
)

;V2

(defun parcurg_st(arb nv nm)
    (cond 
        ((null arb) nil)
        ((= nv (+ 1 nm)) nil)
        (t (cons (car arb) (cons (cadr arb) (parcurg_st (cddr arb) (+ 1 nv) (+ (cadr arb) nm)))))
    )
)

(defun stang(arb)
    (parcurg_st (cddr arb) 0 0)
)


;Se dă o mulțime reprezentată sub forma unei liste liniare. Se cere să se
;determine lista (mulțimea) submulțimilor listei.

(defun insPrimaPoz(e l)
    (cond
        ((null l) nil)
        (t (cons (cons e (car l)) (insPrimaPoz e (cdr l))))
    )
)

(defun subm(l)
    (cond 
        ((null l) (list nil))
        (t (append (subm (cdr l)) (insPrimaPoz (car l) (subm(cdr l)))))
    )
)


; Se o listă liniară numerică. Să se determine lista sortată, folosind sortarea
; arborescentă (un ABC intermediar).

(defun ins(e arb)
    (cond 
        ((null arb) (list e))
        ((<= e (car arb)) (list (car arb) (ins e (cadr arb)) (caddr arb)))
        (t (list (car arb) (cadr arb) (ins e (caddr arb))))
    )
)

(defun construire(l)
    (cond 
        ((null l) nil)
        (t (ins (car l) (construire (cdr l))))
    )
)

(defun inord(arb)
    (cond 
        ((null arb) nil)
        (t (append (inord (cadr arb)) (list (car arb)) (inord (caddr arb))))
    )
)

(defun sortare(l)
    (inord (construire l))
)

;Se dă un arbore binar, ale cărui noduri sunt distincte, reprezentat sub forma unei liste
;neliniare de forma (rădăcină lista-subarbore-stâng lista-subarbore-drept). Să se determine o
;listă liniară reprezentând calea de la rădăcină către un nod e dat.



(defun cale (e l)
    (cond 
        ((null l) nil)
        ((= e (car l)) (list e))
        (t (let ((left (cale e (cadr l)))
                (right (cale e (caddr l))))
            (cond 
                (left (cons (car l) left))
                (right (cons (car l) right))
                (t nil)
            )
        ))
    )
)

; (set 'x 10) ; Atribuie valoarea 10 variabilei x
; (setq x 10) ; Atribuie valoarea 10 variabilei x
; (setf (car my-list) 10) ; Atribuie valoarea 10 primului element din my-list 

