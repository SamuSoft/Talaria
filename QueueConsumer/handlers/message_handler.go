package handlers

import (
	"QueueConsumer/repository"
	"encoding/json"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
)

func Setup() <-chan amqp.Delivery {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to queue")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare(
		"Talaria", // name
		false,     // durable
		false,     // delete when unused
		false,     // exclusive
		false,     // no-wait
		nil,       // arguments
	)
	failOnError(err, "Failed to declare a queue")

	msgs, err := ch.Consume(
		q.Name, // queue
		"",     // consumer
		true,   // auto-ack
		false,  // exclusive
		false,  // no-local
		false,  // no-wait
		nil,    // args
	)
	failOnError(err, "Failed to register a consumer")

	// Start up the database connection
	repository.Setup()

	return msgs
}

func logOnError(err error, msg string) {
	if err != nil {
		log.Print(msg, err)
	}
}

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatal(msg, err)
	}
}

func HandleMessage(receivedMessage []byte) {
	var message repository.Message
	err := json.Unmarshal(receivedMessage, &message)
	logOnError(err, "Unable to parse message")
	if err != nil {
		return // If the unmarshalling fails, we want the goroutine to fail over as well, since the message was bad
	}
	insertMessage(message)
}

func insertMessage(message repository.Message) {
	repository.InsertMessage(message)
}

func deleteMessage(message repository.Message) {

}
