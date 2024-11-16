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
	a dw 1100101001011011b ;1100 1010 0101 1011
	b dd 00000000000000000000000000000000b

	
	
	
;	Se da un cuvant A. Sa se obtina dublucuvantul B astfel:
;	bitii 0-3 ai lui B sunt 0;
;	bitii 4-7 ai lui B sunt bitii 8-11 ai lui A
;	bitii 8-9 si 10-11 ai lui B sunt bitii 0-1 inversati ca valoare ai lui A (deci de 2 ori) ;
;	bitii 12-15 ai lui B sunt biti de 1
;	bitii 16-31 ai lui B sunt identici cu bitii 0-15 ai lui B.
	
	
	
	
	; our code starts here
segment code use32 class=code
    start:
        ; ...
		mov bl,00000000b
		
		mov ax,[a] ;ax=1100 1010 0101 1011
		
		ror ax,8  ;ax=1011 0101 1100 1010
		
		mov bh,al ;bx=1100 1010 0000 0000
		
		ror bx,4  ;bx=0000 1100 1010 0000
		
		;biti 0-1 ai lui a sunt 11
		
		mov ax,[a]
		
		not al ; al=1010 0100
		
		mov cl,al
		
		mov ch,00000000b ; cx=0000 0000 1010 0100
		
		rol cx,6 ;cx=0010 1001 0000 0000
		
		not al ;al=0101 1011
		
		mov ch,al; cx=0101 1011 0000 0000
		
		rol cx,2; cx=0110 1100 0000 0001
		
		mov bh,ch ; bx=0110 1100 1010 0000
		
		mov ch,00000000b
		
		mov cl,bh ;cx=0000 0000 0110 1100
		
		rol cx,4 ; cx=0000 0110 1100 0000
		
		mov ch,11111111b; cx=1111 1111 1100 0000
		
		rol cx,4; cx=1111 1100 0000 1111
		
		mov bh,ch; bx=1111 1100 1010 0000
		
		;bx=1111 1100 1010 0000
		
		mov dx,0000000000000000b
		mov ax,bx
		push ax
		push dx
		pop eax; eax=0000 0000 0000 0000 1111 1100 1010 0000
		
		rol eax,16; eax=1111 1100 1010 0000 0000 0000 0000 0000
		
		mov ax,bx; eax=1111 1100 1010 0000 1111 1100 1010 0000
		
		mov [b],eax
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
