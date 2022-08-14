package com.example.demo.model;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @Column(name = "ma_sinh_vien")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maSinhVien;

    @Column(name = "ten_sinh_vien", nullable = true)
    private String tenSinhVien;

}
