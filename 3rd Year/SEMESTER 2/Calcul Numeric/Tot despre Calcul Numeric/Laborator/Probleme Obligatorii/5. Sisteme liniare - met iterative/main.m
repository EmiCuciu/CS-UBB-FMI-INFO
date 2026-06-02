clear; clc;

n = 5;
A = 5*eye(n) - diag(ones(n-1,1),1) - diag(ones(n-1,1),-1);
b = [4; 3; 3; 3; 4];
x_exact = ones(n,1);

disp(A)

fprintf('\n--- Metoda Jacobi ---\n');
fprintf('Descompunere: M = D, N = L+U\n');
fprintf('Iteratie: x^(k+1) = D^{-1} * (N*x^k + b)\n\n');

[x_jac, ni_jac] = my_Jacobi(A, b, [], 1e-6, 100);

fprintf('  Solutie Jacobi:  [%s]\n', num2str(x_jac', '%.6f  '));
fprintf('  Iteratii:        %d\n', ni_jac);
fprintf('  Eroare (inf):    %.2e\n\n', norm(x_jac - x_exact, inf));

fprintf('--- Calcul omega optim ---\n');
fprintf('omega_O = 2 / (1 + sqrt(1 - p(T_J)^2))\n\n');

D = diag(diag(A));
T_J = D \ (D - A);

p_J = max(abs(eig(T_J)));
fprintf('  p(T_J) = %.6f\n', p_J);

omega_opt = 2 / (1 + sqrt(1 - p_J^2));
fprintf('  omega_O  = %.6f\n\n', omega_opt);

fprintf('--- Metoda SOR ---\n');
fprintf('Descompunere: M = D/omega - L, N = M - A\n');
fprintf('Iteratie: x^(k+1) = M^{-1} * (N*x^k + b)\n\n');

% SOR cu omega optim
[x_sor_opt, ni_sor_opt] = my_relax(A, b, omega_opt, [], 1e-6, 200);
fprintf('  SOR (omega_opt = %.4f):\n', omega_opt);
fprintf('  Solutie: [%s]\n', num2str(x_sor_opt', '%.6f  '));
fprintf('  Iteratii: %d\n', ni_sor_opt);
fprintf('  Eroare (inf): %.2e\n\n', norm(x_sor_opt - x_exact, inf));

% SOR cu omega = 1 (Gauss-Seidel) 
[x_gs, ni_gs] = my_relax(A, b, 1.0, [], 1e-6, 200);
fprintf('  SOR (omega = 1.0, Gauss-Seidel):\n');
fprintf('  Iteratii: %d\n\n', ni_gs);

fprintf('=== COMPARATIE ITERATII ===\n');
fprintf('  Jacobi:             %3d iteratii\n', ni_jac);
fprintf('  Gauss-Seidel:       %3d iteratii\n', ni_gs);
fprintf('  SOR (omega optim):  %3d iteratii\n', ni_sor_opt);
