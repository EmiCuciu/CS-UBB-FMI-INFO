; bits 32 ; assembling for the 32 bits architecture

; ; declare the EntryPoint (a label defining the very first instruction of the program)
; global start        

; ; declare external functions needed by our program
; extern exit,printf               ; tell nasm that exit exists even if we won't be defining it
; import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
; import printf msvcrt.dll                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; ; our data is declared here (the variables needed by our program)
; segment data use32 class=data
    ; ; ...
    ; sir db 'a','b','c','m','n' ;declararea sirului initial
    ; lungime equ $-sir ;stabilirea lungimii sirului
    ; destinatie times lungime db 0 ;rezervarea unui spatiu de dimensiune l pentru sirul destinatie si initializarea acestuia
    
    
    ; ;Se da un sir de caractere format din litere mici.
; ;Sa se transforme acest sir in sirul literelor mari corespunzator.

; ; our code starts here
; segment code use32 class=code
    ; start:
        ; ; ...
        ; mov ecx,lungime
        ; mov esi,0
        ; jecxz final
        ; mov bl,'a'-'A'
        ; repeta:
            ; mov al,[sir+esi]
            ; sub al,bl
            ; mov [destinatie+esi],al
            ; inc esi
            ; loop repeta
            
        ; final:
            ; push destinatie
            ; call [printf]
            ; add esp,4*1
            
        ; ; exit(0)
        ; push    dword 0      ; push the parameter for exit onto the stack
        ; call    [exit]       ; call exit to terminate the program

        ; bits 32
; global start        

; extern exit, printf             
; import exit msvcrt.dll    
; import printf msvcrt.dll                          

; section data use32 class=data
    ; S db 1,2,3,4
    ; len equ $-S
    ; D times len-1 db 0
    ; format db "%d",13,10,0

; section code use32 class=code
; start:
    ; mov eax,len
    ; dec eax
    ; mov ecx,eax
    ; jecxz final
    ; mov esi,0
    ; mov edi,0
    ; repeta:
        ; jecxz afisare
        ; mov al,[S+esi]
        ; inc esi
        ; mov bl,[S+esi]
        ; dec esi
        ; mul bl
                
        ; mov [D+edi],al  
        
        ; inc edi
        ; inc esi
        ; loop repeta
            
    ; mov ecx,edi
    ; afisare:
        ; mov ebx,ecx
        ; jecxz final
        ; mov esi,0
        ; mov eax,[D+esi]
        ; inc esi
        ; push eax
        ; push format
        ; call [printf]
        ; add esp,4*2
        ; mov ecx,ebx
        ; loop afisare
    
    ; final:  
        
    ; ; exit(0)
    ; push    dword 0
    ; call    [exit]


bits 32
global start        

extern exit               
import exit msvcrt.dll    

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


