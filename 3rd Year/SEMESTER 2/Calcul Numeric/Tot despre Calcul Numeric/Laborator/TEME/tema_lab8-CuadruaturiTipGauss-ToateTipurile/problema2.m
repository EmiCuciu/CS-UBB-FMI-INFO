pkg load symbolic
format long
clear; clc;


f = @(x) cos(2*x); 

eps = 1e-10;
n0 = 2;
[gn, gc] = Gauss_Cheb1(n0);
vI_prev  = vquad(gn, gc, f);

fprintf('Cautam n minim cu |DeltaI| < eps = %.0e:\n', eps);
n_final = n0;
vI = vI_prev;
for n = n0+1 : 200
    [gn, gc] = Gauss_Cheb1(n);
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
fprintf('  I(Gauss-Cheb1, n=%d) = %.15f\n', n_final, vI);

syms x
ve2 = double(vpa(int(cos(2*x) / sqrt(1 - x^2), x, -1, 1)));

fprintf('\nValoarea exacta (simbolic, = pi*J0(2)):\n');
fprintf('  ve = %.15f\n', ve2);

fprintf('\nEroare:\n');
fprintf('  |err| = %.6e\n', abs(ve2 - vI));
