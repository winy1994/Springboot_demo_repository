package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import com.example.entity.Employee;
import com.example.exception.CustomDataAccessException;


import java.util.List;

//quang trọng phải có, không tự import
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;


@Repository
public class EmployeeRepository {

	@Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
//	cách 2: 
//	private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
     
		
    }
    
	/*START Chung: 
	 * sử dụng một RowMapper<Employee> để ánh xạ kết quả truy vấn vào đối tượng
	 * Employee. Phương thức findAll() và findById() sử dụng employeeRowMapper để
	 * ánh xạ dữ liệu từ kết quả truy vấn vào đối tượng Employee
	 */
    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) -> {//phải import import org.springframework.jdbc.core.RowMapper;
        Employee employee = new Employee();
        employee.setId_entity(rs.getLong("ID"));
        employee.setFirstName_entity(rs.getString("FIRST_NAME"));
        employee.setLastName_entity(rs.getString("LAST_NAME"));
        employee.setAddress_entity(rs.getString("ADDRESS"));
		/* <!--start :thêm vào --> */
        employee.setAge_entity(rs.getInt("AGE"));
        employee.setBirthDate_entity(rs.getDate("BIRTH_DATE"));
        employee.setActive_entity(rs.getBoolean("ACTIVEE"));
		/* <!--end :thêm vào --> */
        return employee;
    };
    
    /*================================================= END : RowMapper<Employee>====================================================== */
    
	/*================================================= Start : Truy vấn findAll findAll()====================================================== */
    
//    public List<Employee> findAll() {
//        String sql = "SELECT * FROM employee";
//        return namedParameterJdbcTemplate.query(
//        		sql,
//        		new BeanPropertySqlParameterSource(
//        				new Employee()),
//        		(rs, rowNum) -> {
//            Employee employee = new Employee();
//            employee.setId_entity(rs.getLong("ID"));
//            employee.setFirstName_entity(rs.getString("FIRST_NAME"));
//            employee.setLastName_entity(rs.getString("LAST_NAME"));
//            employee.setAddress_entity(rs.getString("ADDRESS"));
//            return employee;
//        });
//    }
    public List<Employee> findAll() {
    	try {
            String sql = "SELECT * FROM employee";
            return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(new Employee()), employeeRowMapper);
        } catch (DataAccessException ex) {
            // Xử lý ngoại lệ theo cách tùy chỉnh
            throw new CustomDataAccessException("Custom Data Access Exception occurred", ex);
        }
    }
    
//    public List<Employee> findAll() {
//        String sql = "SELECT * FROM employee";
//        return namedParameterJdbcTemplate.query(
//        		sql, 
//        		(rs, rowNum) -> {
//            Employee employee = new Employee();
//            employee.setId_entity(rs.getLong("ID"));
//            employee.setFirstName_entity(rs.getString("FIRST_NAME"));
//            employee.setLastName_entity(rs.getString("LAST_NAME"));
//            employee.setAddress_entity(rs.getString("ADDRESS"));
//            return employee;
//        });
//    }
    
//    public List<Employee> findAll() {
//        String sql = "SELECT * FROM employee";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            Employee employee = new Employee();
//            employee.setId_entity(rs.getLong("ID"));
//            employee.setFirstName_entity(rs.getString("FIRST_NAME"));
//            employee.setLastName_entity(rs.getString("LAST_NAME"));
//            employee.setAddress_entity(rs.getString("ADDRESS"));
//            return employee;
//        });
//    }
    
    /*================================================= END :Truy vấn findAll()====================================================== */

    /*================================================= Start : findById()====================================================== */
