pkg load symbolic
format long
clear; clc;

n = 10;
[g_n, g_c] = Gauss_Legendre(n);

f1 = @(z) pi/2 * sin(pi^2/4 * (1+z).^2);
f2 = @(z) pi/2 * cos(pi^2/4 * (1+z).^2);

vI1 = vquad(g_n, g_c, f1);
vI2 = vquad(g_n, g_c, f2);

fprintf('Noduri si coeficienti Gauss-Legendre (n=10):\n');
disp([g_n, g_c']);

fprintf('Valori aproximative:\n');
fprintf('  I1(Gauss-Legendre, n=%d) = %.15f\n', n, vI1);
fprintf('  I2(Gauss-Legendre, n=%d) = %.15f\n', n, vI2);

syms t
ve1 = double(vpa(int(sin(t^2), t, 0, sym(pi))));
ve2 = double(vpa(int(cos(t^2), t, 0, sym(pi))));

fprintf('\nValori exacte (simbolic cu vpa):\n');
fprintf('  ve1 = %.15f\n', ve1);
fprintf('  ve2 = %.15f\n', ve2);

fprintf('\nErori:\n');
fprintf('  |err_I1| = %.6e\n', abs(ve1 - vI1));
fprintf('  |err_I2| = %.6e\n', abs(ve2 - vI2));
