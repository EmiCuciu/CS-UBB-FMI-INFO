function x = my_gausselim(A, b)
% gaussiana cu pivotare scalata pe coloana

n = size(A, 1);
x = zeros(n, 1);

s = sum(abs(A), 2);

Ab = [A, b];

piv = 1:n;

for i = 1:n-1

    r = abs(Ab(piv(i:n), i)) ./ s(piv(i:n));
    [val_max, idx] = max(r);
    p = idx + i - 1;   

    if val_max == 0
        error('Matricea este singulara sau aproape singulara!')
    end

    if p ~= i
        piv([i, p]) = piv([p, i]);
    end

    for j = i+1:n
        mul = Ab(piv(j), i) / Ab(piv(i), i);
        Ab(piv(j), i+1:n+1) = Ab(piv(j), i+1:n+1) - mul* Ab(piv(i), i+1:n+1);
    end
end

if Ab(piv(n), n) == 0
    error('Sistem fara solutie unica!')
end

for i = n:-1:1
    suma = Ab(piv(i), i+1:n) * x(i+1:n);
    x(i) = (Ab(piv(i), n+1) - suma) / Ab(piv(i), i);
end

end
