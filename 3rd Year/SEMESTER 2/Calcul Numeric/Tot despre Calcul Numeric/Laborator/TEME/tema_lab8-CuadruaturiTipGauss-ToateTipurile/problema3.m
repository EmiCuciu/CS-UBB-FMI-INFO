pkg load symbolic
format long
clear; clc;


f = @(z) (1/4) * sin((z + 3) / 2);   

eps = 1e-10;
n0 = 2;
[gn, gc] = Gauss_Cheb2(n0);
vI_prev = vquad(gn, gc, f);

fprintf('Cautam n minim cu |DeltaI| < eps = %.0e:\n', eps);
n_final = n0;
vI = vI_prev;
for n = n0+1 : 200
    [gn, gc] = Gauss_Cheb2(n);
    vI_new   = vquad(gn, gc, f);
    if abs(vI_new - vI_prev) < eps
        n_final = n;
        vI = vI_new;
        fprintf('  Convergenta la n = %d\n', n_final);
        break;
    end
    vI_prev = vI_new;
end

fprintf('\nValoare aproximativa:\n');
fprintf('  I(Gauss-Cheb2, n=%d) = %.15f\n', n_final, vI);

syms t
ve = double(vpa(int(sqrt(3*t - t^2 - 2) * sin(t), t, 1, 2)));

fprintf('\nValoarea exacta (simbolic):\n');
fprintf('  ve = %.15f\n', ve);

fprintf('\nEroare:\n');
fprintf('  |err| = %.6e\n', abs(ve - vI));
