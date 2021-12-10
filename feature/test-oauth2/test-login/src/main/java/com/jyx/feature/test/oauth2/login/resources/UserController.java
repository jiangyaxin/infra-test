package com.jyx.feature.test.oauth2.login.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author JYX
 * @since 2021/12/9 17:13
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping
    public Principal user(Principal principal) {
        return principal;
    }
}
