;Problema. Se da un sir de valori numerice intregi reprezentate pe quadworduri.
;Sa se determine suma cifrelor numarului multiplilor de 8 din sirul octetilor 
;inferiori ai cuvintelor superioare ai dublucuvintelor superioare din elementele sirului de quadworduri.


;Solutie: Parcurgand sirul de quadworduri vom obtine intai numarul multiplilor de 8 din 
;sirul octetilor inferiori ai cuvintelor superioare ai dublucuvintelor superioare din elementele sirului. Apoi vom obtine 
;cifrele acestui numar prin impartiri succesive la 10 si vom calcula suma lor.

bits 32 


global start        


extern exit               
import exit msvcrt.dll    
                          

segment data use32 class=data
    ; ...
        sir  dq  123110110abcb0h,1116adcb5a051ad2h,4120ca11d730cbb0h
        len equ ($-sir)/8;lungimea sirului (in quadwords)
        opt db 8;variabila folosita pentru testarea divizibilitatii cu 8
        zece dd 10;variabila folosita pentru determinarea cifrelor unui numar prin impartiri succesive la 10
        suma dd  0;variabila in care retinem suma cifrelor 
        
        
segment code use32 class=code
    start:
        ; ...
        mov esi,sir
        cld
        mov ecx,len
        mov ebx,0
        repeta:
            lodsd
            lodsd
            shr eax,16
            mov ah,0
            
            div byte[opt]
            cmp ah,0
            jnz nonmultiplu
            inc ebx
            
            nonmultiplu:
                loop repeta
        
        mov eax,ebx
        mov edx,0
        transf:
            div dword[zece]
            add dword[suma],edx
            cmp eax,0
            jz final
            
            mov edx,0
            jmp transf
        
        final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
