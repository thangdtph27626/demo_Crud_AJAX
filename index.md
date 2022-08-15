## Demo CRUD AJAX With Spring Boot

## Giới thiệu

CRUD là từ viết tắt của 4 thao tác như Tạo, Đọc, Cập nhật và Xóa. Tôi sẽ thực hiện các hoạt động CRUD trên thông tin chi tiết của sinh viên. Tôi sẽ sử dụng cơ sở dữ liệu MySQL ở đây để lưu trữ thông tin chi tiết của sinh viên. Tôi sẽ thực hiện từng thao tác không đồng bộ thông qua kỹ thuật AJAX bằng cách sử dụng jQuery ở phía client và Spring Boot với Spring Data JPA ở phía máy chủ.\
Tôi sẽ hiển thị thông tin sinh viên ở định dạng bảng hoặc trong bảng HTML trên giao diện người dùng hoặc giao diện người dùng. Tôi sẽ thực hiện chỉnh sửa nội tuyến trên một hàng. Một hộp cảnh báo sẽ được hiển thị cho dù người dùng có muốn thực sự xóa bản ghi hay không khi nhấp vào nút xóa. Tôi sẽ thêm chi tiết sinh viên mới trên một cửa sổ bật lên.


### Điều kiện tiên quyết

Java tối thiểu 8, Gradle 5.6 - 6.7.1, Maven 3.6.3, Spring Boot 2.2.1 - 2.4.2, Spring Data JPA, MySQL 8.0.17 - 8.0.22, jQuery 3.4.1 - 3.5.1 và tâm hồn đẹp

### Bảng MySQL

Trước khi thực hiện bất kỳ thao tác nào trên dữ liệu, bạn cần tạo bảng cần thiết. Đương nhiên, tôi sẽ tạo một bảng có tênsinh_vienvien và chèn dữ liệu.

Để kiểm tra ứng dụng ngay lập tức, bạn cần có một số dữ liệu trong bảng. Bạn cũng có thể chạy một số thử nghiệm bằng cách tạo thông tin sinh viên  mới từ giao diện người dùng nếu ban đầu bảng của bạn không có bất kỳ dữ liệu nào.


```markdown
create database demoAJax
use demoAJax

create table sinh_vien(
	ma_sinh_vien int AUTO_INCREMENT,
    ten_sinh_vien nvarchar(50) not null,
     PRIMARY KEY (ma_sinh_vien)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

// thêm dữ liệu

INSERT INTO `demoajax`.`sinh_vien`
(`ma_sinh_vien`,
`ten_sinh_vien`)
VALUES (1,'le van A'),(2,'le van B')

```

### Cấu trúc dự án 

Cây thư mục dự án như sau:

<img width="236" alt="image" src="https://user-images.githubusercontent.com/109157942/184499499-717355e1-2c1e-4bd7-8beb-dbcf5dc34323.png">


### Thiết lập dự án

Bạn có thể tạo một dự án dựa trên gradle hoặc maven trong IDE hoặc công cụ yêu thích của bạn. 

Bạn cần bao gồm các phụ thuộc bắt buộc trong  tập lệnh **build.gradle** hoặc **pom.xml**  để làm việc trên ứng dụng này bằng Spring Boot JPA.

```markdown
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation group: 'com.google.code.gson', name: 'gson', version: '2.9.0'

    runtimeOnly 'mysql:mysql-connector-java'
    // https://mvnrepository.com/artifact/log4j/log4j
    implementation group: 'log4j', name: 'log4j', version: '1.2.17'

    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf
    implementation group: 'org.springframework.boot', name: 'spring-boot-starter-thymeleaf', version: '2.7.0'

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation group: 'org.springframework.boot', name: 'spring-boot-starter-validation', version: '2.7.2'

    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-rest-core
    implementation group: 'org.springframework.data', name: 'spring-data-rest-core', version: '2.1.0.RELEASE'

    // https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa
    implementation group: 'org.springframework.data', name: 'spring-data-jpa', version: '2.7.2'

}
```
config trong file **application.properties**

```markdown
spring.datasource.url=jdbc:mysql://localhost:3306/**tên dự database**
spring.datasource.username= ** username **
spring.datasource.password=  ** password**
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.sql-script-encoding=UTF-8
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```

### Lớp thực thể (Entity Class)

Giả sử chúng ta có lớp thực thể sau được gọi SinhVien.


```markdown
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
```

>Các annotation mình sử dụng trong đoạn code trên là các annotation của JPA:

@Entity xác định lớp hiện tại là một entity.\
@Table xác định tên bảng ánh xạ sang.\
@Id xác định thuộc tính hiện tại là ID trong bảng CSDL.\
@GeneratedValue xác định kiểu sinh khóa chính, ở đây là AUTO_INCREMENT.\
@Column xác định thuộc tính hiện tại là một cột trong CSDL.\

