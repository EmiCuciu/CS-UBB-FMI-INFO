clc; clear; close all;

n = 500000;
disp(['Matrice rara pentru n = ', num2str(n)]);

e = ones(n, 1);
A = spdiags([-e, -e, 5*e, -e, -e], [-3, -1, 0, 1, 3], n, n);

A(1, n) = 1;
A(n, 1) = 1;

b = ones(n, 1);
b(1) = 4; b(2) = 2; b(3) = 2;
b(n-2) = 2; b(n-1) = 2; b(n) = 4;

x_exact = ones(n, 1);

tol = 1e-8;  
max_iter = 1000;
x0 = zeros(n, 1);
omega = 1.2;

disp(' ');
disp('--- Metoda Jacobi ---');
tic;
[x_jac, ni_jac] = Jacobi_sparse(A, b, x0, tol, max_iter);
t_jac = toc;
fprintf('Iteratii efectuate:        %d\n', ni_jac);
fprintf('Timp executie:             %.4f secunde\n', t_jac);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_jac - x_exact, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A*x_jac - b, inf));

disp(' ');
disp('--- Metoda Gauss-Seidel ---');
tic;
[x_gs, ni_gs] = Gauss_Seidel_sparse(A, b, x0, tol, max_iter);
t_gs = toc;
fprintf('Iteratii efectuate:        %d\n', ni_gs);
fprintf('Timp executie:             %.4f secunde\n', t_gs);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_gs - x_exact, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A*x_gs - b, inf));

disp(' ');
fprintf('--- Metoda SOR (omega = %.2f) ---\n', omega);
tic;
[x_sor, ni_sor] = relax_sparse(A, b, omega, x0, tol, max_iter);
t_sor = toc;
fprintf('Iteratii efectuate:        %d\n', ni_sor);
fprintf('Timp executie:             %.4f secunde\n', t_sor);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_sor - x_exact, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A*x_sor - b, inf));
disp(' ');


m = 100;
disp(['Matrice densa pentru m = ', num2str(m)]);

A_dens = rand(m, m) + m * eye(m);

x_exact_dens = ones(m, 1);

b_dens = A_dens * x_exact_dens;

x0_dens = zeros(m, 1);

disp(' ');
disp('--- Metoda Jacobi (Densa) ---');
tic;
[x_jac_d, ni_jac_d] = Jacobi_sparse(A_dens, b_dens, x0_dens, tol, max_iter);
t_jac_d = toc;
fprintf('Iteratii efectuate:        %d\n', ni_jac_d);
fprintf('Timp executie:             %.4f secunde\n', t_jac_d);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_jac_d - x_exact_dens, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A_dens*x_jac_d - b_dens, inf));

disp(' ');
disp('--- Metoda Gauss-Seidel (Densa) ---');
tic;
[x_gs_d, ni_gs_d] = Gauss_Seidel_sparse(A_dens, b_dens, x0_dens, tol, max_iter);
t_gs_d = toc;
fprintf('Iteratii efectuate:        %d\n', ni_gs_d);
fprintf('Timp executie:             %.4f secunde\n', t_gs_d);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_gs_d - x_exact_dens, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A_dens*x_gs_d - b_dens, inf));

disp(' ');
fprintf('--- Metoda SOR (omega = %.2f) (Densa) ---\n', omega);
tic;
[x_sor_d, ni_sor_d] = relax_sparse(A_dens, b_dens, omega, x0_dens, tol, max_iter);
t_sor_d = toc;
fprintf('Iteratii efectuate:        %d\n', ni_sor_d);
fprintf('Timp executie:             %.4f secunde\n', t_sor_d);
fprintf('Eroare (||x - x_ex||_inf): %e\n', norm(x_sor_d - x_exact_dens, inf));
fprintf('Reziduu (||Ax - b||_inf):  %e\n', norm(A_dens*x_sor_d - b_dens, inf));
disp(' ');