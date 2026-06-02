function [nodes, w] = my_gauss_cheb1(n)

w = pi/n * ones(1, n);
nodes = cos(pi*((1:n)' - 0.5)/n);
