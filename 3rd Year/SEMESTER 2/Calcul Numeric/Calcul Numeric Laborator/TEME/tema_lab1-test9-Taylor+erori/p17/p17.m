clc; clear; close all;

x = linspace(-2*pi, 2*pi, 1000);

y_err = cosh(x).^2 - sinh(x).^2;

y_remediu = exp(-x) .* exp(x);
% y_remediu = (cosh(x) - sinh(x)) .* (cosh(x) + sinh(x));

figure(1);
plot(x, y_err, 'b-', 'LineWidth', 1.5);
title('normal (fara zoom)');
xlabel('x');
ylabel('f(x)');
grid on;

figure(2);
plot(x, y_err - 1, 'r-', 'LineWidth', 1.5);
hold on;
plot(x, y_remediu - 1, 'g-', 'LineWidth', 2);


title('Eroare: Anulare Catastrofica (Rosu) vs Remediu (Verde)');
xlabel('x');
ylabel('Eroare (y - 1)');
ylim([-2e-12, 2e-12]);      % centrat in 0
legend('Cu Eroare (Scadere)', 'Remediu (Inmultire)', 'Location', 'best');
grid on;