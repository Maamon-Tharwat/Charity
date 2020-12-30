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
public class Case {
    int nationalID;
    String name;
    String address;
    String [] phones;
    String type;
    
    public void setNationalID(int natID){
        nationalID=natID;
    }
     
    public void setName(String name){
        this.name=name;
    }
    
    public void setAddress(String address){
        this.address=address;
    }
    
    public void SetPhones(String [] phone){
        phones=new String[phone.length];
        System.arraycopy(phone, 0, phones, 0, phone.length);
    }
    
    public void setType(String type){
        this.type=type;
    }
    
    
    public int getNationalID(){return nationalID;}
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String [] getPhones(){return phones;}
    public String getType(){return type;}
    
}
