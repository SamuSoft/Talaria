package repository

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"log"
)

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "postgres"
	dbname   = "talaria"
)

type Message struct {
	Uuid           string `json:"uuid"`
	Sent           int32  `json:"sent"`
	Received       bool   `json:"received"`
	ReceiverMedium string `json:"receiverMedium"`
	SenderMedium   string `json:"senderMedium"`
	Message        string `json:"message"`
}

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

func Setup() *sql.DB {
	// connection string
	psqlconn := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", host, port, user, password, dbname)

	// open database connection
	db, err := sql.Open("postgres", psqlconn)
	failOnError(err, "Failed to open connection to DB")

	// check db connection is live
	err = db.Ping()
	failOnError(err, "Failed to check coon")
	return db
}

func Close(db *sql.DB) {
	// close database, ignore errors
	defer db.Close()
}

func InsertMessage(message Message) {
	var db = Setup()
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
	Close(db)
	logOnError(e, "Failed to insert message into DB")
}
