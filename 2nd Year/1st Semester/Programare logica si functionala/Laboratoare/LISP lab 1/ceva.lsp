; (DEFUN ULTIM (X)
; (COND
; ((ATOM X) X)
; ((NULL (CDR X)) (CAR X))
; (T (ULTIM (CDR X)))
; )
; )

; (DEFUN F (X Y)
; (COND
; ((< X Y) X)
; (T Y)
; )
; Y
; )

; inverseaza o lista
; (DEFUN INVERS (L)
; (COND
; ((ATOM L) L)
; (T (APPEND (INVERS (CDR L)) (LIST (CAR L))))
; )
; )

; face perechi de cate 2 elemente: (1 (2 3 4) => ((1 2) (1 3) (1 4))
; (DEFUN LISTA (E L)
; (COND
; ((NULL L) NIL)
; (T (CONS (LIST E (CAR L)) (LISTA E (CDR L))))   ;curs 8 important
; )
; )

; (dublare '(1 b 2 (c (3 h 4)) (d 6)))
; (defun dublare(l)
; (cond
; ((null l) nil)
; ((numberp (car l)) (cons (* 2 (car l)) (dublare (cdr l))))
; ((atom (car l)) (cons (car l) (dublare (cdr l))))
; (t (cons (dublare (car l)) (dublare (cdr l))))
; )
; )
; (defun dublare(l)
;  (cond
;  ((numberp l) (* 2 l))
;  ((atom l) l)
;  (t (cons (dublare (car l)) (dublare (cdr l))))
;  )
; )


; (defun inordine(l)
; (cond
; ((null l) nil)
; (t (append (inordine (cadr l)) (list (car l)) (inordine (caddr l))))
; ; (t (append (inordine (cadr l)) (cons (car l) (inordine (caddr l)))))
; )
; )

(defun f(l)
  (cond
    ((null l) t)
    (((lambda (v)
      (cond
        ((numberp v) t)
        (t nil)
      )
      )
      (car l)
    ) nil)
  (t (f (cdr l)))
  )
)
