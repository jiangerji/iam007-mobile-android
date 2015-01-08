package cn.iam007;

import com.lidroid.xutils.exception.HttpException;

public class HttpExceptionButFoundCache extends HttpException {

    private static final long serialVersionUID = -4985609743532404967L;

    public HttpExceptionButFoundCache(HttpException exception) {
        super(exception);
    }
}
