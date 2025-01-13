

; Sa se inlocuiasca nodurile de la nivelul k cu o valoare e
(defun inlocuieste(l e k niv)
    (cond 
        ((and (atom l) (equal niv k)) e)
        ((atom l) l)
        (t (mapcar #'(lambda (x)
                    (inlocuieste x e k (+ niv 1))
                )
                l
            ))
    )
)