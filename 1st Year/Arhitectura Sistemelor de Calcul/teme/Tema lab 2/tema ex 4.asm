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
    a db 9
    b db 122
    c db 23
    d dd 180
;200 - [3 * (c + b - d / a) - 300]

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ax,[d]
        mov dx,[d+2]
        
        mov bh,0
        mov bl,[a]
        
        div bx
        
        mov bh,0
        mov bl,[c]
        add bl,[d]
        
        sub bx,ax
        
        mov ax,bx
        mov cx,3
        mul cx
        
        sub ax,300
        
        mov bh,0
        mov bl,200
        
        sub bx,ax 
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    exit       ; call exit to terminate the program
