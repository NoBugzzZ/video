package cn.edu.nju.VideoBackend.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping("/")
    public void welcome(HttpServletResponse response) {
        try {
            response.getWriter().print("[success] welcome");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}