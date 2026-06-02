pkg load symbolic
format long
clear; clc;

f = @(x) exp(1/4) * (x - 1/2).^2 .* cos(x - 1/2);

eps = 1e-7;
n0 = 5;
[gn, gc] = Gauss_Hermite(n0);
vI_prev  = vquad(gn, gc, f);

n_final = n0;
vI = vI_prev;
for n = n0+1 : 60
    [gn, gc] = Gauss_Hermite(n);
    vI_new = vquad(gn, gc, f);
    if abs(vI_new - vI_prev) < eps
        n_final = n;
        vI = vI_new;
        fprintf('  Convergenta la n = %d\n', n_final);
        break;
    end
    vI_prev = vI_new;
end

fprintf('\nValoare aproximativa:\n');
fprintf('  I(Gauss-Hermite, n=%d) = %.15f\n', n_final, vI);

syms x 
ve = double(vpa(int(exp(-x^2) * exp(sym(1)/4) * (x - sym(1)/2)^2 * cos(x - sym(1)/2), x, -sym(Inf), sym(Inf))));

fprintf('\nValoarea exacta (simbolic):\n');
fprintf('  ve = %.15f\n', ve);

fprintf('\nEroare:\n');
fprintf('  |err| = %.6e\n', abs(ve - vI));