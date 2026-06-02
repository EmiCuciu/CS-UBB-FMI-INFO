bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
        a dw 34
        b dw 8
        c dw 9
        d dw 10 
           
;(a-b-c)+(a-c-d-d)
   
; our code starts here
segment code use32 class=code
    start:
        ; ...
    MOV EAX,[a] ;EAX=34 =22h
    SUB EAX,[b] ;EAX=34-8=26 =1Ah
    SUB EAX,[c] ;EAX=26-9=17 =11h
    MOV EBX,[a] ;EBX=34 =22h
    SUB EBX,[c] ;EBX=34-9=25 =19h
    SUB EBX,[d] ;EBX=25-10=15 =Fh
    SUB EBX,[d] ;EBX=15-10-5 =5h
    ADD EAX,EBX ;EAX=17+5=22 =16h
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
