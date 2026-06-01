function R = my_cholesky(A)

m = size(A, 1);

for k = 1:m

    if A(k, k) <= 0
        error('Matricea nu este HPD! Pivotul A(%d,%d) = %.4f <= 0', k, k, A(k,k));
    end

    for j = k+1:m
        A(j, j:m) = A(j, j:m) - A(k, j:m) * A(k, j) / A(k, k);
    end
    A(k, k:m) = A(k, k:m) / sqrt(A(k, k));
end

R = triu(A);

end
