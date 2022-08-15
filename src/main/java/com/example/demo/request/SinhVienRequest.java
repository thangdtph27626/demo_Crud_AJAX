package com.example.demo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class SinhVienRequest {
    @NotEmpty
    @NotBlank
    @Min(6)
    private String tenSinhVien;
}
