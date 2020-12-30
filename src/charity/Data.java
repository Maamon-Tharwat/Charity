/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charity;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Maamon
 */
public class Data extends Application {

    User user;

    @Override
    public void start(Stage primaryStage) {
        StackPane root;
        HBox choices;
        VBox content;
        Label lbChoose = new Label("Choose section");
        Button btnUser = new Button("Users");
        Button btnCase = new Button("Case");
        Button btnDonator = new Button("Donator");
        btnUser.setOnAction(e->{
            primaryStage.close();
            UserMangement userMangement = new UserMangement(primaryStage, user);
        });
        
        btnCase.setOnAction(e->{
            primaryStage.close();
            CaseMangement caseMangement = new CaseMangement(primaryStage, user);
        });
        
        btnDonator.setOnAction(e->{
            primaryStage.close();
            DonatorMangement donatorMangement = new DonatorMangement(primaryStage, user);
        });
        
        if (user.getRole().equals("admin")) {
            choices = new HBox(btnUser, btnCase, btnDonator);
        } else {
            choices = new HBox(btnCase, btnDonator);
        }
        choices.setSpacing(20);
        content = new VBox(lbChoose, choices);
        content.setSpacing(20);
        root = new StackPane(content);
        root.setStyle("-fx-padding:130 0 0 130;");
        Scene scence = new Scene(root, 400, 400);
        primaryStage.setScene(scence);
        primaryStage.setTitle("Choice");
        primaryStage.show();;
    }

    public Data(Stage primaryStage, User user) {
        this.user = user;
        System.out.println(this.user.getRole());
        start(primaryStage);
    }

}