### Spring Data JPA Repository
Spring Data JPA API cung cấp hỗ trợ kho lưu trữ cho Java Persistence API (JPA) và nó giúp giảm bớt sự phát triển của các ứng dụng cần truy cập các nguồn dữ liệu JPA.\
Tôi sẽ tạo giao diện kho lưu trữ và bạn không cần tạo bất kỳ phương thức nào trong giao diện này vì Spring cung cấp các phương thức để thực hiện các thao tác CRUD cơ bản.
```markdown
package com.example.demo.repository;

import com.example.demo.model.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinhVienRepo extends JpaRepository<SinhVien, Long> {
}
```

### Request 
 bạn cần tạo một class để chứa các request của sinh viên 
 
```markdown
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
```

### Service

Lớp Service nằm giữa bộ controller và lớp DAO và chứa các phương thức trừu tượng. Nói chung, bạn thực hiện gọi các hàm trong lớp dịch vụ này.

```markdown
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
```

### implements

Lớp  **SinhVienServiceImpl** ghi đè các phương thức của  **SinhVienService**  và thực hiện logic nghiệp vụ trong lớp dịch vụ này\
Bạn nhận được kết quả của các truy vấn nối từ kho lưu trữ và chuyển cho lớp điều khiển REST.\
Tôi sử dụng cùng một phương pháp để lưu hoặc cập nhật thông tin công ty mới hoặc hiện có tương ứng.

```markdown
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
        if(sinhVien.isPresent()){
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
```
### Controller

> SinhVienController: hiển thị tất cả các sinh viên có trong cơ sở dữ liệu của bạn

```markdown
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

```

> SinhVienResController

```markdown
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
    
	// hiển thị tất cả các sinh viên
  @GetMapping()
    public List<SinhVien> listSinhVien(Model model){
        List<SinhVien> list = sinhVienService.getList();
        return list;
    }
	
	// tìm kiếm sinh viên theo id 
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
}


```

## Thực Hiện CRUD với ajax

> chú thích

```markdown
$(selector).action()

 $.ajax({
            type: Một loại yêu cầu http, ví dụ như POST, PUT và GET. Mặc định là GET,
            contentType: Chuỗi chứa một loại nội dung khi gửi nội dung MIME tới máy chủ.Default là "application / x-www-form-urlencoded; charset = UTF-8,
            url: Một chuỗi chứa URL mà yêu cầu được gửi đến,
            data: Một dữ liệu được gửi đến máy chủ. Nó có thể là đối tượng JSON, chuỗi hoặc mảng,
            dataType: Loại dữ liệu mà bạn đang mong đợi trả lại từ máy chủ,
            success: function (responseData) { // Một hàm gọi lại sẽ được thực thi khi yêu cầu Ajax thành công
	    	// responseData là đối tượng trả về chứa tất cả các trường 
                 window.open(SinhVienView, '_self');   
		//windown.open: mở ra một cửa sổ trình duyệt mới hoặc một tab mới, tùy thuộc vào cài đặt trình duyệt của bạn và các giá trị tham số
		// _self: URL thay thế trang hiện tại
                 $("#demo").modal("hide"); 
		// tìm thẻ có id là demo và thực hiện ẩn thẻ
            },
            error: function (e) {  // Một hàm gọi lại được thực thi khi yêu cầu không thành công.
                console.log("ERROR : ", e);
            }
        });
```

### 1: đọc dữ liệu 

1: html 

```markdown
<table id="custom-table" class="table table-bordered m-table d-sm-table m-table--head-bg-primary">
      <thead>
		<tr>
			<td>mã sinh viên</td>
			<td>ten sinh viên</td>
			<td>Hành động</td>
		</tr>
     </thead>
     <tbody id="dataSinhVien">
	     
     </tbody>
 </table>
```

2: js

```markdown
let SinhVienView = "/view"
let SinhVienAPI = "/api"

$(document).ready(function () { //thực hiện các mã js khi trang Mô hình đối tượng tài liệu (DOM) đã sẵn sàng
    $("#sinh_vien_error").text("");
    loadData()  //gọi hàm loadData() ra để thực thi
});

function loadData(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: SinhVienAPI ,
        success: function (responseData) {
            console.log(responseData)
            $("#dataSinhVien").html(responseData.map(function (item) {
                return `
                <tr>
			<td>${item.maSinhVien}</td>
			<td>${item.tenSinhVien}</td>
			  <td>
				<button
					type="button"
					class="btn btn-primary"
				       onclick="openModalUpdateSinhVien(${item.maSinhVien})">
				    Sửa
				</button>
				</button>
				<button
					type="button"
					class="btn btn-danger"
					data-toggle="modal"
				       onclick="openModalRemoveSinhVien(${item.maSinhVien})">
				    Xoá
				</button>
			</td>
                 </tr>
                `
            }))
	        /*
		    hàm map sẽ return về 1 đoạn template string chứa cả 1 dòng trong table
		    trong mảng có bao nhiêu phần tử sẽ lặp bấy nhiêu lần và hiển thị 
        	*/ 
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

```

