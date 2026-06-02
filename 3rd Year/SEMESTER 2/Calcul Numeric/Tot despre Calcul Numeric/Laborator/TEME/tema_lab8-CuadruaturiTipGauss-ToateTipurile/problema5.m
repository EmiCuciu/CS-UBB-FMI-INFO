pkg load symbolic
format long
clear; clc;


function val = intGL5(x, y)
    [g_n, g_c] = Gauss_Laguerre(8);
    val = vquad(g_n, g_c, @(u) 1 ./ (x*y + u));
end

x_test = 2;
y_test = 5;

vI = intGL5(x_test, y_test);
fprintf('Valoare aproximativa (Gauss-Laguerre, 8 noduri):\n');
fprintf('  I(x=%g, y=%g) = %.15f\n', x_test, y_test, vI);

syms t
ve = double(vpa(int(exp(-x_test*t) / (y_test + t), t, 0, sym(Inf))));

fprintf('\nValoarea exacta (simbolic):\n');
fprintf('  ve = %.15f\n', ve);

fprintf('\nEroare:\n');
fprintf('  |err| = %.6e\n', abs(ve - vI));
