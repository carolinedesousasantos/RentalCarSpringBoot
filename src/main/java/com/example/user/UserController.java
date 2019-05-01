package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @PostMapping("create-account")
    public Map<String, Object> createAccount(@RequestParam String name, @RequestParam String surname,
                                             @RequestParam String email, @RequestParam String password,
                                             @RequestParam String confirmedpassword) {
        HashMap<String, Object> answer = new HashMap<>();
        try {
            if (PasswordValidator.validate(password)) {
                String msg = PasswordValidator.checkPasswords(password, confirmedpassword);
                if (msg.isEmpty()) {
                    User user = new User();
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);
                    user.setPassword(PasswordValidator.encryptPassword(password));

                    Collection<User> usersByEmail = userRepository.findEmail(email);
                    if(usersByEmail.isEmpty()){
                        userRepository.saveAndFlush(user);
                    }else{
                        answer.put("error", true);
                        answer.put("msg","Email already registered, please try another email.");
                    }
                } else {
                    answer.put("error", true);
                    answer.put("msg",msg);
                }
            } else {
                answer.put("msg", "La contraseña debe tener entre 8-20 caracteres de longitud y combinación de números, letras y caracteres especiales.");
                answer.put("error", true);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return answer;
    }
    @PostMapping("login")
    public Map<String,Object> login(@RequestParam String email, @RequestParam String password){
        HashMap<String,Object> answer = new HashMap<>();
        String hashedPassword = PasswordValidator.encryptPassword(password);
        Collection<User> users = userRepository.checkingEmailAndPassword(email,hashedPassword);
        if(!users.isEmpty()){
            answer.put("error", false);
        }else{
            answer.put("error", true);
            answer.put("msg","Email e/o contraseña incorrecto/s");
        }
        return answer;
    }


}