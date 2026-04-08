clc; clear; close all;

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

%LUP
[L12, U12, P12] = lu(A12);
disp("Matrice L pentru n=12: ");
disp(L12(1:3, 1:3));


%Cholesky
R_chol_12 = chol(A12);
disp('Matrice R_Cholesky pentru n=12: ');
disp(R_chol_12(1:3, 1:3));


%b)
fprintf('punct b) n = 100 \n ');
n100=100;
[A100, b100] = gen_sistem(n100);

%LUP
[L, U, P] = lu(A100);
y_lup = L \ (P * b100);
x_lup = U \ y_lup;



%Cholesky
R = chol(A100)
y_chol = R' \ b100;
x_chol = R \ y_chol;


fprintf('verificam primele 5 componente \n');
fprintf('Solutie cu LUP: [%f, %f, %f, %f, %f]\n', x_lup(1:5));
fprintf('Solutie cu Cholesky: [%f, %f, %f, %f, %f]\n', x_chol(1:5));


eroare = norm(x_lup - x_chol);
fprintf('Eroare: %e \n', eroare);