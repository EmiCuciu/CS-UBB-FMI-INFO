clc; clear; close all;

n = 500000;

disp(['Matricea rara pentru n = ', num2str(n)]);

e = ones(n,1);

A = spdiags([-e, -e, 5*e, -e , -e], [-3, -1, 0, 1, 3], n, n);

A(1,n) = 1;
A(n,1) = 1;

b = ones(n, 1);
b(1) = 4;
b(2) = 2;
b(3) = 2;
b(n-2) = 2;
b(n-1) = 2;
b(n) = 4;



tol = 1e-6;
max_iter = 200;
x0 = zeros(n, 1);
omega = 1.2;


x_exact = ones(n, 1);


disp('----------------------------------------------------');

disp('Rezolvam cu Jacobi...');
tic;
[x_jac, ni_jac] = Jacobi_sparse(A, b, x0, tol, max_iter);
timp_jac = toc;
err_jac = norm(x_jac - x_exact, inf);
fprintf('Jacobi: iteratii = %d, eroare = %e, timp = %.2f s\n', ni_jac, err_jac, timp_jac);


disp('Rezolvam cu Gauss-Seidel...');
tic;
[x_gs, ni_gs] = Gauss_Seidel_sparse(A, b, x0, tol, max_iter);
timp_gs = toc;
err_gs = norm(x_gs - x_exact, inf);
fprintf('Gauss-Seidel: iteratii = %d, eroare = %e, timp = %.2f s\n', ni_gs, err_gs, timp_gs);


fprintf('Rezolvam cu SOR (omega = %.1f)...\n', omega);
tic;
[x_sor, ni_sor] = relax_sparse(A, b, omega, x0, tol, max_iter);
timp_sor = toc;
err_sor = norm(x_sor - x_exact, inf);
fprintf('SOR: iteratii = %d, eroare = %e, timp = %.2f s\n', ni_sor, err_sor, timp_sor);

disp('----------------------------------------------------');