bits 32 ; Asamblarea pentru arhitectura pe 32 de biți

; Declaram EntryPoint (o eticheta care defineste prima instructiune a programului)
global start

; Declaram functiile externe necesare programului nostru
extern exit
import exit msvcrt.dll

; Segmentul pentru date (variabilele necesare programului)
section .data
    a db 8
    b db 9
    c dw 2
    e dd 16
    x dq 19

; Segmentul pentru cod
section .text
start:
    ; Calculeaza a + b * c + 2 / c / (2 + a) + e + x

    ; Incarcam valorile din memoria de date
    mov al, [b]
    cbw
    mov dx, [c]
    imul dx
    mov bx, ax
    mov al, [a]
    cbw
    add ax, bx

    mov bx, ax

    mov al, 2
    cbw
    cwde
    mov dx, [c]

    ; Verificam daca c este zero inainte de a efectua impartirea
    cmp dx, 0
    je divide_by_zero_error

    idiv dx

    add bx, ax

    mov al, [a]
    add al, 2
    cbw

    mov dx, ax
    mov ax, bx
    idiv dx

    cwde

    ; Adunam e la rezultat
    mov ebx, [e]
    add eax, ebx

    ; Adunam x la rezultat
    mov ebx, [x]
    add eax, ebx

    ; Apelam functia "exit" pentru a termina programul
    push dword 0
    call [exit]

divide_by_zero_error:
    ; Tratam eroarea de impartire la zero aici
    ; Poti alege sa afisezi un mesaj de eroare si sa iesi din program cu un cod de eroare specific
    ; sau sa iei orice alte masuri necesare in cazul unei astfel de erori
    ; In acest exemplu, programul se va termina cu codul de eroare 1
    push dword 1
    call [exit]
