function x = my_backsubst(U, y)
% rezolva U*x = y
n = length(y);
x = zeros(n, 1);

for k = n:-1:1
    suma = U(k, k+1:n) * x(k+1:n);   
    x(k) = (y(k) - suma) / U(k, k);  
end

end
