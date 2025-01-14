(defun nivel(l x niv)
    (cond
        ((null l) nil)
        ((and (atom l) (equal l x)) niv)
        ((atom l) nil)
        ((equal (car l) x) niv)
        (t (or (nivel (cadr l) x (+ niv 1))
                (nivel (caddr l) x (+ niv 1))))
    )
)

(defun main(l x)
    (nivel l x 0)
)