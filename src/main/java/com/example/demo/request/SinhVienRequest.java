package com.example.demo.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
@Getter
@Setter
public class SinhVienRequest {
    @NotEmpty
    @NotBlank
    @Min(6)
    private String tenSinhVien;
}
