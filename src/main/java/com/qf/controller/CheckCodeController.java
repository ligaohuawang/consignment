package com.qf.controller;

import cn.dsna.util.images.ValidateCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/check")
public class CheckCodeController {

    /**
     * 验证码获取
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/code")
    public void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ValidateCode code = new ValidateCode(120, 40, 4, 10);
        System.out.println("1:"+code);
        String strCode = code.getCode();
        System.out.println("2"+strCode);
        request.getSession().setAttribute("chCode", strCode);
        code.write(response.getOutputStream());
    }
}
