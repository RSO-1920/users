package si.fri.rso.lib;

public class ResponseDTO {

    private Integer status;
    private String message;
    private Object data;

    public ResponseDTO(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

}
