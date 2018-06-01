package com.p.library.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.p.library.A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jiang on 2016/4/26.
 * <p/>
 */
public class FileUtil {

    public static File createDownloadFile(String fileName) {
        if (existsSdcard()) {
            // 已挂载
            File pic = new File(A.getInstance().getExternalCacheDir(), "download");
            if (!pic.exists() || !pic.isDirectory()) {
                pic.mkdirs();
            }
            return new File(pic, fileName);
        } else {
            File cacheDir = A.getInstance().getCacheDir();
            return new File(cacheDir, fileName);
        }

    }


    /**
     * @return file
     */
    public static File createTmpVoiceFile() {

        SimpleDateFormat format = TimeUtil.getSimpleFormat("yyyyMMdd_HHmmss_sss");
        String fileName = format.format(System.currentTimeMillis()) + String.valueOf(System.currentTimeMillis()).substring(8) + ".amr";
        Context context = A.getInstance();
        if (existsSdcard()) {
            // 已挂载
            File pic = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "amr");

            if (!pic.exists() || !pic.isDirectory()) {
                pic.mkdirs();
            }
            return new File(pic, fileName);
        } else {
            File cacheDir = new File(context.getCacheDir(), "amr");

            if (!cacheDir.exists() || !cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
            return new File(cacheDir, fileName);
        }

    }

    public static File createTmpFile() {
        if (existsSdcard()) {
            // 已挂载
            File pic = new File(A.getInstance().getExternalCacheDir(), "images");
            if (!pic.exists() || !pic.isDirectory()) {
                pic.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsssss", Locale.CHINA).format(new Date());
            String fileName = "image_" + timeStamp + "";
            return new File(pic, fileName + ".jpg");
        } else {
            File cacheDir = A.getInstance().getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsssss", Locale.CHINA).format(new Date());
            String fileName = "image_" + timeStamp + "";
            return new File(cacheDir, fileName + ".jpg");
        }

    }

    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getFilePath(Uri uri) {
        String empty = "";
        if (uri == null)
            return empty;
        Context context = A.getInstance();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        String st8 = "找不到图片";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (picturePath == null || picturePath.equals("null")) {
                ToastUtil.showToast(st8);
                return empty;
            }
            File file = new File(picturePath);
            if (!file.exists()) {
                ToastUtil.showToast(st8);
                return empty;
            }
            return picturePath;
        } else {
            File file = new File(uri.getPath());
            if (!file.exists()) {
                ToastUtil.showToast(st8);
                return empty;
            }
            return file.getAbsolutePath();
        }
    }

    public static void clearCache(Context context) {
        deleteFile(context.getExternalCacheDir());
        deleteFile(context.getCacheDir());
    }

    private static void deleteFile(File file) {
        if (file == null)
            return;
        if (file.exists() && file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (File childFile : childFiles) {
                deleteFile(childFile);
            }
//            file.delete();
        }
    }






    /**
     * 压缩图片
     */
    public static File transImage(File file) {
        int width = 720;
        int height = 1280;
        File transFile = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 设置inJustDecodeBounds为true
            opts.inJustDecodeBounds = true;
            // 使用decodeFile方法得到图片的宽和高
            BitmapFactory.decodeFile(file.getPath(), opts);

            opts.inJustDecodeBounds = false;
            int bitmapWidth = opts.outWidth;
            int bitmapHeight = opts.outHeight;
            // 缩放图片的尺寸
            int scale = 1;
            if (width > height) {
                if (bitmapHeight > height) {
                    scale = bitmapHeight / height;
                }
            } else {
                if (bitmapWidth > width) {
                    scale = bitmapWidth / width;
                }
            }
            if (scale <= 0)
                scale = 1;
            if (scale > 3)
                scale = 3;
            // 产生缩放后的Bitmap对象
            opts.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);

            float scaleF = 1; //宽高缩放比例
            bitmapHeight = bitmap.getHeight();
            bitmapWidth = bitmap.getWidth();
            if (width > height) {
                if (bitmapHeight > height) {
                    scaleF = (float) height / bitmapHeight;
                }
            } else {
                if (bitmapWidth > width) {
                    scaleF = (float) width / bitmapWidth;
                }
            }
            float scaleH = 1;
            if (bitmapHeight > 3900) {
                scaleH = 3900F / bitmapHeight;
            }
            scaleF = Math.min(scaleF, scaleH);
            Matrix matrix = new Matrix();
            matrix.postScale(scaleF, scaleF);
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            // save file
            transFile = createTmpFile();
            FileOutputStream out = new FileOutputStream(transFile);
            int opt = 100; //质量
//            if (bitmapWidth >= 2000 || bitmapHeight >= 2000)
//                opt = 92;
            LogUtil.i("scale:" + scale + "scaleF:" + scaleF + "opt :" + opt);
            if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, opt, out)) {
                out.flush();
                out.close();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();//记得释放资源，否则会内存溢出
            }
            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        if (transFile == null)
            transFile = file;
        return transFile;
    }


    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
    public static String getTotalCacheSize(Context context) {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (FileUtil.existsSdcard()) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    private static long getFolderSize(File file) {
        long size = 0;
        if (file == null || !file.exists())
            return size;
        try {
            if (file.isFile() && file.exists())
                return file.length();
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                if (listFile == null || !listFile.exists())
                    continue;
                if (listFile.isDirectory()) {
                    size += getFolderSize(listFile);
                } else {
                    size += listFile.length();
                }
            }
        } catch (Exception e) {
            LogUtil.w("getFolderSize:", e);
        }
        return size;

    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


//    public static void WriteStringToFile(String content) {
//        try {
//            File file = new File(A.getInstance().getExternalCacheDir(), "hospital.txt");
//            FileWriter fw = new FileWriter(file, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.newLine();
//            bw.write(content);
//            bw.close();
//            fw.close();
//
//
////            FileOutputStream fileOutputStream = new FileOutputStream(file);
////            PrintStream ps = new PrintStream(fileOutputStream);
////            ps.append(content);// 往文件里写入字符串
////            fileOutputStream.close();
////            ps.close();
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
//    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

}
