function wilkinsonu(p)
%WILKINSONU perturbatie uniforma U[0, 1e-10] pentru polinomul p
xi = roots(p);
h = plot(real(xi), imag(xi), '.');
set(h, 'Markersize', 15);
hold on
for k = 1:1000
    r = 1e-10 * rand(1, length(p));
    pr = p + r.*p;
    z = roots(pr);
    h2 = plot(z, 'k.');
    set(h2, 'Markersize', 4)
end
axis equal
end
