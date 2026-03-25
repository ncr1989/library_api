package com.cs2i.libraryapi.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
