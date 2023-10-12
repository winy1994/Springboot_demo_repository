package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee") // Chỉ định tên bảng trong cơ sở dữ liệu
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID") // Chỉ định tên cột trong cơ sở dữ liệu
    private Long id_entity;

    @Column(name = "FIRST_NAME") // Chỉ định tên cột trong cơ sở dữ liệu
    private String firstName_entity;

    @Column(name = "LAST_NAME") // Chỉ định tên cột trong cơ sở dữ liệu
    private String lastName_entity;

    @Column(name = "ADDRESS") // Chỉ định tên cột trong cơ sở dữ liệu
    private String address_entity;
    
    @Column(name = "AGE")
    private int age_entity;
   
    @Column(name = "BIRTH_DATE") // Chỉ định tên cột trong cơ sở dữ liệu
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate_entity; // Sử dụng kiểu Date cho trường birthDate_entity
    
    @Column(name = "ACTIVEE") // Chỉ định tên cột trong cơ sở dữ liệu
    private Boolean active_entity;
    

}
