package pri.hzhu.file.common.model;

/**
 * @authod: pp_lan on 2021/10/26.
 */
public class Response<T> {

    private int code;

    private T data;

    private Page page;

    public Response(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> Response ok(T data) {
        return new Response(200, data);
    }

    public static <T> Response ok(T data, Page page) {
        Response response = new Response(200, data);
        response.setPage(page);
        return response;
    }

    public static Response error(String msg) {
        return new Response(500, msg);
    }

    public static Response error(Throwable e) {
        return new Response(500, e.getMessage());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
