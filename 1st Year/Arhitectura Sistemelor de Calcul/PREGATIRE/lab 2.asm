bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll                          

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a db 2
    b db 6
    c db 4
    e dw 5 
    f dw 1
    g dw 3
    h dw 8
    format db "Rezultatul este: %d",0
    
    ;abcd byte   efgh word
    ;(e + g) * 2 / (a * c) + (h – f) + b * 3
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ax,[e]
        add ax,word[g]
        mov dl,2
        mul dl
        
        push ax
        
        mov al,[a]
        mul byte[c]
        push ax
        mov ax,[esp+2]
        mov bl,[esp]
        
        div bl
        
        push ax
        
        mov cx,[h]
        sub cx,word [f]
        
        mov al,[b]
        mov ah,3
        mul ah
        
        add cx,ax
        
        mov bx,[esp]
        
        add bx,cx
       
        mov dx,0
        mov ax,bx
        
        push dx
        push ax
        pop eax
        
        push eax
        push format
        call [printf]
        add esp,4*2
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
