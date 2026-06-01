function my_hermite_cubic(P0, T0, P1, T1)

tt  = linspace(0, 1, 300);
h00 = 2*tt.^3 - 3*tt.^2 + 1;
h10 = tt.^3  - 2*tt.^2 + tt;
h01 = -2*tt.^3 + 3*tt.^2;
h11 = tt.^3  - tt.^2;

x_t = h00*P0(1) + h10*T0(1) + h01*P1(1) + h11*T1(1);
y_t = h00*P0(2) + h10*T0(2) + h01*P1(2) + h11*T1(2);

plot(x_t, y_t, 'b-', 'LineWidth', 2.5, 'DisplayName', 'Curba Hermite');
hold on;
plot([P0(1), P1(1)], [P0(2), P1(2)], 'ro', 'MarkerSize', 9, ...
     'MarkerFaceColor', 'r', 'DisplayName', 'Puncte P_0, P_1');
s = 0.25; 
quiver(P0(1), P0(2), T0(1)*s, T0(2)*s, 0, 'g', 'LineWidth', 2, ...
       'MaxHeadSize', 0.5, 'DisplayName', 'Tangente T_0, T_1');
quiver(P1(1), P1(2), T1(1)*s, T1(2)*s, 0, 'g', 'LineWidth', 2, ...
       'MaxHeadSize', 0.5, 'HandleVisibility', 'off');
grid on; axis equal;
end
