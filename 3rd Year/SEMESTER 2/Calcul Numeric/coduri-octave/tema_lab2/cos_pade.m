function y = cos_pade(x, m, k)
    % 1. REDUCEREA RANGULUI la intervalul [-pi, pi]
    x_red = mod(x + pi, 2*pi) - pi;

    % 2. Generarea coeficientilor Maclaurin pentru cos(x)
    % cos(x) = 1 - x^2/2! + x^4/4! - x^6/6! ...
    n = m + k;
    c = zeros(1, n + 1);

    for i = 0:n
        if mod(i, 2) == 0 % Daca puterea este para (0, 2, 4...)
            semn = (-1)^(i/2); % Alterneaza semnul: +, -, +, -
            c(i+1) = semn / factorial(i);
        end
    end

    % 3. Apelam functia ta
    [a, b] = pade_approx(c, m, k);

    % 4. Evaluam functia
    y = polyval(a, x_red) ./ polyval(b, x_red);
end
