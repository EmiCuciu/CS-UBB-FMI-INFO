clear; clc;

A = [1 1 1;
          1 1 2;
          2 4 2];
b = [3; 4; 8];
x = [1; 1; 1];   

x_gauss = my_gausselim(A, b);
fprintf('\n[Gauss]  x = [%.4f, %.4f, %.4f]\n', x_gauss);
fprintf('         eroare = %.2e\n', norm(x_gauss - x));

[L, U, P] = my_lup(A);
fprintf('\n[LUP]   Verificare P*A = L*U: eroare = %.2e\n', norm(P*A - L*U));

x_lup = my_lup_solve(A, b);
fprintf('[LUP]   x = [%.4f, %.4f, %.4f]\n', x_lup);
fprintf('        eroare = %.2e\n', norm(x_lup - x));

fprintf('\n  L =\n'); disp(L);
fprintf('  U =\n');   disp(U);
fprintf('  P =\n');   disp(P);


A_chol = [1 2 1;
               2 5 3;
               1 3 3];
b_chol = [4; 10; 7];
x_exact_chol = [1; 1; 1];   

R = my_cholesky(A_chol);
fprintf('\n[Cholesky]  Factorul R:\n');
disp(R);
fprintf('  Verificare A = R''*R: eroare = %.2e\n', norm(A_chol - R'*R));

x_chol = my_cholesky_solve(A_chol, b_chol);
fprintf('[Cholesky]  x = [%.4f, %.4f, %.4f]\n', x_chol);
fprintf('            eroare = %.2e\n', norm(x_chol - x_exact_chol));


dimensiuni_hpd = [5, 10, 20, 50, 100];
fprintf('\n%-6s | %-16s | %-16s | %-12s\n', ...
        'n', 'Err A-R''R (rel)', 'Err solutie', 'Timp(s)');
for n = dimensiuni_hpd
    
    X = rand(n);
    A_hpd = X' * X + n * eye(n);
    x_sol = ones(n, 1);
    b_hpd = A_hpd * x_sol;

    tic;
    R_r     = my_cholesky(A_hpd);
    x_c     = my_cholesky_solve(A_hpd, b_hpd);
    t_chol  = toc;

    err_fact = norm(R_r'*R_r - A_hpd) / norm(A_hpd);
    err_sol  = norm(x_c - x_sol) / norm(x_sol);

    fprintf('%-6d | %-16.2e | %-16.2e | %-12.6f\n', n, err_fact, err_sol, t_chol);
end