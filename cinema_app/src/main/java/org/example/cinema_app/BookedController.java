package org.example.cinema_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import javax.sql.rowset.CachedRowSet;
import javax.swing.plaf.basic.BasicTableUI;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class BookedController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int pageNumber = 0;
    private CachedRowSet crs;
    @FXML
    private Rectangle bookingRect1,bookingRect2;
    @FXML
    private ImageView image1;
    @FXML
    private StackPane booked1,booked2;
    @FXML
    private Button previous;
    @FXML
    private Label bookingTitle1,bookingTitle2,bookingTiming1,bookingTiming2,bookingSeats1,bookingSeats2;

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,700);
        stage.setScene(scene);
        stage.setMaxHeight(700);
        stage.setMaxWidth(600);
        stage.show();
    }
    public void initialize(URL url,ResourceBundle resources){

        File file1 = new File("src\\main\\resources\\cinema-logo.png");
        Image images1 = new Image(file1.getAbsolutePath());
        image1.setImage(images1);
//        booked2.setVisible(false);
        if (pageNumber == 0){
            previous.setDisable(true);
        }
        getBookings();
        populateBookings();
    }
    public void getBookings(){
        try{
            Socket socket = new Socket("localhost",4450);

            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            out.println("getBookings," + Login.user.id);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            this.crs = (CachedRowSet) in.readObject();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    public void populateBookings(){
        try {
            this.crs.beforeFirst();
            int size = crs.size();
            this.crs.beforeFirst();
            int a = this.pageNumber*2;
            int c = a+2;
            if(c> size){
                c = size;
            }
            if (!(c%2 == 0)){
                this.booked2.setVisible(false);
            }else {
                this.booked2.setVisible(true);
            }
            int x = 0;
            for(int i = a;i<c;i++){
                if (x!=i && x < i ){
                    for(x = 0;x<i;x++){
                        this.crs.next();
                    }
                }else{
                    x++;
                }
                this.crs.next();
                if((i%2 == 0 )){
                    Image image = new Image(new ByteArrayInputStream(this.crs.getBytes("image")));
                    this.bookingRect1.setFill(new ImagePattern(image));
                    this.bookingTitle1.setText(this.crs.getString("title"));
                    this.bookingSeats1.setText("number of seats:    "+this.crs.getString("numseats"));
                    this.bookingTiming1.setText(this.crs.getString("timing"));
                }
                else if (!(i%2 == 0)){
                    Image image = new Image(new ByteArrayInputStream(this.crs.getBytes("image")));
                    this.bookingRect2.setFill(new ImagePattern(image));
                    this.bookingTitle2.setText(this.crs.getString("title"));
                    this.bookingSeats2.setText("number of seats:    "+this.crs.getString("numseats"));
                    this.bookingTiming2.setText(this.crs.getString("timing"));
                }
        }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void nextPage(){
        this.pageNumber ++;
        populateBookings();
        if(pageNumber!=0){
            previous.setDisable(false);
        }
    }
    public void previousPage(){
        this.pageNumber --;
        populateBookings();
        if(pageNumber==0){
            previous.setDisable(true);
        }
    }
}
