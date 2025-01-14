(defun produs(l x)
    (cond 
        ((null l) nil)
        ((equal x 0) (list 0))
        ((atom l) (* l x))
        (t (mapcar #'(lambda (v)
                        (produs v x)
                    )
                    l
                    ))
    )
)

;(produs '(1 9 3 5 9 9) * '2) => (3 8 7 1 9 8)
;(produs '(2 2 4) '2) => (4 4 8)
;(produs ;(1 2 3 5 6) '0) => (0)


(defun produs_2(l x)
    (cond 
        ((null l) nil)
        ((equal x 0) (list 0))
        ((and (atom l) (>= (* l x) 10)) (cons (/ (* l x) 10) (mod (* l x) 10)))
        ((atom l) (* l x))
        (t (mapcar #'(lambda (v)
                        (produs_2 v x)
                    )
                    l
                    ))
    )
)

