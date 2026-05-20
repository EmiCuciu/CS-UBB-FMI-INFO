clear; close all; clc;

f = @(x) log(x); 
a = 1;
b = 2;
exact = 2*log(2) - 1;

fprintf('Integrala: int_1^2 ln(x) dx\n');
fprintf('Valoarea exacta = 2*ln(2) - 1 = %.15f\n\n', exact);

nMax = 9;
R = rombergTable(f, a, b, nMax);

fprintf('a)\n');
fprintf('  h_i = (b-a)/2^(i-1),  n_Simpson = 2^i subintervale\n\n');
fprintf('%-4s  %-14s  %-14s  %-10s\n', 'i', 'R(i,2)', 'Simpson(2^i)', '|Diferenta|');
fprintf('%s\n', repmat('-',1,50));
for i = 2:nMax-1
    n_simp = 2^i;                           
    S_dir = mySimpson(f, a, b, n_simp);
    fprintf('%4d  %14.10f  %14.10f  %.3e\n', i, R(i,2), S_dir, abs(R(i,2)-S_dir));
end
fprintf('\n');

fprintf('b)\n');
fprintf('  h_i = (b-a)/2^(i-1),  n_Boole = 2^(i+1) subintervale\n\n');
fprintf('%-4s  %-14s  %-14s  %-10s\n', 'i', 'R(i,3)', 'Boole(2^(i+1))', '|Diferenta|');
fprintf('%s\n', repmat('-',1,55));
for i = 3:nMax-2
    n_boole = 2^(i+1);                      
    B_dir   = myBoole(f, a, b, n_boole);
    fprintf('%4d  %14.10f  %14.10f  %.3e\n', i, R(i,3), B_dir, abs(R(i,3)-B_dir));
end
fprintf('\n');

%c)
K = 8;                            
m_vals = 2.^(1:K);                    
H_vals = (b - a) ./ m_vals;           

err_T = zeros(1, K);
err_S = zeros(1, K);
err_B = zeros(1, K);

for k = 1:K
    m = m_vals(k);
    err_T(k) = abs(myTrapezoid(f, a, b, m)     - exact);
    err_S(k) = abs(mySimpson(f,  a, b, 2*m)    - exact);
    err_B(k) = abs(myBoole(f,    a, b, 4*m)    - exact);
end

floor_val = eps;
err_T_plot = max(err_T, floor_val);
err_S_plot = max(err_S, floor_val);
err_B_plot = max(err_B, floor_val);

ref_T = H_vals.^2 * (err_T_plot(1) / H_vals(1)^2);
ref_S = H_vals.^4 * (err_S_plot(1) / H_vals(1)^4);
ref_B = H_vals.^6 * (err_B_plot(1) / H_vals(1)^6);

figure('Name','Convergenta cuadratura','Position',[100 100 820 560]);

loglog(H_vals, err_T_plot, 'b-o', 'LineWidth', 2, 'MarkerSize', 7, ...
       'DisplayName', 'Trapez [R(i,1)]');
hold on;
loglog(H_vals, err_S_plot, 'r-s', 'LineWidth', 2, 'MarkerSize', 7, ...
       'DisplayName', 'Simpson [R(i,2)]');
loglog(H_vals, err_B_plot, 'g-^', 'LineWidth', 2, 'MarkerSize', 7, ...
       'DisplayName', 'Boole [R(i,3)]');

loglog(H_vals, ref_T, 'b--', 'LineWidth', 1, 'DisplayName', 'O(H^2) ref');
loglog(H_vals, ref_S, 'r--', 'LineWidth', 1, 'DisplayName', 'O(H^4) ref');
loglog(H_vals, ref_B, 'g--', 'LineWidth', 1, 'DisplayName', 'O(H^6) ref');

mid = round(K/2);
text(H_vals(mid), ref_T(mid)*3,  'panta \approx 2', 'Color','b','FontSize',11);
text(H_vals(mid), ref_S(mid)*3,  'panta \approx 4', 'Color','r','FontSize',11);
text(H_vals(mid), ref_B(mid)*0.3,'panta \approx 6', 'Color',[0 0.6 0],'FontSize',11);

xlabel('H  (latimea panoului = (b-a)/m)', 'FontSize', 12);
ylabel('|eroare|',                         'FontSize', 12);
legend('Location', 'southeast', 'FontSize', 10);
grid on;
set(gca, 'FontSize', 11);

valid_T = err_T > 10 * eps;
if sum(valid_T) >= 2
    slope_T = mean(diff(log(err_T(valid_T))) ./ diff(log(H_vals(valid_T))));
else, slope_T = NaN; end

valid_S = err_S > 10 * eps;
if sum(valid_S) >= 2
    slope_S = mean(diff(log(err_S(valid_S))) ./ diff(log(H_vals(valid_S))));
else, slope_S = NaN; end

valid_B = err_B > 10 * eps;
if sum(valid_B) >= 2
    slope_B = mean(diff(log(err_B(valid_B))) ./ diff(log(H_vals(valid_B))));
else, slope_B = NaN; end

fprintf('  Trapez:  panta medie = %.2f  (teoretic: 2)\n', slope_T);
fprintf('  Simpson: panta medie = %.2f  (teoretic: 4)\n', slope_S);
fprintf('  Boole:   panta medie = %.2f  (teoretic: 6)\n', slope_B);
