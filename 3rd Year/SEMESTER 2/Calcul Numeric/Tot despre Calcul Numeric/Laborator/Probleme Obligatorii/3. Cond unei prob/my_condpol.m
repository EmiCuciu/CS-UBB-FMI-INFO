function nc = my_condpol(p, xi)
%CONDPOL conditionarea unei ecuatii polinomiale p(x)=0
if nargin < 2
    xi = roots(p); end

n = length(p) - 1;
dp = (n:-1:1) .* p(1:end-1);  % derivata
nc = 1./(abs(xi.*polyval(dp,xi))) .* (polyval(abs(p(2:end)),abs(xi)));
end
