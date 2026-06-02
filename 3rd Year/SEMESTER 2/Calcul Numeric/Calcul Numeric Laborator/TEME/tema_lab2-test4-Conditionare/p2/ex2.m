clc; clear; close all;

coef_f = poly(1:6); 

c = zeros(1, 21);
c(1) = 1;           
c(15:21) = -coef_f; 

r_exact = roots(c);

% Calculul Numerelor de Conditionare
dp = polyder(c); 
k_rel = zeros(size(r_exact));

fprintf('Estimarea numarului de conditionare relativ pentru cele 20 de radacini:\n');
for i = 1:length(r_exact)
    r_val = r_exact(i);
    
    % Vectorul de puteri pentru a calcula sum(|a_k| * |r|^k)
    puteri = 20:-1:0;
    
    % Numaratorul formulei 
    suma_numarator = sum(abs(c) .* (abs(r_val).^puteri));
    
    % Numitorul pentru numarul de conditionare relativ: |r| * |P'(r)| 
    numitor_relativ = abs(r_val) * abs(polyval(dp, r_val));
    
    k_rel(i) = suma_numarator / numitor_relativ; 
    
    fprintf('Radacina %7.3f %+7.3fi are K_rel = %e\n', real(r_val), imag(r_val), k_rel(i));
end

figure(1); hold on;
% Reprezentam radacinile exacte cu stele rosii
plot(real(r_exact), imag(r_exact), 'rp', 'MarkerSize', 10, 'MarkerFaceColor', 'r');

m = 1000;            % Repetam de 1000 de ori 
sigma = sqrt(1e-2);  % delta_k ~ N(0, 10^-2), deci deviatia standard e sqrt(10^-2) 

fprintf('\nRulam experimentul grafic cu %d iteratii...\n', m);

for j = 1:m
    % Generam perturbarile pentru fiecare coeficient
    delta = sigma * randn(1, 21);
    
    % Perturbam coeficientii: a_k * (1 + delta_k) 
    c_pert = c .* (1 + delta);
    
    % Calculam radacinile polinomului perturbat 
    r_pert = roots(c_pert);
    
    % Reprezentam radacinile in planul complex cu puncte albastre 
    plot(real(r_pert), imag(r_pert), 'b.', 'MarkerSize', 2);
end

title('Experiment Grafic: Efectul perturbarii coeficientilor');
xlabel('Axa Reala (Re)');
ylabel('Axa Imaginara (Im)');
axis equal; 
grid on;
legend('Radacini exacte', 'Radacini perturbate', 'Location', 'best');
hold off;