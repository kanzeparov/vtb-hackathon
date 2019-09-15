import requests
ADDRESS = "http://localhost:5000"
URL = "/api/patent"
r = requests.post(ADDRESS+URL, json={"key": "value"})
print(r.json())