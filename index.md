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
