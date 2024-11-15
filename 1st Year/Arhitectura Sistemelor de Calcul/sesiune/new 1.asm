bits 32
global start

extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
    max equ 50
    s1 times max+1 db 0
    s2 times max+1 db 0
    d times 2*max+1 db 0
        
    format_r db "%s", 0
    format_p db "Sir final: %s", 13, 10, 0
    text_1 db "Dati primul sir: ", 0
    text_2 db "Dati al doilea sir: ", 0

segment code use32 class=code
    start:
        ; ...
        push dword text_1
        call [printf]
        add esp,4*1
        
        push dword s1
        push dword format_r
        call [scanf]
        add esp,4*2
        
        
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
