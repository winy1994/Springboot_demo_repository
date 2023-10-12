package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Employee;
import com.example.service.EmployeeService;

import com.example.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
//cho các phương thức khác
import java.time.ZonedDateTime;
import java.util.Collection;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    
    private ValidateException validateException;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.validateException = new ValidateException();
    }
	/*================= START : listEmployees =================== */
    @GetMapping("")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findAll();
        System.out.println(employees);
        model.addAttribute("employees", employees);
        return "list";
    }
    
	/*================= END : listEmployees  =======================*/
    
    /*================= START : searchEmployees  =================== */
    @PostMapping("/search")
    public String searchEmployees(@RequestParam String searchTerm, Model model) {
        List<Employee> searchResults = employeeService.searchEmployees(searchTerm);
        System.out.println(searchResults);
        model.addAttribute("searchResults", searchResults);
        return "search-results";
    }
    /*================= END : searchEmployees  =======================*/

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "view";
    }

    @GetMapping("/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "create";
    }
    
	/****************** có Exception */
    @PostMapping("/new")
    public String createEmployee(@ModelAttribute Employee employee, Model model) {
    	List<String> errorMessages = new ArrayList<>();
    	
    	
        // Kiểm tra firstName_entity
        if (!validateException.isValidInput(employee.getFirstName_entity())) {
            errorMessages.add("Họ không được chứa ký tự đặc biệt.");
        }

        // Kiểm tra lastName_entity
        if (!validateException.isValidInput(employee.getLastName_entity())) {
            errorMessages.add("Tên không được chứa ký tự đặc biệt.");
        }

        // Kiểm tra address_entity
        if (!validateException.isValidInput(employee.getAddress_entity())) {
            errorMessages.add("Địa chỉ không được chứa ký tự đặc biệt.");
        }
     // Kiểm tra age_entity (tuổi)
        if (employee.getAge_entity() < 0) {
            errorMessages.add("Tuổi không hợp lệ. Chỉ chấp nhận số nguyên dương.");
        }

     // Kiểm tra active_entity (trường "Active")
        Boolean activeValue = employee.getActive_entity();

        if (activeValue == null) {
            errorMessages.add("Vui lòng chọn trạng thái Active (Có hoặc Không).");
        }

        // Kiểm tra xem có lỗi nào không
        if (!errorMessages.isEmpty()) {
            // Có lỗi, trả về trang tạo nhân viên với danh sách thông báo lỗi
            model.addAttribute("errorMessages", errorMessages);
            return "create";
        }
        
        System.out.println("test lỗi");
        System.out.println(employee);
        // Nếu không có ký tự đặc biệt, thực hiện lưu thông tin nhân viên
        employeeService.newEmployee(employee);

        return "redirect:/employees";
    }
    
//    @PostMapping("/new")
//    public String createEmployee(@ModelAttribute Employee employee) {
//        // Lưu thông tin nhân viên
//        employeeService.newEmployee(employee);
//        return "redirect:/employees";
//    }

    
    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "edit";// Trả về trang chỉnh sửa thông tin của nhân viên
    }

    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, @ModelAttribute Employee employee) {
        // Thiết lập ID của nhân viên dựa trên đường dẫn
        employee.setId_entity(id);

        // Lưu thông tin nhân viên đã chỉnh sửa
        employeeService.update(employee);

        return "redirect:/employees";// Chuyển hướng đến danh sách nhân viên sau khi cập nhật
    }


    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
    	// Xóa nhân viên dựa trên ID
        employeeService.delete(id);
        
        return "redirect:/employees";// Chuyển hướng đến danh sách nhân viên sau khi xóa
    }
    /*================================================= START :Truy vấn Khác()====================================================== */
   
 // Xử lý ngoại lệ tùy chỉnh
    @ExceptionHandler(ValidateException.class)
    public String handleValidateException(ValidateException ex, Model model) {
        model.addAttribute("error", "Validation error: " + ex.getMessage());
        return "error";
    }
    /*================================================= END :Truy vấn Khác()====================================================== */
    
}
