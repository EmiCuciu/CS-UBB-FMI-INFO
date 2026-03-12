HermiteBasicPol:=proc(x, r, k,j,t)
  local u,m,i,H,nu,s;
  begin
  m:=nops(x)-1;
  s:=0; u:=1; 
  for i from 0 to m do   
     if k<>i then u:=u*(t-x[i+1])^(r[i+1]+1):
     end_if:
  end_for:
  H:=(t-x[k+1])^j/j!*u; 
  s:=(1/u)|t=x[k+1]:
  for nu from 1 to r[k+1]-j do
      s:=s+(t-x[k+1])^nu/nu!*(diff(1/u,t$nu)|t=x[k+1]):
  end_for;
  return(H*s);
  end_proc:

HermitePol:=proc(x,r,t,f)
  local k,j,s,m;
  begin
  m:=nops(x)-1; 
  s:=0;
  for k from 0 to m do
     s:=s+HermiteBasicPol(x,r,k,0,t)*f(x[k+1]);
  end_for;
  for k from 0 to m do
     for j from 1 to r[k+1] do
        s:=s+HermiteBasicPol(x,r,k,j,t)*(D@@j)(f)(x[k+1]);
     end_for;
  end_for;
  return(collect(s,f));
end_proc: