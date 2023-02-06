package repository

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"log"
	"time"
)

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "postgres"
	dbname   = "talaria"
)

type Message struct {
	Uuid           string
	Sent           time.Time
	Received       bool
	ReceiverMedium string
	SenderMedium   string
	Message        string
}

var db *sql.DB

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatal(msg, err)
	}
}

func logOnError(err error, msg string) {
	if err != nil {
		log.Print(msg, err)
	}
}

func Setup() {
	// connection string
	psqlconn := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", host, port, user, password, dbname)

	// open database connection
	db, err := sql.Open("postgres", psqlconn)
	failOnError(err, "Failed to open connection to DB")

	// check db connection is live
	err = db.Ping()
	failOnError(err, "Failed to check coon")
}

func Close() {
	// close database, ignore errors
	defer db.Close()
}

func InsertMessage(message Message) {
	insertStmt := `insert into "message"("uuid", 
                      "message", 
                      "received", 
                      "receiver_medium", 
                      "sender_medium", 
                      "sent") values($1, $2, $3, $4, $5, $6)`
	_, e := db.Exec(insertStmt,
		message.Uuid,
		message.Message,
		message.Received,
		message.ReceiverMedium,
		message.SenderMedium,
		message.Sent)
	logOnError(e, "Failed to insert message into DB")
}
