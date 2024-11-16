bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,printf,scanf,fopen,fprintf,fread,fclose               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

import printf msvcrt.dll
import scanf msvcrt.dll
import fprintf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fread msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    nume_fisier db "ex 17.txt",0
    mod_acces db "w",0
    descriptor_fisier dd -1
    format_print db "Dati 6 numere, ultimul trebuie sa fie 0: ",0
    format_scanf db "%d",0
    len equ 6
    text times len db 0
    a dd 0
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        push format_print
        call [printf]
        add esp,4
        
        push mod_acces
        push nume_fisier
        call [fopen]
        add esp,4*2
        
        mov [descriptor_fisier],eax
        
        cmp eax,0
        je final
        
        mov esi,0
        repeta:
            push a
            push format_scanf
            call [scanf]
            add esp,4*2
            
            mov eax,[a]
            
            cmp eax,0
            je div_7
            
            mov [text+esi],eax
            add esi,2
            jmp repeta
        
        
        mov ecx,esi
        div_7:
            mov esi,0
            lop:
                mov ax,[text+esi]
                mov dx,ax
                add esi,2
                mov bl,7
                div bl
                
                cmp ah,0
                je afisare
                
                loop lop
        
        afisare:
            mov ax,dx
            cwde
            push eax
            push dword [descriptor_fisier]
            call [fprintf]
            add esp,4*2
        
        push dword [descriptor_fisier]
        call [fclose]
        add esp,4
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
