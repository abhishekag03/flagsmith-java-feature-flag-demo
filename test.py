import requests
import time


data = {
    'id': '4',
    'title': 'Test book',
    'author': 'Betty Carter',
    'price': 49.99,
}

start = time.time()
for i in range(50):
    x = requests.post('http://localhost:8000/books', json=data)

end = time.time()
print("Total time taken: ", end - start)