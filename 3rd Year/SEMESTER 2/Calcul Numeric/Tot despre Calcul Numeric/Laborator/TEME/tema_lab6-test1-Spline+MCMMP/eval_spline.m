function yy = eval_spline(x, a, b, c, d, xx)
    yy = zeros(size(xx));
    n = length(x) - 1;
    
    for k = 1:length(xx)
        idx = find(x <= xx(k), 1, 'last');
        if isempty(idx), idx = 1; end
        if idx > n, idx = n; end
        
        dx = xx(k) - x(idx);
        yy(k) = a(idx) + b(idx)*dx + c(idx)*dx^2 + d(idx)*dx^3;
    end
end