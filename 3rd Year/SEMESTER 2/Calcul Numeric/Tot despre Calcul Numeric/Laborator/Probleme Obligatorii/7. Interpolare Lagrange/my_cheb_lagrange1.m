function ff = my_cheb_lagrange1(y, xx, a, b)

n = length(y) - 1;
if nargin == 2, a = -1; b = 1; end

c = sin((2*(0:n)'+1)*pi/(2*n+2)) .* (-1).^((0:n)');
x = sort(cos((2*(0:n)'+1)*pi/(2*n+2))) * (b-a)/2 + (a+b)/2;

ff = my_bary_interp(x, y, xx, c);
end
