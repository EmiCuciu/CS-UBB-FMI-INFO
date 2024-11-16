import string
import random


def random_string(size):
    return random.choice(string.ascii_uppercase) + ''.join(
        random.choice(string.ascii_lowercase) for _ in range(size - 1))


def random_number(size):
    return ''.join(random.choice(string.digits) for _ in range(size))


def random_string_recursive(size, current_size=0):
    if current_size == size:
        return ''
    else:
        if current_size == 0:
            return random.choice(string.ascii_uppercase) + random_string_recursive(size, current_size + 1)
        else:
            return random.choice(string.ascii_lowercase) + random_string_recursive(size, current_size + 1)


def random_number_recursive(size, current_size=0):
    if current_size == size:
        return ''
    else:
        return random.choice(string.digits) + random_number_recursive(size, current_size + 1)


# Exemplu de folosire:
random_str = random_string_recursive(8)
random_num = random_number_recursive(4)


# print(f"Random String: {random_str}")
# print(f"Random Number: {random_num}")

def gett_id(item):
    return item.get_id()


def gett_genre(item):
    return item.get_genre()


def merge_sort(self, arr, key=None, reverse=False):
    if len(arr) > 1:
        mid = len(arr) // 2
        left_half = arr[:mid]
        right_half = arr[mid:]

        self.merge_sort(left_half, key=key, reverse=reverse)
        self.merge_sort(right_half, key=key, reverse=reverse)

        i = j = k = 0

        while i < len(left_half) and j < len(right_half):
            if reverse:
                if key(left_half[i]) < key(right_half[j]):
                    arr[k] = left_half[i]
                    i += 1
                else:
                    arr[k] = right_half[j]
                    j += 1
            else:
                if key(left_half[i]) > key(right_half[j]):
                    arr[k] = left_half[i]
                    i += 1
                else:
                    arr[k] = right_half[j]
                    j += 1
            k += 1

        while i < len(left_half):
            arr[k] = left_half[i]
            i += 1
            k += 1

        while j < len(right_half):
            arr[k] = right_half[j]
            j += 1
            k += 1


def bingo_sort(self, arr, key=None, reverse=False):
    n = len(arr)
    swapped = True

    while swapped:
        swapped = False
        for i in range(0, n - 1):
            if reverse:
                if key(arr[i]) < key(arr[i + 1]):
                    arr[i], arr[i + 1] = arr[i + 1], arr[i]
                    swapped = True
            else:
                if key(arr[i]) > key(arr[i + 1]):
                    arr[i], arr[i + 1] = arr[i + 1], arr[i]
                    swapped = True


# orodnam alfabetic dupa gen , in caz ca sunt identice se oroneaza invers dupa titlu

def get_reverse_title(item):
    return item.get_title()[::-1]



