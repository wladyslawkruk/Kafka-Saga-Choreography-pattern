# Inventory-service


To top up the stock with a new (or existing) item you can send POST request:

```bash
curl -X 'POST' \
  'http://localhost:8085/topupstock' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
  "itemName": "beer",
  "price": 50,
  "amount": 20
}'
```


