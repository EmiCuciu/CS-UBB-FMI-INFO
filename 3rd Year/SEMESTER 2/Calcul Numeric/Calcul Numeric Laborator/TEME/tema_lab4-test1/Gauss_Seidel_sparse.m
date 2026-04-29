function [x, ni] = Gauss_Seidel_sparse(A, b, x0, err, nitmax)
    if nargin < 5, nitmax = 1000; end
    if nargin < 4, err = 1e-8; end
    if nargin < 3, x0 = zeros(size(b)); end 

    x = x0(:);
    
    M = tril(A); 
    N = M - A;

    for i = 1:nitmax
       x0 = x;
       x = M \ (N * x0 + b);
       
       if norm(x - x0, inf) < err
          ni = i;
          return;
       end
    end
    ni = nitmax;
    warning('Gauss-Seidel: numarul maxim de iteratii a fost depasit');
end