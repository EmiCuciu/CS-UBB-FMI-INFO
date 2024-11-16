bits 32
global start        

extern exit               
import exit msvcrt.dll    

; Se dau 2 siruri de octeti A si B. Sa se construiasca sirul R care sa contina doar elementele impare si pozitive din cele 2 siruri.
; Exemplu:
; A: 2, 1, 3, -3
; B: 4, 5, -5, 7
; R: 1, 3, 5, 7

section data use32 class=data
    A db 2,1,3,-3
    lenA equ $-A
    B db 4,5,-5,7
    lenB equ $-B
    lenRez equ lenA+lenB
    R times lenRez db 0

section code use32 class=code
start:
    ; Initializăm registrele și variabilele
    mov esi, 0
    mov edi, 0

    ; Procesăm șirul A
    mov ecx, lenA
    repetaA:
        mov al, [A + esi]
        test al, 1
        jz nextA  ; Saltă la nextA dacă al nu este impar
        cmp al, 0
        jle nextA  ; Saltă la nextA dacă al nu este pozitiv
        mov [R + edi], al
        inc edi
    nextA:
        inc esi
        loop repetaA

    ; Procesăm șirul B
    mov ecx, lenB
    mov esi, 0
    repetaB:
        mov al, [B + esi]
        test al, 1
        jz nextB  ; Saltă la nextB dacă al nu este impar
        cmp al, 0
        jle nextB  ; Saltă la nextB dacă al nu este pozitiv
        mov [R + edi], al
        inc edi
    nextB:
        inc esi
        loop repetaB

    ; exit(0)
    push    dword 0      
    call    [exit]