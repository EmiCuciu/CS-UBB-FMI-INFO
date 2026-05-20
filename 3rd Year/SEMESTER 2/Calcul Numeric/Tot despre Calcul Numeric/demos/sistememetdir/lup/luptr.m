function [L,U,P]=luptr(A,trace)
%LUP find LUP decomposition of matrix A
%call [L,U,P]=lup(A)
%permute effectively lines

if nargin<2, trace=0; end
[m,n]=size(A);
P=zeros(m,n);
piv=(1:m)';
if trace 
    disp("Initial matrix;")
    disp([piv,A]); 
end
for i=1:m-1
    if trace, fprintf('step %d\n', i); end
    %pivoting
    [pm,kp]=max(abs(A(i:m,i)));
    kp=kp+i-1;
    %line interchange
    if i~=kp
        A([i,kp],:)=A([kp,i],:);
        piv([i,kp])=piv([kp,i]);
        if trace 
            fprintf('exchange rows %d and %d\n', i, kp); 
            disp([piv,A]);
        end
    end
    %Schur complement
    lin=i+1:m;
    A(lin,i)=A(lin,i)/A(i,i);
    if trace
        SC=A(lin,lin)-A(lin,i)*A(i,lin);
        disp("Schur complement")
        disp(SC);
    end
    A(lin,lin)=A(lin,lin)-A(lin,i)*A(i,lin);
    if trace, disp([piv,A]);  end
end
for i=1:m
    P(i,piv(i))=1;
end
U=triu(A);
L=tril(A,-1)+eye(m);

