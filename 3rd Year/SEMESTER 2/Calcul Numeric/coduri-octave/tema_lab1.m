% curatam ecranul si memoria
clc; clear; close all;

% definim domeniul de evaluare [-1, 1]
x = linspace(-1, 1, 200);

% b) Apox Pade pt f(x) = e^x
% Ne trebuie R_1,1 si R_2,2

disp(' b) Aproximare Pade pentru e^x ');

% coef taylor pt e^x => c_n = 1/n!
% R_2,2 necesita m+k = 4, deci avem nevoie de coeficienti pana la c_4

%seria MacLaurin pt e^x
c_exp = zeros(1,5); % 5 elem: c_0,c_1,c_2,c_3,c_4
for i = 0: 4
  c_exp(i+1) = 1 / factorial(i);
end

% R_1,1 (m=1, k=1)
[a_exp_11, b_exp_11] = pade_approx(c_exp, 1, 1);
y_exp_11 = polyval(a_exp_11, x) ./ polyval(b_exp_11, x);
%  ./    =   impartire element cu element p0 / q0 , p1 / q1


% R_2,2 (m=2, k=2)
[a_exp_22, b_exp_22] = pade_approx(c_exp, 2, 2);
y_exp_22 = polyval(a_exp_22, x) ./ polyval(b_exp_22, x);

% valoarea exxacta
y_exp_exact = exp(x); % f(x)

figure(1);
plot(x, y_exp_exact, 'k', 'LineWidth', 2); hold on;
plot(x, y_exp_11, 'r--', 'LineWidth', 1.5);
plot(x, y_exp_22, 'b-', 'LineWidth', 1.5);
title('Aproximare Pade pentru: f(x) = e^x');
legend('e^x (Exact)', 'R_{1,1}', 'R_{2,2}', 'Location', 'northwest');
grid on;
xlabel('x'), ylabel('y');



% c) Aprox Pade pt g(x) = ln(1+x)
% Ne trebuie R_2,2 si R_3,1

disp(' c) Aproximare Pade pentru ln(1+x) ');

% coef taylor pentru ln(1+x) sunt:
% c_0 = 0
% c_n = (-1)^(n-1) / n  ;  n >= 1
% R_3,1 necesita m+k = 4
c_ln = zeros(1,5);
c_ln(1) = 0;
for i = 1:4
  c_ln(i+1) = ((-1)^(i-1)) / i;
end


% R_2,2
[a_ln_22, b_ln_22] = pade_approx(c_ln, 2,2);
y_ln_22 = polyval(a_ln_22, x) ./ polyval(b_ln_22, x);

% R_3,1
[a_ln_31, b_ln_31] = pade_approx(c_ln, 3,1);
y_ln_31 = polyval(a_ln_31, x) ./ polyval(b_ln_31, x);

y_ln_exact = log(1+x);

figure(2);
plot(x, y_ln_exact, 'k', 'LineWidth', 2); hold on;
plot(x, y_ln_22, 'r--', 'LineWidth', 1.5);
plot(x, y_ln_31, 'b-', 'LineWidth', 1.5);
% Setam limite pentru grafic ca sa nu o ia razna la asimptota din x=-1
ylim([-4, 1]);
title('Aproximare Pade pentru: g(x) = ln(1+x)');
legend('ln(1+x) (Exact)', 'R_{2,2}', 'R_{3,1}', 'Location', 'southeast');
grid on;
xlabel('x'), ylabel('y');
