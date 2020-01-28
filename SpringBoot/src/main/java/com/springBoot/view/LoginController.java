package com.springBoot.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springBoot.payload.JwtAuthenticationResponse;
import com.springBoot.payload.LoginRequest;
import com.springBoot.repository.RoleRepository;
import com.springBoot.repository.UserRepository;
import com.springBoot.security.JwtTokenProvider;

@Controller
public class LoginController {
	
	@Autowired
	private LocalValidatorFactoryBean validator;

	public static final String ATTR_USER_ACCOUNT_DATA = "login";

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;
	 
	 @InitBinder(ATTR_USER_ACCOUNT_DATA)
	    public void initBinder(WebDataBinder binder){
	      
	      /* The default validator do not need to set. It can take effect automatically by default. 
	       * If set the default validator here, the web page will show two same error messages.
	       * */
	        binder.setValidator(validator);

	        // The custom validator need to be set or add here to take effect.
	      //  binder.setValidator(customUserAccountValidator);

	        //binder.addValidators(customUserAccountValidator);

	        /* Register form object java.util.Date, Integer and Float type property editor. 
	           The first parameter is property data type class, the second parameter is related property editor instance object.
	        */
//	        binder.registerCustomEditor(Date.class, new DateEditor());
//	        binder.registerCustomEditor(Integer.class, new AgeEditor());
//	        binder.registerCustomEditor(Float.class, new SalaryEditor());
	        
	    }
	
    @GetMapping({"/hello"})
    public String hello(Model model, @RequestParam(value="name", required=false, defaultValue="World") String name) {
        model.addAttribute("name", name);
        return "hello";
    }
    
    @GetMapping({"/"})
    public String loginPage(Model model, LoginRequest loginRequest) {  	
    	model.addAttribute(ATTR_USER_ACCOUNT_DATA, loginRequest);
    	return "login";
    }
    
    @PostMapping("/signin")
  
    public String authenticateUser(Model model, @ModelAttribute(ATTR_USER_ACCOUNT_DATA) @Validated @RequestBody LoginRequest loginRequest, BindingResult result) {
	
    	System.out.println(loginRequest.getUsernameOrEmail());
    	//model.addAttribute("login", loginRequest);
    	
    	 if(result.hasErrors()){
           return  "login";
         }
    	 
    	 Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         loginRequest.getUsernameOrEmail(),
                         loginRequest.getPassword()
                 )
         );

         SecurityContextHolder.getContext().setAuthentication(authentication);

         String jwt = tokenProvider.generateToken(authentication);
         System.out.println(ResponseEntity.ok(new JwtAuthenticationResponse(jwt)));
         return "hello"; 	
    }
}
