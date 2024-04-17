package org.example.cinema_app;
import javafx.event.ActionEvent;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ServiceHandler extends Thread {
    Socket socket;
    String message;

    public ServiceHandler(Socket socket){
        this.socket = socket;
        try{
            message = receiveMessage();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
    public String receiveMessage()throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = in.readLine();
        return message;
    }

    public void loginPage() throws IOException,SQLException {
        String result = "";

        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        String[] request = this.message.split(",");


        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        if (request[2].equals("login")) {
            String sql = "SELECT * FROM users WHERE emailadress = ? AND password = ?; ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,request[0]);
            preparedStatement.setString(2,request[1]);
            ResultSet resultSet = preparedStatement.executeQuery();
            int size = 0;
            while(resultSet.next()){
                size = resultSet.getInt("userid");
            }
            if(size == 0){
                result = "unavailable";
            }else{result = size + "";}
        } else if (request[2].equals("signup")) {
            String check = "Select * FROM users WHERE emailadress = ? AND password = ?;";
            PreparedStatement available = con.prepareStatement(check);
            available.setString(1,request[0]);
            available.setString(2,request[1]);
            ResultSet rs = available.executeQuery();
            int count = 0;
            while(rs.next()){
                count++;
            }
            if(count == 0){
                String sql = "INSERT INTO users (emailadress,password) VALUES(?,?); ";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1,request[0]);
                preparedStatement.setString(2,request[1]);
                int inserted = preparedStatement.executeUpdate();
                if(inserted!=0){
                    result = "account created";
                }else {
                    result = "process failed";
                }
            }else{ result = "account taken" ;}
        }
            out.println(result);
            con.close();
            out.close();

    }
    public void getMovies() throws SQLException,IOException{

        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
        String sql = "SELECT * FROM movies;";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet crs = factory.createCachedRowSet();
        crs.populate(resultSet);
        out.writeObject(crs);
        con.close();
        out.close();


    }
    public void getTiming() throws SQLException,IOException{

        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
        String sql = "SELECT * FROM shows WHERE movieid =?;";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1,Integer.parseInt(message.substring(0,1)));
        ResultSet resultSet = statement.executeQuery();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet crs = factory.createCachedRowSet();
        crs.populate(resultSet);
        out.writeObject(crs);
        out.close();
        con.close();
    }

    public void checkAvailability() throws SQLException,IOException{
        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(),true);
        String[] text = message.split(",");
        String sql = "SELECT seatnumber FROM bookings WHERE userid = ? AND movieid = ? AND timing = ? ";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1,Integer.parseInt(text[1]));
        statement.setInt(2,Integer.parseInt(text[2]));
        statement.setTimestamp(3,Timestamp.valueOf(text[3]));
        ResultSet rs = statement.executeQuery();
        String bookedSeats = "";
        while (rs.next()){
            bookedSeats += rs.getString("seatnumber") + ",";
        }
        out.println(bookedSeats);
    }

    public void bookSeats() throws SQLException,IOException{
        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(),true);
        String sql = "INSERT INTO bookings (userid,movieid,seatnumber,timing) VALUES (?,?,?,?);";
        PreparedStatement statement = con.prepareStatement(sql);
        String[] details = message.split(",");
        for(int i = 4;i<details.length;i++){
            statement.setInt(1,Integer.parseInt(details[1]));
            statement.setInt(2,Integer.parseInt(details[2]));
            statement.setTimestamp(4,Timestamp.valueOf(details[3]));
            statement.setInt(3,Integer.parseInt(details[i]));
            statement.executeUpdate();
        }
        out.println("database updated");
        statement.close();
        out.close();
        con.close();
    }
    public void getBookings() throws  SQLException,IOException{
        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cinema","cinemauser","password");
        ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
        String sql = "WITH booked_cte AS (\n" +
                "SELECT DISTINCT bookings.movieid, bookings.timing, count(bookings.seatnumber) AS numseats\n" +
                "FROM bookings\n" +
                "WHERE bookings.userid = ?\n" +
                "GROUP BY bookings.movieid, bookings.timing\n)" +
                "SELECT booked_cte.movieid, booked_cte.numseats ,movies.image, movies.image, movies.title,booked_cte.timing\n" +
                "FROM booked_cte\n" +
                "INNER JOIN movies ON movies.movieid = booked_cte.movieid;";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1,Integer.parseInt(message.split(",")[1]));
        ResultSet rs = statement.executeQuery();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet crs = factory.createCachedRowSet();
        crs.populate(rs);
        out.writeObject(crs);
        rs.close();
        statement.close();
        out.close();
        con.close();
    }

    public void run(){
        try{

            if(message.endsWith("login") || message.endsWith("signup"))
                this.loginPage();
            else if (message.endsWith("get movies"))
                this.getMovies();
            else if (message.endsWith("getTiming"))
                this.getTiming();
            else if (message.contains("checkAvailability")) checkAvailability();
            else if (message.contains("bookSeats")) bookSeats();
            else if (message.contains("getBookings")) getBookings();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try{
            this.socket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
