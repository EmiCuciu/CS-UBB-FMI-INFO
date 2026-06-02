function [I, nfev] = my_romberg(f, a, b, epsi, nmax)

if nargin < 5, nmax = 10; end
if nargin < 4, epsi = 1e-6; end
R = zeros(nmax, nmax);
h = b-a;
R(1,1) = h/2 * (f(a) + f(b));
nfev = 2;
for k = 2:nmax
    x = a + ((1:2^(k-2)) - 0.5)*h;
    R(k,1) = 0.5*(R(k-1,1) + h*sum(f(x)));
    nfev = nfev + length(x);
    p = 4;
    for j = 2:k
        R(k,j) = (p*R(k,j-1) - R(k-1,j-1))/(p-1);
        p = p*4;
    end
    if (abs(R(k,k)-R(k-1,k-1)) < epsi) && (k > 3)
        I = R(k,k); return
    end
    h = h/2;
end
error('Romberg: nr. iteratii depasit')
