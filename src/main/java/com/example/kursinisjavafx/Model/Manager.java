package com.example.kursinisjavafx.Model;

import java.io.Serializable;


public class Manager extends User implements Serializable {

    //private String kazkoksDocas;

    public Manager(int id, String username, String password, String fullname, String surname, String email, String phoneNo, String dateOfBirth, String role) {
        super(id, username, password, fullname, surname, email, phoneNo, dateOfBirth, role);
    }


}
