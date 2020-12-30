/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Maamon
 */
public class Login extends Application {

    private TextField[] morePhones;
    User user = new User();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        //default center components
        Label lUserName = new Label("User Name");
        TextField txtUser = new TextField();
        txtUser.setMaxWidth(200);
        Label lPassword = new Label("Password");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setMaxWidth(200);
        Button btnLogin = new Button("Log in");
        VBox center = new VBox();
        center.getChildren().addAll(lUserName, txtUser, lPassword, txtPassword, btnLogin);

        btnLogin.setOnAction(e -> {
            user.setUsername(txtUser.getText());
            user.setPassword(txtPassword.getText());
            String select = "select username,password,role from users where username=\"" + user.getUsername() + "\"and password=\""
                    + user.getPassword() + "\";";
            System.out.println(select);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/charity", "root", "");

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(select);
                if (rs.next()) {
                    if (rs.getString(1).equals(user.getUsername()) && rs.getString(2).equals(user.getPassword())) {
                        user.setRole(rs.getString(3));
                        System.out.println("sucess");
                        primaryStage.close();
                        Data data = new Data(primaryStage, user);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "InValid Data try again");
                }
                con.close();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "InValid Data try again");
            }
        });

        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                user.setUsername(txtUser.getText());
                user.setPassword(txtPassword.getText());
                String select = "select username,password,role from users where username=\"" + user.getUsername() + "\"and password=\""
                        + user.getPassword() + "\";";
                System.out.println(select);
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost/charity", "root", "");

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(select);
                    if (rs.next()) {
                        if (rs.getString(1).equals(user.getUsername()) && rs.getString(2).equals(user.getPassword())) {
                            user.setRole(rs.getString(3));
                            System.out.println("sucess");
                            primaryStage.close();
                            Data data = new Data(primaryStage, user);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "InValid Data try again");
                    }
                    con.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "InValid Data try again");
                }
            }
        });

//border components
        root.setTop(new Rectangle(400, 50, Paint.valueOf("white")));
        center.setSpacing(3);
        ScrollPane scrollCenter = new ScrollPane(center);
        scrollCenter.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCenter.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCenter.setPadding(new Insets(20));
        root.setCenter(scrollCenter);
        root.setLeft(new Rectangle(100, 300, Paint.valueOf("white")));
        root.setRight(new Rectangle(100, 300, Paint.valueOf("white")));
        root.setBottom(new Rectangle(400, 50, Paint.valueOf("white")));
        root.backgroundProperty().setValue(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.show();
        txtUser.requestFocus();
    }

}
