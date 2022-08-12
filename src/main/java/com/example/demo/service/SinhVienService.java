package com.example.demo.service;

import com.example.demo.model.SinhVien;
import com.example.demo.request.SinhVienRequest;

import java.util.List;

public interface SinhVienService {

    public List<SinhVien> getList();

    public SinhVien addNew(SinhVienRequest sinhVien);

    public boolean delete(long id);

    public SinhVien update(long id, SinhVienRequest sinhVien);

    public SinhVien findById(long id);

}
