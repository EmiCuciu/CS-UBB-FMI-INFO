function [nodes, w] = my_gauss_legendre(n)

alpha = zeros(n, 1);
beta  = [2, (4 - (1:n-1).^(-2)).^(-1)];
[nodes, w] = my_gaussquad(alpha, beta);
