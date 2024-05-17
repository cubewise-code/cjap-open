import keyring
import sys
        
if __name__ == "__main__":
    if len(sys.argv) < 2:
        quit()
    try:
        user = sys.argv[1]
        network = 'test'
        print(keyring.get_password(network, user))
    except Exception as e:
        print('')