function [nodes, w] = my_gauss_jacobi(n, a, b)

if nargin<2, a=0; end; if nargin<3, b=a; end
if n<=0 || a<=-1 || b<=-1, error('parametri invalizi'); end
k1=1:n-1; k2=2:n-1;
b0 = 2^(a+b+1)*gamma(a+1)*gamma(b+1)/gamma(a+b+2);
if n==1
    alpha=[(b-a)/(a+b+2)]; beta=[b0];
else
    b1 = 4*(1+a)*(1+b)/((2+a+b)^2*(3+a+b));
    if a==b
        alpha = zeros(1,n);
    else
        alpha = [(b-a)/(a+b+2), (b^2-a^2)./(2*k1+a+b)./(2*k1+a+b+2)];
    end
    beta = [b0, b1, 4*k2.*(k2+a+b).*(k2+a).*(k2+b)...
            ./(2*k2+a+b-1)./(2*k2+a+b).^2./(2*k2+a+b+1)];
end
[nodes, w] = my_gaussquad(alpha, beta);
