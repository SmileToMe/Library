package com.p.library.ok;

/**
 * 加密 函数
 *
 * @author JH
 * @since 3.0.0  2016/12/28
 */
public class S {

    static {
        try {
            System.loadLibrary("hushi-video");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }


    /**
     * @param temp1 time
     * @param temp2 token
     * @return String
     */
    public static native String getString(String temp1, String temp2);

    public static native String getString1();
}
