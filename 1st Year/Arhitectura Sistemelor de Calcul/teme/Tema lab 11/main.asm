bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, scanf, fopen, fclose, fprintf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import scanf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll
import fprintf msvcrt.dll

extern find_min

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    nr dd 0
    val dd 0
    min_len equ 25
    arr times min_len dd 0
    min_val dd 0
    
    file_name db "minim.txt", 0
    access_mode db "w", 0
    file_descr dd -1
    
    r_format db "%d", 0
    w_format db "%x", 0

; our code starts here
segment code use32 class=code
    start:
        ;citim numarul de valori
        push dword nr
        push dword r_format
        call [scanf]
        add esp, 4*2
        
        ;citim valorile
        mov edi, arr
        
        citire:
            cmp dword [nr], 0
            je exit_citire 
            
            push dword val
            push dword r_format
            call [scanf]
            add esp, 4*2
          
            mov eax, [val]
            stosd
            
            dec dword [nr]
        loop citire
        
        exit_citire:
        
            ;deschidem fisierul
            push dword access_mode
            push dword file_name
            call [fopen]
            add esp, 4*2
            
            ;salvam descriptorul de fisier
            mov [file_descr], eax
            
            ;verificam daca a fost deschis cu succes
            cmp eax, 0
            je final
            
            ;apelam functia find_min
            push dword arr
            call find_min
            
            ;salvam valoarea minima
            mov [min_val], eax
            
            ;scriem valoarea minima in fisier
            push dword [min_val]
            push dword w_format
            push dword [file_descr]
            call [fprintf]
            add esp, 4 * 3
            
            ;inchidem fisierul
            push dword [file_descr]
            call [fclose]
            add esp, 4 * 1

        final:
            push dword 0
            call [exit]