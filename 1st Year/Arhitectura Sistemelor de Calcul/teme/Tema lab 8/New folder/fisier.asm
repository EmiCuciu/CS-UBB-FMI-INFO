bits 32 ; Asamblează pentru arhitectura pe 32 de biți

; Declarația punctului de intrare (o etichetă care definește prima instrucțiune a programului)
global start        

; Declarația funcțiilor externe necesare programului nostru
extern exit, fopen, fread, fclose, printf
import exit msvcrt.dll
import fopen msvcrt.dll
import fread msvcrt.dll
import fclose msvcrt.dll
import printf msvcrt.dll

; Declarația datelor noastre (variabilele necesare programului)
section data use32 class=data
    nume_fisier db "input.txt", 0  ; numele fisierului care va fi deschis
    mod_acces db "r", 0            ; modul de deschidere a fisierului - r - pentru citire
    descriptor_fis dd -1           ; variabila in care vom salva descriptorul fisierului
    len equ 100                    ; numarul maxim de elemente citite din fisier
    text times len db 0            ; sirul in care se va citi textul din fisier
    format_cuvinte db "Numarul de cuvinte: %d", 0  ; Formatul pentru printf

; Codul nostru începe aici
section code use32 class=code
start:
    ; Apelăm fopen pentru a deschide fisierul
    ; eax = fopen(nume_fisier, mod_acces)
    push dword mod_acces     
    push dword nume_fisier
    call [fopen]
    add esp, 4*2                ; eliberam parametrii de pe stiva

    mov [descriptor_fis], eax   ; salvam valoarea returnata de fopen
    
    ; Verificam daca functia fopen a creat cu succes fisierul (daca EAX != 0)
    cmp eax, 0
    je final

    ; Citim textul din fisierul deschis folosind functia fread
    ; eax = fread(text, 1, len, descriptor_fis)
    push dword [descriptor_fis]
    push dword len
    push dword 1
    push dword text        
    call [fread]
    add esp, 4*4                 ; dupa apelul functiei fread, EAX contine numarul de caractere citite din fisier

    ; Apelăm functia fclose pentru a închide fisierul
    ; fclose(descriptor_fis)
    push dword [descriptor_fis]
    call [fclose]
    add esp, 4

    ; Determinarea numărului de cuvinte în șirul text
    mov ecx, 0             ; Inițializam numarul de cuvinte cu 0
    mov esi, text           ; ESI pointeaza la începutul șirului text
    
    jmp verifica_cuvant     ; Incepem bucla verificarii direct cu un jump

verifica_cuvant:
    ; Cautam primul caracter care nu este spațiu sau punct
    mov al, [esi]
    cmp al, 0
    je  afisare_numar_cuvinte  ; Daca am ajuns la sfârșitul șirului, afișam rezultatul
    
    cmp al, ' '             ; Comparam cu spațiu
    je  incrementare_cuvant  ; Daca caracterul este spațiu, trecem la urmatorul caracter
    
    cmp al, '.'             ; Comparam cu punct
    je  incrementare_cuvant  ; Daca caracterul este punct, trecem la urmatorul caracter
    
    inc esi                 ; Incrementam registrul ESI pentru a trece la urmatorul caracter
    jmp verifica_cuvant     ; Daca caracterul nu este spațiu sau punct, continuam verificarea

incrementare_cuvant:
    ; Am gasit un cuvant, incrementam numarul de cuvinte
    inc ecx
    inc esi                 ; Incrementam registrul ESI pentru a trece la urmatorul caracter
    jmp verifica_cuvant     ; Continuam verificarea cuvantului

afisare_numar_cuvinte:
    ; Afisam numarul de cuvinte pe ecran
    push    ecx             ; Push numarul de cuvinte pe stiva
    push    dword format_cuvinte ; Push formatul pentru printf
    call    [printf]        ; Apelam functia printf
    add     esp, 4*2         ; Eliberam parametrii de pe stiva

final:
    ; exit(0)
    push dword 0      ; push parametrul pentru exit pe stiva
    call [exit]       ; apelam exit pentru a termina programul
    
