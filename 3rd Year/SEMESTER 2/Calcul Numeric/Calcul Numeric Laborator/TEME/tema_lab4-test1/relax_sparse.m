function [x, ni] = relax_sparse(A, b, omega, x0, err, nitmax)
    if nargin < 6, nitmax = 100; end
    if nargin < 5, err = 1e-5; end
    if nargin < 4, x0 = zeros(size(b)); end

    n = size(A, 1);
    % Folosim spdiags pentru matricea diagonala D
    M = (1/omega) * spdiags(diag(A), 0, n, n) + tril(A, -1);
    N = M - A;

    x = x0;
    for i = 1:nitmax
        x0 = x;
        x = M \ (N * x0 + b);
        if norm(x - x0, inf) < err * norm(x, inf)
            ni = i;
            return;
        end
    end
    error('Numarul maxim de iteratii a fost depasit in SOR');
end