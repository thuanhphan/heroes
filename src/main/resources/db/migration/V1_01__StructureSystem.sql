DROP TABLE IF EXISTS PUBLIC."user";
CREATE TABLE PUBLIC."user"
(
	"id"				SERIAL PRIMARY KEY,
	"user_name"			VARCHAR(64),
	"email"				VARCHAR(120),
	"password"			VARCHAR(30),
	"first_name"		VARCHAR(64),
	"last_name"			VARCHAR(64)
);

