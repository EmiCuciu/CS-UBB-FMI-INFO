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
    n dd 0
    m dd 0
    nr dd 0
    text resd 10

    format_m db "Cate numere citesc: %d",0
    format_nr db "%d",0
    format_n db "Minimul de cifre zecimale: %d",0
    
    len db 0
    
    nume_fisier db "output.txt",0
    mod_acces db "w",0
    
    descriptor_fisier dd -1
    
    
segment code use32 class=code
    start:
        ; ...
        
        push dword m
        push dword format_nr
        call [scanf]
        add esp,4*2
        
        push dword [m]
        push dword format_m
        call [printf]
        add esp,4*2
        
        push dword n
        push dword format_nr
        call [scanf]
        add esp,4*2
        
        push dword [n]
        push dword format_n
        call [printf]
        add esp,4*2
        
        
        mov ecx,[m]
        mov esi,0
        repeta:
            push ecx
            push dword nr
            push dword format_nr
            call [scanf]
            add esp,4*2
            
            mov edx,[nr]
            
            mov [text+esi],edx
            add esi,4
            pop ecx
            loop repeta
            
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp,4*2
        
        mov [descriptor_fisier],eax
        
        cmp eax,0
        je final
        
        
        mov esi,0
        verifica:
            mov al,[text+esi]
            mov ah,0
            mov dl,2
            div dl
            
            cmp ah,0
            jg next
            
            cbw
            cwde
            push dword[descriptor_fisier]
            push dword eax
            call [fprintf]
            add esp,4*2
            
            cmp ecx,0
            je close
            
            next:
            inc esi
            jmp verifica
            
        close:
            push dword [descriptor_fisier]
            call [fclose]
            add esp,4
        
        final:
        ; exit(0)
        push dword 0      ; push the parameter for exit onto the stack
        call [exit]