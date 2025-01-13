(defun lg (l)
    (cond
        ((atom l) 1)
        (t (apply #'+ (mapcar #'lg l)))
    )
)


;Să se definească o funcţie care având ca parametru o listă neliniară să
;returneze numărul de subliste (inclusiv lista) având lungime număr par (la nivel superficial).

(defun par (l)
    (cond
        ((= 0 (mod (length l) 2)) t)
        (t nil)
    )
)

(defun nr (l)
    (cond
        ((atom l) 0)
        ((par l) (+ 1 (apply #'+ (mapcar #'nr l))))
        (t (apply #'+ (mapcar #'nr l)))
    )
)

;Să se definească o funcţie care având ca parametru o listă neliniară să
;returneze lista atomilor (de la orice nivel) din listă.

(defun atomi (l)
    (cond 
        ((atom l) (list l))
        (t (apply #'append(mapcar #'atomi l)))
    )
)

;Să se definească o funcţie care determină numărul de apariții, de la orice
;nivel, ale unui element într-o listă neliniară.

(defun nr_ap (e l)
    (cond
        ((equal e l) 1)
        ((atom l) 0)
        (t (apply #'+ (mapcar #'(lambda (l)
                                    (nr_ap e l)
                                )
                                l
                        )
            )
        )
    )
)

;Se dă o listă neliniară. Se cere să se returneze lista din care au fost șterși atomii
;numerici negativi. Se va folosi o funcție MAP.

(defun sterg (l)
    (cond
        ((and (numberp l) (minusp l)) nil)
        ((atom l) (list l))
        (t (list (apply #'append(mapcar #'sterg l))))
    )
)

(defun stergere (l)
    (car(sterg l))
)

;Se dă un arbore n-ar nevid, reprezentat sub forma unei liste neliniare de
;forma (rădăcina lista_sub1.....lista_sub_n) (V1 de reprezentare a arborilor binari, Curs 9). Se
;cere să se determine numărul de noduri din arbore.

(defun nrNoduri(l)
    (cond 
        ((null (cdr l)) 1)
        (t (+ 1 (apply #'+ (mapcar #'nrNoduri (cdr l)))))
    )
)

;Se dă un arbore n-ar nevid, reprezentat sub forma unei liste neliniare de
;forma (rădăcina lista_sub1.....lista_sub_n) (V1 de reprezentare a arborilor binari, Curs 9). Se
;cere să se determine adâncimea arborelui. Observație. Nivelul rădăcinii este 0.

(defun adancime(l)
    (cond
        ((null (cdr l)) 1)
        (t (+ 1 (apply #'max (mapcar #'adancime (cdr l)))))
    )
)