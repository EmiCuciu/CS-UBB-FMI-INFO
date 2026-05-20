function coefs = mcmmp_discreta(x, y, grad)
    m = length(x);
    V = zeros(m, grad + 1);
    
    for j = 1:(grad + 1)
        V(:, j) = x(:).^(grad + 1 - j);
    end
    
    y = y(:);
    coefs = (V' * V) \ (V' * y);
end