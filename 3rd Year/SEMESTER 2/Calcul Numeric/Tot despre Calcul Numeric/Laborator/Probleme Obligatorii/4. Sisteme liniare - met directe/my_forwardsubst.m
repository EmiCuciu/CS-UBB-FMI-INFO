function y = my_forwardsubst(L, b)
n = length(b);
y = zeros(n, 1);

for k = 1:n
    suma = L(k, 1:k-1) * y(1:k-1);   
    y(k) = (b(k) - suma) / L(k, k);
end

end
