from Lexer import Lexer
import sys

if __name__ == "__main__":

    # INPUT_FILE = "examples/cod_example.txt"
    # INPUT_FILE = "examples/cod_example_fara_spatii.txt"
    # INPUT_FILE = "examples/cod_example_ERORI.txt"
    INPUT_FILE = "examples/af_examples.txt"

    lexer = Lexer(INPUT_FILE)
    lexer.tokenize()

    ts_path, fip_path = lexer.save(".")
