package nudt.web.anno;


//用户自己定义的一个异常
public class UserNotExistException extends RuntimeException {


    public UserNotExistException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
