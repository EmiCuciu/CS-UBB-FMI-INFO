function [x, ni] = relax_sparse(A, b, omega, x0, err, nitmax)
    if nargin < 6, nitmax = 1000; end
    if nargin < 5, err = 1e-8; end
    if nargin < 4, x0 = zeros(size(b)); end
    if nargin < 3, omega = 1.2; end

    n = size(A, 1);
    
    if issparse(A)
        D = spdiags(diag(A), 0, n, n);
    else
        D = diag(diag(A));
    end
    
    M = (1/omega) * D + tril(A, -1);
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
    warning('SOR: numarul maxim de iteratii a fost depasit');
end