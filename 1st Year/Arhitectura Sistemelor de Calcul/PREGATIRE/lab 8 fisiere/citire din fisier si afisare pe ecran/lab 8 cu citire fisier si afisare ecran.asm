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
        nume_fisier db "ana.txt",0
        mod_acces db "r",0
        descriptor_fisier dd -1
        len equ 100
        text times (len+1) db 0
        format db "Am citit %d caractere. Textul este %s",0
    

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
        
        ;fread = (text,1,len,descriptor_fisier)
        push dword [descriptor_fisier]
        push dword len
        push dword 1
        push text
        call [fread]
        add esp,4*4
        
        ;printf = (format,eax,text)
        push dword text
        push dword eax
        push dword format
        call [printf]
        add esp,4*3
        
        ;fclose = (descriptor_fisier)
        push dword [descriptor_fisier]
        call [fclose]
        add esp,4
        
        
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program