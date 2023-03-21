package com.m1a2st.simplebackendpractice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Author m1a2st
 * @Date 2023/3/21
 * @Version v1.0
 */
@Component
public class ResponseUtils {
    public static void responseJsonWriter(HttpServletResponse response, String msg) throws IOException {
        send(response, msg);
    }

    public static void responseJsonWriter(HttpServletResponse response, Map<String, String> msg) throws IOException {
        send(response, msg);
    }

    private static void send(HttpServletResponse response, Object obj) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(obj);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }
}