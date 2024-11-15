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
    a db 1
    b db 1
    c dw 1
    e dd 1
    x dq 1
    
    ;(a+b*c+2/c)/(2+a)+e+x; a,b-byte; c-word; e-doubleword; x-qword
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al,[b]
        cbw
        
        mul word [c]
        mov bx,ax
        
        mov al,[a]
        cbw
        add ax,bx
        
        mov bx,ax
        
        mov al,2
        cbw
        div word[c]
        
        add bx,ax
        
        mov al,[a]
        cbw
        add ax,2
        mov dx,ax
        mov ax,bx
        idiv dx 
        
        cwde
        
        mov ebx,[e]
        add eax,ebx
        
        mov ecx,[x+4]
        mov ebx,[x]
        
        add eax,ebx
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
