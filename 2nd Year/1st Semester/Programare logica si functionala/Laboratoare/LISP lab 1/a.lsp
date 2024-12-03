;8.
;   a) Sa se elimine elementul de pe pozitia a n-a a unei liste liniare.
;
;   b) Definiti o functie care determina succesorul unui numar reprezentat cifra 
;  cu cifra intr-o lista. De ex: (1 9 3 5 9 9) --> (1 9 3 6 0 0)
;
;c) Sa se construiasca multimea atomilor unei liste.Exemplu: (1 (2 (1 3 (2 4) 3) 1) (1 4)) ==> (1 2 3 4)
;
;d) Sa se scrie o functie care testeaza daca o lista liniara este o multime.

; a)

(defun remove-nth (n lst)
  (if (or (null lst) (<= n 0))
      lst  ; Dacă lista este goală sau n <= 0, returnează lista
      (if (= n 1)
          (cdr lst)  ; Dacă n este 1, returnează restul listei fără primul element
          (cons (car lst) (remove-nth (1- n) (cdr lst))))))  ; Altfel, construiește lista fără elementul de pe poziția n

;(remove-nth 2 '(a b c d e))   => (a c d e)

;(remove-nth 5 '(a b c d e f g h))