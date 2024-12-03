;8.
;   a) Sa se elimine elementul de pe pozitia a n-a a unei liste liniare.
;
;   b) Definiti o functie care determina succesorul unui numar reprezentat cifra 
;  cu cifra intr-o lista. De ex: (1 9 3 5 9 9) --> (1 9 3 6 0 0)
;
;c) Sa se construiasca multimea atomilor unei liste.Exemplu: (1 (2 (1 3 (2 4) 3) 1) (1 4)) ==> (1 2 3 4)
;
;d) Sa se scrie o functie care testeaza daca o lista liniara este o multime.

(defun my-member (elem lst)
  (cond ((null lst) nil)                     ; Dacă lista este goală, returnează nil
        ((equal elem (car lst)) t)           ; Dacă elementul este egal cu primul element din listă, returnează t
        (t (my-member elem (cdr lst)))))     ; Altfel, verifică restul listei


(defun superficializeaza (lst)
  (cond ((null lst) nil)  ; Lista goală devine nil
        ((atom (car lst)) ; Dacă primul element este atom
         (cons (car lst) (superficializeaza (cdr lst))))  ; Adaugă și continuă cu restul
        (t (append (superficializeaza (car lst)) (superficializeaza (cdr lst)))))) ; Dacă este listă, aplatizează recursiv

;(superficializeaza '(1 (2 (1 3 (2 4) 3) 1) (1 4))) => (1 2 1 3 2 4 3 1 1 4)

(defun elimina-duplicatele (lst)
  (if (null lst)
      nil
      (if (my-member (car lst) (cdr lst))
          (elimina-duplicatele (cdr lst))
          (cons (car lst) (elimina-duplicatele (cdr lst))))))

;(elimina-duplicatele '(1 2 1 3 2 4 3 1 1 4)) => (1 2 3 4)


(defun atomi-unici (lst)
  (elimina-duplicatele (superficializeaza lst)))

; (atomi-unici '(1 (2 (1 3 (2 4) 3) 1) (1 4)))   => (2 1 3 4)
; (atomi-unici '()) => NIL; 