clear; clc; close all;

fprintf('--- Aproximare discreta CMMP ---\n');

x_d = linspace(-1, 1, 30)';
f1  = @(x) abs(x);            

fprintf('  f(x) = |x|, N=30 puncte uniforme:\n');
fprintf('  %-5s  %-12s\n', 'grad n', '||reziduu||');
for n = [2, 4, 6, 8]
    c = my_lsq_poly(x_d, f1(x_d), n);
    res = norm(my_poly_eval(c, x_d) - f1(x_d));
    fprintf('  %-5d  %-12.6f\n', n, res);
end


xx = linspace(-1, 1, 300);
c4 = my_lsq_poly(x_d, f1(x_d), 4);
c8 = my_lsq_poly(x_d, f1(x_d), 8);

figure('Name', 'Aproximare discreta CMMP');
plot(x_d, f1(x_d), 'ko', 'MarkerSize', 4, 'DisplayName', 'date');  hold on;
plot(xx, f1(xx),   'k-', 'LineWidth', 2, 'DisplayName', 'f(x)=|x|');
plot(xx, my_poly_eval(c4, xx), 'b-', 'LineWidth', 1.5, 'DisplayName', 'grad 4');
plot(xx, my_poly_eval(c8, xx), 'r-', 'LineWidth', 1.5, 'DisplayName', 'grad 8');
grid on; legend; xlabel('x');
title('Aproximare CMMP — f(x)=|x|');
fprintf('\n');


fprintf('--- asteroid ---\n');

x_a = [-1.024940 -0.949898 -0.866114 -0.773392 -0.671372 ...
        -0.559524 -0.437067 -0.302909 -0.159493 -0.007464]';
y_a = [-0.389269 -0.322894 -0.265256 -0.216557 -0.177152 ...
        -0.147582 -0.128618 -0.121353 -0.127348 -0.148895]';

A_ell = [y_a.^2, x_a.*y_a, x_a, y_a, ones(10,1)];
b_rhs = x_a.^2;
p_ell = A_ell \ b_rhs;              
res_ell = norm(A_ell*p_ell - b_rhs);
SS_tot  = norm(b_rhs - mean(b_rhs))^2;
R2_ell  = 1 - res_ell^2 / SS_tot;

fprintf('  Model elipsoidal: x^2 = a*y^2 + b*x*y + c*x + d*y + e\n');
fprintf('  Parametri: a=%.4f  b=%.4f  c=%.4f  d=%.4f  e=%.4f\n', p_ell);
fprintf('  Reziduu = %.4e,  R^2 = %.6f\n\n', res_ell, R2_ell);

A_par  = [y_a, ones(10,1)];
p_par  = A_par \ b_rhs;             % [a, e]
res_par = norm(A_par*p_par - b_rhs);
R2_par  = 1 - res_par^2 / SS_tot;

fprintf('  Model parabolic: x^2 = a*y + e\n');
fprintf('  Parametri: a=%.4f  e=%.4f\n', p_par);
fprintf('  Reziduu = %.4e,  R^2 = %.6f\n\n', res_par, R2_par);

fprintf('  Concluzie: R^2_ell=%.4f > R^2_par=%.4f => modelul elipsoidal e mai probabil\n\n', R2_ell, R2_par);

figure('Name', 'Orbita asteroid');
plot(x_a, y_a, 'ro', 'MarkerSize', 8, 'MarkerFaceColor','r', 'DisplayName', 'observatii'); hold on;
a_e=p_ell(1); b_e=p_ell(2); c_e=p_ell(3); d_e=p_ell(4); e_e=p_ell(5);
fimplicit(@(X,Y) X.^2 - a_e*Y.^2 - b_e*X.*Y - c_e*X - d_e*Y - e_e, ...
          [-1.3 0.3 -0.5 0.2], 'b-', 'LineWidth', 2, 'DisplayName', 'Elipsa');
a_p=p_par(1); e_p=p_par(2);
fimplicit(@(X,Y) X.^2 - a_p*Y - e_p, ...
          [-1.3 0.3 -0.5 0.2], 'g--', 'LineWidth', 2, 'DisplayName', 'Parabola');
grid on; legend; axis equal;
title('Orbita asteroid — elipsa vs parabola');


fprintf('--- Populatia SUA ---\n');

year = [1900 1910 1920 1930 1940 1950 1960 1970 1980 1990 2000 2010 2020]';
pop  = [75.995 91.972 105.710 123.200 131.670 150.700 179.320 ...
        203.210 226.510 249.630 281.420 308.790 350.686]';
t    = (year - 1900) / 10;          


A_pol = [ones(size(t)), t, t.^2, t.^3];
c_pol = A_pol \ pop;
res_pol = norm(A_pol*c_pol - pop);

fprintf('  Model polinomial grad 3: y = c0+c1*t+c2*t^2+c3*t^3\n');
fprintf('  c0=%.4f  c1=%.4f  c2=%.4f  c3=%.4f\n', c_pol);
fprintf('  ||reziduu|| = %.4f\n\n', res_pol);

A_exp = [ones(size(t)), t];
c_exp = A_exp \ log(pop);
K_exp = exp(c_exp(1));  lam = c_exp(2);
res_exp = norm(K_exp*exp(lam*t) - pop);

fprintf('  Model exponential: y = K*exp(lambda*t)\n');
fprintf('  K=%.4f,  lambda=%.4f  (per deceniu)\n', K_exp, lam);
fprintf('  ||reziduu|| = %.4f\n\n', res_exp);

t_pred = ([1975, 2015] - 1900) / 10;
A_pred = [ones(2,1), t_pred', t_pred'.^2, t_pred'.^3];
y_pol_pred = A_pred * c_pol;
y_exp_pred = K_exp * exp(lam * t_pred);

fprintf('  Predictii:\n');
fprintf('  %-8s  %-14s  %-14s\n', 'Anul', 'Model polinomial', 'Model exponential');
fprintf('  %-8s  %-14.3f  %-14.3f\n', '1975', y_pol_pred(1), y_exp_pred(1));
fprintf('  %-8s  %-14.3f  %-14.3f\n', '2015', y_pol_pred(2), y_exp_pred(2));

tt = linspace(0, 13, 300);
y_pol_fit = [ones(size(tt')), tt', tt'.^2, tt'.^3] * c_pol;
y_exp_fit = K_exp * exp(lam * tt);

figure('Name', 'Populatia SUA');
plot(year, pop, 'ko', 'MarkerSize', 8, 'MarkerFaceColor','k', 'DisplayName','date reale'); hold on;
plot(1900 + tt*10, y_pol_fit, 'b-',  'LineWidth', 2, 'DisplayName', 'Polinomial gr.3');
plot(1900 + tt*10, y_exp_fit, 'r--', 'LineWidth', 2, 'DisplayName', 'Exponential');
scatter([1975,2015], y_pol_pred, 80, 'b^', 'filled', 'DisplayName', 'Predictii pol.');
scatter([1975,2015], y_exp_pred, 80, 'rv', 'filled', 'DisplayName', 'Predictii exp.');
grid on; legend('Location','northwest');
xlabel('Anul'); ylabel('Populatie (milioane)');
title('Populatia SUA — CMMP');
