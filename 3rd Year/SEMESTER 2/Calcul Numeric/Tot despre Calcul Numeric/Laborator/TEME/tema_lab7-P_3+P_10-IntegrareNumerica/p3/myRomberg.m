function I = myRomberg(f, a, b, tol, nmax)
%MYROMBERG  Metoda Romberg: trapeze repetate + extrapolare Richardson
%  I = myRomberg(f, a, b, tol, nmax)
%  f    - functia integranda (function handle)
%  a,b  - capetele intervalului
%  tol  - toleranta (implicit 1e-6)
%  nmax - numar maxim de iteratii (implicit 15)

if nargin < 5, nmax = 15; end
if nargin < 4, tol  = 1e-6; end

R = zeros(nmax, nmax);
h = b - a;
R(1,1) = h/2 * (f(a) + f(b));

for k = 2:nmax
    % Adaugam punctele de mijloc ale subintervalelor curente
    x = a + ((1:2^(k-2)) - 0.5) * h;
    R(k,1) = 0.5 * (R(k-1,1) + h * sum(f(x)));

    % Extrapolare Richardson: R(k,j) = (4^(j-1)*R(k,j-1) - R(k-1,j-1)) / (4^(j-1)-1)
    p4 = 4;
    for j = 2:k
        R(k,j) = (p4 * R(k,j-1) - R(k-1,j-1)) / (p4 - 1);
        p4 = p4 * 4;
    end

    if k > 3 && abs(R(k,k) - R(k-1,k-1)) < tol
        I = R(k,k);
        return;
    end
    h = h / 2;
end

I = R(nmax, nmax);
warning('myRomberg: numarul maxim de iteratii atins.');
end
