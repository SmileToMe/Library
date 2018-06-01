package com.p.library.widget;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.p.library.ok.OkHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Glide 图片加载质量、 加载网络框架设为okHttp
 * Created by Jiang on 2016/9/22.
 * <p>
 */

public class GlideModuleM extends OkHttpGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new MyOkHttpUrlLoader.Factory());
    }


    private static class MyOkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

        private final Call.Factory client;

        MyOkHttpUrlLoader(Call.Factory client) {
            this.client = client;
        }

        @Override
        public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
            return new MyOkHttpStreamFetcher(client, model);
        }

        /**
         * The default factory for {@link com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader}s.
         */
        private static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
            //            private static volatile Call.Factory internalClient;
            private Call.Factory client;

            /**
             * Constructor for a new Factory that runs requests using a static singleton client.
             */
            Factory() {
//                this(getInternalClient());
                this(OkHttp.getInstance().getOkHttpClient());
            }


            /**
             * Constructor for a new Factory that runs requests using given client.
             *
             * @param client this is typically an instance of {@code OkHttpClient}.
             */
            Factory(Call.Factory client) {
                this.client = client;
            }

//            private static Call.Factory getInternalClient() {
//                if (internalClient == null) {
//                    synchronized (MyOkHttpUrlLoader.Factory.class) {
//                        if (internalClient == null) {
//                            internalClient = new OkHttpClient();
//                        }
//                    }
//                }
//                return internalClient;
//            }

            @Override
            public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
                return new MyOkHttpUrlLoader(client);
            }

            @Override
            public void teardown() {
                // Do nothing, this instance doesn't own the client.
            }
        }
    }


    private static class MyOkHttpStreamFetcher implements DataFetcher<InputStream> {
        private final Call.Factory client;
        private final GlideUrl url;
        private InputStream stream;
        private ResponseBody responseBody;
        private volatile Call call;

        MyOkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
            this.client = client;
            this.url = url;
        }

        @Override
        public InputStream loadData(Priority priority) throws Exception {
            Request.Builder requestBuilder = new Request.Builder().url(url.toStringUrl());
            for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
                String key = headerEntry.getKey();
                String value = headerEntry.getValue();
                if (checkNameAndValue(key, value))
                    requestBuilder.addHeader(key, value);

            }
            Request request = requestBuilder.build();
            Response response;
            call = client.newCall(request);
            response = call.execute();
            responseBody = response.body();
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            long contentLength = responseBody.contentLength();
            stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
            return stream;
        }

        @Override
        public void cleanup() {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignored
            }
            if (responseBody != null) {
                responseBody.close();
            }
        }

        @Override
        public String getId() {
            return url.getCacheKey();
        }

        @Override
        public void cancel() {
            Call local = call;
            if (local != null) {
                local.cancel();
            }
        }
    }


    private static boolean checkNameAndValue(String name, String value) {
        if (TextUtils.isEmpty(name))
            return false;
        for (int i = 0, length = name.length(); i < length; i++) {
            char c = name.charAt(i);

            if (c <= '\u001f' || c >= '\u007f') {
                return false;
            }
        }
        if (TextUtils.isEmpty(value))
            return false;
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                return false;
            }
        }
        return true;
    }
}
