package org.example.cinema_app;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javax.sql.rowset.CachedRowSet;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private int pageNumber = 0;

    @FXML
    private Button logOut,previousPage,nextPage;

    @FXML
    private Label label1,label2,label3,label4;
    @FXML
    private ImageView image1;
    @FXML
    private Rectangle rect1,rect2,rect3,rect4;
    public static CachedRowSet crs;
    public static int selectedMovie;
    @FXML
    private StackPane firstStackPane, secondStackPane, thirdStackPane, fourthStackPane;

    public void switchToScene2(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("booked.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,700);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToScene3(ActionEvent event) throws IOException{
        Button button =(Button) event.getSource();
        String id = button.getId();
        selectedMovie =Integer.parseInt(id.substring(id.length()-1,id.length()));
        root = FXMLLoader.load(getClass().getResource("movie-details.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,700);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLogin() throws IOException{
        Stage currentStage = (Stage) logOut.getScene().getWindow();
        currentStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(CinemaApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 250);
        Stage stage = new Stage();
        stage.setTitle("Vono Cinema");
        stage.setScene(scene);
        stage.setMaxHeight(250);
        stage.setMaxWidth(300);
        stage.show();
    }

    public void initialize (URL url, ResourceBundle resources){

        File file1 = new File("src\\main\\resources\\cinema-logo.png");
        Image images1 = new Image(file1.getAbsolutePath());
        image1.setImage(images1);
        previousPage.setDisable(true);
        try{
            getMovieDetails();
            populateMovie();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }


    }
    public void populateMovie() throws SQLException,IOException{



//       /* this.crs.beforeFirst();*/
        for(int i = 0;i<4;i++){

            this.crs.next();
            Image image = new Image(new ByteArrayInputStream((byte[]) this.crs.getObject("image")));
            String title = crs.getString("title");
            switch(i){
                case 0: rect1.setFill(new ImagePattern(image));
                label1.setText(title);
                case 1: rect2.setFill(new ImagePattern(image));
                label2.setText(title);
                case 2: rect3.setFill(new ImagePattern(image));
                label3.setText(title);
                case 3: rect4.setFill(new ImagePattern(image));
                label4.setText(title);
                }
        }
    }
    public void setNextPage(){
        try{
            this.pageNumber++;
            populateMovie();
            previousPage.setDisable(false);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void setPreviousPage(){
        try{
            this.pageNumber--;
            this.crs.beforeFirst();
            for(int i = 0; i<pageNumber*4;i++){
                this.crs.next();
            }
            populateMovie();
            if(pageNumber == 0){
                this.previousPage.setDisable(true);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
    public void getMovieDetails() throws SQLException, IOException,ClassNotFoundException {
        Socket socket = new Socket("localhost",4450);
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        out.println("get movies");
        ObjectInputStream in = new ObjectInputStream( socket.getInputStream());
        this.crs = (CachedRowSet) in.readObject();
        out.close();
        in.close();
        socket.close();
    }

}