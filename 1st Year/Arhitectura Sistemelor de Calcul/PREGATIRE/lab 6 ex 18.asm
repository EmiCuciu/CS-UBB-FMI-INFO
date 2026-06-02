bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; Se da un sir de dublucuvinte. Sa se ordoneze crescator sirul cuvintelor superioare ale acestor dublucuvinte.          Cuvintele inferioare raman neschimbate.
; Exemplu:
; dandu-se:
; sir DD 12AB 5678h, 1256 ABCDh, 1234 4344h 

; rezultatul va fi
; 12345678h, 1256ABCDh, 12AB4344h.                          
                          
                          
; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    sir DD 12AB5678h, 1256ABCDh, 12344344h 
    l equ ($-sir)/4 ;lungimea sirului sir
    sir_high times l dw 0 ; acest sir va contine partea high a dw din sir
    sir_low times l dw 0 ;  acest sir va contine partea low a dw din sir
    rez times l dd 0    
    cont db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        cld ;sirul va fi parcurs de la stanga la dreapta 
        mov ECX,l ; punem in registrul contor, lungimea sirului 
        mov ESI,sir ;punem in ESI adresa de inceput a sirului
        mov EDX,0   ;EDX va fi registru index pentru sir_high si sir_low
        jecxz sfarsit ; prevenim intrarea intr-un ciclu infinit
        st_loop:
            lodsd  ;aduncem in EAX valoarea de la [sir:ESI]
            mov BX,AX ;aducem in BX partea low a dublucuvantului 
            and EAX,0FFFF0000h ;izolam cuvantul high din EAX
            shr EAX,16 ;aducem dublucuvantul high in locul dublucuvantului low
            ;acum avem BX si in AX cele doua componente ale numarului cu care vom opera
            mov [sir_high+EDX],AX  ;punem partea high, stocata in AX in sir_high
            mov [sir_low+EDX] ,BX  ;punem partea low, stocata in BX in sir_low
            add EDX,2              ;incrementam cu 2 indexul sirurilor 
        loop st_loop
        sfarsit:
        ;vom proiecta un algoritm de sortare pentru sir_high
        mov ECX,l ;punem in registrul contor, lungimea sirului
        mov ESI,0 ;punem in ESI 0, pentru a parcurge sirul cu el
        bg_loop:
            mov EDI,0          ;initializam EDI cu 0, folosindul ca index 
            mov byte[cont],CL  
            dec byte[cont]     
            stra:
            add EDI,2          ;il aducem prin aceasta structura pe EDI la valoarea lui SI
            cmp EDI,ESI
            jbe stra
            slp:
                mov AX,[sir_high+ESI]       ;preluam in AX elementul din sirul high 
                cmp AX,word[sir_high+EDI]   ;comparam cu elementul de pe pozitia j 
                jb mic
                mov BX,[sir_high+EDI]       ;
                mov [sir_high+ESI],BX       ;realizam swap-ul 
                mov [sir_high+EDI],AX       ;
                mic: 
                add EDI,2                   ;din moment ce am ales sa parcugem manual 
                dec byte[cont]              ;decrementam contorul 
                cmp byte[cont],0            ;implementam conditia de oprire a loop-ului
            jge slp
            add ESI,2                       ;incrementam manual si valoarea lui ESI
        loop bg_loop 
        ;reconstituim sirul 
        mov EDI,rez ; punem in EDI, adresa rezultatului, astfel incat sa il construim automat
        mov ECX,l  ;punem in ECX lungimea sirului, pentru a realiza loop ul
        mov ESI,0  ;punem in ESI,0 deoarece vom parcuge manual acest sir
        
        lp_st:
        
            mov EAX,0
            mov AX,[sir_high+ESI]  ;aducem in AX(word ul low al EAX) partea high a sirului 
            
            shl EAX,16             ;shiftam spre stanga cu 16 pozitii in asa fel incat ducem partea high pe pozitie
            mov AX,[sir_low+ESI]   ;adaugam partea low 
            
            stosd                  ;stocam partea low in [rez:EDI]
            
            add ESI,2              ;incrementam manual indexul
        
        loop lp_st
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
