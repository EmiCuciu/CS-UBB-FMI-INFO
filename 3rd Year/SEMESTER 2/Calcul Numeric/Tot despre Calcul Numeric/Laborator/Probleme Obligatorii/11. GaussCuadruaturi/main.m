clear; clc;

fprintf('--- Prob 1: Noduri si ponderi Gauss (n=5) ---\n\n');

[~,w] = my_gauss_legendre(5);
fprintf('Legendre:  sum(w)=%.6f \n', sum(w));
[~,w] = my_gauss_cheb1(5);
fprintf('Cheb I:    sum(w)=%.6f \n', sum(w));
[~,w] = my_gauss_cheb2(5);
fprintf('Cheb II:   sum(w)=%.6f \n', sum(w));
[~,w] = my_gauss_laguerre(5);
fprintf('Laguerre:  sum(w)=%.6f \n', sum(w));
[t,w] = my_gauss_hermite(5);
fprintf('Hermite:   sum(w)=%.6f \n', sum(w));


fprintf('\n--- Prob 2  ---\n');

f1 = @(x) sin(x.^2);
f2 = @(x) cos(x.^2);
tol = 1e-7;
n0 = 5;

[gn,gc] = my_gauss_legendre(n0);
v1 = my_vquad(gn, gc, f1);
v2 = my_vquad(gn, gc, f2);

for n = n0+1:4*n0
    [gn,gc] = my_gauss_legendre(n);
    v1_new  = my_vquad(gn, gc, f1);
    v2_new  = my_vquad(gn, gc, f2);
    if abs(v1_new - v1) < tol
        fprintf('I1=int sin(x^2): n=%d noduri, I1=%.10f\n', n, v1_new);
        break
    end
    v1 = v1_new;
end
for n = n0+1:4*n0
    [gn,gc] = my_gauss_legendre(n);
    v2_new  = my_vquad(gn, gc, f2);
    if abs(v2_new - v2) < tol
        fprintf('I2=int cos(x^2): n=%d noduri, I2=%.10f\n', n, v2_new);
        break
    end
    v2 = v2_new;
end


fprintf('\n--- Prob 3: Gauss-Cheb I, 10 noduri ---\n');
fprintf('int(-1,1) cos(x)/sqrt(1-x^2)dx  si  int(-1,1) x*exp(-x^2)/sqrt(1-x^2)dx\n');

[gn, gc] = my_gauss_cheb1(10);

I3a = my_vquad(gn, gc, @cos);
fprintf('int cos(x)/sqrt(1-x^2) = %.12f  (exact pi*J0(1)=%.12f)\n', I3a, pi*besselj(0,1));

f3b = @(x) x .* exp(-x.^2);
I3b = my_vquad(gn, gc, f3b);
I3b_exact = integral(@(x) x.*exp(-x.^2)./sqrt(1-x.^2), -1, 1);
fprintf('int x*exp(-x^2)/sqrt(1-x^2) = %.12f  (exact=%.12f)\n', I3b, I3b_exact);
fprintf('Eroare: %.2e\n', abs(I3b-I3b_exact));


fprintf('\n--- Prob 4: Gauss-Cheb II, 10 noduri ---\n');

[gn, gc] = my_gauss_cheb2(10);

I4a = my_vquad(gn, gc, @cos);
fprintf('int sqrt(1-x^2)*cos(x) = %.12f  (exact pi*J1(1)=%.12f)\n', I4a, pi*besselj(1,1));

f4 = @(x) exp(-x.^2);
I4  = my_vquad(gn, gc, f4);
I4e = integral(@(x) sqrt(1-x.^2).*exp(-x.^2), -1, 1);
fprintf('int sqrt(1-x^2)*exp(-x^2) = %.12f  (exact=%.12f)\n', I4, I4e);
fprintf('Eroare: %.2e\n', abs(I4-I4e));


fprintf('\n--- Prob 5: Gauss-Laguerre, 8 zecimale exacte ---\n');

for n = 6:20
    [gn, gc] = my_gauss_laguerre(n);
    I5s = my_vquad(gn, gc, @sin);
    I5c = my_vquad(gn, gc, @cos);
    if abs(I5s - 0.5) < 5e-9 && abs(I5c - 0.5) < 5e-9
        fprintf('n=%d noduri suficiente\n', n);
        fprintf('int e^-x sin(x) = %.10f  (exact 0.5)\n', I5s);
        fprintf('int e^-x cos(x) = %.10f  (exact 0.5)\n', I5c);
        break
    end
end


fprintf('\n--- Prob 6: Gauss-Hermite ---\n');

f6 = @(x) sin(x) + cos(x);
exact_s = sqrt(pi)*exp(-0.25)*sin(0.5);
exact_c = sqrt(pi)*exp(-0.25);

for n = 5:15
    [gn, gc] = my_gauss_hermite(n);
    I6 = my_vquad(gn, gc, f6);
    exact_sc = exact_s + exact_c;
    if abs(I6 - exact_sc) < 1e-8
        fprintf('n=%d: int e^-x^2*(sin(x)+cos(x)) = %.10f  (exact=%.10f)\n', n, I6, exact_sc);
        break
    end
end

[gn7,gc7] = my_gauss_hermite(7);
I6s = my_vquad(gn7, gc7, @sin);
I6c = my_vquad(gn7, gc7, @cos);
fprintf('n=7: int e^-x^2*sin(x) = %.10f  (exact=%.10f)\n', I6s, exact_s);
fprintf('n=7: int e^-x^2*cos(x) = %.10f  (exact=%.10f)\n', I6c, exact_c);


fprintf('\n--- Prob 7: Gauss-Jacobi, 9 zecimale exacte ---\n');
fprintf('int(0,pi/2) 1/sqrt(sin(x)) dx\n');

exact7 = gamma(1/4)*gamma(1/2)/(2*gamma(3/4));
fprintf('Valoare exacta: %.12f\n', exact7);


g7 = @(t) (pi/4) * sqrt((1+t) ./ sin(pi*(1+t)/4));

for n = 5:30
    [gn, gc] = my_gauss_jacobi(n, 0, -0.5);
    I7 = my_vquad(gn, gc, g7);
    if abs(I7 - exact7) < 5e-10
        fprintf('n=%d noduri: I = %.12f  err=%.2e\n', n, I7, abs(I7-exact7));
        break
    end
end
