package com.p.library.ok;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.p.library.A;
import com.p.library.BuildConfig;
import com.p.library.R;
import com.p.library.en.Result;
import com.p.library.utils.DesUtil;
import com.p.library.utils.GsonUtil;
import com.p.library.utils.LogUtil;
import com.p.library.utils.NetUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * RxJava 版OkHttp
 *
 * @author JH
 * @since 3.0.0
 */
public class OkHttp {

    private OkHttpClient mOkHttpClient;

    private Handler mHandler;

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private OkHttp() {
        mOkHttpClient = new OkHttpClient.Builder().
                connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        LogUtil.i("OkHttp create");
    }

    private <T extends Result> Observable<T> post(final boolean returnAll, final String url, final BaseParam baseParams, final Type type) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                if (!NetUtils.checkNetConnect()) {
                    onError(e, A.getInstance().getString(R.string.error_https));
                } else {
                    try {
                        Request request = getPostRequest(url, baseParams);
                        Response response = mOkHttpClient.newCall(request).execute();

                        if (!response.isSuccessful()) {
                            onError(e, A.getInstance().getString(R.string.error_json) + response.code());
                            if (LogUtil.openLog) {
                                LogUtil.d(url + " " + baseParams + "\n" + response);
                            }
                        } else {
                            String responses = response.body().string();
                            if (LogUtil.openLog) {
                                LogUtil.d(url + " " + baseParams + "\nresponses:" + responses);
                            }
                            final T result = GsonUtil.fromJson(responses, type);
                            if (result == null) {
                                onError(e, A.getInstance().getString(R.string.error_json));
                            } else {
                                if (returnAll) { //返回全部类型
                                    if (result.getCode() == 401) { //401 请先登录后使用
                                        loginOther(result.getMessage());
                                        onError(e, "");
                                    } else {
                                        onNext(e, result);
                                    }
                                } else {
                                    if (result.getCode() == 401) {
                                        loginOther(result.getMessage());
                                        onError(e, "");
                                    } else if (result.getCode() != 1) {
                                        onError(e, result.getMessage());
                                    } else {
                                        onNext(e, result);
                                    }
                                }
                            }
                        }
                    } catch (Exception | AssertionError e1) {
                        onError(e, A.getInstance().getString(R.string.error_timeout));
                        if (LogUtil.openLog) {
                            LogUtil.d("OkHttp " + url + baseParams, e1);
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Request getPostRequest(String url, BaseParam baseParams) {
        FormBody.Builder paramBuilder = new FormBody.Builder();
        boolean online = A.getInstance().isOnline();
        if (!online) {
            baseParams.put("admin", "!QS#$^Tghi0");
        }
        Set<Map.Entry<String, String>> paramSet = baseParams.entrySet();
        for (Map.Entry<String, String> entry : paramSet) {
            if (online && TextUtils.equals(entry.getKey(), "params")) {
                String temp = DesUtil.encode(entry.getValue());
                paramBuilder.add(entry.getKey(), temp);
            } else {
                paramBuilder.add(entry.getKey(), entry.getValue());
            }
        }


        return new Request.Builder().url(url).headers(getHeaders())
                .post(paramBuilder.build()).build();
    }

    private <T> void onNext(ObservableEmitter<T> e, T result) {
        if (!e.isDisposed()) {
            if (result != null) {
                e.onNext(result);
            }
            e.onComplete();
        }
    }

    private <T> void onError(ObservableEmitter<T> e, String msg) {
        if (!e.isDisposed()) {
            try {
                if (msg == null)
                    msg = "";
                if (msg.length() > 20) {
                    msg = A.getInstance().getString(R.string.error_json) + "。";
                }
                e.onError(new HttpThrowable(msg));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * 返回Result 全部类型 不判断是否成功
     */
    public <T extends Result> Observable<T> postAll(final String url, final BaseParam baseParams, final Type type) {
        return post(true, url, baseParams, type);
    }

    /**
     * 返回Result getResult 类型 成功时返回
     */
    public <T extends Result> Observable<T> post(final String url, final BaseParam baseParams, final Type type) {
        return post(false, url, baseParams, type);
    }

    public Response downloadFile(final String url) {
        try {
            if (!NetUtils.checkNetConnect()) {
                return null;
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Call downloadFile(final String url, final String destFileDir,
                             final PostCallback.FileCallback<String> fileCallback) {
        if (!NetUtils.checkNetConnect()) {
            postFailure(fileCallback, A.getInstance().getString(R.string.error_http));
            return null;
        }
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            int progress = -1;

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.w(e.toString());
                postFailure(fileCallback, A.getInstance().getString(R.string.error_timeout));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    postFailure(fileCallback, response.body().string());
                    return;
                }
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    final long total = response.body().contentLength();
                    long sumNow = 0;
                    File file = new File(destFileDir);
                    if (file.exists())
                        file.delete();

                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        sumNow += len;
                        fos.write(buf, 0, len);
                        int now = (int) (sumNow * 99 / total);
                        if (progress != now) {
                            progress = now;
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    fileCallback.onProgress(progress);
                                }
                            });
                        }
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            fileCallback.onSuccess(destFileDir);
                        }
                    });
                } catch (IOException ex) {
                    postFailure(fileCallback, ex.toString());
                } finally {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                }
            }
        });
        return call;
    }

    private void postFailure(final PostCallback callback, final String msg) {
        if (callback == null)
            return;
        postRunnable(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(msg);
            }
        });
    }

    public void postFile(String url, File file, String fileType, final PostCallback.FileCallback<String> fileCallback) {
        MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=utf-8");
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .addFormDataPart("type", fileType);
        Set<Map.Entry<String, String>> paramsEntrySet = new BaseParam().entrySet();
        for (Map.Entry<String, String> entry : paramsEntrySet) {
            requestBody.addFormDataPart(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder().url(url).post(new RequestProgressBody(requestBody.build(), fileCallback)).build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.w("postFile onFailure", e);
                postFileFailure(fileCallback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responses = response.body().string();
                LogUtil.d("postFile :" + responses);
                if (!response.isSuccessful()) {
                    postFileFailure(fileCallback, responses);
                    return;
                }
                if (fileCallback == null)
                    return;
                final Result<String> result = GsonUtil.fromJson(responses, new TypeToken<Result<String>>() {
                }.getType());
                if (result == null) {
                    postFileFailure(fileCallback, A.getInstance().getString(R.string.error_json));
                    return;
                }
                if (result.getCode() != 1) {
                    postFileFailure(fileCallback, result.getMessage());
                    return;
                }
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        fileCallback.onSuccess(result.getResult());
                    }
                });
            }
        });
    }


    private Headers getHeaders() {
        Headers.Builder header = new Headers.Builder();
        header.add("version", BuildConfig.VERSION_NAME);
        header.add("os", "ANDROID");
        return header.build();
    }

    public void postRunnable(Runnable runnable) {
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(runnable);
    }

    /**
     * 在其他地方登陆
     */
    private void loginOther(final String msg) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                A.getInstance().logout(false);
            }
        });
    }

    private void postFileFailure(final PostCallback.FileCallback<String> fileCallback, final String msg) {
        postRunnable(new Runnable() {
            @Override
            public void run() {
                fileCallback.onFailure(msg);
            }
        });
    }

    private static class HttpThrowable extends Throwable {

        HttpThrowable(String detailMessage) {
            super(detailMessage);
        }
    }

    public static OkHttp getInstance() {
        return OkHttpHolder.instance;
    }

    private static class OkHttpHolder {
        private static final OkHttp instance = new OkHttp();
    }
}
