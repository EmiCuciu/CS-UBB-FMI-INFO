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
    a db 7
    b dw 6
    c dd 8
    d dq 20
    ;a - byte, b - word, c - double word, d - qword - Interpretare fara semn
    
    ;  (d+d)-a-b-c
     
; our code starts here
segment code use32 class=code
    start:
        ; ...
    mov edx,[d+4]
    mov eax,[d]
    add eax,eax
    adc edx,edx
    
    movzx ebx,byte [a]
    
    sub eax,ebx
    sbb edx,0
    
    movzx ebx,word[b]
    
    sub eax,ebx
    
    mov ebx,eax 
    
    mov eax,dword[c]
    
    sub ebx,eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
