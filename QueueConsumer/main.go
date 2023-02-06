package main

import (
	"QueueConsumer/handlers"
	"log"
)

func main() {

	var wait chan struct{}
	// Start up the queue connection and queue
	queue := handlers.Setup()
	go func() {
		for message := range queue {
			log.Printf("Received a message: %s", message.Body)
			handlers.HandleMessage(message.Body)
		}
	}()

	<-wait // Waits forever, since this channel will never send anything
}
