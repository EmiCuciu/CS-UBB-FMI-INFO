bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a db 8
    b db 9
    c dw 2
    e dd 16
    x dq 19
    
    ;(a+b*c+2/c)/(2+a)+e+x; a,b-byte; c-word; e-doubleword; x-qword
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al,[b]
		cbw
		mov dx,[c]
		mul dx
		mov bx,ax
		mov al,[a]
		cbw
		add ax,bx
		
		mov bx,ax
		
		mov al,2
		cbw
		cwde
		mov dx,[c]
		div dx
		
		add bx,ax
		
		mov al,[a]
		add al,2
		cbw
		
		mov dx,ax
		mov ax,bx
		div dx
		
		cwde
		
		mov ebx,[e]
		add eax,ebx
		mov ebx,eax
		
		mov eax,[x]
		mov edx,[x+4]
		
		add ebx,eax
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
