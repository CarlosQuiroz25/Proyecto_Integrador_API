package com.cesde.proyecto_integrador.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    @Column(name = "name")
    private String name = "";

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "address")
    private String address = "";

    @Column(name = "url_photo")
    private String urlPhoto = "";
}
