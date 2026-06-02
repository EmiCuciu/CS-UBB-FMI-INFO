clear; clc;

fprintf('1.4) f(x) = cos(x) - x = 0\n\n');

f1 = @(x) cos(x) - x;
fd1 = @(x) -sin(x) - 1;
g1 = @(x) cos(x);
tol = 1e-10;

[z_N, ni_N] = my_newton(f1, fd1, pi/4, tol);
[z_S, ni_S] = my_secant(f1, 0.5, pi/4, tol);
[z_M, ni_M] = my_mas(g1, 0.5, tol);
[z_St, ni_St] = my_steffensen(g1, 0.5, tol);

fprintf('%-14s  z = %.12f  ni = %d\n', 'Newton', z_N, ni_N);
fprintf('%-14s  z = %.12f  ni = %d\n', 'Secanta', z_S, ni_S);
fprintf('%-14s  z = %.12f  ni = %d\n', 'MAS', z_M, ni_M);
fprintf('%-14s  z = %.12f  ni = %d\n', 'Steffensen', z_St, ni_St);

fprintf('f(x) = x^3 + 4x^2 - 10 = 0  (ex. Steffensen)\n');
f2 = @(x) x^3 + 4*x^2 - 10;
fd2 = @(x) 3*x^2 + 8*x;
g2 = @(x) sqrt(10/(x+4));

[z2_N,ni2_N] = my_newton(f2, fd2, 1.5, tol);
[z2_S,ni2_S] = my_secant(f2, 1.0,  1.5, tol);
[z2_M,ni2_M] = my_mas(g2, 1.5, tol);
[z2_St, ni2_St] = my_steffensen(g2, 1.5, tol);

fprintf('%-14s  z = %.12f  ni = %d\n', 'Newton', z2_N, ni2_N);
fprintf('%-14s  z = %.12f  ni = %d\n', 'Secanta', z2_S, ni2_S);
fprintf('%-14s  z = %.12f  ni = %d\n', 'MAS',  z2_M, ni2_M);
fprintf('%-14s  z = %.12f  ni = %d\n', 'Steffensen', z2_St, ni2_St);
fprintf('\n');


fprintf('Sistem 2D\n');
fprintf('{ x^2+y^2=1,  x^3-y=0 }\n\n');

f2d = @(v) [v(1)^2 + v(2)^2 - 1;
            v(1)^3 - v(2)];

J2d = @(v) [2*v(1),   2*v(2);
            3*v(1)^2, -1    ];

starts2d = {[0.8; 0.6], [-0.8; -0.6]};
for i = 1:2
    x0 = starts2d{i};
    [z2, ni2] = my_newton(f2d, J2d, x0, 1e-12, 0, 50);
    fprintf('  Newton: start=[%5.2f,%5.2f] -> sol=[%10.8f, %10.8f]  ni=%d\n', ...
            x0(1), x0(2), z2(1), z2(2), ni2);
end

fprintf('\n  MAS (J fix la x0):\n');
x0_mas = [0.8; 0.6];
J0 = J2d(x0_mas);
phi2d  = @(v) v - J0\f2d(v);
[z_mas2, ni_mas2] = my_mas(phi2d, x0_mas, 1e-10, 0, 200);
fprintf('  MAS: sol=[%10.8f, %10.8f]  ni=%d\n\n', z_mas2(1), z_mas2(2), ni_mas2);

fprintf('Sistem 3D \n\n');

f3d = @(v) [9*v(1)^2 + 36*v(2)^2 + 4*v(3)^2 - 36;
            v(1)^2 - 2*v(2)^2 - 20*v(3);
            v(1)^2 - v(2)^2  + v(3)^2];
J3d = @(v) [18*v(1), 72*v(2),  8*v(3);
             2*v(1), -4*v(2), -20;
             2*v(1), -2*v(2),  2*v(3)];

starts3d = {[1;1;0], [1;-1;0], [-1;1;0], [-1;-1;0]};
for i = 1:4
    x0 = starts3d{i};
    [z3, ni3] = my_newton(f3d, J3d, x0, 1e-10, 0, 100);
    fprintf('  start=[%2d,%2d,%d] -> sol=[%8.5f, %8.5f, %8.5f]  ni=%d\n', ...
            x0(1), x0(2), x0(3), z3(1), z3(2), z3(3), ni3);
    fprintf('         ||f(z)||=%.2e\n', norm(f3d(z3)));
end

fprintf('\n  MAS (J fix la [1,1,0]):\n');
x0_3d = [1; 1; 0];
J0_3d = J3d(x0_3d);
phi3d = @(v) v - J0_3d\f3d(v);
[z_mas3, ni_mas3] = my_mas(phi3d, x0_3d, 1e-8, 0, 500);
fprintf('  MAS: sol=[%8.5f, %8.5f, %8.5f]  ni=%d\n', z_mas3(1), z_mas3(2), z_mas3(3), ni_mas3);
