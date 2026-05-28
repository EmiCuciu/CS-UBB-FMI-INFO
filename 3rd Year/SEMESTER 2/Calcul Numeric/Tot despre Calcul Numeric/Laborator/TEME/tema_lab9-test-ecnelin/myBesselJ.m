function val = myBesselJ(n, x)
val = zeros(size(x));

for i = 1:numel(x)
    xi = x(i);

    if xi == 0
        if n == 0
            val(i) = 1;
        else
            val(i) = 0;
        end
        continue;
    end

    half_x = xi / 2;
    hx2 = half_x^2;
    term = half_x^n / factorial(n);
    current_val = term;

    for m = 0:500
        term        = -term * hx2 / ((m+1) * (m+n+1));
        current_val = current_val + term;
        if abs(term) < 1e-15 * abs(current_val)
            break;
        end
    end

    val(i) = current_val;
end
