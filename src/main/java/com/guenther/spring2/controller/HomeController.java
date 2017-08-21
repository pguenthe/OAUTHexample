package com.guenther.spring2.controller;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


@Controller
public class HomeController {
//    @RequestMapping("/oauth1")
//    public ModelAndView oauth1() {
//        String baseURL = "https://github.com/login/oauth/authorize?client_id=";
//        String parameters = "&scope=user&state=12345&";
//        String redirectURL = "redirect_uri=http://localhost:8080/oauth2";
//
//        String url = baseURL + Credentials.CLIENT_ID +
//                parameters + redirectURL;
//
//        String result = "";
//
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet getRequest = new HttpGet(url);
//        //1. Make http request to GitHub OAuth web service to get access code
//        HttpResponse response;
//        try {
//            response = client.execute(getRequest);
//            HttpEntity entity = response.getEntity();
//
//            InputStream content = entity.getContent();
//            while (content.available() > 0) {
//                result += (char) content.read();
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//            return new ModelAndView("error", "errmsg", e.getStackTrace());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ModelAndView("error", "errmsg", e.getStackTrace());
//        }
//        return new ModelAndView("oauth1", "result", result);
//    }

    //step 1: Have the user login and get a code
    @RequestMapping("/")
    public ModelAndView oauth1() {
        System.out.println("Running / " + Credentials.CLIENT_ID );
        ModelAndView mv = new ModelAndView ("oauth1", "clientID",
                Credentials.CLIENT_ID);
        mv.addObject("redirect", "http://localhost:8080/oauth2");
        return mv;
    }

    @RequestMapping("/oauth2")
    public ModelAndView oauth2(@RequestParam("code") String code,
                               @RequestParam("state") String state) {
        System.out.println("Received code back: " + code);

        try {
            //step 2: exchange the code for a token

            //this HttpCLient will make requests from the other server
            HttpClient http = HttpClientBuilder.create().build();

            //Unlike the weather example, here we're POSTing for
            //  additional security
            // Setting the info GitHub needs
            //we're skipping the HttpHost here and doing it directly
            //note GitHub requires https
            HttpPost postPage = new HttpPost("https://github.com/login/oauth/access_token");

            ArrayList <NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("client_id", Credentials.CLIENT_ID));
            postParameters.add(new BasicNameValuePair("client_secret", Credentials.CLIENT_SECRET));
            postParameters.add(new BasicNameValuePair("code", code));//what we got back from GitHub
            //postParameters.add(new BasicNameValuePair("accept", "json"));//how we want the info
            postParameters.add(new BasicNameValuePair("accept", "JSON"));//how we want the info

            postPage.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

            //actually run it and pull in the response
            HttpResponse resp = http.execute(postPage);

            //get the actual content from inside the response
            String response = EntityUtils.toString(resp.getEntity());

            //I don't know why we're not getting JSON back
            //  but we can deal with this response too
            String [] pairs = response.split("&");
            String token = null;
            for (String pair : pairs) {
                String [] keyValue = pair.split("=");
                if (keyValue[0].equals("access_token")) {
                    token = keyValue[1];
                }
            }
            System.out.println("Token: " + token);

            int status = resp.getStatusLine().getStatusCode();
//            System.out.println("Status: " + status);
//            System.out.println("Blarg: " + resp.toString());
//            System.out.println("Response: " + response);


            //step 3: use the token to make authenticated requests
            //asking for the user info

            //couldn't get this to work with post for some reason
//            HttpPost postPage2 = new HttpPost("https://api.github.com/user");
//            ArrayList <NameValuePair> postParameters2 = new ArrayList<NameValuePair>();
//            postParameters2.add(new BasicNameValuePair("access_token", token));//pass the token back
//
//            postPage2.setEntity(new UrlEncodedFormEntity(postParameters2, "UTF-8"));
            //actually run it and pull in the response
//            HttpResponse resp2 = http.execute(postPage2);

            //so doing it with get instead
            HttpGet getPage = new HttpGet("https://api.github.com/user?access_token=" + token);
            HttpResponse resp2 = http.execute(getPage);

            int status2 = resp2.getStatusLine().getStatusCode();

            //get the actual content from inside the response
            String response2 = EntityUtils.toString(resp2.getEntity());

            JSONObject json = new JSONObject(response2);
            String username = json.getString("login");
            String name = json.getString("name");

            ModelAndView mv = new ModelAndView("oauth2", "code", code);
            mv.addObject ("status", status);
            mv.addObject ("response", response);
            mv.addObject("token", token);//for debugging
            mv.addObject ("status2", status2);
            mv.addObject("response2", response2);
            mv.addObject("username", username);
             mv.addObject("name", name);
            return mv;

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}