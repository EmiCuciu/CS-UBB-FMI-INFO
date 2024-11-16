bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,printf,scanf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dd 0
    message db "Scrieti numarul a=",0
    format_scanf db "%d",0
    format_raspuns db "a=<%d> (baza 10), a=<%x> (baza 16)",0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push message
        call [printf]
        add esp,4
        
        push a
        push format_scanf
        call [scanf]
        add esp,4*2
        
        mov eax,[a]
        
        push eax
        push eax
        push format_raspuns
        call [printf]
        add esp,4*3
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
