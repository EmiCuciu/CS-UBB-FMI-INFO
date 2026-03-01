section .note.GNU-stack noalloc noexec nowrite progbits
section .data
    format_in db "%d", 0
    format_out db "Rezultat: %d", 10, 0

section .bss
    t0 resd 1
    t1 resd 1
    t2 resd 1
    t3 resd 1
    t4 resd 1
    t5 resd 1
    t6 resd 1
    t7 resd 1
    t8 resd 1
    t9 resd 1
    a resd 1
    b resd 1
    c resd 1
    rez resd 1

section .text
    global main
    extern printf, scanf

main:
    push rbp
    mov rbp, rsp
    ; hear >> a
    lea rdi, [rel format_in]
    lea rsi, [rel a]
    xor rax, rax
    call scanf
    ; hear >> b
    lea rdi, [rel format_in]
    lea rsi, [rel b]
    xor rax, rax
    call scanf
    ; hear >> c
    lea rdi, [rel format_in]
    lea rsi, [rel c]
    xor rax, rax
    call scanf
    mov eax, [rel b]
    imul eax, [rel c]
    mov [rel t0], eax
    mov eax, [rel a]
    add eax, [rel t0]
    mov [rel t1], eax
    ; rez <- expr
    mov eax, [rel t1]
    mov [rel rez], eax
    ; speak << expr
    lea rdi, [rel format_out]
    mov esi, [rel rez]
    xor rax, rax
    call printf
    mov rax, 0
    pop rbp
    ret
