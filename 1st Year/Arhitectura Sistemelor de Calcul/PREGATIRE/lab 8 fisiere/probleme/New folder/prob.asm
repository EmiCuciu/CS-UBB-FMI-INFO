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
    format_scan_numar db "%d",0
    
    nume_fisier resb 100
    format_scan_fisier db "%s",0
    
    mod_acces db "r",0
    descriptor_fisier dd -1
    len equ 100
    text times len dd 0
    
    format_rez db "%d cuvinte au %d vocale",0
    
segment code use32 class=code
start:
    ; ...
    push dword n
    push dword format_scan_numar
    call dword [scanf]
    add esp, 4 * 2
    
    push dword nume_fisier
    push dword format_scan_fisier
    call dword [scanf]
    add esp, 4 * 2
    
    push dword mod_acces
    push dword nume_fisier
    call dword [fopen]
    add esp, 4 * 2 ; Ajustează stiva pentru a face loc parametrului "mod_acces"
    
    mov [descriptor_fisier], eax
    cmp eax, 0
    je final
    
    push dword [descriptor_fisier]
    push dword len
    push dword 1
    push dword text
    call dword [fread]
    add esp, 4 * 4
    
    mov ecx, eax
    mov esi, text
    
repeta:
    mov al, [esi]
    cmp al, '/'
    je urm_cuvant
    
    cmp al, 'a'
    je plus
    
    cmp al, 'e'
    je plus
    
    cmp al, 'i'
    je plus
    
    cmp al, 'o'
    je plus
    
    cmp al, 'u'
    je plus
    
    inc esi
    cmp ecx, 0 ; Verifică dacă mai sunt caractere de citit
    jg repeta
    
plus:
    add bl, 1
    jmp repeta
    
urm_cuvant:
    cmp bl, [n]
    je afisare
    mov bl, 0
    inc esi
    
afisare:
    add dl, 1
    
    mov al, dl
    cbw
    cwde
    push dword eax
    push dword [n]
    push dword format_rez
    call dword [printf]
    add esp, 4 * 3
    
final:
    ; exit(0)
    push dword 0 ; push the parameter for exit onto the stack
    call dword [exit]