package com.outlook.app.contoller;

import com.outlook.app.Config.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Controller
public class IndexController {
  Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

  @RequestMapping("/index")
  public String index(final Map<String, Object> model, HttpServletRequest request) {
    UUID state = UUID.randomUUID();
    UUID nonce = UUID.randomUUID();

    // Save the state and nonce in the session so we can
    // verify after the auth process redirects back
    HttpSession session = request.getSession();
    session.setAttribute("expected_state", state);
    session.setAttribute("expected_nonce", nonce);

    String loginUrl = AuthHelper.getLoginUrl(state, nonce);
    System.out.println(loginUrl);
    model.put("loginUrl", loginUrl);
    return "index";
  }
}