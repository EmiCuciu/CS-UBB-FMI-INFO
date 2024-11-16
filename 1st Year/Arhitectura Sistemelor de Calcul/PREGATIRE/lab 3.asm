bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,printf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a db 3
    b dw 2
    e dd 5
    x dq 6
    format db "Rezultatul este: %d",0
    
    
    ;(a*a/b+b*b)/(2+b)+e-x; a-byte; b-word; e-doubleword; x-qword
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al,[a]
        imul byte [a]
        cwd
        idiv word [b]
        
        push dx
        push ax
        
        mov ax,[b]
        imul word [b]
        
        add ax,[esp]
        adc dx,[esp+2]
        
        mov bx,[b]
        add bx,2
        
        idiv bx
        
        mov bx,[e]
        mov cx,[e+2]
        
        add ax,bx
        adc dx,cx
        
        mov ah,0
        cwde
        push eax
        mov ax,dx
        cwde
        mov edx,eax
        pop eax
        
        sub eax,[x]
        sbb edx,[x+4]
        
        push edx
        push eax
        push format
        call [printf]
        add esp,4*3
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
