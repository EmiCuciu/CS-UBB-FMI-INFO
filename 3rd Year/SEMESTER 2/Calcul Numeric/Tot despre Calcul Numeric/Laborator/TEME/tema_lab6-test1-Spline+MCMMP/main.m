clc; clear; close all;

f = @(x) x + sin(x.^2);
df = @(x) 1 + 2 * x .* cos(x.^2);
d2f = @(x) 2 * cos(x.^2) - 4 * x.^2 .* sin(x.^2);

A_int = -pi; B_int = pi;
n = 10;
xx = linspace(A_int, B_int, 500);
yy_exact = f(xx);

x_eq = linspace(A_int, B_int, n+1);
y_eq = f(x_eq);

k = 0:n;
x_ceb2 = cos(k * pi / n); 
x_ceb2 = sort((A_int + B_int)/2 + (B_int - A_int)/2 * x_ceb2);
y_ceb2 = f(x_ceb2);

figure('Name', '1. Spline Natural');
[a_nat_eq, b_nat_eq, c_nat_eq, d_nat_eq] = spline_natural(x_eq, y_eq);
[a_nat_cb, b_nat_cb, c_nat_cb, d_nat_cb] = spline_natural(x_ceb2, y_ceb2);
hold on; grid on; title('1. Spline Natural');
plot(xx, yy_exact, 'k-', 'LineWidth', 1.5);
plot(xx, eval_spline(x_eq, a_nat_eq, b_nat_eq, c_nat_eq, d_nat_eq, xx), 'b--');
plot(xx, eval_spline(x_ceb2, a_nat_cb, b_nat_cb, c_nat_cb, d_nat_cb, xx), 'r-.');
legend('Exact', 'Echidistant', 'Cebisev II', 'Location', 'best');

figure('Name', '2. Spline Complet');
dfa = df(A_int); dfb = df(B_int);
[a_cl_eq, b_cl_eq, c_cl_eq, d_cl_eq] = spline_complet(x_eq, y_eq, dfa, dfb);
[a_cl_cb, b_cl_cb, c_cl_cb, d_cl_cb] = spline_complet(x_ceb2, y_ceb2, dfa, dfb);
hold on; grid on; title('2. Spline Complet (Clamped)');
plot(xx, yy_exact, 'k-', 'LineWidth', 1.5);
plot(xx, eval_spline(x_eq, a_cl_eq, b_cl_eq, c_cl_eq, d_cl_eq, xx), 'b--');
plot(xx, eval_spline(x_ceb2, a_cl_cb, b_cl_cb, c_cl_cb, d_cl_cb, xx), 'r-.');
legend('Exact', 'Echidistant', 'Cebisev II', 'Location', 'best');

figure('Name', '3. Reproducerea D2');
d2fa = d2f(A_int); d2fb = d2f(B_int);
[a_d2_eq, b_d2_eq, c_d2_eq, d_d2_eq] = spline_reprod_d2(x_eq, y_eq, d2fa, d2fb);
[a_d2_cb, b_d2_cb, c_d2_cb, d_d2_cb] = spline_reprod_d2(x_ceb2, y_ceb2, d2fa, d2fb);
hold on; grid on; title('3. Reproducerea D2');
plot(xx, yy_exact, 'k-', 'LineWidth', 1.5);
plot(xx, eval_spline(x_eq, a_d2_eq, b_d2_eq, c_d2_eq, d_d2_eq, xx), 'b--');
plot(xx, eval_spline(x_ceb2, a_d2_cb, b_d2_cb, c_d2_cb, d_d2_cb, xx), 'r-.');
legend('Exact', 'Echidistant', 'Cebisev II', 'Location', 'best');

figure('Name', '4. Spline deBoor');
[a_db_eq, b_db_eq, c_db_eq, d_db_eq] = spline_deboor(x_eq, y_eq);
[a_db_cb, b_db_cb, c_db_cb, d_db_cb] = spline_deboor(x_ceb2, y_ceb2);
hold on; grid on; title('4. Spline deBoor (Not-a-knot)');
plot(xx, yy_exact, 'k-', 'LineWidth', 1.5);
plot(xx, eval_spline(x_eq, a_db_eq, b_db_eq, c_db_eq, d_db_eq, xx), 'b--');
plot(xx, eval_spline(x_ceb2, a_db_cb, b_db_cb, c_db_cb, d_db_cb, xx), 'r-.');
legend('Exact', 'Echidistant', 'Cebisev II', 'Location', 'best');

figure('Name', 'MCMMP Discreta');

k_I = 0:n;
x_ceb1 = cos((2*k_I + 1) * pi / (2*n + 2));
x_ceb1 = sort((A_int+B_int)/2 + (B_int-A_int)/2 * x_ceb1);
y_ceb1 = f(x_ceb1);

grad = 5; 
coefs = mcmmp_discreta(x_ceb1, y_ceb1, grad);

y_mcmmp = polyval(coefs, xx);

plot(xx, yy_exact, 'k-', 'LineWidth', 1.5); hold on;
plot(xx, y_mcmmp, 'm--', 'LineWidth', 1.5);
plot(x_ceb1, y_ceb1, 'mo', 'MarkerFaceColor', 'm');
title(['MCMMP Discreta, Noduri Cebisev I, Polinom Grad = ', num2str(grad)]);
legend('Exact', 'MCMMP', 'Noduri Cebisev I', 'Location', 'best');
grid on;