//    public Employee findById(Long id) {
//        String sql = "SELECT * FROM employee WHERE ID = :id";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("id", id);
//        try {
//            return namedParameterJdbcTemplate.queryForObject(
//            		sql,
//            		paramMap,
//            		(rs, rowNum) -> {
//                Employee employee = new Employee();
//                employee.setId_entity(rs.getLong("ID"));
//                employee.setFirstName_entity(rs.getString("FIRST_NAME"));
//                employee.setLastName_entity(rs.getString("LAST_NAME"));
//                employee.setAddress_entity(rs.getString("ADDRESS"));
//                return employee;
//            });
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
    public Employee findById(Long id) {
        String sql = "SELECT * FROM employee WHERE ID = :id";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("id", id);
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, paramMap, employeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	/*
	 * Cả hai đoạn mã bạn đưa ra đều liên quan đến Spring Framework và JDBC Template
	 * để thực hiện các truy vấn cơ sở dữ liệu. Tuy nhiên, từ góc độ bảo mật, cách
	 * bạn sử dụng chúng có sự khác biệt.
	 * 
	 * MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("id",
	 * id);:
	 * 
	 * Đoạn mã này tạo một đối tượng MapSqlParameterSource rỗng ban đầu và sau đó
	 * thêm giá trị "id" vào đối tượng này. Việc này có thể không an toàn nếu bạn
	 * không kiểm tra và xác minh giá trị id trước khi đưa vào đối tượng
	 * MapSqlParameterSource. Nếu giá trị id chưa được xác minh và có thể chứa các
	 * dữ liệu đầu vào độc hại, thì có thể xảy ra các lỗ hổng bảo mật như SQL
	 * injection.
	 * 
	 * SqlParameterSource paramMap = new MapSqlParameterSource("id", id);:
	 * 
	 * Đoạn mã này tạo một đối tượng MapSqlParameterSource và khai báo giá trị "id"
	 * khi bạn tạo đối tượng. Điều này giúp bạn đảm bảo rằng giá trị "id" đã được
	 * khai báo và xác minh từ trước. Tuy nhiên, việc này không thể thêm các giá trị
	 * khác sau khi đã tạo đối tượng MapSqlParameterSource.
	 * 
	 * Về mặt bảo mật, bạn nên ưu tiên cách thứ hai, tức là tạo đối tượng
	 * MapSqlParameterSource với giá trị đã được xác minh từ trước. Điều này giúp
	 * giảm nguy cơ SQL injection và đảm bảo tính toàn vẹn dữ liệu khi truy vấn cơ
	 * sở dữ liệu. Tuy nhiên, nếu bạn cần thêm các giá trị khác sau khi tạo đối
	 * tượng MapSqlParameterSource, bạn có thể xem xét cách sử dụng cả hai cách một
	 * cách an toàn bằng cách xác minh và kiểm tra giá trị trước khi thêm chúng vào
	 * đối tượng MapSqlParameterSource.
	 */
    
//    public Employee findById(Long id) {
//        String sql = "SELECT * FROM employee WHERE ID = ?";
//        try {
//            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
//                Employee employee = new Employee();
//                employee.setId_entity(rs.getLong("ID"));
//                employee.setFirstName_entity(rs.getString("FIRST_NAME"));
//                employee.setLastName_entity(rs.getString("LAST_NAME"));
//                employee.setAddress_entity(rs.getString("ADDRESS"));
//                return employee;
//            });
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
    /*================================================= End :Truy vấn findById()====================================================== */

    /*================================================= START  :Truy vấn findByFirstName()====================================================== */
    public List<Employee> findByFirstName(String firstName) {
        String sql = "SELECT * FROM employee WHERE FIRST_NAME = :firstName";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("firstName", firstName);
        SqlParameterSource paramMap = new MapSqlParameterSource("firstName", firstName);
        return namedParameterJdbcTemplate.query(
        		sql, 
        		paramMap, 
        		(rs, rowNum) -> {
            Employee employee = new Employee();
            employee.setId_entity(rs.getLong("ID"));
            employee.setFirstName_entity(rs.getString("FIRST_NAME"));
            employee.setLastName_entity(rs.getString("LAST_NAME"));
            employee.setAddress_entity(rs.getString("ADDRESS"));
            return employee;
        });
    }
    /*================================================= END :Truy vấn findByFirstName()====================================================== */
    
    /*================================================= Start :Truy vấn update()====================================================== */
    public void update(Employee employee) {
        String sql = "UPDATE employee SET "
        		+ "FIRST_NAME = :firstName_entity,"
        		+ " LAST_NAME = :lastName_entity,"
        		+ " ADDRESS = :address_entity,"
        		+ "AGE = :age_entity,"
        		+ "BIRTH_DATE = :birthDate_entity,"
        		+ "ACTIVEE = :active_entity "
        		+ "WHERE ID = :id_entity";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource()
        SqlParameterSource paramMap = new MapSqlParameterSource()
            .addValue("firstName_entity", employee.getFirstName_entity())
            .addValue("lastName_entity", employee.getLastName_entity())
            .addValue("address_entity", employee.getAddress_entity())
            .addValue("age_entity", employee.getAge_entity())
            .addValue("birthDate_entity", employee.getBirthDate_entity())
            .addValue("active_entity", employee.getActive_entity())
            .addValue("id_entity", employee.getId_entity());

        int rowsUpdated = namedParameterJdbcTemplate.update(sql, paramMap);
        if (rowsUpdated == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }
    
//    public void update(Employee employee) {
//        String sql = "UPDATE employee SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ? WHERE ID = ?";
//        int rowsUpdated = jdbcTemplate.update(sql, employee.getFirstName_entity(), employee.getLastName_entity(), employee.getAddress_entity(), employee.getId_entity());
//        if (rowsUpdated == 0) {
//            throw new EmptyResultDataAccessException(1);
//        }
//    }
    public boolean updateByFirstName(Long id, String name) {
        String sql = "UPDATE employee SET FIRST_NAME = :name WHERE ID = :id";
        MapSqlParameterSource paramMap = new MapSqlParameterSource()
            .addValue("name", name)
            .addValue("id", id);

        int rowsUpdated = namedParameterJdbcTemplate.update(sql, paramMap);
        return rowsUpdated > 0;
    }

	/*
	 * Không, bạn không cần dòng List<User> users =
	 * namedParameterJdbcTemplate.query(sql, parameters, new UserRowMapper()); trong
	 * tất cả các phương thức của lớp EmployeeRepository. Dòng này chỉ cần xuất hiện
	 * khi bạn thực sự muốn lấy kết quả trả về từ truy vấn cơ sở dữ liệu. Trong
	 * trường hợp các phương thức khác, nếu bạn không muốn lấy kết quả, bạn có thể
	 * xử lý ngoại lệ hoặc trả về void hoặc kiểu dữ liệu phù hợp.
	 * 
	 * Ví dụ, trong phương thức update, bạn không cần trả về kết quả từ truy vấn,
	 * bạn chỉ cần cập nhật dữ liệu trong cơ sở dữ liệu. Do đó, bạn có thể loại bỏ
	 * dòng List<User> users = namedParameterJdbcTemplate.query(sql, parameters, new
	 * UserRowMapper()); từ phương thức update.
	 * 
	 * Tóm lại, bạn cần thêm dòng List<User> users =
	 * namedParameterJdbcTemplate.query(sql, parameters, new UserRowMapper()); chỉ
	 * khi bạn muốn lấy kết quả từ truy vấn cơ sở dữ liệu. Trong các phương thức
	 * khác, bạn có thể loại bỏ nó nếu không cần lấy kết quả.
	 */
    /*================================================= END :Truy vấn update()====================================================== */
    
    /*================================================= Start :Truy vấn delete()====================================================== */
    public void delete(Long id) {
        String sql = "DELETE FROM employee WHERE ID = :id";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("id", id);
        SqlParameterSource paramMap = new MapSqlParameterSource("id", id);

        int rowsDeleted = namedParameterJdbcTemplate.update(
        		sql,
        		paramMap);
        if (rowsDeleted == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }
    
//    public void delete(Long id) {
//        String sql = "DELETE FROM employee WHERE ID = ?";
//        int rowsDeleted = jdbcTemplate.update(sql, id);
//        if (rowsDeleted == 0) {
//            throw new EmptyResultDataAccessException(1);
//        }
//    }
    /*================================================= END :Truy vấn delete()====================================================== */
    
    /*================================================= Start :Truy vấn save()====================================================== */
    
    public void save(Employee employee) {
        String sql = "INSERT INTO employee (FIRST_NAME, LAST_NAME, ADDRESS,AGE,BIRTH_DATE,ACTIVEE) "
        		+ "VALUES (:firstName_entity, :lastName_entity, :address_entity, :age_entity, :birthDate_entity, :active_entity)";
//        MapSqlParameterSource paramMap = new MapSqlParameterSource()
        SqlParameterSource paramMap = new MapSqlParameterSource()
            .addValue("firstName_entity", employee.getFirstName_entity())
            .addValue("lastName_entity", employee.getLastName_entity())
            .addValue("address_entity", employee.getAddress_entity())
        	.addValue("age_entity", employee.getAge_entity())
        	.addValue("birthDate_entity", employee.getBirthDate_entity())
        	.addValue("active_entity", employee.getActive_entity());

        namedParameterJdbcTemplate.update(sql, paramMap);
    }
    
//    public void save(Employee employee) {
//        String sql = "INSERT INTO employee (FIRST_NAME, LAST_NAME, ADDRESS) VALUES (?, ?, ?)";
//        jdbcTemplate.update(sql, employee.getFirstName_entity(), employee.getLastName_entity(), employee.getAddress_entity());
//    }
    /*================================================= END :Truy vấn save()====================================================== */
    
    /*================================================= START :Truy vấn searchEmployees()====================================================== */
//    public List<Employee> searchEmployees(String searchTerm) {
//    	String sql = "SELECT * FROM employee WHERE " +
//    		    "(:searchTerm IS NULL OR :searchTerm = '' OR " +
//    		    "FIRST_NAME LIKE :searchTermWildCard OR " +
//    		    "LAST_NAME LIKE :searchTermWildCard OR " +
//    		    "ADDRESS LIKE :searchTermWildCard)";
//        
//        MapSqlParameterSource paramMap = new MapSqlParameterSource()
//            .addValue("searchTerm", searchTerm)
//            .addValue("searchTermWildCard", "%" + searchTerm + "%");
//
//        return namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper<>(Employee.class));
//    }
    public List<Employee> searchEmployees(String searchTerm) {
        String sql = "SELECT * FROM employee WHERE " +
            "(:searchTerm IS NULL OR :searchTerm = '' OR " +
            "ID = :searchTerm OR " +
            "FIRST_NAME LIKE :searchTermWildCard OR " +
            "LAST_NAME LIKE :searchTermWildCard OR " +
            "ADDRESS LIKE :searchTermWildCard)";
        
//        MapSqlParameterSource paramMap = new MapSqlParameterSource()
        SqlParameterSource paramMap = new MapSqlParameterSource()
            .addValue("searchTerm", searchTerm)
            .addValue("searchTermWildCard", "%" + searchTerm + "%");

        return namedParameterJdbcTemplate.query(sql, paramMap, employeeRowMapper);
    }
    /*================================================= END :Truy vấn searchEmployees()====================================================== */

    /*================================================= START  :Truy vấn có thể tăng tính bảo mật()====================================================== */
	/*
	 * Sử dụng Prepared Statements: Mã này đã sử dụng
	 * PreparedStatementCreatorFactory để tạo Prepared Statements từ câu SQL và các
	 * tham số, điều này giúp ngăn chặn các cuộc tấn công SQL injection.
	 * 
	 * Sử dụng SqlParameterSource: Sử dụng SqlParameterSource để truyền tham số cho
	 * các câu lệnh SQL thay vì tạo câu lệnh SQL bằng cách nối chuỗi.
	 * SqlParameterSource đảm bảo rằng tham số được truyền an toàn và tránh các lỗ
	 * hổng SQL injection.
	 * 
	 * Xác thực dữ liệu đầu vào: Trước khi thực hiện bất kỳ truy vấn SQL nào, hãy
	 * kiểm tra và xác thực dữ liệu đầu vào. Điều này bao gồm kiểm tra các giá trị
	 * được truyền vào các tham số SqlParameterSource và đảm bảo rằng chúng phù hợp
	 * với mô hình dữ liệu dự kiến.
	 * 
	 * Giới hạn quyền truy cập cơ sở dữ liệu: Đảm bảo rằng ứng dụng chỉ sử dụng
	 * quyền truy cập cơ sở dữ liệu cần thiết và không có quyền truy cập không cần
	 * thiết. Điều này giúp giảm thiểu các rủi ro bảo mật.
	 * 
	 * Cập nhật các phiên bản phụ thuộc: Đảm bảo rằng tất cả các phiên bản của các
	 * thư viện và phần mềm được sử dụng trong mã đều được cập nhật lên phiên bản
	 * mới nhất để khắc phục các lỗ hổng bảo mật đã biết.
	 * 
	 * Xác thực và ủy quyền: Hãy đảm bảo rằng bạn đã xác thực và ủy quyền người dùng
	 * trước khi cho phép họ thực hiện các truy vấn cơ sở dữ liệu. Sử dụng các giải
	 * pháp như Spring Security để quản lý xác thực và ủy quyền.
	 * 
	 * Đảm bảo rằng bạn đã cấu hình kết nối cơ sở dữ liệu một cách an toàn, bao gồm
	 * việc sử dụng các thông số kết nối an toàn, giới hạn quyền truy cập và bảo vệ
	 * mật khẩu cơ sở dữ liệu.
	 * 
	 * Lưu ý rằng một số biện pháp bảo mật có thể cần thiết tùy thuộc vào môi trường
	 * và yêu cầu cụ thể của ứng dụng của bạn.
	 */
    /*================================================= END :Truy vấn có thể tăng tính bảo mật()====================================================== */
    
}
