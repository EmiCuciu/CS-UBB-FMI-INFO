function [xx, yy] = my_spline_param(pts, type)

if nargin < 2, type = 2; end

N  = size(pts, 1);
t  = (0:N-1)';
cx = my_cubic_spline(t, pts(:,1), type);
cy = my_cubic_spline(t, pts(:,2), type);

tt = linspace(0, N-1, 500);
xx = my_eval_spline(t, cx, tt);
yy = my_eval_spline(t, cy, tt);
end
