package com.example.kursinisjavafx.Model;

import java.io.Serializable;


public class Driver extends User implements Serializable {

    //private String medCertificate;

    public Driver(int id, String username, String password, String fullname, String surname, String email, String phoneNo, String dateOfBirth, String role) {
        super(id, username, password, fullname, surname, email, phoneNo, dateOfBirth, role);
    }


}

