package cn.yhq.http.core.interceptor;

import java.io.IOException;

import cn.yhq.http.core.CacheStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自定义缓存处理的拦截器
 *
 * Created by Yanghuiqiang on 2016/10/14.
 */
public class HttpRequestCacheInterceptor implements Interceptor {
    private CacheStrategy cacheStrategy;
    private int cacheMaxStale;

    public void setCacheStrategy(CacheStrategy cacheStrategy, int cacheMaxStale) {
        this.cacheStrategy = cacheStrategy;
        this.cacheMaxStale = cacheMaxStale;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String cacheHeaderName = "Cache-Control";
        String cacheHeaderValue = "max-stale=" + this.cacheMaxStale;
        switch (cacheStrategy) {
            case BOTH:
                break;
            case NOCACHE:
                cacheHeaderValue = "no-cache";
                break;
            case ONLY_CACHE:
                cacheHeaderValue = "only-if-cached";
                break;
            case ONLY_NETWORK:
                cacheHeaderValue = "no-cache";
                break;
            default:
                break;
        }
        request = request.newBuilder().removeHeader(cacheHeaderName)
                .addHeader(cacheHeaderName, cacheHeaderValue).build();
        Response response = chain.proceed(request);
        return response;
    }
}
