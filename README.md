![build and test maven](https://github.com/bifrurcated/wallet/actions/workflows/build_and_test_maven.yml/badge.svg)
# REST application
Сервис для управления кошельком
## Usage
Запрос на изменения суммы денег в кошельке \
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

Вернуть баланс кошелька по UUID
```
GET api/v1/wallets/{WALLET_UUID}
```
