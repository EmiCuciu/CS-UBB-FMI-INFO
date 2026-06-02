function c = my_cubic_spline(x, f, type, der)
if nargin < 4 || type == 2, der = [0, 0]; end

n = length(x);
x = x(:); f  = f(:);
dx = diff(x);
ddiv = diff(f) ./ dx;        
ds = dx(1:end-1);
dd = dx(2:end);
dp = 2*(ds + dd);           
md = 3*(dd.*ddiv(1:end-1) + ds.*ddiv(2:end)); 

switch type
    case 0  % complet: m1=f'(a), mn=f'(b)
        dp1=1;  dpn=1;  vd1=0;      vdn=0;
        md1=der(1);         mdn=der(2);
    case {1,2}  % D2 sau natural
        dp1=2;  dpn=2;  vd1=1;      vdn=1;
        md1=3*ddiv(1)   - 0.5*dx(1)*der(1);
        mdn=3*ddiv(end) + 0.5*dx(end)*der(2);
    case 3  % deBoor (not-a-knot)
        x31 = x(3)-x(1);  xn = x(n)-x(n-2);
        dp1 = dx(2);       dpn = dx(end-1);
        vd1 = x31;         vdn = xn;
        md1 = ((dx(1)+2*x31)*dx(2)*ddiv(1)+dx(1)^2*ddiv(2))/x31;
        mdn = (dx(end)^2*ddiv(end-1)+(2*xn+dx(end))*dx(end-1)*ddiv(end))/xn;
end


dp_full = [dp1; dp;  dpn];
sup = [0;  vd1; dd ];
sub = [ds; vdn; 0  ];
md_full = [md1; md; mdn];
A = spdiags([sub, dp_full, sup], -1:1, n, n);
m = A \ md_full;              

c(:,4) = f(1:end-1);
c(:,3) = m(1:end-1);
c(:,1) = (m(2:end) + m(1:end-1) - 2*ddiv) ./ (dx.^2);
c(:,2) = (ddiv - m(1:end-1)) ./ dx - dx .* c(:,1);
end
