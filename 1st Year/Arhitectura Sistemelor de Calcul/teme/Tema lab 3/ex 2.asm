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
    b dw 9
    c dd 5
    d dq 65
        
    
    ;a - byte, b - word, c - double word, d - qword - Interpretare cu semn

    ;(d-b)-a-(b-c)          =(65-9)-7-(9-5)=56-7-4=45
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx,[d+4]
        mov ebx,[d]
        
        mov al,[b]
        cbw
        cwde
        sub ebx,eax
        sbb ecx,0
        
        mov al,[a]
        cbw
        cwde
        sub ebx,eax
        sbb ecx,0
        
        mov al,[a]
        cbw
        cwde
        sub eax,[c]
        
        sub ebx,eax
        sbb ecx,0
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
