(defun f(l)
    (cdr l)
)


(defun interval (a b)
  (if (> a b)
      nil
      (cons a (interval (+ a 1) b))))


(defun suma_sup (l)
    (cond 
        ((null l) 0)
        ((numberp (car l)) (+ (car l)(suma_sup (cdr l))))
        (t (suma_sup (cdr l)))
    )
)


(defun suma (l)
    (cond
        ((null l) 0)
        ((numberp (car l)) (+ (car l) (suma (cdr l))))
        ((atom (car l)) (suma (cdr (l))))
        (t (+ (suma (car l)) (suma (cdr l))))
    )
)


; PENTRU COND SE RETURNEAZA ULTIMA VALOARE DIN RAMURA: ADICA (CONS 'A '(B))
(defun test (X)
    (cond 
        ((> X 5) (LIST 'A) (CONS 'A '(B)))
        (t 'A)
    )
)

(defun ultim (X)
    (cond 
        ((atom X) X)
        ((NULL (cdr X)) (car X))
        (t (ultim (CDR X)))
    )
)

(defun F (X Y)
    (cond 
        ((< X Y) X)
        (t Y)
    )
    Y
)

(defun adaug (e l)
    (cond 
        ((null l) (list e))
        (t (cons (car l)(adaug e (cdr l))))
    )
)

;Metoda variabilei colectoare. Să se definească o funcție care inversează o
;listă liniară.
(defun invers_aux (l col)
    (cond
        ((null l) col)
        (t (invers_aux(cdr l)(cons (car l) col)))
    )
)

(defun invers(l)
    (invers_aux l ())
)

;Să se definească o funcţie care să determine lista perechilor dintre un
;elelement dat si elementele unei liste.
(defun lista_el(e l)
    (cond 
        ((null l) nil)
        (t (cons (list e (car l)) (lista_el e (cdr l))))
    )
)

;. Să se definească o funcţie care să determine lista perechilor cu elemente în
;ordine strict crescătoare care se pot forma cu elementele unei liste numerice (se va păstra
;ordinea elementelor din listă).
(defun per (e l)
    (cond 
        ((null l) nil)
        (t (cond
            ((< e (car l)) (cons (list e (car l)) (per e (cdr l))))
            (t (per e (cdr l)))
        ))

    )
)

(defun perechi(l)
    (cond 
        ((null l) nil)
        (t (append (per (car l)(cdr l)) (perechi (cdr l))))
    )
)

;Se dă o listă neliniară. Se cere să se dubleze valorile numerice de la orice nivel
;al listei, păstrând structura ierarhică a acesteia.

(defun dublare(l)
    (cond
        ((null l) nil)
        ((numberp (car l)) (cons (* 2 (car l)) (dublare (cdr l))))
        ((atom (car l)) (cons (car l) (dublare (cdr l))))
        (t (cons (dublare (car l)) (dublare (cdr l))))
    )
)

(defun dub(l)
    (cond 
        ((numberp l)(* 2 l))
        ((atom l) l)
        (t (cons (dub (car l)) (dub (cdr l))))
    )
)

(defun lst(l)
    (cond 
        ((null l) nil)
        ((numberp (car l)) (cons (car l) (lst (cdr l))))
        (t (lst (cdr l)))
    )
)

(defun lista_aux (l col)
 (cond
 ((null l) col)
 ((numberp (car l)) (lista_aux (cdr l) (append col (list (car l)))))
 (t (lista_aux (cdr l) col))
 )
)
(defun listaa (l)
 (lista_aux l nil)
)

(defun parcurg_aux(L k col)
(cond
((null L) nil)
((= k 0) (list col L))
(t (parcurg_aux (cdr L) (- k 1) (cons (car l) col)))
)
)
(defun parcurg (L k)
(parcurg_aux L k nil)
)
