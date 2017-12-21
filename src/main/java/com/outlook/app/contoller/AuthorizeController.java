package com.outlook.app.contoller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.outlook.app.Entity.OutlookUser;
import com.outlook.app.service.OutlookService;
import com.outlook.app.service.OutlookServiceBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.outlook.app.Config.AuthHelper;
import com.outlook.app.authorization.IdToken;
import com.outlook.app.authorization.TokenResponse;

@Controller
public class AuthorizeController {

  @RequestMapping(value = "/authorize", method = RequestMethod.POST)
  public String authorize(@RequestParam("code") String code, @RequestParam("id_token") String idToken,
                          @RequestParam("state") UUID state, HttpServletRequest request, Map<String,Object> model) {
    {
      // Get the expected state value from the session
      HttpSession session = request.getSession();
      UUID expectedState = (UUID) session.getAttribute("expected_state");
      UUID expectedNonce = (UUID) session.getAttribute("expected_nonce");

      // Make sure that the state query parameter returned matches
      // the expected state
      String error = null;
      if (state.equals(expectedState)) {
        IdToken idTokenObj = IdToken.parseEncodedToken(idToken, expectedNonce.toString());
        if (idTokenObj != null) {
          TokenResponse tokenResponse = AuthHelper.getTokenFromAuthCode(code, idTokenObj.getTenantId());
          session.setAttribute("tokens", tokenResponse);
          session.setAttribute("userConnected", true);
          session.setAttribute("userName", idTokenObj.getName());
          // Get user info
          OutlookService outlookService = OutlookServiceBuilder.getOutlookService(tokenResponse.getAccessToken(), null);
          OutlookUser user;
          try {
            user = outlookService.getCurrentUser().execute().body();
            session.setAttribute("userEmail", user.getMail());
          } catch (IOException e) {
            session.setAttribute("error", e.getMessage());
          }
          session.setAttribute("userTenantId", idTokenObj.getTenantId());
        } else {
          session.setAttribute("error", "ID token failed validation.");
        }
      } else {
        session.setAttribute("error", "Unexpected state returned from authority.");
//        error = "Unexpected state returned from authority.";
      }
//      model.put("authCode",code);
//      model.put("idToken",idToken);
//      model.put("session",session);
      return "base";
    }
  }

  @RequestMapping("/logout")
  public String logout(HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.invalidate();
    return "redirect:/index.html";
  }
}