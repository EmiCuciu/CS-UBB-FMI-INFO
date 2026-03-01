function x=Geppdemo(A,b,trace)
%Gepp - Gaussian elimination with partial pivoting
%call x=Gepp(A,b)
%A - matrix, b- right hand side vector
%trace - tracing flag
%x - solution

%initialization
if nargin<3, trace=0; end
[l,n]=size(A);
x=zeros(size(b));
%s=max(abs(A),[],2);
A=[A,b]; %extended matrix
%Elimination
disp("Elimination")
if trace, disp(A); end
for i=1:n-1
    if trace, fprintf('step %d\n', i); end
    [u,p]=max(abs(A(i:n,i)));
    p=p+i-1;
    if u==0, error('no unique solution'), end
    if p~=i %line interchange
        A([i,p],i:n+1)=A([p,i],i:n+1);
        if trace 
            fprintf('exchange rows %d and %d\n', i, p); 
            disp(A);
        end
    end
    j=i+1:n;
    m=A(j,i)/A(i,i);
    A(j,i+1:n+1)=A(j,i+1:n+1)-m*A(i,i+1:n+1); 
    A(j,i)=0;
    if trace, disp(A);  end;
end
%back substitution
disp("Back substitution")
x(n)=A(n,n+1)/A(n,n);
if trace
    fprintf('x(%d)=%+f\n',n,x(n));
end
for i=n-1:-1:1
    x(i)=(A(i,n+1)-A(i,i+1:n)*x(i+1:n))/A(i,i);
    if trace
        fprintf('x(%d)=%g\n',i,x(i));
    end
end
