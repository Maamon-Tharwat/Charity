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
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 *
 * @author Maamon
 */
public class UserMangement extends Application {

    User user;
    private TextField morePhones[];

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        HBox top = new HBox();
        VBox center = new VBox();
        RadioButton rInsert = new RadioButton("Insert");
        RadioButton rModify = new RadioButton("Modify");
        RadioButton rShowAll = new RadioButton("Show All");
        ToggleGroup grpSelection = new ToggleGroup();
        rInsert.setToggleGroup(grpSelection);
        rModify.setToggleGroup(grpSelection);
        rShowAll.setToggleGroup(grpSelection);
        rInsert.selectedProperty().setValue(Boolean.TRUE);
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(rInsert, rShowAll, rModify);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Label lUserName = new Label("User Name");
        TextField txtUser = new TextField();
        txtUser.setMaxWidth(200);
        Label lPassword = new Label("Password");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setMaxWidth(200);
        Label lNationalID = new Label("Natoional ID");
        TextField txtNationalID = new TextField();
        Label lName = new Label("Name");
        TextField txtName = new TextField();
        Label lRole = new Label("Role");
        RadioButton admin = new RadioButton("admin");
        RadioButton volunteer = new RadioButton("volunteer");
        ToggleGroup adVol = new ToggleGroup();
        admin.setToggleGroup(adVol);
        volunteer.setToggleGroup(adVol);
        HBox rolePane = new HBox();
        rolePane.getChildren().addAll(admin, volunteer);
        Label lAddress = new Label("Address");
        TextField txtAddress = new TextField();
        Label lPhone = new Label("Phone");
        TextField txtPhone = new TextField();
        Button btnAddPhone = new Button("Add Phone");
        Button btnSigup = new Button("Add");
        center.getChildren().addAll(lNationalID, txtNationalID, lUserName, txtUser, lPassword, txtPassword, lName, txtName, lRole, rolePane,
                lAddress, txtAddress, lPhone, txtPhone, btnAddPhone, btnSigup);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnAddPhone.setOnAction(add -> {
            int numberOfPhones = Integer.parseInt(JOptionPane.showInputDialog("Enter number of phones to be added"));
            morePhones = new TextField[numberOfPhones];
            center.getChildren().removeAll(center.getChildren());
            center.getChildren().addAll(lNationalID, txtNationalID, lUserName, txtUser, lPassword, txtPassword, lName, txtName, lRole, rolePane,
                    lAddress, txtAddress, lPhone, txtPhone);
            for (int x = 0; x < morePhones.length; ++x) {
                morePhones[x] = new TextField();
                center.getChildren().add(morePhones[x]);
            }
            center.getChildren().addAll(btnAddPhone, btnSigup);
        });

