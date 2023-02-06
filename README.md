# Talaria

This is a message service which adds new messages thorough a queue to the database, separating the heaviest part of a message system into a async flow. 

## Setup

To run this software, you need to have GoLang, Java and maven installed, and PostgreSQL and RabbitMQ running

Before starting the software, you'll need to configure the DB with the settings in 'db_init.sql', and start the Java part of the software for it to setup the necessary db table.

To start the portal, move to the portal directory and run:
`mvn spring-boot:run`

To start the queue consumer, move to that directory and build and run it.

## Using

The endpoints will be at `http://localhost:8080/`, and can be used with ex Postman. 

Available endpoints and HTTP methods are: 

`GET messages?user={user}`

`GET messages/{id}`

`DELETE messages/{id}`


