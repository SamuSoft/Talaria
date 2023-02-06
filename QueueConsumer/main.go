package main

import (
	"QueueConsumer/handlers"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatal(msg, err)
	}
}

func main() {

	var wait chan struct{}
	// Start up the queue connection and queue
	//queue := handlers.Setup()

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

	queue, err := ch.Consume(
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
	//repository.Setup()

	go func() {
		for message := range queue {
			log.Printf("Received a message: %s", message.Body)
			handlers.HandleMessage(message.Body)
		}
	}()

	<-wait // Waits forever, since this channel will never send anything
}
