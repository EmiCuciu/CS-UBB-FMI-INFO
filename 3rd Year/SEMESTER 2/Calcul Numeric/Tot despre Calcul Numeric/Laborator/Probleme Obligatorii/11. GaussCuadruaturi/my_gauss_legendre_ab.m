function I = my_gauss_legendre_ab(f, n, a, b)

[t, w] = my_gauss_legendre(n);
I = (b-a)/2 * w * f((b-a)/2*t + (b+a)/2);
