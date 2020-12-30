/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charity;

/**
 *
 * @author Maamon
 */
public class User {
    int nationalID;
    String username;
    String password;
    String name;
    String address;
    String role;
    String [] phones;
    
    public void setNationalID(int natID){
        nationalID=natID;
    }
    
    public void setUsername(String usr){
        username=usr;
    }
    
    public void setPassword(String pass){
        password=pass;
    }
    
    public void setName(String name){
        this.name=name;
    }
    
    public void setAddress(String address){
        this.address=address;
    }
    
    public void setRole(String role){
        this.role=role;
    }
    
    public void SetPhones(String [] phone){
        phones=new String[phone.length];
        System.arraycopy(phone, 0, phones, 0, phone.length);
    }
    
    public int getNationalID(){return nationalID;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getRole(){return role;}
    public String [] getPhones(){return phones;}
}
