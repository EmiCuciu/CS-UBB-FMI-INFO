;8.
;   a) Sa se elimine elementul de pe pozitia a n-a a unei liste liniare.
;
;   b) Definiti o functie care determina succesorul unui numar reprezentat cifra 
;  cu cifra intr-o lista. De ex: (1 9 3 5 9 9) --> (1 9 3 6 0 0)
;
;c) Sa se construiasca multimea atomilor unei liste.Exemplu: (1 (2 (1 3 (2 4) 3) 1) (1 4)) ==> (1 2 3 4)
;
;d) Sa se scrie o functie care testeaza daca o lista liniara este o multime.


(defun succesor (num-list)      ; b)
  (labels ((add-one (lst)           ; Adaugă 1 la ultimul element
             (if (null lst)         ; Dacă lista este goală
                 '(1)               ; Returnează 1
                 (let ((sum (+ 1 (car lst))))   ; Adună 1 la ultimul element
                   (if (< sum 10)               ; Dacă suma este mai mică decât 10
                       (cons sum (cdr lst))     ; Returnează suma și restul listei
                       (cons 0 (add-one (cdr lst))))))))    ; Altfel, adaugă 0 și continuă cu restul listei
    (reverse (add-one (reverse num-list)))))    ; Returnează lista inversată pentru a avea cifrele în ordinea corectă

; (succesor '(1 2 3)) => (1 2 4)
; (succesor '(9 9 9)) => (1 0 0 0) 