% Curatam memoria
clc; clear; close all;

% Definim un interval mare pentru x, ca sa testam reducerea rangului
% De exemplu de la -4*pi la 4*pi
x = linspace(-4*pi, 4*pi, 400);

% Gradele pentru numitor si numarator
m = 4;
k = 4;

% Calculam valorile folosind functiile noastre
y_sin_pade = sin_pade(x, m, k);
y_cos_pade = cos_pade(x, m, k);

% Valorile exacte din MATLAB
y_sin_exact = sin(x);
y_cos_exact = cos(x);

% ===== GRAFIC SINUS =====
figure(1);
plot(x, y_sin_exact, 'k', 'LineWidth', 2); hold on;
plot(x, y_sin_pade, 'r--', 'LineWidth', 1.5);
title(sprintf('Aproximare Padé R_{%d,%d} pentru sin(x)', m, k));
legend('sin(x) exact', 'Padé sin(x)', 'Location', 'best');
grid on; xlabel('x'); ylabel('y');
ylim([-2 2]); % Limitam axa y ca sa arate frumos

% ===== GRAFIC COSINUS =====
figure(2);
plot(x, y_cos_exact, 'k', 'LineWidth', 2); hold on;
plot(x, y_cos_pade, 'b--', 'LineWidth', 1.5);
title(sprintf('Aproximare Padé R_{%d,%d} pentru cos(x)', m, k));
legend('cos(x) exact', 'Padé cos(x)', 'Location', 'best');
grid on; xlabel('x'); ylabel('y');
ylim([-2 2]);

% Testam valorile extreme cerute in problema 2 din PDF
x_test = 10 * pi;
fprintf('Test pentru x = 10*pi:\n');
fprintf('sin_pade(10*pi) = %f (Exact: %f)\n', sin_pade(x_test, m, k), sin(x_test));
fprintf('cos_pade(10*pi) = %f (Exact: %f)\n', cos_pade(x_test, m, k), cos(x_test));
