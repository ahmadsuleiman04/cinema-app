package org.example.cinema_app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookSeats implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView screenBackGround;
    private List<Integer> selectedSeats = new ArrayList<>();
    public void initialize(URL url, ResourceBundle resourceBundle){
        File file = new File("src\\main\\resources\\show room.png");
        Image background = new Image(file.getAbsolutePath());
        screenBackGround.setImage(background);
        Platform.runLater(() -> checkAvailability()); ;
    }
    public void switchToMovieDetails(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("movie-details.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,700);
        stage.setScene(scene);
        stage.show();
    }
    public void checkAvailability(){
        try{
            Socket socket = new Socket("localhost",4450);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("checkAvailability," + Login.user.id +"," + MainController.selectedMovie+","+MovieDetails.selectedShow);
            String[] bookedSeats = in.readLine().split(",");

            for(int i = 0;i<bookedSeats.length;i++){
                String seat = "seat" +  bookedSeats[i];
                try{
                    Button button = (Button) screenBackGround.getScene().lookup("#"+seat);
                    button.setDisable(true);
                }catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }

            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void selectSeats(ActionEvent event){
            Button button =(Button) event.getSource();
            String id = button.getId().replaceAll("seat","");
            if(!selectedSeats.contains(Integer.parseInt(id))){
                System.out.println(id);
                selectedSeats.add(Integer.parseInt(id));
                button.setStyle("-fx-background-color: green;");
            }else{
                selectedSeats.remove(id.toString());
                button.setStyle("");
            }
    }
    public void bookSeats(){
        String message = "bookSeats,"+ Login.user.id+ "," + MainController.selectedMovie + ","+ MovieDetails.selectedShow;
        for(int i = 0; i<selectedSeats.size();i++){
            message += "," + selectedSeats.get(i) ;
        }
        try{
            Socket socket = new Socket("localhost",4450);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            out.println(message);
            String response = in.readLine();
            System.out.println(response);
            checkAvailability();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }


    }
}


