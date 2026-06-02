function [nodes, w] = my_gaussquad(alpha, beta)

n = length(alpha);
rb = sqrt(beta(2:n));
J = diag(alpha) + diag(rb,-1) + diag(rb,1);
[v, d] = eig(J);
nodes = diag(d);
w  = beta(1) * v(1,:).^2;
