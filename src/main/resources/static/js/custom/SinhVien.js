let SinhVienView = "/view"
let SinhVienAPI = "https://63369b9d65d1e8ef266f9da2.mockapi.io/api/users"

$(document).ready(function () {
    $("#sinh_vien_error").text("");
    loadData()
});

function loadData() {
    $.ajax({
        type: "GET", contentType: "application/json", url: SinhVienAPI, success: function (responseData) {
            console.log(responseData)
            $("#dataSinhVien").html(responseData.map(function (item) {
                return `
                <tr>
                <td>${item.id}</td>
                <td>${item.name}</td>
                 <td>
                        <button
                                type="button"
                                class="btn btn-primary"
                               onclick="openModalUpdateSinhVien(${item.id})">
                            Sửa
                        </button>
                        </button>
                        <button
                                type="button"
                                class="btn btn-danger"
                                data-toggle="modal"
                               onclick="openModalRemoveSinhVien(${item.id})">
                            Xoá
                        </button>
                    </td>
                    </tr>
                `
            }))
        }, error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

$("#form_create_sinh_vien").submit(function (event) {
    event.preventDefault();
    let tenSinhVien = $("#tenSinhVien").val();
    console.log(tenSinhVien)
    let sinhVienRequest = {};
    sinhVienRequest["name"] = tenSinhVien;
    console.log(sinhVienRequest)
    if (tenSinhVien.length === 0) {
        $("#sinh_vien_error").text("Tên sinh vien không được để trống");
    } else if (tenSinhVien.length < 6) {
        $("#sinh_vien_error").text("Tên sinh vien tối thiếu 6 ký tự");
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: SinhVienAPI,
            data: JSON.stringify(sinhVienRequest),
            dataType: 'json',
            success: function () {
                window.open(SinhVienView, '_self');
                $("#modal_create").modal("hide");
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
});

function openModalUpdateSinhVien(idSinhVien) {
    $("#modal_update_sinh_vien").modal('show');
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: SinhVienAPI + "/" + idSinhVien,
        data: JSON.stringify(idSinhVien),
        dataType: 'json',
        success: function (responseData) {
            console.log(responseData)
            $("#id_sinh_vien_update").val(responseData.id);
            $("#ten_sinh_vien_update").val(responseData.name);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

$("#form_sinh_vien_update").submit(function (event) {
    event.preventDefault();
    let tenSinhVien = $("#ten_sinh_vien_update").val();
    let idSinhvien = $("#id_sinh_vien_update").val();
    let sinhVienRequest = {};
    sinhVienRequest["name"] = tenSinhVien;
    if (tenSinhVien.length === 0) {
        $("#errorMessageUpdate").text("Tên sinh vien không được để trống");
    } else if (tenSinhVien.length < 6) {
        $("#errorMessageUpdate").text("Tên sinh vien tối thiếu 6 ký tự");
    } else {
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: SinhVienAPI + "/" + idSinhvien,
            data: JSON.stringify(sinhVienRequest),
            dataType: 'json',
            success: function () {
                window.open(SinhVienView, '_self');
                $("#modal_update_hoc_ky").modal("hide");
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
});

function openModalRemoveSinhVien(sinhvienId) {
    console.log(sinhvienId)
    $("#modal_sinh_vien_remove").modal('show');
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: SinhVienAPI + "/" + sinhvienId,
        data: JSON.stringify(sinhvienId),
        dataType: 'json',
        success: function () {
            $("#remove_sinh_vien").val(sinhvienId);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

$("#form_sinh_vien_delete").submit(function (event) {
    event.preventDefault();
    let sinhvienId = $("#remove_sinh_vien").val();

    $.ajax({
        type: "DELETE",
        contentType: "application/json",
        url: SinhVienAPI + "/" + sinhvienId,
        dataType: 'json',
        success: function () {
            window.open(SinhVienView, '_self');
            $("#modal_update_hoc_ky").modal("hide");
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });

});