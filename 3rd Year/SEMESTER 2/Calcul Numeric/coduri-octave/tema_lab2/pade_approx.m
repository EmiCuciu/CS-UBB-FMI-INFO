function [a, b] = pade_approx(c, m, k)
  % c - vector cu coef Taylor (c_0, c_1, ... , c_(m+k) )
  % m - gradul numaratorului
  % k - gradul numitorului

  % 1. Aflam coef numitorului (b)
  if k == 0
    b = 1;  % daca numitorul are grad 0, ramane doar seria Taylor
  else
    % construim sistemul C * b_coeffs = r
    C = zeros(k, k);
    r = zeros(k, 1);

    for i = 1:k
      for j = 1:k
        idx = m - j + i;

        if idx >= 0
          C(i,j) = c(idx + 1);
        else
          C(i,j) = 0;  % indice negatic
        endif
      endfor
      r(i) = -c(m + i + 1);
    endfor

    % sistemul r = C * b_coeffs
    % \ rezolva sisteme de ecuatii liniare fara a calcula inversa matricei C, ceea ce este costisitor
    % aflam coef numitorului

    % Ecuatia matriceala este :    C * b = r
    % dar trebuie sa afla vectorul de coeficienti
    % \ rezolva munca de: b = inv(C) * r,
    %     - analizeaza mat C si alege un alg eficient care rezolva sistemul

    b_coeffs = C \ r;
    b = [1; b_coeffs]; % adaugam b_0 = 1 la inceput
  endif

  % 2. Aflam coef numaratorului (a)
  a = zeros(m + 1, 1);
  for j = 0:m
    suma = 0;
    for l = 0:j
      if l <= k  % nu depasim gradul numitorului
        suma = suma + c(j - l + 1) * b(l + 1);
      endif
    endfor
    a(j+1) = suma;
  endfor

  % 3. Inversam vectorii pentru a-i putea folosu cu functia polyval()
  % functia polyval asteapta coeficientii in ordinea: x^n, x^(n-1), ... , x^0
  a = flip(a);
  b = flip(b);
 end
