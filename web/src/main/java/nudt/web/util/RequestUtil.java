package nudt.web.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static String getUserIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
