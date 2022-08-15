package com.example.demo.service;

import com.example.demo.model.SinhVien;
import com.example.demo.request.SinhVienRequest;

import java.util.List;

public interface SinhVienService {

     List<SinhVien> getList();

     SinhVien addNew(SinhVienRequest sinhVien);

     boolean delete(long id);

     SinhVien update(long id, SinhVienRequest sinhVien);

     SinhVien findById(long id);

}
