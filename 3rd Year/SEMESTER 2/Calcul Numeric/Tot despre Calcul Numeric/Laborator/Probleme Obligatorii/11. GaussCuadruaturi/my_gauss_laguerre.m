function [nodes, w] = my_gauss_laguerre(n, a)

if nargin < 2, a = 0; end
k = 1:n-1;
alpha = [a+1, 2*k+a+1];
beta  = [gamma(1+a), k.*(k+a)];
[nodes, w] = my_gaussquad(alpha, beta);
