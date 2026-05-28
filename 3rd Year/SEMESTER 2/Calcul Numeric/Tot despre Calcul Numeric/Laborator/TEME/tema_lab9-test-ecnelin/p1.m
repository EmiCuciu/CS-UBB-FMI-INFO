%  f(x)  = 2*x*J1(x) - J0(x)
%    J0'(x) = -J1(x)
%    J1'(x) = (J0(x) - J2(x)) / 2
clear; clc; format long;

f = @(x) 2*x.*myBesselJ(1,x) - myBesselJ(0,x);
fd = @(x) 3*myBesselJ(1,x) + x.*(myBesselJ(0,x) - myBesselJ(2,x));

xv = 0.1:0.05:15;
figure('Name','f(x) = 2x J1(x) - J0(x)');
plot(xv, f(xv), 'b-', 'LineWidth', 2); hold on;
yline(0, 'k--', 'LineWidth', 1);
xlabel('x'); ylabel('f(x)');
ylim([-4 4]); grid on;

ea = 1e-10;
er = 0;
nmax = 100;


x0_N = [1.5,  4.5,  7.5];   
x0_S = [1.3,  4.3,  7.3];   
x1_S = [1.7,  4.7,  7.7];   

radacini_N = zeros(1,3);
radacini_S = zeros(1,3);

fprintf('=== METODA LUI NEWTON ===\n');
fprintf('%10s  %22s  %6s  %12s\n', 'Radacina', 'x', 'Iter', '|f(x)|');
for k = 1:3
    [z, ni] = Newton(f, fd, x0_N(k), ea, er, nmax);
    radacini_N(k) = z;
    fprintf('%10d  %22.15f  %6d  %12.4e\n', k, z, ni, abs(f(z)));
end


fprintf('\n=== METODA SECANTEI ===\n');
fprintf('%10s  %22s  %6s  %12s\n', 'Radacina', 'x', 'Iter', '|f(x)|');
for k = 1:3
    [z, ni] = secant(f, x0_S(k), x1_S(k), ea, er, nmax);
    radacini_S(k) = z;
    fprintf('%10d  %22.15f  %6d  %12.4e\n', k, z, ni, abs(f(z)));
end


fprintf('\n=== REZUMAT ===\n');
fprintf('%10s  %22s  %22s  %12s\n', 'Radacina', 'Newton', 'Secanta', 'Diferenta');
for k = 1:3
    fprintf('%10d  %22.15f  %22.15f  %12.4e\n', ...
        k, radacini_N(k), radacini_S(k), abs(radacini_N(k)-radacini_S(k)));
end

plot(radacini_N, f(radacini_N), 'ro', ...
    'MarkerSize', 10, 'MarkerFaceColor', 'r', 'DisplayName', 'Radacini');
legend('f(x)', 'y=0', 'Radacini', 'Location', 'northeast');
hold off;
