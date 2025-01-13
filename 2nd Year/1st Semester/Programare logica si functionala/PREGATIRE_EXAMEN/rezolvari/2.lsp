; ; 1.1
; (defun f(l)
;     (cond 
;         ((null l) 0)
;         (t ((lambda (x)
;             (cond
;                 ((> x 2)(+ (car x) (f(cdr x))))
;                 (t x)
;             )
;         )
;         (f (car l))
;         ))
;     )
; )

; ;1.3
; (defun f(x &rest y)
;     (cond 
;         ((null y) x)
;         (t (append x (mapcar #'car y)))
;     )
; )

;3
(defun inloc(l k nivel)
    (cond
        ((and (atom l) (equal k nivel)) 0)
        ((atom l) l)
        (t (mapcar #'(lambda (x)
                        (inloc x k (+ nivel 1))
            )
            l
            ))
    )
)