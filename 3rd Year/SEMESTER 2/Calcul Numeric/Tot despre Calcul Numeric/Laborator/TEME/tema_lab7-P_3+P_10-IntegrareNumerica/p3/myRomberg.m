function [I, ni] = myRomberg(f, a, b, tol, nmax)

if nargin < 5, nmax = 15; end
if nargin < 4, tol  = 1e-6; end

R = zeros(nmax, nmax);
h = b - a;
R(1,1) = h/2 * (f(a) + f(b));

for k = 2:nmax
    x    = a + ((1:2^(k-2)) - 0.5) * h;
    R(k,1) = 0.5 * (R(k-1,1) + h * sum(f(x)));

    plj = 4;
    for j = 2:k
        R(k,j) = (plj * R(k,j-1) - R(k-1,j-1)) / (plj - 1);
        plj    = plj * 4;
    end

    if k > 3 && abs(R(k,k) - R(k-1,k-1)) < tol
        I  = R(k,k);
        ni = k;
        return;
    end

    h = h / 2;
end

I  = R(nmax, nmax);
ni = nmax;
warning('myRomberg: numarul maxim de nivele (%d) a fost atins fara convergenta.', nmax);
end
