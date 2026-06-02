import socket
import struct

a ="plm"
v = a.replace
print(v)

def main():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    port = int(input("Introduceti portul: "))
    ip = input("Introduceti ip-ul: ")

    try:
        client_socket.connect((ip, port))
        print("Conectat la server")
    except socket.error as e:
        print(f"Eroare la conectarea la server: {e}")
        return
    
    try:
        a = int(input("Introduceti primul numar: "))
        b = int(input("Introduceti al doilea numar: "))
        
        # Trimitem numerele ca uint16_t (format binar, big-endian)
        client_socket.sendall(struct.pack('!HH', a, b))
        
        # Primim rezultatul tot în format binar
        data = client_socket.recv(1024)
        suma = struct.unpack('!H', data)[0]
        print(f"Suma primită de la server: {suma}")
    except socket.error as e:
        print(f"Eroare la trimiterea/primirea datelor: {e}")
    finally:
        client_socket.close()

if __name__ == "__main__":
    main()
