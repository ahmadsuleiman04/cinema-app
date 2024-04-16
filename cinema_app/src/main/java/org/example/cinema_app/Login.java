package org.example.cinema_app;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Login implements Initializable {
        @FXML
        private Button signIn;
        @FXML
        private TextField email;
        @FXML
        private PasswordField password;
        @FXML
        private Label errorMessage;
        @FXML
        private ToggleButton login;
        public static User user;
    @FXML
    public void switchToScene1() throws IOException {
        Stage currentStage = (Stage) signIn.getScene().getWindow();
        currentStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 700);
        Stage stage = new Stage();
        stage.setTitle("Vono Cinema");
        stage.setScene(scene);
        stage.setMaxHeight(700);
        stage.setMaxWidth(600);
        stage.show();
    }
    public void initialize(URL url, ResourceBundle resourceBundle){
        signIn.setDisable(true);
        login.setSelected(true);


    }
    @FXML
    public void checkValidity(){
        if (email.getText().trim().endsWith(".com") && email.getText().contains("@")) {
            signIn.setDisable(false);
        }else signIn.setDisable(true);
    }
    @FXML
    public String getCredentials(){
        String credentials = email.getText().trim() + "," + password.getText().trim();
        return credentials;
    }
    @FXML
    public void entry() {
        try{
            Socket socket = new Socket("localhost",4450);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] theCredentials = getCredentials().split(",");
            if(login.isSelected()){
                out.println(getCredentials()+",login");
                String available = in.readLine();

                if(available.equals("unavailable")){
                    errorMessage.setText("account unavailable");
                }else{
                    user = new User(theCredentials[0],theCredentials[1],Integer.parseInt(available));
                    switchToScene1();
                }
            }else{
                out.println(getCredentials()+",signup");
                String available = in.readLine();
                if(available.equals("account taken")){
                    errorMessage.setText("account Taken");
                }
            }
            out.close();
            in.close();
            socket.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
