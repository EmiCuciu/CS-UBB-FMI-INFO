function c = my_lsq_poly(x, f, n)

x = x(:);
f = f(:);
A = zeros(length(x), n+1);
for k = 0:n
    A(:, k+1) = x.^k;
end
c = A \ f;
end
