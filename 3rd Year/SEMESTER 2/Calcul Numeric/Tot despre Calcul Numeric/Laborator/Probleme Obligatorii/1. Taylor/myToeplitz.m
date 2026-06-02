function t = myToeplitz(c, r)

if nargin < 2
    c(1) = conj(c(1));
    r = c;
    c = conj(c);
end

r = r(:);
c = c(:);
p = length(r);
m = length(c);
x = [r(p:-1:2, 1) ; c];                 
ij = (0:m-1)' + (p:-1:1);               
t = x(ij);                              
if isrow(ij) && ~isempty(t)             
    t = t.';
end