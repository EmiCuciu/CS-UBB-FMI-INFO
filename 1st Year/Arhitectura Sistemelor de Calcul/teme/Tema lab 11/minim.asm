bits 32         
global find_min

segment data use32 class = data
    min_val dd 0xFFFFFFFF

segment code use32 public code
    find_min:
        mov esi, [esp + 4]
        
        repeta:
            lodsd
            cmp eax, 0
            je exit_repeta
            
            cmp eax, [min_val]
            jae no_modify
            
            mov [min_val], eax
            
            no_modify:
        loop repeta
        
        exit_repeta:
        
        mov eax, [min_val]
        ret