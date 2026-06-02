function [x, ni] = Jacobi_sparse(A, b, x0, err, nitmax)
    if nargin < 5, nitmax = 1000; end
    if nargin < 4, err = 1e-8; end
    if nargin < 3, x0 = zeros(size(b)); end
    
    n = size(A, 1);
    
    if issparse(A)
        M = spdiags(diag(A), 0, n, n);
    else
        M = diag(diag(A));
    end
    N = M - A;
    
    x = x0(:);
    for i = 1:nitmax
       x0 = x;
       x = M \ (N * x0 + b);
       
       if norm(x - x0, inf) < err
          ni = i;
          return;
       end
    end
    ni = nitmax;
    warning('Jacobi: numarul maxim de iteratii a fost depasit');
end