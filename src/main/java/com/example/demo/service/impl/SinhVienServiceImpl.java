package com.example.demo.service.impl;

import com.example.demo.model.SinhVien;
import com.example.demo.repository.SinhVienRepo;
import com.example.demo.request.SinhVienRequest;
import com.example.demo.service.SinhVienService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SinhVienServiceImpl implements SinhVienService {

    @Autowired
    private SinhVienRepo sinhVienRepository;

    @Override
    public List<SinhVien> getList() {
        return sinhVienRepository.findAll();
    }

    @Override
    public SinhVien addNew(SinhVienRequest sinhVienRequest) {
        SinhVien sinhVien = new SinhVien();
        BeanUtils.copyProperties(sinhVienRequest, sinhVien);
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public boolean delete(long id) {
        Optional<SinhVien> sinhVien = sinhVienRepository.findById(id);
        if (sinhVien.isPresent()) {
            sinhVienRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SinhVien update(long id, SinhVienRequest sinhVienRequest) {
        SinhVien sinhVien = sinhVienRepository.findById(id).orElse(null);
        sinhVien.setTenSinhVien(sinhVienRequest.getTenSinhVien());
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public SinhVien findById(long id) {
        return sinhVienRepository.findById(id).orElse(null);
    }
}