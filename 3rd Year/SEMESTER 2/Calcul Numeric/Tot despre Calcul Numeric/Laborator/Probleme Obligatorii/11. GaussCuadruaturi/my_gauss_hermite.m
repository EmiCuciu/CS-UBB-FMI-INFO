function [nodes, w] = my_gauss_hermite(n)

alpha = zeros(n, 1);
beta = [sqrt(pi), (1:n-1)/2];
[nodes, w] = my_gaussquad(alpha, beta);
