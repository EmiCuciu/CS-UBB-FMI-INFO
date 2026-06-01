function ff = my_bary_lagrange(x, y, xx)

c = my_bary_weights(x);
ff = my_bary_interp(x, y, xx, c);
end
