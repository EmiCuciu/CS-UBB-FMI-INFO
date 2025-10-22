from Lexer import Lexer

if __name__ == "__main__":
    INPUT_FILE = "cod_example.txt"
    lexer = Lexer(INPUT_FILE)
    lexer.tokenize()
    ts_path, fip_path = lexer.save(".")
