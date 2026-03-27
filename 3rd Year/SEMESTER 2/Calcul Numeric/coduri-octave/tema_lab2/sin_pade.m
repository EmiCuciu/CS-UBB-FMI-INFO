function y = sin_pade(x, m, k)
    % 1. REDUCEREA RANGULUI la intervalul [-pi, pi]
    % Aceasta previne erorile catastrofale pentru x foarte mare (ex: x = 10*pi)
    x_red = mod(x + pi, 2*pi) - pi;

    % 2. Generarea coeficientilor Maclaurin pentru sin(x)
    % sin(x) = x - x^3/3! + x^5/5! - x^7/7! ...
    n = m + k; % Avem nevoie de coeficienti pana la gradul m+k
    c = zeros(1, n + 1);

    for i = 0:n
        % Indicii in Octave incep de la 1, deci c(1) corespunde lui x^0
        if mod(i, 2) ~= 0 % Daca puterea este impara (1, 3, 5...)
            semn = (-1)^((i-1)/2); % Alterneaza semnul: +, -, +, -
            c(i+1) = semn / factorial(i);
        end
        % Pentru puteri pare, c(i+1) ramane 0, cum a fost initializat
    end

    % 3. Apelam "motorul" tau din laboratorul 1
    [a, b] = pade_approx(c, m, k);

    % 4. Evaluam functia rationala Pade
    % polyval evalueaza polinomul dat de coeficientii a si b
    % Folosim x_red in loc de x !
    y = polyval(a, x_red) ./ polyval(b, x_red);
end
