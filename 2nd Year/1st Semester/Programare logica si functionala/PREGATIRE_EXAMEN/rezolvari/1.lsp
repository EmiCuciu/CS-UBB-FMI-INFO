; schimb (l:list, e1:el, e2:el)

; model matematic
; schimb(l,e1,e2) = {l,         l=atom si l!=e1}
;                   {e2,        l=atom si l=e1}
;                   {schimb(l1)U...Uschimb(l1), altfel}

(defun schimb (l e1 e2)
    (cond 
        ((and (atom l) (not(equal l e1))) l)
        ((and (atom l) (equal l e1)) e2)
        (t (mapcar #'(lambda (v)
                    (schimb v e1 e2)
        )
        l
        ))
    )
)




; substituie(l,e,e1,niv) =  {e,     l=atom si l=e si niv%2==0}
;                           {e1,    l=atom si l=e1 si niv%2==1}
;                           {substituie(l1,e,e1,niv+1)U..Usubstituie(ln,e,e1,niv+1), altfel}

(defun substituie(l e e1 niv)
    (cond 
        ((and (atom l) (not (equal l e))) l)
        ((and (atom l) (equal l e) (equal (mod niv 2) 0)) e)
        ((and (atom l) (equal l e) (equal (mod niv 2) 1)) e1)
        (t (mapcar #'(lambda (v)
                    (substituie v e e1 (+ niv 1))
            )
            l
            ))
    )
)

(defun main(l e e1)
    (substituie l e e1 0)
)