### 2: thêm dữ liệu 
---

1: html 

```markdown
<div class="m-portlet__body">
                <div class="row">
                    <div class="col-6 d-inline">
                        <button
                                data-toggle="modal"
                                data-target="#modal_create"
                                class="col-12 col-sm-8 col-md-4 btn btn-success">
                            Thêm sinh vien
                        </button>
                        <div class="modal fade "
                             id="modal_create"
                             tabindex="-1"
                             aria-labelledby="exampleModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <form id="form_create_sinh_vien">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel">Thêm Sinh Vien</h5>
                                            <button type="button" class="close" data-dismiss="modal"
                                                    aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="form-group">
                                                <label class="col-form-label">Ten Sinh Vien</label>
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        id="tenSinhVien">
                                                <span class="text-danger"
                                                      id="sinh_vien_error"></span>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary"
                                                    data-dismiss="modal">Hủy
                                            </button>
                                            <button type="submit" class="btn btn-primary">Thêm mới
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
```
2: trong file **SinhVienResController** bạn thực hiện save dữ liệu vào db 

```markdown

@RestController
@RequestMapping("/api")
public class SinhVienResController {

    @Autowired()
    private SinhVienService sinhVienService;

    @PostMapping()
    public SinhVien addNew(@RequestBody SinhVienRequest sinhVien){
        return sinhVienService.addNew(sinhVien);
    }
    
}
```

3: thực hiện Phần tiếp theo chứa lập trình jQuery AJAX để thực hiện các hoạt động thêm trong file ** SinhVien.js **

```markdown
let SinhVienView = "/view"
let SinhVienAPI = "/api"

$("#form_create_sinh_vien").submit(function (event) {
//(tìm thẻ có id **form_create_sinh_vien**).(sự kiện) 
//         sự kiện này sẽ thực hiện chức năng trong function
//         trong function truyền vào 1 tham số là event: tham số này được sử dụng để chặn sự kiện reload
    event.preventDefault(); 
    // chặn sự kiện reload
    let tenSinhVien = $("#tenSinhVien").val();
    // tìm phần tử có id **tenSinhVien** và gán cho biến tenSinhVien
    console.log(tenSinhVien)
    let sinhVienRequest = {};  
    // khởi tạo một object sinhvienrequest 
    sinhVienRequest["tenSinhVien"] = tenSinhVien;
    //               key             value
    
    // kiểm tra dữ liệu của form
    if (tenSinhVien.length === 0) {
        $("#sinh_vien_error").text("Tên học kỳ không được để trống");
    } else if (tenSinhVien.length < 6) {
        $("#sinh_vien_error").text("Tên học kỳ tối thiếu 6 ký tự");
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: SinhVienAPI,
            data: JSON.stringify(sinhVienRequest),  // JSON.stringify() chuyển đổi dữ liệu sang kiểu chuỗi
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
```

### 3: xóa 

1:html

```markdown
 <!-- begin delete thông tin Sinh Viên -->
            <div
                    class="modal fade"
                    id="modal_sinh_vien_remove"
                    tabindex="-1"
                    aria-labelledby="exampleModalLabel"
                    aria-hidden="true">
                <div class="modal-dialog modal-lg modal-lg"
                     role="document">
                    <div class="modal-content">
                        <form id="form_sinh_vien_delete">
                            <div class="modal-header">
                                <h5 class="modal-title">Xoá sinh viên</h5>
                                <button type="button" class="close"
                                        data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p>Bạn có muốn xoá không ?</p>
                                <span id="remove_sinh_vien" style="color:red;"></span>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                        data-dismiss="modal">Hủy
                                </button>
                                <button type="submit" class="btn btn-primary">Xoá
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- end update thông tin Sinh Viên -->
```

2: SinhVienResController

   bạn thêm một hàm thực hiện xóa dữ liệu trong db
   
   ```markdown
    
   @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") long id){
        return sinhVienService.delete(id);
    }
   ```

3: sinhvien.js