        btnSigup.setOnAction(excute -> {
            try {
                user.setNationalID(Integer.parseInt(txtNationalID.getText()));
                user.setUsername(txtUser.getText());
                user.setPassword(txtPassword.getText());
                user.setName(txtName.getText());
                if (admin.isSelected()) {
                    user.setRole(admin.getText());
                } else {
                    user.setRole(volunteer.getText());
                }
                user.setAddress(txtAddress.getText());
                String phoneString = txtPhone.getText();
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/charity", "root", "");

                Statement stmt = con.createStatement();
                String insert = "insert into users VALUES(" + user.getNationalID() + ",\"" + user.getUsername() + "\",\""
                        + user.getPassword() + "\",\"" + user.getName() + "\",\""
                        + user.getAddress() + "\",\"" + user.getRole() + "\");";
                int rs = stmt.executeUpdate(insert);
                System.out.println("sucess");

                if (!txtPhone.getText().equals("")) {

                    if (morePhones != null) {
                        String strMorePhones[] = new String[morePhones.length + 1];
                        strMorePhones[0] = phoneString;
                        for (int x = 1; x < morePhones.length + 1; ++x) {
                            strMorePhones[x] = morePhones[x - 1].getText();
                            insert = "insert into userphone VALUES(" + user.getNationalID() + ",\"" + strMorePhones[x] + "\");";
                            rs = stmt.executeUpdate(insert);
                            System.out.println("sucess");
                        }
                        user.SetPhones(strMorePhones);
                    }
                    insert = "insert into userphone VALUES(" + user.getNationalID() + ",\"" + phoneString + "\");";
                    rs = stmt.executeUpdate(insert);
                    System.out.println("sucess");
                }
                con.close();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "InValid Data try again");
            }
        });

        rInsert.setOnAction(e -> {
            center.getChildren().removeAll(center.getChildren());
            //center components for sign up
            center.getChildren().addAll(lNationalID, txtNationalID, lUserName, txtUser, lPassword, txtPassword, lName, txtName, lRole, rolePane,
                    lAddress, txtAddress, lPhone, txtPhone, btnAddPhone, btnSigup);
            primaryStage.setTitle("User-Add");

        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        rShowAll.setOnAction(e -> {
            try {
                primaryStage.setTitle("User-Show All");
                center.getChildren().removeAll(center.getChildren());
                TableView<User> userData = new TableView<>();
                TableColumn colNationalID = new TableColumn("National ID");
                colNationalID.setCellValueFactory(new PropertyValueFactory<>("nationalID"));
                TableColumn colUsername = new TableColumn("Username");
                colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
                TableColumn colName = new TableColumn("Name");
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                TableColumn colAddress = new TableColumn("Address");
                colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                TableColumn colRole = new TableColumn("Role");
                colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
                TableColumn[] colPhone;

                String select = "select count(up.phone) from users u left join userphone up on u.nationalid=up.nationalid group by u.nationalid;";

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/charity", "root", "");

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(select);

                rs.next();
                int phoneSize = rs.getInt(1);
                int rows = 1;
                while (rs.next()) {
                    ++rows;
                    phoneSize = Integer.max(phoneSize, rs.getInt(1));
                }
                rs = stmt.executeQuery(select);
                int[] phonesSizes = new int[rows];
                rows = 0;
                while (rs.next()) {
                    phonesSizes[rows++] = rs.getInt(1);
                }
                colPhone = new TableColumn[phoneSize];

                select = "select u.nationalid,u.username,u.name,u.address,u.role,up.phone from users u left join userphone up on u.nationalid=up.nationalid;";
                rs = stmt.executeQuery(select);
                String phones[];
                User users[] = new User[phonesSizes.length];
                for (int x = 0; x < phonesSizes.length; ++x) {
                    phones = new String[phonesSizes[x]];
                    users[x] = new User();
                    rs.next();
                    users[x].setNationalID(rs.getInt(1));
                    users[x].setUsername(rs.getString(2));
                    users[x].setName(rs.getString(3));
                    users[x].setAddress(rs.getString(4));
                    users[x].setRole(rs.getString(5));
                    if (phonesSizes[x] > 0) {
                        phones[0] = rs.getString(6);
                        for (int y = 1; y < phonesSizes[x]; ++y) {
                            rs.next();
                            phones[y] = rs.getString(6);
                        }
                        users[x].SetPhones(phones);
                    }

                }
                con.close();
                ObservableList<User> items = FXCollections.observableArrayList(users);
                userData.setItems(items);
                userData.getColumns().addAll(colNationalID, colUsername, colName, colAddress, colRole);
                for (int x = 0; x < phoneSize; ++x) {
                    colPhone[x] = new TableColumn("Phone" + (x + 1));
                    colPhone[x].setCellValueFactory(createArrayValueFactory(User::getPhones, x));
                    userData.getColumns().add(colPhone[x]);
                }
                center.getChildren().add(userData);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(UserMangement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        rModify.setOnAction(e -> {
            primaryStage.setTitle("User-Modify");
            center.getChildren().removeAll(center.getChildren());
            RadioButton rUpdate = new RadioButton("Update");
            RadioButton rDelete = new RadioButton("Delete");
            ToggleGroup upDel = new ToggleGroup();
            rUpdate.setToggleGroup(upDel);
            rDelete.setToggleGroup(upDel);
            HBox upDelPan = new HBox();
            upDelPan.getChildren().addAll(rUpdate, rDelete);

            Button btnUpdate = new Button("Update");
            Label upLNationalID = new Label("National ID");
            TextField upTxtNationalID = new TextField();
            VBox updatePane = new VBox();
            updatePane.getChildren().addAll(upLNationalID, upTxtNationalID, lPassword, txtPassword, lRole, rolePane, btnUpdate);

            Button btnDelete = new Button("Delete");
            Label delLNationalID = new Label("National ID");
            TextField delTxtNationalID = new TextField();
            VBox DeletePane = new VBox();
            DeletePane.getChildren().addAll(delLNationalID, delTxtNationalID, btnDelete);

            rUpdate.selectedProperty().set(true);
            updatePane.disableProperty().set(true);
            DeletePane.disableProperty().set(false);

            rUpdate.setOnAction(acUp -> {
                updatePane.disableProperty().set(false);
                DeletePane.disableProperty().set(true);
            });

            rDelete.setOnAction(acDel -> {
                updatePane.disableProperty().set(true);
                DeletePane.disableProperty().set(false);
            });

            btnUpdate.setOnAction(up -> {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost/charity", "root", "");
                    Statement stmt = con.createStatement();
                    String update;
                    if (!txtPassword.getText().equals("")) {
                        update = "update users set password =\"" + txtPassword.getText() + "\" where nationalid = " + upTxtNationalID.getText() + ";";
                        System.out.println(update);
                        int rs = stmt.executeUpdate(update);
                        System.out.println("sucess");
                    }
                    if (admin.isSelected()) {
                        update = "update users set role =\"" + admin.getText() + "\" where nationalid = " + upTxtNationalID.getText() + ";";
                        System.out.println(update);
                        int rs = stmt.executeUpdate(update);
                        System.out.println("sucess");
                    } else {
                        update = "update users set role =\"" + volunteer.getText() + "\" where nationalid = " + upTxtNationalID.getText() + ";";
                        System.out.println(update);
                        int rs = stmt.executeUpdate(update);
                        System.out.println("sucess");
                    }
                    con.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "InValid Data try again");
                }
                
            });

            btnDelete.setOnAction(del -> {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost/charity", "root", "");
                    Statement stmt = con.createStatement();
                    String delete = "delete from users  where nationalid = " + delTxtNationalID.getText() + ";";
                    System.out.println(delete);
                    int rs = stmt.executeUpdate(delete);
                    System.out.println("sucess");
                    con.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "InValid Data try again");
                }
            });
            updatePane.disableProperty().set(false);
            DeletePane.disableProperty().set(true);

            center.getChildren().addAll(upDelPan, updatePane, DeletePane);

        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HBox bottom = new HBox();
        Button btnUser = new Button("Users");
        Button btnCase = new Button("Case");
        Button btnDonator = new Button("Donator");
        btnUser.setOnAction(e -> {
            primaryStage.close();
            UserMangement userMangement = new UserMangement(primaryStage, user);
        });

        btnCase.setOnAction(e -> {
            primaryStage.close();
            CaseMangement caseMangement = new CaseMangement(primaryStage, user);
        });

        btnDonator.setOnAction(e -> {
            primaryStage.close();
            DonatorMangement donatorMangement = new DonatorMangement(primaryStage, user);
        });

        if (user.getRole().equals("admin")) {
            bottom = new HBox(btnUser, btnCase, btnDonator);
        } else {
            bottom = new HBox(btnCase, btnDonator);
        }
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);

        root.setTop(top);
        center.setSpacing(3);
        ScrollPane scrollCenter = new ScrollPane(center);
        scrollCenter.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCenter.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCenter.setPadding(new Insets(20));
        root.setCenter(scrollCenter);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User-Add");
        primaryStage.show();
    }

    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }

    public UserMangement(Stage primaryStage, User user) {
        this.user = user;
        start(primaryStage);
    }

}
