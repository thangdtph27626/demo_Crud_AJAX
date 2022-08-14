package com.example.demo.controller;

import com.example.demo.model.SinhVien;
import com.example.demo.request.SinhVienRequest;
import com.example.demo.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SinhVienResController {

    @Autowired()
    private SinhVienService sinhVienService;

    @PostMapping()
    public SinhVien addNew(@RequestBody SinhVienRequest sinhVien){
        System.out.println(sinhVien.getTenSinhVien());
        return sinhVienService.addNew(sinhVien);
    }

    @GetMapping("/{id}")
    public SinhVien DetailSinhVien(@PathVariable("id") long id){
        SinhVien sinhVien = null;
        try {
            sinhVien = sinhVienService.findById(id);
        }catch (Exception e){
            System.out.println(e);
        }
        return sinhVien;
    }

    @PutMapping("/{id}")
    public SinhVien update(@PathVariable("id") long id, @RequestBody SinhVienRequest sinhVienRequest){
        return sinhVienService.update(id, sinhVienRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") long id){
        return sinhVienService.delete(id);
    }

    @GetMapping()
    public List<SinhVien> listSinhVien(Model model){
        List<SinhVien> list = sinhVienService.getList();
        return list;
    }
}
