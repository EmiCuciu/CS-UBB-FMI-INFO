bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
section data use32 class=data
    ; ...
    text db 'cojoc capac civic abcba acba', 0  ; Exemplu de text

; our code starts here
section code use32 class=code
    start:
    ; Parcurgem textul pana la terminatorul nul
    mov esi, text
    mov ecx, 0

    next_word:
        ; Ignoram spatii si alte caractere non-alfabetice
        ignore_non_alphabetic:
            cmp byte [esi], 0
            je end_program
            cmp byte [esi], ' '
            je skip_char
            cmp byte [esi], 'a'
            jl skip_char
            cmp byte [esi], 'z'
            jg skip_char
            jmp process_char
        skip_char:
            inc esi
            jmp ignore_non_alphabetic
        
        process_char:
            ; Initializam variabilele pentru verificarea palindromului
            mov edi, esi          ; edi va pointa la sfarsitul cuvantului
            find_word_end:
                cmp byte [edi], 0
                je check_palindrome
                cmp byte [edi], ' '
                je check_palindrome
                cmp byte [edi], 'a'
                jl check_palindrome
                cmp byte [edi], 'z'
                jg check_palindrome
                inc edi
                jmp find_word_end

            check_palindrome:
                ; Verificam daca cuvantul este palindrom
                cmp esi, edi
                jge next_word  ; trecem la urmatorul cuvant daca am ajuns la sfarsitul cuvantului
                mov al, [esi]       ; incarcam prima valoare in al
                mov bl, [edi]       ; incarcam a doua valoare in bl
                cmp al, bl
                jne not_palindrome
                inc esi
                dec edi
                jmp check_palindrome
            not_palindrome:
                ; Daca nu este palindrom, continuam la urmatorul cuvant
                mov esi, edi
                jmp next_word

    end_program:
        ; exit(0)
        push dword 0
        call exit
