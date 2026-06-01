function s = taylor_cos(x)
    [x, ~, cos_sign] = reduce(x);
    s = 0;
    t = 1;
    n = 0;
    while s + t ~= s
        n = n + 1;
        s = s + t;
        t = (-1)^n * ((x^(2 * n)) / (factorial(2 * n)));
    end
    s = cos_sign * s;
end
