;8.
;   a) Sa se elimine elementul de pe pozitia a n-a a unei liste liniare.
;
;   b) Definiti o functie care determina succesorul unui numar reprezentat cifra 
;  cu cifra intr-o lista. De ex: (1 9 3 5 9 9) --> (1 9 3 6 0 0)
;
;c) Sa se construiasca multimea atomilor unei liste.Exemplu: (1 (2 (1 3 (2 4) 3) 1) (1 4)) ==> (1 2 3 4)
;
;d) Sa se scrie o functie care testeaza daca o lista liniara este o multime.

; d) 
(defun my-member (elem lst)
  (cond ((null lst) nil)                     ; Dacă lista este goală, returnează nil
        ((equal elem (car lst)) t)           ; Dacă elementul este egal cu primul element din listă, returnează t
        (t (my-member elem (cdr lst)))))     ; Altfel, verifică restul listei

; (my-member 3 '(1 2 3 4 5))  => t
; (my-member 6 '(1 2 3 4 5))  => nil


(defun is-set (lst)
  (cond ((null lst) t)                      ; Dacă lista este goală, este mulțime
        ((my-member (car lst) (cdr lst)) nil)  ; Dacă primul element este în restul listei, nu este mulțime
        (t (is-set (cdr lst)))))            ; Altfel, verifică restul listei

; (is-set '(1 2 3 4 5))   => t
; (is-set '(1 2 3 4 1))   => nil
