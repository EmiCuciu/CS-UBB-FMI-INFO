function [z, ni] = Gauss_Seidel_sparse(A, b, x0, err, nitmax)
    if nargin < 5, nitmax = 100; end
    if nargin < 4, err = 1e-5; end
    if nargin < 3, x0 = zeros(length(b), 1); end

    x = x0;
    M = tril(A); % tril mentine structura rara
    N = M - A;

    for i = 1:nitmax
       xn = M \ (N * x + b);
       if norm(xn - x, inf) < err * norm(xn, inf)
          z = xn;
          ni = i;
          return;
       end
       x = xn;
    end
    error('Numarul maxim de iteratii a fost depasit in Gauss-Seidel');
end