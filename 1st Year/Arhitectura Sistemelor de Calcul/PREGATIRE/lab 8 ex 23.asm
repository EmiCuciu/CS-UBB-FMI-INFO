bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,printf,scanf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import scanf msvcrt.dll
                          
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
        a dd 0
        format db "Numarul hexa este a=",0
        format_scanf db "%x",0
        format_print db "Numarul in baza 10 este %d",13,10,0
        format_binar_unsigned db "Numarul binar fara semn este: %d",13,10,0
        format_binar_signed db "Numarul binar cu semn este : %d",13,10,0
        
; our code starts here
segment code use32 class=code
    start:
        ; ...
        push format
        call [printf]
        add esp,4
        
        push a
        push format_scanf
        call [scanf]
        add esp,4*2
        
        
        push dword [a]
        push format_print
        call [printf]
        add esp,4*2
        
        mov eax,0
        movzx ebx,byte [a]
        sub ebx,'0'
        shl eax,4 
        add eax,ebx
        
        push eax
        push format_binar_unsigned
        call [printf]
        add esp,4*2
        
        movsx ecx,byte [a]
        sub ecx,'0'
        
        push ecx
        push format_binar_signed
        call [printf]
        add esp,4*2
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
