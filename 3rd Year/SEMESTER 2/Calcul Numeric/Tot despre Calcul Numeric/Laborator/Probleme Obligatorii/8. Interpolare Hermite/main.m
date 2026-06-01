clear; clc; close all;

fprintf('--- Interpolare Hermite noduri duble (Powell) ---\n');

x  = [0.30, 0.32, 0.35];
f  = [0.29552, 0.31457, 0.34290];     % sin(x)
fd = [0.95534, 0.94924, 0.93937];     % cos(x)

t0 = 0.34;
h  = my_hermite_eval(x, f, fd, t0);

fprintf('  H5f(0.34)  = %.8f\n', h);
fprintf('  sin(0.34)  = %.8f\n', sin(t0));
fprintf('  eroare     = %.2e\n\n', abs(h - sin(t0)));

[z, td] = my_divdiffdn(x, f, fd);
fprintf('  Noduri duble z: '); fprintf('%.2f  ', z); fprintf('\n');
fprintf('  Coeficienti Newton (prima linie td):\n  ');
fprintf('%.7f  ', td(1,:)); fprintf('\n\n');


xx = linspace(x(1), x(end), 400);
hh = my_hermite_eval(x, f, fd, xx);

figure('Name', 'Prob 2: f si polinomul Hermite');
plot(xx, sin(xx), 'k-', 'LineWidth', 2, 'DisplayName', 'f(x) = sin(x)');
hold on;
plot(xx, hh,      'r--','LineWidth', 2, 'DisplayName', 'H_5f (Hermite)');
scatter(x, f, 60, 'bo', 'filled', 'DisplayName', 'noduri (x_k, f(x_k))');
grid on; legend('Location','best'); xlabel('x');
title('Prob 2: f(x)=sin(x) si interpolantul Hermite H_5f');


figure('Name', 'Prob 3: Cubica parametrica Hermite');

subplot(1,2,1);
P0=[0,0]; T0=[1,3];   P1=[2,2]; T1=[1,-1];
my_hermite_cubic(P0, T0, P1, T1);
legend('Location','best'); title('Exemplu 1');

subplot(1,2,2);        
P0=[1,0];  T0=[0,2];
P1=[-1,0]; T1=[0,-2];
my_hermite_cubic(P0, T0, P1, T1);
P0b=[-1,0]; T0b=[0,-2]; P1b=[1,0]; T1b=[0,2];
my_hermite_cubic(P0b, T0b, P1b, T1b);
title('Exemplu 2: curba inchisa');
sgtitle('Prob 3: Cubica parametrica Hermite');