package org.mma.training.java.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mma.training.java.spring.model.User;
import org.mma.training.java.spring.repository.UserRepository;
import org.mma.training.java.spring.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = new ArrayList<>();
		try {
			userRepository.findAll().forEach(users::add);
			if(users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUsersById(@PathVariable("id") long id) {
		Optional<User> usersData = userRepository.findById(id);

		if (usersData.isPresent()) {
			return new ResponseEntity<>(usersData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
    
    @PostMapping(value = "/add")
	public ResponseEntity<User> postUser(@RequestBody User user) {
    	
		try {
			User userData = userRepository.save(user);
			return new ResponseEntity<>(userData, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
    
    @DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
    	System.out.println("Deleting id -> " + id);
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
    
    @DeleteMapping("/delete-all")
	public ResponseEntity<HttpStatus> deleteAllUsers() {
    	System.out.println("Deleting all users");
		try {
			userRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
    
    // Updates article
//  	@PutMapping(value= "/update", produces= { MediaType.APPLICATION_XML_VALUE })
    @PutMapping(value= "/update")
  	public ResponseEntity<User> updateUser(@RequestBody User user) {
  		System.out.println("User FirstName : " + user.getFirstName());
  		User userObj = new User();
  		BeanUtils.copyProperties(user, userObj);		
  		userService.updateUser(userObj);
  		
  		User ob = new User();
  		BeanUtils.copyProperties(userObj, ob);
  		return new ResponseEntity<User>(ob, HttpStatus.OK);
  	}
  	
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
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userDetails) {
    	Optional<User> user = userRepository.findById(userId);
    	System.out.println(user.get().getFirstName());
//      @Valid @RequestBody User employeeDetails) throws ResourceNotFoundException {
    	//User user = userRepository.findById(userId)
//     .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
 		User userObj = new User();
 		
  		BeanUtils.copyProperties(user, userObj);
  		System.out.println(userObj.getAddress().getZip4());
     final User updatedUser = userRepository.save(userObj);
     return ResponseEntity.ok(updatedUser);
    }

}
