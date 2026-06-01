clear; clc; close all;

fprintf('--- Problema 1: PIL clasic ---\n');

f  = @(x) exp(x.^2 - 1);
x  = [1.0, 1.1, 1.2, 1.3, 1.4];
y  = f(x);
x0 = 1.25;

val = my_lagrange(x, y, x0);
fprintf('  L4f(1.25) = %.8f\n', val);
fprintf('  f(1.25)   = %.8f\n', f(x0));
fprintf('  eroare    = %.2e\n\n', abs(val - f(x0)));

fprintf('--- Problema 2: Polinoame fundamentale ---\n');

m  = 4;
x2 = linspace(-1, 1, m+1);
xx = linspace(-1, 1, 400);
L  = my_lagrange_basis(x2, xx);

figure('Name', 'Prob 2: Polinoame fundamentale');
hold on;
for k = 1:m+1
    plot(xx, L(:,k), 'LineWidth', 2, 'DisplayName', sprintf('ell_%d', k-1));
end
plot(x2, ones(size(x2)),  'k^', 'MarkerFaceColor', 'k', 'DisplayName', 'ell_k(x_k)=1');
plot(x2, zeros(size(x2)), 'ko', 'MarkerFaceColor', 'w', 'DisplayName', 'ell_k(x_j)=0');
yline(0, 'k-',  'LineWidth', 0.5, 'HandleVisibility', 'off');
yline(1, 'k--', 'LineWidth', 0.5, 'HandleVisibility', 'off');
grid on; legend('Location', 'best');
xlabel('x'); title(sprintf('Polinoame fundamentale Lagrange (m=%d)', m));

fprintf('--- Problema 3: f si Lmf ---\n');

f3  = @(x) 1./(1 + x.^2);
a3  = -5; b3 = 5;
xx3 = linspace(a3, b3, 500);

figure('Name', 'Prob 3: f si Lmf');

subplot(1,2,1); hold on;
title('Noduri uniforme');
plot(xx3, f3(xx3), 'k-', 'LineWidth', 2, 'DisplayName', 'f(x)');
for m3 = [6, 10, 14]
    x3 = linspace(a3, b3, m3+1);
    plot(xx3, my_lagrange(x3, f3(x3), xx3), 'LineWidth', 1.5, ...
         'DisplayName', sprintf('L_%df', m3));
end
ylim([-1.5 2]); grid on; legend; xlabel('x');

subplot(1,2,2); hold on;
title('Noduri Chebyshev II');
plot(xx3, f3(xx3), 'k-', 'LineWidth', 2, 'DisplayName', 'f(x)');
for m3 = [6, 10, 14]
    x3 = sort(cos((0:m3)'*pi/m3)) * (b3-a3)/2 + (a3+b3)/2;
    y3 = f3(x3);
    plot(xx3, my_cheb_lagrange2(y3, xx3, a3, b3), 'LineWidth', 1.5, ...
         'DisplayName', sprintf('L_%df', m3));
end
ylim([-0.1 1.3]); grid on; legend; xlabel('x');
sgtitle('Prob 3: f(x)=1/(1+x^2) pe [-5,5]');

fprintf('--- Problema 4: Aproximare f(x) ---\n\n');

fprintf('  f(x)=exp(x^2-1), f(1.25):\n');
f4a = @(x) exp(x.^2 - 1);
x4a = [1.0, 1.1, 1.2, 1.3, 1.4];
y4a = f4a(x4a);
fprintf('  %-4s  %-14s  %-10s\n', 'm', 'L_mf(1.25)', 'eroare');
for m4 = 1:4
    v = my_lagrange(x4a(1:m4+1), y4a(1:m4+1), 1.25);
    fprintf('  %-4d  %-14.8f  %.2e\n', m4, v, abs(v - f4a(1.25)));
end

fprintf('\n--- Problema 5: Baricentric ---\n');

f5  = @(x) 1./(1 + x.^2);
a5  = -5; b5 = 5; m5 = 14;
xx5 = linspace(a5, b5, 500);

x5u  = linspace(a5, b5, m5+1);
fi5a = my_bary_lagrange(x5u, f5(x5u), xx5);
fprintf('\n  Baricentric uniform  (m=%d): max|f-p| = %.3e  <- Runge\n', m5, max(abs(f5(xx5)-fi5a)));

x5c1 = sort(cos((2*(0:m5)'+1)*pi/(2*m5+2))) * (b5-a5)/2 + (a5+b5)/2;
fi5b = my_cheb_lagrange1(f5(x5c1), xx5, a5, b5);
fprintf('  Chebyshev I          (m=%d): max|f-p| = %.3e\n', m5, max(abs(f5(xx5)-fi5b)));

x5c2 = sort(cos((0:m5)'*pi/m5)) * (b5-a5)/2 + (a5+b5)/2;
fi5c = my_cheb_lagrange2(f5(x5c2), xx5, a5, b5);
fprintf('  Chebyshev II         (m=%d): max|f-p| = %.3e\n', m5, max(abs(f5(xx5)-fi5c)));

figure('Name', 'Prob 5: Baricentric');
plot(xx5, f5(xx5), 'k-', 'LineWidth', 2);  hold on;
plot(xx5, fi5b, 'b-',  'LineWidth', 1.5);
plot(xx5, fi5c, 'g--', 'LineWidth', 1.5);
ylim([-0.1 1.4]); grid on;
legend('f(x)', 'Cheb I', 'Cheb II');      
title(sprintf('Baricentric Chebyshev, f=1/(1+x^2), m=%d', m5));