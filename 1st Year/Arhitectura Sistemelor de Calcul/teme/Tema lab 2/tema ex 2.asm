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
    
    ; d-(a+b)+c
    
    a db 3
    b db 4
    c db 5
    d db 9
; our code starts here
segment code use32 class=code
    start:
        ; ...
    MOV AL,[a] ;AL=3
    ADD AL,[b] ;AL=3+4=7
    MOV DL,[d] ;DL=9
    SUB DL,AL  ;DL-AL=9-7=2
    ADD DL,[c] ;DL=2+5=7
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
