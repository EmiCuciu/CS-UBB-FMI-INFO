bits 32

global start

extern exit, scanf, printf, fopen, fread, fclose, fprintf

import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll

segment data use32 class=data
    nume_fisier resb 100
    mod_acces_creare db "w",0
    format_creare_fisier db "%s",0
    descriptor_fisier dd -1
    
    
segment code use32 class=code
    start:
        ; ...
        push dword nume_fisier
        push dword format_creare_fisier
        call [scanf]
        add esp,4*2
        
        push dword mod_acces_creare
        push dword nume_fisier
        call [fopen]
        add esp,4*2
        
        mov [descriptor_fisier],eax
        cmp eax,0
        je final
        
        
        
        
        final:
        ; exit(0)
        push dword 0      ; push the parameter for exit onto the stack
        call [exit]
        
        