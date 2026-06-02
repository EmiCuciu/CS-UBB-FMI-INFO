pkg load symbolic
format long
clear; clc;

alpha = 2;
beta = 2;
aJ = (beta  - 1) / 2; 
bJ = (alpha - 1) / 2;
factor = 1 / 2^((alpha + beta) / 2);

g = @(x) x ./ (2 + x);   

fprintf('Parametrii Jacobi: alpha_J = %.4f, beta_J = %.4f\n', aJ, bJ);
fprintf('Factor = 1/2^{(a+b)/2} = %.6f\n\n', factor);

n0 = 3;
[gn, gc] = Gauss_Jacobi(n0, aJ, bJ);
vI_prev  = factor * vquad(gn, gc, g);

n_final = n0;
vI = vI_prev;
for n = n0+1 : 100
    [gn, gc] = Gauss_Jacobi(n, aJ, bJ);
    vI_new   = factor * vquad(gn, gc, g);
    if abs(vI_new - vI_prev) < 1e-12
        n_final = n;
        vI = vI_new;
        fprintf('  Convergenta la n = %d\n', n_final);
        break;
    end
    vI_prev = vI_new;
end

fprintf('\nValoare aproximativa:\n');
fprintf('  I(Gauss-Jacobi, n=%d) = %.15f\n', n_final, vI);

syms t
f = cos(t) / (2 + cos(t));
ve = double(vpa(int(f * (cos(t/2))^alpha * (sin(t/2))^beta, t, 0, sym(pi))));

fprintf('\nValoarea exacta (simbolic):\n');
fprintf('  ve = %.15f\n', ve);

fprintf('\nEroare:\n');
fprintf('  |err| = %.6e\n', abs(ve - vI));