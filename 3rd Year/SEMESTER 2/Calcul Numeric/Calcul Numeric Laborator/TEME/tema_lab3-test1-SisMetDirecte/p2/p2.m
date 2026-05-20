clc; clear; close all;


function [L, U, P] = lup_decomp(A)
    n = size(A, 1);
    U = A;
    L = eye(n);
    P = eye(n);
    
    for k = 1:n-1
        [~, idx] = max(abs(U(k:n, k)));
        idx = idx + k - 1;
        
        U([k, idx], :) = U([idx, k], :);
        P([k, idx], :) = P([idx, k], :);
        
        if k > 1
            L([k, idx], 1:k-1) = L([idx, k], 1:k-1);
        end
        
        for i = k+1:n
            if U(k, k) == 0
                error('Matrice singulara');
            end
            L(i, k) = U(i, k) / U(k, k);
            U(i, k:n) = U(i, k:n) - L(i, k) * U(k, k:n);
        end
    end
end

function R = cholesky_decomp(A)
    n = size(A, 1);
    R = zeros(n);
    
    for j = 1:n
        s = A(j, j) - sum(R(1:j-1, j).^2);
        if s <= 0
            error('Matricea nu este pozitiv definita');
        end
        R(j, j) = sqrt(s);
        
        for i = j+1:n
            R(j, i) = (A(j, i) - sum(R(1:j-1, j) .* R(1:j-1, i))) / R(j, j);
        end
    end
end

function x = subst_directa(L, b)
    n = length(b);
    x = zeros(n, 1);
    for i = 1:n
        x(i) = (b(i) - L(i, 1:i-1) * x(1:i-1)) / L(i, i);
    end
end

function x = subst_inversa(U, b)
    n = length(b);
    x = zeros(n, 1);
    for i = n:-1:1
        x(i) = (b(i) - U(i, i+1:n) * x(i+1:n)) / U(i, i);
    end
end

function [A, b] = gen_sistem(n)
    A = 3 * eye(n);
    for i = 1:n-1
        A(i, i+1) = -1;
        A(i+1, i) = -1;
    end
    for i = 1:n
        if i ~= n/2 && i ~= (n/2 + 1)
            A(i, n + 1 - i) = 0.5;
        end
    end
    b = 1.5 * ones(n, 1);
    b(1) = 2.5;
    b(n) = 2.5;
    b(n/2) = 1.0;
    b(n/2 + 1) = 1.0;
end


%a)
fprintf('punct a) Descopuneri pentru n = 12 \n');
n12 = 12;
[A12, b12] = gen_sistem(n12);

[L12, U12, P12] = lup_decomp(A12);
fprintf('\nMatrice L (primele 3x3) pentru LUP cu n=12:\n');
disp(L12(1:3, 1:3));
fprintf('Matrice U (primele 3x3) pentru LUP cu n=12:\n');
disp(U12(1:3, 1:3));

err_lup12 = norm(P12*A12 - L12*U12, 'fro');
fprintf('Eroare descompunere LUP (||PA - LU||_F): %e\n', err_lup12);

R12 = cholesky_decomp(A12);
fprintf('\nMatrice R Cholesky (primele 3x3) pentru n=12:\n');
disp(R12(1:3, 1:3));

err_chol12 = norm(A12 - R12'*R12, 'fro');
fprintf('Eroare descompunere Cholesky (||A - R^T*R||_F): %e\n', err_chol12);




fprintf('\npunct b) Rezolvare sistem pentru n = 100\n');
n100 = 100;
[A100, b100] = gen_sistem(n100);

[L, U, P] = lup_decomp(A100);
y_lup = subst_directa(L, P * b100);
x_lup = subst_inversa(U, y_lup);

R = cholesky_decomp(A100);
y_chol = subst_directa(R', b100);
x_chol = subst_inversa(R, y_chol);

fprintf('\nPrimele 5 componente ale solutiei:\n');
fprintf('Solutie cu LUP:      [%.6f, %.6f, %.6f, %.6f, %.6f]\n', x_lup(1:5));
fprintf('Solutie cu Cholesky: [%.6f, %.6f, %.6f, %.6f, %.6f]\n', x_chol(1:5));

res_lup  = norm(A100 * x_lup  - b100, inf);
res_chol = norm(A100 * x_chol - b100, inf);
fprintf('\nReziduu LUP      (||Ax-b||_inf): %e\n', res_lup);
fprintf('Reziduu Cholesky (||Ax-b||_inf): %e\n', res_chol);

eroare = norm(x_lup - x_chol, inf);
fprintf('Diferenta intre solutii (||x_lup - x_chol||_inf): %e\n', eroare);