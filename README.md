![build and test maven](https://github.com/bifrurcated/wallet/actions/workflows/CI.yml/badge.svg)
# REST application
Сервис для управления кошельком
## Usage
### Запрос на изменения суммы денег в кошельке
DEPOSIT - положить сумму денег \
WITHDRAW - снять сумму денег
```
POST api/v1/wallet
{
walletId: UUID,
operationType: DEPOSIT or WITHDRAW,
amount: 1000
}
```
Example:
```
POST api/v1/wallet
{
  "walletId": "1307c35d-31d6-41e4-a3df-6c240a22a067",
  "operationType": "DEPOSIT",
  amount: 1000
}
```
Return:
```
{
    "id": "1307c35d-31d6-41e4-a3df-6c240a22a067",
    "amount": 9000.0
}
```

### Вернуть баланс кошелька по UUID
```
GET api/v1/wallets/{WALLET_UUID}
```
Example:
```
GET api/v1/wallets/1307c35d-31d6-41e4-a3df-6c240a22a067
```
Return:
```
{
    "amount": 9000.0
}
```
