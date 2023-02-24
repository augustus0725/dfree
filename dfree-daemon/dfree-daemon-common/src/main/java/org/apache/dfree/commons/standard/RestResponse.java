package org.apache.dfree.commons.standard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangcanbin@hongwangweb.com
 * @date 2022/5/30 17:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Slf4j
public class RestResponse<T> {

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private int code;
    private T data;
    private String message;

    private boolean success;

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<T>()
                .setSuccess(true)
                .setCode(HttpStatus.OK.value())
                .setMessage("success")
                .setData(data);
    }

    public static <T> RestResponse<T> badRequest(T data, String msg) {
        return new RestResponse<T>()
                .setSuccess(false)
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(msg)
                .setData(data);
    }

    public static <T> RestResponse<T> error(T data, String msg) {
        return new RestResponse<T>()
                .setSuccess(false)
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(msg)
                .setData(data);
    }

    public static RestResponse<String> redirectToCasLogin(String casServerLoginUrl) {
        return new RestResponse<String>()
                .setSuccess(false)
                .setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage("No user is logged in or login has timed out.")
                .setData(casServerLoginUrl);
    }

    public static <T> void unauthorized(HttpServletResponse response, T data, String msg) {
        RestResponse<T> result = new RestResponse<T>()
                .setSuccess(false)
                .setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage(msg)
                .setData(data);
        writeJsonToResponse(GSON.toJson(result), response);
    }

    public static <T> RestResponse<T> unauthorized(T data, String msg) {
        return new RestResponse<T>()
                .setSuccess(false)
                .setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage(msg)
                .setData(data);
    }

    public static void forbidden(HttpServletResponse response, String msg) {
        RestResponse<String> result = new RestResponse<String>()
                .setSuccess(false)
                .setCode(HttpStatus.FORBIDDEN.value())
                .setMessage("当前访问的资源无权限")
                .setData(StringUtils.defaultIfBlank(msg, "Access Denied!"));
        writeJsonToResponse(GSON.toJson(result), response);
    }

    public static void writeJsonToResponse(String json, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        byte[] bytes = StringUtils.defaultIfBlank(json, "{}")
                .getBytes(StandardCharsets.UTF_8);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(new String(bytes, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Write response fail. ", e);
        }
    }

}
