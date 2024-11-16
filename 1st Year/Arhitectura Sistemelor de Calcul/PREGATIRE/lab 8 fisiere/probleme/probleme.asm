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
    c dd 0
    format_scan_caracter db "%c",0
    
    n dd 0
    format_scan_numar db "%d",0
    
    nume_fisier resb 100
    mod_acces_creare db "w",0
    format_creare_fisier db "%s",0
    
    descriptor_fisier dd -1
    
    continut_fisier_txt db "ana",0
    
    len equ 100
    text times len dd 0
    
    format_egale db "Numarul de aparitii al caracterului %c ESTE egal cu numarul %d citit",0
    format_nu_egale db "Numarul de aparitii al caracterului %c NU ESTE egal cu numarul %d citit",0
    
    
segment code use32 class=code
    start:
        ; ...
        
        ;caracter
        push dword c
        push dword format_scan_caracter
        call [scanf]
        add esp,4*2
        
        push dword [c]
        push dword format_scan_caracter
        call [printf]
        add esp,4*2
        
        
        ;numar
        push dword n
        push dword format_scan_numar
        call [scanf]
        add esp,4*2
        
        push dword [n]
        push dword format_scan_numar
        call [printf]
        add esp,4*2
        

        
        ;fisier
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
        
        push dword continut_fisier_txt
        push dword [descriptor_fisier]
        call [fprintf]
        add esp,4*2
        
        push dword [descriptor_fisier]
        push dword len
        push dword 1
        push dword text
        mov ebx,eax
        call [fread]
        add esp,4*4
        
        
        
        push dword [descriptor_fisier]
        call [fclose]
        add esp,4
        
        mov ecx,0
        mov esi,text
        sub esi,ebx
        sub esi,1
        
        mov dl,[c]
        mov bl,0
        
        jmp verifica_cuvant
        
        verifica_cuvant:
            mov al,[esi]
            cmp al,0
            je afisare
            
            cmp al,dl
            je incr
            
            inc esi
            jmp verifica_cuvant
            

        incr:
            inc bl
            inc esi
            jmp verifica_cuvant
           
        afisare:
            cmp bl,[n]
            je egale
            
            push dword [n]
            push dword [c]
            push format_nu_egale
            call [printf]
            add esp,4*3
            jmp final
            
            egale:
                push dword [n]
                push dword [c]
                push format_egale
                call [printf]
                add esp,4*3
            
        final:
        ; exit(0)
        push dword 0      ; push the parameter for exit onto the stack
        call [exit]
        
        