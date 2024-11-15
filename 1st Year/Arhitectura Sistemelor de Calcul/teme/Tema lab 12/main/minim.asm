section .text
    global _find_min 

_find_min:
    mov esi, [esp + 4]   ; Adresa primului element al array-ului
    mov eax, [esi]       ; Inițializează min_val cu primul element al array-ului

    mov ecx, 1           ; Setează ecx la 1, deoarece avem deja primul element

repeta:
    add esi, 4           ; Mărește adresa array-ului la următorul element

    cmp ecx, [esi - 4] ; Verifică dacă am ajuns la sfârșitul array-ului
    jge exit_repeta

    mov edx, [esi]       ; Încărcăm următorul element în edx
    cmp edx, eax         ; Comparam cu min_val
    jae no_modify

    mov eax, edx         ; Daca este mai mic, actualizăm min_val

no_modify:
    inc ecx              ; Incrementăm contorul
    jmp repeta

exit_repeta:
    mov eax, eax         ; păstrează rezultatul în eax
    ret
