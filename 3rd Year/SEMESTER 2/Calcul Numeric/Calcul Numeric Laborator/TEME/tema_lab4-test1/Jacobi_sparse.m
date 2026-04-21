function [x, ni] = Jacobi_sparse(A, b, x0, err, nitmax)
    if nargin < 5, nitmax = 100; end
    if nargin < 4, err = 1e-5; end
    if nargin < 3, x0 = zeros(size(b)); end
    
    n = size(A, 1);
    M = spdiags(diag(A), 0, n, n);
    N = M - A;
    
    x = x0(:);
    for i = 1:nitmax
       x0 = x;
       x = M \ (N * x0 + b);
       if norm(x - x0, inf) < err * norm(x, inf)
          ni = i;
          return;
       end
    end
    error('Numarul maxim de iteratii a fost depasit in Jacobi');
end