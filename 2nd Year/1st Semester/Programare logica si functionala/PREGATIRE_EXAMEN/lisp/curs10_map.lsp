(defun f (l)
    (cond 
        ((null l) t)
        (((lambda (v)
            (cond 
                ((numberp v) t)
                (t nil)
            )
        ) (car l)
        ) nil)
        (t (f (cdr l)))
    )
)


;Să se scrie o funcţie care primeşte ca parametru o listă de liste formate din atomi şi
;întoarce T dacă toate listele conţin atomi numerici şi NIL în caz contrar.

(defun test(l)
    (cond
        ((null l) t)
        ((labels ((test1 (l)
                    (cond 
                        ((null l) t)
                        ((numberp (car l)) (test1 (cdr l)))
                        (t nil)
                    )
                    )
                    ) 
            (test1 (car l))
        )
        (test (cdr l)))
        (t nil)
    )
)


;;;  Utilizarea expresiilor LAMBDA pentru evitarea apelurilor repetate  "(f l)"
(defun g(l)
    (cond
        ((null l) nil)
        (t (cons (car (f l)) (cadr (f l))))
    )
)


(defun g(l)
    (cond
        ((null l) nil)
        (t ((lambda (v)
            (cons (car v) (cadr v))
        )
        (f l)
        ))
    )
)

(defun g(l)
    ((lambda (v)
        (cond
            ((null l) nil)
            (t (cons (car v) (cadr v)))
        )
    )
    (f l)
    )
)

(defun subm(l)
    (cond 
        ((null l) (list nil))
        (t ((lambda (v)
            (append v (insPrimaPoz (car l) v))
        )
            (subm (cdr l))
        ))
    )
)

; la APPLY necesita lista
; la FUNCALL se poate si parametrii

(defun F()
    #'(lambda (x) (car x))
)

;CLOSURE CLisp
;1
(defun increment (x)
    (lambda (y)
        (+ x y)
    )
)

(setq inc5 (increment 5))
(print (funcall inc5 3))

;2

(defun two-funs (x)
    (list
        (function (lambda () x))
        (function (lambda (y) (setq x y)))
    )
)

(setq funs1 (two-funs 6))


; MAPCAR

;Să se definească o funcție MODIF care să modifice o listă dată ca parametru astfel: atomii
;nenumerici rămân nemodificaţi iar cei numerici îşi dublează valoarea; modificarea trebuie
;făcută la toate nivelurile.

(defun modif (l)
    (cond 
        ((numberp l) (* 2 l))
        ((atom l) l)
        (t (mapcar #'modif l))
    )
)

;Să se construiască o funcţie LGM ce determină lungimea celei mai lungi subliste dintr-o listă
;dată L (dacă lista este formată numai din atomi atunci lungimea cerută este chiar cea a listei
;L).

(defun lgm (l)
    (cond 
        ((atom l) 0)
        (t (max (length l)(apply #'max (mapcar #'lgm l))))
    )
)


(defun F2(l)
    (cdr l)
)