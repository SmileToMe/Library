package com.p.library.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

    /**
     * 加密
     *
     * @param src 加密字符串
     * @param key 密钥
     * @return 加密后的字符串
     */
    public static String Encrypt(String src, String key) throws Exception {
        // 判断密钥是否为空
        if (key == null) {
            System.out.print("密钥不能为空");
            return null;
        }

        // 密钥补位
        int plus = 16 - key.length();
        byte[] data = key.getBytes("utf-8");
        byte[] raw = new byte[16];
        byte[] plusbyte = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        for (int i = 0; i < 16; i++) {
            if (data.length > i)
                raw[i] = data[i];
            else
                raw[i] = plusbyte[plus];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 算法/模式/补码方式
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes("utf-8"));

        return Base64.encode(encrypted);//base64
//        return binary(encrypted, 16); //十六进制
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);    // 这里的1代表正数
    }


    /**
     * 解密
     *
     * @param src 解密字符串
     * @param key 密钥
     * @return 解密后的字符串
     */
    public static String Decrypt(String src, String key) throws Exception {
        try {
            // 判断Key是否正确
            if (key == null) {
                System.out.print("Key为空null");
                return null;
            }

            // 密钥补位
            int plus = 16 - key.length();
            byte[] data = key.getBytes("utf-8");
            byte[] raw = new byte[16];
            byte[] plusbyte = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
            for (int i = 0; i < 16; i++) {
                if (data.length > i)
                    raw[i] = data[i];
                else
                    raw[i] = plusbyte[plus];
            }

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] encrypted1 = new Base64().decode(src);//base64
//            byte[] encrypted1 = toByteArray(src);//十六进制

            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        if (hexString.isEmpty())
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 执行解码并生成解码后文件
     *
     * @param sourceFile 加密文件
     * @param targetFile 解密后生成文件
     **/
    public static void decryptFile(String sourceFile, String targetFile) {
        // 密钥
        String key = "smbk20160101shao";

        try {
            File file = new File(targetFile);
            if (file.exists())
                file.delete();
            file.createNewFile();

            // read net source file to local target file
            URL url = new URL(encodeUrlToUTF8(sourceFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                writeFile(targetFile, Decrypt(changeString(s), key));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Url中的中文转换
     */
    public static String encodeUrlToUTF8(String url) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            String s = url.substring(i, i + 1);
            byte[] bytes = s.getBytes("UTF-8");
            // 中文字符是2个字节，符号和英文为1个字节
            if (bytes.length == 1) {
                if (bytes[0] == ' ')
                    sb.append("%20");
                else
                    sb.append(s);
            } else {
                sb.append(URLEncoder.encode(s, "UTF-8"));
            }
        }
        return sb.toString();
    }

    /**
     * 除去乱码
     */
    private static String changeString(String str) {
        String str_Result = "", str_OneStr = "";
        for (int z = 0; z < str.length(); z++) {
            str_OneStr = str.substring(z, z + 1);
            if (!str_OneStr.matches("[\u4e00-\u9fa5]+")) {
                if (str_OneStr.matches("[\\x00-\\x7F]+")) {
                    str_Result = str_Result + str_OneStr;
                }
            }
        }
        return str_Result;
    }

    /**
     * 将内容写入输出文件
     */
    public static void writeFile(String fileName, String content) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        fw.write(content);
        //刷新缓冲区
        fw.flush();
        //关闭文件流对象
        fw.close();
    }

}
