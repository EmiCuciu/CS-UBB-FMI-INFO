function wilkinson(p)
%WILKINSON perturbatie normala N(0, 1e-10) pentru polinomul p
xi = roots(p);
h = plot(real(xi), imag(xi), '.');
set(h, 'Markersize', 15);
hold on
for k = 1:1000
    r = randn(1, length(p));
    pr = p .* (1 + 1e-10*r);
    z = roots(pr);
    h2 = plot(z, 'k.'); 
    set(h2, 'Markersize', 4)
end
axis equal
end
