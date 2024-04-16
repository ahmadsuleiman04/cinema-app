package org.example.cinema_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class MovieDetails implements Initializable {
    private Stage stage;
    private   Scene scene;
    public static Parent root;
    @FXML
    private Rectangle detailsRect;
    private int movieNumber = MainController.selectedMovie;
    int movieId ;
    private CachedRowSet crs = MainController.crs;
    private CachedRowSet timingCrs;
    @FXML
    private Label leadActor,imdbRating,title;
    @FXML
    private ChoiceBox date,time;
    public static String selectedShow;


    public void setMovieNumber(int number){
        movieNumber= number;
    }
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,700);
        stage.setScene(scene);
        stage.setMaxHeight(700);
        stage.setMaxWidth(600);
        stage.show();
    }
    public void switchToScene4(ActionEvent event) throws IOException {
        selectedShow = date.getSelectionModel().getSelectedItem().toString()+" " + time.getSelectionModel().getSelectedItem().toString();
        Parent root1 = FXMLLoader.load(getClass().getResource("book-seats.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root1,600,700);
        stage.setScene(scene);
        stage.setMaxHeight(700);
        stage.setMaxWidth(600);
        stage.show();
    }
    @FXML
    public void setDetails() throws SQLException{
        crs.beforeFirst();
        for(int i = 1;i <= movieNumber;i++){
            crs.next();
            if (i == movieNumber){
                Image image = new Image(new ByteArrayInputStream((byte[]) crs.getObject("image")));
                detailsRect.setFill(new ImagePattern(image));
                leadActor.setText(crs.getString("leadactor"));
                imdbRating.setText(crs.getString("rating"));
                movieId = crs.getInt("movieid");
                title.setText(crs.getString("title"));
            }
        }
    }
    public void getShowTiming() throws IOException,ClassNotFoundException{
        Socket socket = new Socket("localhost",4450);
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        out.println(this.movieId +"getTiming");
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        timingCrs =(CachedRowSet) in.readObject();
    }
    @FXML
    public void setShowTiming() throws SQLException{


        while(this.timingCrs.next()){
            Timestamp timestamp = this.timingCrs.getTimestamp("timing");
            Date date1 = new Date(timestamp.getTime());
            if(this.date.getItems().contains(date1.toString()));
            else{
                date.getItems().add(date1.toString());
            }

        }
    }
    public void setTime(){
        try{
            time.getItems().clear();
            time.hide();
            timingCrs.beforeFirst();
            String date = (String)  this.date.getSelectionModel().getSelectedItem();
            System.out.println(date);
            while(timingCrs.next()){
                Timestamp timestamp = timingCrs.getTimestamp("timing");
                Date date1 = new Date(timestamp.getTime());
                if(date1.toString().equals(date)){
                    Time time1 = new Time(timestamp.getTime());
                    time.getItems().add(time1.toString());
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    public void initialize(URL url,ResourceBundle resources){
        try{
            setDetails();
            getShowTiming();
            setShowTiming();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
