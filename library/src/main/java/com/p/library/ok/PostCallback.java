package com.p.library.ok;

/**
 * Created by Jiang on 2016/3/8.
 * <p/>
 */
public interface PostCallback<T> {

    void onSuccess(T obj);

    void onFailure(String msg);


    interface FileCallback<T> extends PostCallback<T> {
        void onProgress(int progress);
    }
}
