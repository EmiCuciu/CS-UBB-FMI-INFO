bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,scanf,printf  ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import scanf msvcrt.dll   ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll

; Sa se citeasca de la tastatura un numar in baza 10 si un numar in baza 16. Sa se afiseze in baza 10 numarul de biti 1 ai sumei celor ;doua numere citite. Exemplu:
; a = 32 = 0010 0000b
; b = 1Ah = 0001 1010b
; 32 + 1Ah = 0011 1010b
; Se va afisa pe ecran valoarea 4.

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a dd 0
    b dd 0
    cont dd 0
    format_dec dd "%d",0
    format_hex dd "%x",0

; our code starts here
segment code use32 class=code
    start:
        
        ;scanf("%d",&a)
        
        push dword a
        push dword format_dec
        call [scanf]
        add ESP,4*2
        
        ;scanf("%x",&b)
        
        push dword b
        push dword format_hex
        call [scanf]
        add ESP,4*2
        
        mov EAX,[a]
        add EAX,[b]
        
        mov ECX,0 ;in ECX se numara bitii de 1
        ;numaram biti de 1 ai rezultatului 
        cnt:
            mov dword[a],1  ;Se încarcă valoarea 1 în variabila a ca un dword
            and [a],EAX     ;Păstrează doar biții care sunt setați la 1 în EAX
            add ECX,[a]     ;ECX acumulează numărul total de biți setați la 1 în registrul EAX
            shr EAX,1       ;Este utilizată pentru a trece la următorul bit în cadrul ciclului
        jnz cnt
        
        mov [cont],ECX ;punem in cont ce vrem sa afisam
        ;printf("%d",cont)
        
        push dword [cont]
        push dword format_dec
        call [printf]
        add ESP,4*2
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program