```markdown

 /*
        khởi tạo hàm openModalRemoveSinhVien để tìm kiếm sinh viên
        truyền vào hàm 1 tham số sinhvienId để tìm ra thông tin sinh viên cần xóa
*/
function openModalRemoveSinhVien(sinhvienId) {  
    $("#modal_sinh_vien_remove").modal('show');
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: SinhVienAPI + "/" + sinhvienId,
        data: JSON.stringify(sinhvienId),
        dataType: 'json',
        success: function () {
            $("#remove_sinh_vien").val(sinhvienId); // gán giá trị cho thẻ có id là ** remove_sinh_vien**  
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

/*
        Hàm được thực thi khi người dùng nhấp nút submit 
*/
$("#form_sinh_vien_delete").submit(function (event) { // thực hiện xóa Sinh viên 
    event.preventDefault();
    let sinhvienId = $("#remove_sinh_vien").val();
     // tìm phần tử có id **remove_sinh_vien** và gán cho biến sinhvienId

    $.ajax({
        type: "DELETE",
        contentType: "application/json",
        url: SinhVienAPI + "/" + sinhvienId,
        data: JSON.stringify(sinhvienId),
        dataType: 'json',
        success: function () {
            window.open(SinhVienView, '_self');
            $("#form_sinh_vien_delete").modal("hide");
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });

});
```

 ### 4: update 
 
 1: html 
 
 ```markdown
            <div
                    class="modal fade"
                    tabindex="-1"
                    id="modal_update_sinh_vien"
                    aria-labelledby="exampleModalLabel"
                    aria-hidden="true">
                <div class="modal-dialog modal-lg modal-lg"
                     role="document">
                    <div class="modal-content">
                        <form id="form_sinh_vien_update">
                            <div class="modal-header">
                                <h5 class="modal-title">Cập
                                    nhật Sinh Viên</h5>
                                <button type="button" class="close"
                                        data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <span id="id_sinh_vien_update" style="color:red;"></span>

                                <div class="form-group">
                                    <label
                                            class="col-form-label">Tên Sinh Viên</label>
                                    <input
                                            type="text"
                                            class="form-control"
                                            id="ten_sinh_vien_update"/>
                                    <small class="text-danger"
                                           id="errorMessageUpdate"></small>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                        data-dismiss="modal">Hủy
                                </button>
                                <button type="submit" class="btn btn-primary">Cập
                                    nhật
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
 ```
2: SinhVienResController.java

```markdown
 @PutMapping("/{id}")
    public SinhVien update(@PathVariable("id") long id, @RequestBody SinhVienRequest sinhVienRequest){
        return sinhVienService.update(id, sinhVienRequest);
    }
```

3: sinhvien.js

```markdown
   function openModalUpdateSinhVien(idSinhVien) { // thực hiện tìm kiếm sinh viên 
    $("#modal_update_sinh_vien").modal('show');
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: SinhVienAPI + "/" + idSinhVien,
        data: JSON.stringify(idSinhVien),
        dataType: 'json',
        success: function (responseData) {
            console.log(responseData.data)
            $("#id_sinh_vien_update").val(idSinhVien);
            $("#ten_sinh_vien_update").val(responseData.tenSinhVien); 
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

$("#form_sinh_vien_update").submit(function (event) {  // update Sinh viên
    event.preventDefault();
    let tenSinhVien = $("#ten_sinh_vien_update").val();
    let idSinhvien = $("#id_sinh_vien_update").val();
    let sinhVienRequest = {};
    sinhVienRequest["tenSinhVien"] = tenSinhVien;
    if (tenSinhVien.length === 0) {
        $("#errorMessageUpdate").text("Tên học kỳ không được để trống");
    } else if (tenSinhVien.length < 6) {
        $("#errorMessageUpdate").text("Tên học kỳ tối thiếu 6 ký tự");
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

```



### kết quả 
> màn hình chính

<img width="960" alt="image" src="https://user-images.githubusercontent.com/109157942/184519763-3671dbf8-fa32-4f46-be6c-5c1d0855f761.png">

> thêm mới sinh viên 

<img width="609" alt="image" src="https://user-images.githubusercontent.com/109157942/184519778-a7e36263-ff81-4b45-bf66-66356a903328.png">

> xóa sinh viên 

<img width="603" alt="image" src="https://user-images.githubusercontent.com/109157942/184519789-8dbf87a1-83d3-46d1-af64-1cfe7543626d.png">

> update sinh viên 

<img width="602" alt="image" src="https://user-images.githubusercontent.com/109157942/184519799-b3a5fa5f-83cc-4ad6-a998-8b7e97271b09.png">


### Kết luận
Trong bài viết này, chúng ta đã học cách xây dựng một ứng dụng web CRUD cơ bản với ajax.

tất cả các mẫu mã hiển thị trong bài viết đều có sẵn [trên github](https://github.com/thangdtph27626/demo_crud_ajax.github.io)
