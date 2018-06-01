package com.p.library.ok;


import com.p.library.utils.LogUtil;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Jiang on 2016/4/27.
 * <p>
 */
public class RequestProgressBody extends RequestBody {

    //实际的待包装请求体
    private RequestBody mRequestBody;
    //进度回调接口
    private PostCallback.FileCallback mFileCallback;


    public RequestProgressBody(RequestBody requestBody, PostCallback.FileCallback fileCallback) {
        mRequestBody = requestBody;
        mFileCallback = fileCallback;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    /**
     * 重写进行写入
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        LogUtil.i("writeTo ");
        CountingSink countingSink = new CountingSink(sink); //包装完成的BufferedSink
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();  //必须调用flush，否则最后一部分数据可能不会被写入
    }

    /**
     * 重写调用实际的响应体的contentLength
     */
    @Override
    public long contentLength() {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            LogUtil.w("RequestProgressBody", e);
            return -1;
        }
    }

    /**
     * 包装
     */
    private final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;   //当前写入字节数
        private long contentLength = 0;  //总字节长度，避免多次调用contentLength()方法
        private long lastRefreshUiTime;  //最后一次刷新的时间
        private long lastWriteBytes;     //最后一次写入字节数据


        public CountingSink(Sink sink) {
            super(sink);
        }


        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (contentLength <= 0) contentLength = contentLength(); //获得contentLength的值，后续不再调用
            bytesWritten += byteCount;

            long curTime = System.currentTimeMillis();
            //每200毫秒刷新一次数据
            if (curTime - lastRefreshUiTime >= 200 || bytesWritten == contentLength) {
                //计算下载速度
                long diffTime = (curTime - lastRefreshUiTime) / 1000;
                if (diffTime == 0) diffTime += 1;
                long diffBytes = bytesWritten - lastWriteBytes;
                long networkSpeed = diffBytes / diffTime;
                final int progressNow = (int) (bytesWritten * 99 / contentLength);
                LogUtil.i("progressNow " + progressNow);
                OkHttp.getInstance().postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        mFileCallback.onProgress(progressNow);
                    }
                });
                lastRefreshUiTime = System.currentTimeMillis();
                lastWriteBytes = bytesWritten;

            }

        }
    }
}