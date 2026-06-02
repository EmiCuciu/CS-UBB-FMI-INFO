clear; clc;

fprintf('--- Prob 1: Formule repetate ---\n');
fprintf('f = sin(x) pe [0,pi], exact = 2\n\n');

f  = @sin;  a = 0; b = pi; exact = 2;

fprintf('%-5s %-14s %-14s %-14s\n','n','Trapez','Dreptunghi','Simpson');
fprintf('%s\n', repmat('-',1,50));
for n = [4, 8, 16, 64]
    I_t = my_trapez(f, a, b, n);
    I_r = my_rectangle(f, a, b, n);
    I_s = my_simpson(f, a, b, n);
    fprintf('%-5d %-14.8f %-14.8f %-14.8f\n', n, I_t, I_r, I_s);
end

fprintf('\nErori:\n');
fprintf('%-5s %-12s %-12s %-12s\n','n','err Trap','err Rect','err Simp');
for n = [4, 8, 16, 64]
    fprintf('%-5d %-12.2e %-12.2e %-12.2e\n', n, ...
        abs(my_trapez(f,a,b,n)-exact), ...
        abs(my_rectangle(f,a,b,n)-exact), ...
        abs(my_simpson(f,a,b,n)-exact));
end

fprintf('\n--- Prob 3: Cuadraturi adaptive (tol=1e-6) ---\n');

f3 = @(x) 4./(1+x.^2);   
a3=0; b3=1; exact3=pi;

I_at = my_adapt_trapez(f3, a3, b3, 1e-6);
I_ar = my_adapt_rect  (f3, a3, b3, 1e-6);
I_as = my_adapt_simpson(f3, a3, b3, 1e-6);

fprintf('f = 4/(1+x^2) pe [0,1], exact = pi\n');
fprintf('  Adapt trapez:   %.10f  err=%.2e\n', I_at, abs(I_at-exact3));
fprintf('  Adapt dreptunghi:  %.10f  err=%.2e\n', I_ar, abs(I_ar-exact3));
fprintf('  Adapt Simpson:  %.10f  err=%.2e\n', I_as, abs(I_as-exact3));

fprintf('\n--- Prob 4: Romberg (tol=1e-8) ---\n');

[I_rom, nfev_rom] = my_romberg(@sin, 0, pi, 1e-8);
fprintf('∫sin(x)dx pe [0,pi] = %.12f  (nfev=%d)\n', I_rom, nfev_rom);
fprintf('Eroare = %.2e\n', abs(I_rom - 2));

f4 = @(x) exp(-x.^2);
[I_rom2, nfev2] = my_romberg(f4, 0, 1, 1e-10);
fprintf('∫exp(-x^2)dx pe [0,1] = %.12f  (nfev=%d)\n', I_rom2, nfev2);
fprintf('Exact (erf) = %.12f\n', sqrt(pi)/2*erf(1));

fprintf('\n--- Prob 5: adquad (tol=1e-8) ---\n');

[I_adq, fc_adq] = my_adquad(@sin, 0, pi, 1e-8);
fprintf('∫sin(x)dx pe [0,pi] = %.12f  (nfev=%d)\n', I_adq, fc_adq);
fprintf('Eroare = %.2e\n', abs(I_adq - 2));

fprintf('\nComparatie nfev pentru ∫sin(x)dx:\n');
[~, nr] = my_romberg(@sin, 0, pi, 1e-8);
[~, na] = my_adquad(@sin, 0, pi, 1e-8);
fprintf('  Romberg: %d  |  adquad: %d\n', nr, na);