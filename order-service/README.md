# Order-service

To create an order you can send POST request:

```bash
curl -X 'POST' \
  'http://localhost:8080/api/order' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
  "description": "string",
  "departureAddress": "string",
  "destinationAddress": "string",
  "cost": 0,
  "amount": 2
}'
```

You can list orders with GET request:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/order' \
  -H 'accept: */*'
```
