package com.example.exception;

import org.springframework.dao.DataAccessException;

public class CustomDataAccessException extends DataAccessException {

    public CustomDataAccessException(String msg) {
        super(msg);
    }

    public CustomDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
	/*
	 * Để sử dụng một lớp tùy chỉnh để xử lý ngoại lệ, bạn có thể cài đặt một
	 * ExceptionTranslator tùy chỉnh và gán nó cho NamedParameterJdbcTemplate. Trong
	 * trường hợp này, bạn không cần tạo CustomSQLErrorCodeTranslator, nhưng nếu bạn
	 * muốn tùy chỉnh xử lý lỗi cụ thể cho cơ sở dữ liệu, bạn có thể tạo một
	 * SQLExceptionTranslator
	 * 
	import org.springframework.dao.support.PersistenceExceptionTranslator;
	import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
	import org.springframework.stereotype.Repository;
	
	@Repository
	public class EmployeeRepository {
	
	    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	    public EmployeeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
	        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	        
	        // Sử dụng một ExceptionTranslator tùy chỉnh nếu cần
	        namedParameterJdbcTemplate.setExceptionTranslator(new CustomPersistenceExceptionTranslator());
	    }
	
	    // Các phương thức khác
	
	    private static class CustomPersistenceExceptionTranslator implements PersistenceExceptionTranslator {
	
	        @Override
	        public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
	            if (ex instanceof SQLException) {
	                // Xử lý ngoại lệ SQL cụ thể ở đây
	                return new CustomDataAccessException("Custom Data Access Exception occurred", ex);
	            }
	            return null;
	        }
	    }
	}

	 */
}
