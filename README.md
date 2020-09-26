## Send database table (rows filtered by criteria) to Kafka

Every second by the scheduler in one transaction
1. Lock rows in database table by criteria (to concurrency with another instance of microservices)
1. Select rows
1. Convert rows to messages
1. Send messages to Kafka
1. Update selected rows in database table
1. Unlock rows

Used [Simple Moving Average (SMA)](https://www.investopedia.com/terms/s/sma.asp)
for adjust the amount of rows selected from database 

Core in `GetTradeRsProcessor.java`

***

* Execute `postgresql.sql` instance to create database table
* Run `DataGenerator.java` to fill table by test data before execution
* Build `mvn clean install`
* Run 1st instance `./run.sh`
* Run 2nd instance `./run2.sh`
* To monitoring process, execute
```postgresql
select count(*) from sample.trade t where t.sent = true;
```

Test data like this:
```postgresql
INSERT INTO sample.trade (id, client_id, exchange, ticker, price, amount, date, status, sent) 
VALUES (13784, 29321, 'NASDAQ', 'AAPL', 246.31, 28, '2020-09-25 17:22:28.931+00', 'COMPLETED', false);
INSERT INTO sample.trade (id, client_id, exchange, ticker, price, amount, date, status, sent) 
VALUES (13785, 43001, 'NYSE', 'DIS', 226.14, 2, '2020-09-25 17:22:28.931+00', 'CANCELLED', false);
INSERT INTO sample.trade (id, client_id, exchange, ticker, price, amount, date, status, sent) 
VALUES (13786, 14295, 'NASDAQ', 'MSFT', 290.8, 97, '2020-09-25 17:22:28.931+00', 'CANCELLED', false);
```

- http://localhost:8081/actuator/health
- http://localhost:8081/actuator/info

***

1. Run Zookeeper `bin/zookeeper-server-start.sh config/zookeeper.properties`
1. Run Kafka `bin/kafka-server-start.sh config/server.properties`
1. See Topics `bin/kafka-topics.sh --list --bootstrap-server localhost:9092`
