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
    a db 9
    c db 12
    d db 8
    f dw 7

;f+(c-2)*(3+a)/(d-4)= 7+10*12/4=7+120/4=7+30=37
    
; our code starts here
segment code use32 class=code
    start:
        ; ...       
        mov ah,0
        mov al,[a]
        add al,3
        
        mov cx,[c]
        mov ch,0
        sub cx,2
        mul cx
        
        mov dl,[d]
        sub dl,4
        
        div dl 
        
        add ax,[f]
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
