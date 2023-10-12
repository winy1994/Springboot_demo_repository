package com.example.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//chú ý : Thực hiện thực thi các tệp SQL cho MySQL
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.sql.DataSource;
@Configuration
public class SpringJdbcConfig {

	@Bean(name = "mysqlDataSource")
	@Primary // Đánh dấu mysqlDataSource là @Primary
    DataSource mysqlDataSource() {
		 DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	        dataSource.setUrl("jdbc:mysql://localhost:3306/springjdbc");
	        dataSource.setUsername("root");
	        dataSource.setPassword("phong@1994");

	     
			// Kiểm tra xem bảng "EMPLOYEE" đã tồn tại hay không
	        if (!tableExists(dataSource, "springjdbc", "EMPLOYEE")) {
	            System.out.println("Bảng 'EMPLOYEE' không tồn tại.");
	            createTable(dataSource, "templates/employee_sql/employee.sql");
	        } else {
	            Scanner scanner = new Scanner(System.in);
	            System.out.print("Bảng 'EMPLOYEE' đã tồn tại. Bạn có muốn xóa và tạo lại không? (yes/no): ");
	            String response = scanner.nextLine().trim();
	            if (response.equalsIgnoreCase("yes")) {
	                dropTable(dataSource, "springjdbc", "EMPLOYEE");
	                createTable(dataSource, "templates/employee_sql/employee.sql");
	            } else {
	                boolean altered = false;
	                do {
	                    System.out.print("Bạn có muốn chạy hàm alterEmployee không? (yes/no): ");
	                    String alterResponse = scanner.nextLine().trim();
	                    if (alterResponse.equalsIgnoreCase("yes")) {
	                        try {
	                            alterEmployee(dataSource, "templates/employee_sql/alter_employee.sql");
	                            altered = true;
	                        } catch (Exception e) {
	                            System.err.println("Lỗi khi thực hiện alterEmployee: " + e.getMessage());
	                        }
	                    } else {
	                        break;
	                    }
	                } while (!altered);
	            }
	        } 
	        


	        return dataSource;
   }
	
	@Bean(name = "mysqlJdbcTemplate")
	 public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
	     return new NamedParameterJdbcTemplate(mysqlDataSource());
	 }
	
	 // Hàm kiểm tra xem một bảng đã tồn tại trong cơ sở dữ liệu hay chưa
    private boolean tableExists(DataSource dataSource, String databaseName, String tableName) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SHOW TABLES FROM " + databaseName + " LIKE '" + tableName + "'");
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm tạo mới bảng EMPLOYEE
    private void createTable(DataSource dataSource, String sqlScript) {
        // Thực hiện thực thi tệp SQL để tạo bảng
        Resource sqlScriptResource = new ClassPathResource(sqlScript);

        DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(sqlScriptResource), dataSource);
        System.out.println("Bảng 'EMPLOYEE' đã được tạo.");
    }

    // Hàm xóa bảng
    private void dropTable(DataSource dataSource, String databaseName, String tableName) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + databaseName + "." + tableName);
            System.out.println("Bảng '" + tableName + "' đã bị xóa.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Hàm alterEmployee để thay đổi cấu trúc bảng theo yêu cầu của bạn
    private void alterEmployee(DataSource dataSource, String sqlScript) {
        // Thực hiện tệp SQL để thay đổi cấu trúc bảng
        Resource sqlScriptResource = new ClassPathResource(sqlScript);

        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, sqlScriptResource);
            System.out.println("Bảng 'EMPLOYEE' đã được sửa đổi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
