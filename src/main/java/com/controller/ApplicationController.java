package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.models.entity.Users;
import com.models.dataaccess.DataAccess;
import java.util.*;

import com.models.utility.Constants;
import com.models.WebsiteHandlers;

/**
 * Created by kdao on 11/21/15.
 */
@Controller
public class ApplicationController {

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String indexPage(ModelMap model) {

        model.addAttribute("message", "Log In");
        return "index";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public void logUserIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataAccess da = new DataAccess();
        // get request parameters for userID and password
        String username = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        Users user = new Users(username, pwd);
        try {
            user = da.getUser(user);
            if(user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user.getUsername());
                session.setAttribute("uid", user.getUid().toString());
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(30*60);
                Cookie uid = new Cookie("uid", user.getUid().toString());
                uid.setMaxAge(30 * 60);
                response.addCookie(uid);
                response.sendRedirect("tenant"); //Create tenant after login
            } else {
                throw new Exception("user doesnot exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/?error");
        }
    }

    @RequestMapping(value="/dashboard", method=RequestMethod.GET)
    public static String dashboard(HttpServletRequest request, HttpServletResponse response) {
        DataAccess db = new DataAccess();
        HttpSession session = request.getSession();
        HashMap<String,ArrayList<String>> allservices = new HashMap<String,ArrayList<String>>();
        ArrayList<String> webservices = new ArrayList<String>();
        ArrayList<String> dbservices = new ArrayList<String>();
        try {
            Cookie loginCookie = null;
            Cookie[] cookies = request.getCookies();
            int userid = 0;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("uid")) {
                        loginCookie = cookie;
                        userid = Integer.parseInt(loginCookie.getValue().toString());
                        break;
                    }
                }
            }
            if (userid == 0) {
                throw new Exception("Invalid user");
            } else {
                allservices = db.getAllServices(userid);
                if(allservices != null) {
                    webservices = allservices.get(Constants.WEB_SERVICE);
                    dbservices = allservices.get(Constants.DB_SERVICE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dashboard";
    }

    @RequestMapping(value="/tenant", method=RequestMethod.GET)
    public String tenant(ModelMap model) {
        WebsiteHandlers ws = new WebsiteHandlers();
        String id = ws.getTenantId();
        model.addAttribute("message", "Tenant");
        model.addAttribute("name", Constants.OPENSTACK_USER);
        model.addAttribute("id", id);
        return "tenant";
    }

}
