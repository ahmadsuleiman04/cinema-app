# cinema-app
full-stack cinema management application to streamline cinema operations. 
download the database backup named "cinema.sql" and import it using pgadmin4. populate the database movie table with movies data using the follwoing psql method :
INSERT INTO movies values (int movieid,text 'title', text 'lead actor', pg_read_binary_file('location to your file')::bytea,text imdb rating).
change the user credential in the ServiceHandler to the user credentials you make.
