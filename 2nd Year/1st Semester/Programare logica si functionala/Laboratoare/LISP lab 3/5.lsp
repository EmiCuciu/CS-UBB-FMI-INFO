;; 5. Definiti o functie care testeaza apartenenta unui nod
;; intr-un arbore n-ar reprezentat sub forma
;; (radacina lista_noduri_subarb1... lista_noduri__subarbn)
;; Ex: arborelele este (a (b (c)) (d) (e (f))) 
;; si nodul este 'b => adevarat

(defun sau(l)
    (cond
        ((null l) nil)
        (t (or (car l) (sau (cdr l))))
    )
)

(defun apare(l e)
    (cond 
        ((null l) nil)
        ((equal (car l) e) t)
        (t(sau (mapcar #'(lambda (x)
                            (apare x e)
                        )
                        (cdr l)
                        )))
    )
)