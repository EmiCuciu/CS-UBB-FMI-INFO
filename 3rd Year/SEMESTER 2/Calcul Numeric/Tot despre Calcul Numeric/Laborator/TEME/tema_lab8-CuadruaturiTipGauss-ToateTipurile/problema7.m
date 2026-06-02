pkg load symbolic
format long
clear; clc;

tol = 1e-10;

syms x
ve_Ic = double(vpa(int(cos(x)/sqrt(x), x, 0, 1)));
ve_Is = double(vpa(int(sin(x)/sqrt(x), x, 0, 1)));
fprintf('Valori exacte (simbolic):\n');
fprintf('  Ic_exact = %.15f\n', ve_Ic);
fprintf('  Is_exact = %.15f\n\n', ve_Is);

fprintf('a) Cuadratura adaptiva \n');

fc_a_trans = @(t) 2 * cos(t.^2);
fs_a_trans = @(t) 2 * sin(t.^2);

[Ic_a, eval_c] = myAdQuad(fc_a_trans, 0, 1, tol);
[Is_a, eval_s] = myAdQuad(fs_a_trans, 0, 1, tol);

fprintf('  Ic(adaptiv) = %.15f,  |err| = %.3e (evaluari: %d)\n', Ic_a, abs(Ic_a - ve_Ic), eval_c);
fprintf('  Is(adaptiv) = %.15f,  |err| = %.3e (evaluari: %d)\n\n', Is_a, abs(Is_a - ve_Is), eval_s);


fprintf('b) Gauss-Legendre \n');

fc_b = @(z) cos((z+1).^2 / 4);
fs_b = @(z) sin((z+1).^2 / 4);

n0 = 5;
[gn, gc] = Gauss_Legendre(n0);
Ic_prev = vquad(gn, gc, fc_b);
Is_prev = vquad(gn, gc, fs_b);

n_final_b = n0;
Ic_b = Ic_prev; Is_b = Is_prev;
for n = n0+1 : 200
    [gn, gc] = Gauss_Legendre(n);
    Ic_n = vquad(gn, gc, fc_b);
    Is_n = vquad(gn, gc, fs_b);
    if abs(Ic_n - Ic_prev) < tol && abs(Is_n - Is_prev) < tol
        n_final_b = n;
        Ic_b = Ic_n; Is_b = Is_n;
        fprintf('  Convergenta la n = %d\n', n_final_b);
        break;
    end
    Ic_prev = Ic_n; Is_prev = Is_n;
end

fprintf('  Ic(GL, n=%d) = %.15f,  |err| = %.3e\n', n_final_b, Ic_b, abs(Ic_b - ve_Ic));
fprintf('  Is(GL, n=%d) = %.15f,  |err| = %.3e\n\n', n_final_b, Is_b, abs(Is_b - ve_Is));

fprintf('c) Gauss-Jacobi\n');

aJ = 0;      
bJ = -1/2;   
fac = 1/sqrt(2);

fc_c = @(z) cos((z+1)/2);
fs_c = @(z) sin((z+1)/2);

n0 = 5;
[gn, gc] = Gauss_Jacobi(n0, aJ, bJ);
Ic_prev = fac * vquad(gn, gc, fc_c);
Is_prev = fac * vquad(gn, gc, fs_c);

n_final_c = n0;
Ic_c = Ic_prev; Is_c = Is_prev;
for n = n0+1 : 200
    [gn, gc] = Gauss_Jacobi(n, aJ, bJ);
    Ic_n = fac * vquad(gn, gc, fc_c);
    Is_n = fac * vquad(gn, gc, fs_c);
    if abs(Ic_n - Ic_prev) < tol && abs(Is_n - Is_prev) < tol
        n_final_c = n;
        Ic_c = Ic_n; Is_c = Is_n;
        fprintf('  Convergenta la n = %d\n', n_final_c);
        break;
    end
    Ic_prev = Ic_n; Is_prev = Is_n;
end

fprintf('  Ic(Jacobi, n=%d) = %.15f,  |err| = %.3e\n', n_final_c, Ic_c, abs(Ic_c - ve_Ic));
fprintf('  Is(Jacobi, n=%d) = %.15f,  |err| = %.3e\n\n', n_final_c, Is_c, abs(Is_c - ve_Is));
