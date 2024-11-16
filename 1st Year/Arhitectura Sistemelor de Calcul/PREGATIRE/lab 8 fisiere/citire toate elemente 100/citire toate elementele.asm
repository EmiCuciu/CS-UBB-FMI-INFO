bits 32

global start

extern exit,fopen,fclose,fprintf,fread,printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll
import fread msvcrt.dll
import printf msvcrt.dll

segment data use32 class=data
        nume_fisier db "input.txt",0
        mod_acces db "r",0
        descriptor_fisier dd -1
        len equ 200
        nr_caractere_citite dd 0
        buffer resb len
        format db "Numarul de caractere citite este: %d",0
    

segment code use32 class=code
    start:
        ; ...
        ;fopen = (nume_fisier,mod_acces)
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp,4*2
        
        mov [descriptor_fisier],eax
        
        cmp eax,0
        je final
        
        ;fread = (buffer,1,len,descriptor_fisier)
        repeta:
            push dword [descriptor_fisier]
            push dword len
            push dword 1
            push dword buffer
            call [fread]
            add esp,4*4
            
            add [nr_caractere_citite],eax
            
            cmp eax,0
            je cleanup
            
            jmp repeta
            
        cleanup:
            ;fclose = (descriptor_fisier)
            push dword [descriptor_fisier]
            call [fclose]
            add esp,4
            
        push dword [nr_caractere_citite]
        push dword format
        call [printf]
        add esp,4*2
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program