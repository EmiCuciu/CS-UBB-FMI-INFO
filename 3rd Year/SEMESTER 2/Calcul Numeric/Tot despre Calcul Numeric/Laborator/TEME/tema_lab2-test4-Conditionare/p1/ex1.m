clc; clear; close all;

r = 6;
epsilon = -1e-6;


% radacina aporpiata de 6
g_r = r^7;

% f'(6) = (x-1)(x-2)(x-3)(x-4)(x-5) = 5!
f_prim_r = 120;


delta_r = - (epsilon * g_r) / f_prim_r;

radacina_apropiata_teoretic = r + delta_r;

fprintf('∆r: %f\n', delta_r);
fprintf('Radacina estimata (aproape de 6): %f\n', radacina_apropiata_teoretic);


radacina_mare_teoretic = 1 / abs(epsilon);

fprintf('Cea mai mare rad ( x^{6} - 10^{-6}x^7 ): %f\n', radacina_mare_teoretic);

fprintf(' \nVerificare \n');

%generam polinomul f(x)
coef_f = poly(1:6);

coef_P = [-1e-6, coef_f];   % prima poz, cea mai amre putere

radacini_P = roots(coef_P);

% cautam radacina cea mai apropiata de 6 calculand distantele
[~, idx_6] = min(abs(radacini_P - 6));
radacina_apropiata_numeric = radacini_P(idx_6);

% cautam cea mai mare radacina
[~, idx_max] = max(real(radacini_P));
radacina_mare_numeric = radacini_P(idx_max);

fprintf('Radacina apropiata de 6 (reala): %f\n', real(radacina_apropiata_numeric));
fprintf('Cea mai mare radacina (reala):   %f\n', real(radacina_mare_numeric));
