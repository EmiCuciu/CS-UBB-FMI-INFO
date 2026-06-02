function [nodes, w] = my_gauss_cheb2(n)

alpha = zeros(n, 1);
beta = [pi/2, 1/4*ones(1, n-1)];
[nodes, w] = my_gaussquad(alpha, beta);
