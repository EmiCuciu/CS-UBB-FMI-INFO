function L = my_lagrange_basis(x, xx)

m1 = length(x);
L  = zeros(numel(xx), m1);

for k = 1:m1
    ek    = zeros(1, m1);
    ek(k) = 1;
    L(:,k) = my_lagrange(x, ek, xx(:));
end
end
