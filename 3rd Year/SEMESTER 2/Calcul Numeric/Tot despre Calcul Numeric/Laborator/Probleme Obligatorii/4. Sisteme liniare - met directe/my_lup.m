function [L, U, P] = my_lup(A)
m = size(A, 1);
piv = 1:m;   

for k = 1:m-1

    [~, idx] = max(abs(A(piv(k:m), k)));
    idx_global = idx + k - 1;   

    if idx_global ~= k
        piv([k, idx_global]) = piv([idx_global, k]);
    end

    if A(piv(k), k) == 0
        error('Matricea este singulara!')
    end

    lin_sub = k+1:m;   

    A(piv(lin_sub), k) = A(piv(lin_sub), k) / A(piv(k), k);

    A(piv(lin_sub), lin_sub) = A(piv(lin_sub), lin_sub) ...
        - A(piv(lin_sub), k) * A(piv(k), lin_sub);
end

A_reord = A(piv, :);

U = triu(A_reord);

L = tril(A_reord, -1) + eye(m);

P = zeros(m);
for i = 1:m
    P(i, piv(i)) = 1;
end

end
