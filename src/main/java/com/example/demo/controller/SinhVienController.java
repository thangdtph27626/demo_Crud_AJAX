package com.example.demo.controller;

import com.example.demo.model.SinhVien;
import com.example.demo.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/view")
public class SinhVienController {

    @Autowired()
    private SinhVienService sinhVienService;

    @GetMapping()
    public String listSinhVien(Model model){
        return "sinhViens";
    }
}

