/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charity;

import static charity.UserMangement.createArrayValueFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Maamon
 */
public class DonatorMangement extends Application {
    
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
        Label lNationalID = new Label("Natoional ID");
        TextField txtNationalID = new TextField();
        Label lName = new Label("Name");
        TextField txtName = new TextField();
        Label lAddress = new Label("Address");
        TextField txtAddress = new TextField();
        Label lPhone = new Label("Phone");
        TextField txtPhone = new TextField();
        Button btnAddPhone = new Button("Add Phone");
        Button btnSigup = new Button("Add");
        center.getChildren().addAll(lNationalID, txtNationalID, lName, txtName, lAddress, txtAddress,
                lPhone, txtPhone, btnAddPhone, btnSigup);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnAddPhone.setOnAction(add -> {
            int numberOfPhones = Integer.parseInt(JOptionPane.showInputDialog("Enter number of phones to be added"));
            morePhones = new TextField[numberOfPhones];
            center.getChildren().removeAll(center.getChildren());
            center.getChildren().addAll(lNationalID, txtNationalID, lName, txtName, lAddress, txtAddress,
                    lPhone, txtPhone, btnAddPhone, btnSigup);
            for (int x = 0; x < morePhones.length; ++x) {
                morePhones[x] = new TextField();
                center.getChildren().add(morePhones[x]);
            }
            center.getChildren().addAll(btnAddPhone, btnSigup);
        });
        
        btnSigup.setOnAction(excute -> {
            Donator donator = new Donator();
            try {
                donator.setNationalID(Integer.parseInt(txtNationalID.getText()));
                donator.setName(txtName.getText());
                donator.setAddress(txtAddress.getText());
                String phoneString = txtPhone.getText();
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/charity", "root", "");
                
                Statement stmt = con.createStatement();
                String insert = "insert into donators VALUES(" + donator.getNationalID() + ",\"" + donator.getName() + "\",\""
                        + donator.getAddress() + "\");";
                System.out.println(insert);
                int rs = stmt.executeUpdate(insert);
                System.out.println("sucess");
                
                if (!txtPhone.getText().equals("")) {
                    
                    if (morePhones != null) {
                        String strMorePhones[] = new String[morePhones.length + 1];
                        strMorePhones[0] = phoneString;
                        for (int x = 1; x < morePhones.length + 1; ++x) {
                            strMorePhones[x] = morePhones[x - 1].getText();
                            insert = "insert into donatorphone VALUES(" + donator.getNationalID() + ",\"" + strMorePhones[x] + "\");";
                            rs = stmt.executeUpdate(insert);
                            System.out.println("sucess");
                        }
                        donator.SetPhones(strMorePhones);
                    }
                    insert = "insert into donatorphone VALUES(" + donator.getNationalID() + ",\"" + phoneString + "\");";
                    rs = stmt.executeUpdate(insert);
                    System.out.println("sucess");
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Charity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserMangement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        rInsert.setOnAction(e -> {
            center.getChildren().removeAll(center.getChildren());
            //center components for sign up
            center.getChildren().addAll(lNationalID, txtNationalID, lName, txtName, lAddress, txtAddress,
                    lPhone, txtPhone, btnAddPhone, btnSigup);
            primaryStage.setTitle("Donator-Add");
            
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        rShowAll.setOnAction(e -> {
            try {
                primaryStage.setTitle("Donator-Show All");
                center.getChildren().removeAll(center.getChildren());
                TableView<Donator> donatorData = new TableView<>();
                TableColumn colNationalID = new TableColumn("National ID");
                colNationalID.setCellValueFactory(new PropertyValueFactory<>("nationalID"));
                TableColumn colName = new TableColumn("Name");
                colName.setCellValueFactory(new PropertyValueFactory<>("name"));
                TableColumn colAddress = new TableColumn("Address");
                colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                TableColumn[] colPhone;
                
                String select = "select count(dp.phone) from donators d left join donatorphone dp on d.nationalid=dp.nationalid group by d.nationalid;";
                
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
                
                select = "select d.nationalid,d.name,d.address,dp.phone from donators d left join donatorphone dp on d.nationalid=dp.nationalid;";
                rs = stmt.executeQuery(select);
                String phones[];
                Donator donators[] = new Donator[phonesSizes.length];
                for (int x = 0; x < phonesSizes.length; ++x) {
                    phones = new String[phonesSizes[x]];
                    donators[x] = new Donator();
                    rs.next();
                    donators[x].setNationalID(rs.getInt(1));
                    donators[x].setName(rs.getString(2));
                    donators[x].setAddress(rs.getString(3));
                    if (phonesSizes[x] > 0) {
                        phones[0] = rs.getString(4);
                        for (int y = 1; y < phonesSizes[x]; ++y) {
                            rs.next();
                            phones[y] = rs.getString(6);
                        }
                        donators[x].SetPhones(phones);
                    }
                }
                ObservableList<Donator> items = FXCollections.observableArrayList(donators);
                donatorData.setItems(items);
                donatorData.getColumns().addAll(colNationalID, colName, colAddress);
                for (int x = 0; x < phoneSize; ++x) {
                    colPhone[x] = new TableColumn("Phone" + (x + 1));
                    colPhone[x].setCellValueFactory(createArrayValueFactory(Donator::getPhones, x));
                    donatorData.getColumns().add(colPhone[x]);
                }
                center.getChildren().add(donatorData);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(UserMangement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        rModify.setOnAction(e -> {
            primaryStage.setTitle("Donator-Modify");
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
            updatePane.getChildren().addAll(upLNationalID, upTxtNationalID, lAddress, txtAddress, btnUpdate);
            
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
                    if (!txtAddress.getText().equals("")) {
                        update = "update donators set address =\"" + txtAddress.getText() + "\" where nationalid = " + upTxtNationalID.getText() + ";";
                        System.out.println(update);
                        int rs = stmt.executeUpdate(update);
                        System.out.println("sucess");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Charity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Charity.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
            
            btnDelete.setOnAction(del -> {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost/charity", "root", "");
                    Statement stmt = con.createStatement();
                    String delete = "delete from donators  where nationalid = " + delTxtNationalID.getText() + ";";
                    System.out.println(delete);
                    int rs = stmt.executeUpdate(delete);
                    System.out.println("sucess");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Charity.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Charity.class.getName()).log(Level.SEVERE, null, ex);
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
        
        root.setTop(top);
        center.setSpacing(3);
        center.setPadding(new Insets(20));
        root.setCenter(center);
        root.setBottom(bottom);
        
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Donator-Add");
        primaryStage.show();
    }
    
    public DonatorMangement(Stage primaryStage, User user) {
        this.user = user;
        start(primaryStage);
    }
    
}
