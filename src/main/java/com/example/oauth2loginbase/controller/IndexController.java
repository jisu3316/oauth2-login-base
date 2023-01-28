package com.example.oauth2loginbase.controller;

import com.example.oauth2loginbase.model.PrincipalUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //인증 객체를 가져와 만약 null이 아니라면 유저네임을 출력한다.
    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal PrincipalUser principalUser) {

        String view = "/index";
        if (authentication != null) {

            String userName = principalUser.providerUser().getUsername();

            model.addAttribute("user", userName);
            model.addAttribute("provider", principalUser.providerUser().getProvider());
            if (!principalUser.providerUser().isCertificated()) view = "selfcert";
        }

        return view;
    }
}
