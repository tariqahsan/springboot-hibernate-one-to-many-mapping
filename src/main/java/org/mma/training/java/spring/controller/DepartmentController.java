package org.mma.training.java.spring.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.mma.training.java.spring.exception.ResourceNotFoundException;
import org.mma.training.java.spring.model.Department;
import org.mma.training.java.spring.repository.DepartmentRepository;
import org.mma.training.java.spring.service.DepartmentService;
import org.mma.training.java.spring.util.ErrorMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DepartmentController {

	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	DepartmentService departmentService;

	@GetMapping("/departments")
	public ResponseEntity<List<Department>> getAllDepartments() {
		List<Department> departments = new ArrayList<>();
		try {
			departmentRepository.findAll().forEach(departments::add);
			if(departments.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/departments/{id}")
	public ResponseEntity<Department> getDepartmentsById(@PathVariable("id") long id) {
		Optional<Department> departmentsData = departmentRepository.findById(id);
		System.out.println("departmentsData : " + departmentsData.get().getName());
		if (departmentsData.isPresent()) {
			return new ResponseEntity<>(departmentsData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/department/add")
	public ResponseEntity postDepartment(@RequestBody @Valid final Department department, BindingResult bindingResult) {

		try {

			List<String> errorList = new ArrayList<>();
			List<ErrorMessage> errorMessages = new ArrayList<>();
			if (bindingResult.hasErrors()) {
				bindingResult.getFieldErrors().forEach(fieldError ->
				errorList.add(fieldError.getField() + ": " + messageSource.getMessage(fieldError, Locale.US))
						);
				bindingResult
				.getFieldErrors()
				.stream()
				.forEach(fieldError -> {            
					ErrorMessage errorMessage = new ErrorMessage(messageSource.getMessage(fieldError, Locale.US), fieldError.getField());           	
					System.out.println(errorMessage.getMessage());
					System.out.println(errorMessage.getFieldName());
					errorMessages.add(errorMessage);
				});
				return new ResponseEntity<>(errorMessages, HttpStatus.NOT_ACCEPTABLE);
		
			}

			Department departmentData = departmentRepository.save(department);
			return new ResponseEntity<>(departmentData, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/department/delete/{id}")
	public ResponseEntity<HttpStatus> deleteDepartment(@PathVariable("id") long id) {
		System.out.println("Deleting id -> " + id);
		try {
			departmentRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/department/delete-all")
	public ResponseEntity<HttpStatus> deleteAllDepartments() {
		System.out.println("Deleting all departments");
		try {
			departmentRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	// Updates article
	//  	@PutMapping(value= "/update", produces= { MediaType.APPLICATION_XML_VALUE })
	@PutMapping(value= "/department/update/id/{id}")
	public ResponseEntity<Department> updateDepartmentById(@PathVariable(value = "id") Long id, @RequestBody Department departmentData) throws ResourceNotFoundException, IllegalAccessException, InvocationTargetException {
		System.out.println("departmentData.getName() -> " + departmentData.getName());
		Optional<Department> departmentObj = departmentRepository.findById(id);
		System.out.println("departmentObj.get() -> " + departmentObj.get().getId() + " " + departmentObj.get().getName());
		Department department = departmentObj.orElseThrow(() -> new ResourceNotFoundException("ID not found for this Research Project :: " + id));
		System.out.println("1 department.getName() -> " + department.getName());
//		BeanUtils.copyProperties(department, departmentData);
		BeanUtils.copyProperties(departmentData, department);
		System.out.println("2 department.getName() -> " + department.getName());
		System.out.println("2 departmentData.getName() -> " + departmentData.getName());
		departmentRepository.save(department);

		return new ResponseEntity<>(null, HttpStatus.OK);
		//  		Department departmentObj = new Department();
		//  		BeanUtils.copyProperties(department, departmentObj);		
		//  		departmentService.updateDepartment(departmentObj);
		//  		
		//  		Department ob = new Department();
		//  		BeanUtils.copyProperties(departmentObj, ob);
		//  		return new ResponseEntity<Department>(ob, HttpStatus.OK);
	}

	//    @PutMapping("/department/update/id/{id}")
	//    public ResponseEntity<Department> updateDepartment(@PathVariable(value = "id") Integer id,
	//                                    @Valid @RequestBody DepartmentDto departmentDto) throws ResourceNotFoundException, IllegalAccessException, InvocationTargetException {
	//                    Optional<Department> departmentData = departmentRepository.findById(id);
	//                    Department department = departmentData.orElseThrow(() -> new ResourceNotFoundException("ID not found for this Research Project :: " + id));
	//
	//                    BeanUtils.copyProperties(department, departmentDto);
	//
	//                    departmentRepository.save(department);
	//
	//                    return new ResponseEntity<>(null, HttpStatus.OK);
	//    }

	//Updates article
	//  	@PutMapping(value= "article", produces= { MediaType.APPLICATION_XML_VALUE })
	//  	public ResponseEntity<ArticleInfo> updateArticle(@RequestBody ArticleInfo articleInfo) {
	//  		Article article = new Article();
	//  		BeanUtils.copyProperties(articleInfo, article);		
	//  		articleService.updateArticle(article);
	//  		
	//  		ArticleInfo ob = new ArticleInfo();
	//  		BeanUtils.copyProperties(article, ob);
	//  		return new ResponseEntity<ArticleInfo>(ob, HttpStatus.OK);
	//  	}
	@PutMapping("/departments/{id}")
	public ResponseEntity<Department> updateDepartment(@PathVariable(value = "id") Long departmentId, @RequestBody Department departmentDetails) {
		Optional<Department> department = departmentRepository.findById(departmentId);
		System.out.println(department.get().getName());
		//      @Valid @RequestBody Department employeeDetails) throws ResourceNotFoundException {
		//Department department = departmentRepository.findById(departmentId)
		//     .orElseThrow(() -> new ResourceNotFoundException("Department not found for this id :: " + departmentId));
		Department departmentObj = new Department();

		BeanUtils.copyProperties(department, departmentObj);
		//System.out.println(departmentObj.getEmployees().);
		final Department updatedDepartment = departmentRepository.save(departmentObj);
		return ResponseEntity.ok(updatedDepartment);
	}

}
