package vr.com.apps.transactionLinking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class MainController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    public String main(HttpSession httpSession, ModelMap model) {
        model.addAttribute("message", "Transaction Linking!" );

        return "index";
    }

}