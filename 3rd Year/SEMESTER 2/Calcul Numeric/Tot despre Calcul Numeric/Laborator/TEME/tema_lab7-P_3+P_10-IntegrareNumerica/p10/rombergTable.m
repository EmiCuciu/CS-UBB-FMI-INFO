function R = rombergTable(f, a, b, nMax)

R = zeros(nMax, nMax);

for i = 1:nMax
    ni = 2^(i-1);                
    R(i,1) = myTrapezoid(f, a, b, ni);
end

for j = 2:nMax
    fac = 4^(j-1);
    for i = 1:(nMax - j + 1)
        R(i,j) = (fac * R(i+1,j-1) - R(i,j-1)) / (fac - 1);
    end
end
end
