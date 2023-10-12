package com.example.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ValidateException extends Exception {
	// Phương thức kiểm tra chuỗi có ký tự đặc biệt
	public boolean isValidInput(String input) {
	    String regex = "^[a-zA-Z0-9]*$"; // Cho phép chữ cái và số
	    return input.matches(regex);
	}
	
	/*
	 * có extends Exception để dùng : // Xử lý ngoại lệ tùy chỉnh
	 * 
	 * @ExceptionHandler(ValidateException.class) public String
	 * handleValidateException(ValidateException ex, Model model) {
	 * model.addAttribute("error", "Validation error: " + ex.getMessage()); return
	 * "error"; } bên package com.example.controller;
	 */
	
}
