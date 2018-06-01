package com.p.library.widget;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.p.library.R;
import com.p.library.utils.LogUtil;
import com.p.library.utils.PermissionUtil;
import com.p.library.utils.ToastUtil;

import java.io.File;

/**
 * 选择图片
 * Created by ZQL on 2017/7/30.
 */
public class ChooseImageDialog {


    public static void showDialog(final Activity activity, final File file, final int REQUEST_CAMERA,
                                  final int REQUEST_CHOOSE_IMAGE) {
        String[] items = activity.getResources().getStringArray(R.array.choose_image);

        ListAdapter listAdapter = new ArrayAdapter<String>(activity, R.layout.item_text_view, items);
        new AlertDialogBuilder(activity).setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (PermissionUtil.requestPermission(activity, Manifest.permission.CAMERA)) {
                        startCamera(activity, file, REQUEST_CAMERA);
                    }
                } else if (which == 1) {
                    if (PermissionUtil.requestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        chooseImage(activity, REQUEST_CHOOSE_IMAGE);
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }


    public static void onlyCamera(Activity activity, File file, int REQUEST_CAMERA) {
        startCamera(activity, file, REQUEST_CAMERA);
    }

    /**
     * 照相获取图片
     */
    public static void startCamera(Activity activity, File file, int REQUEST_CAMERA) {
//        if (!FileUtil.existsSdcard()) {
//            return;
//        }
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                uri = Uri.fromFile(file);
            } else {
                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            ToastUtil.showToast("相机打开出错，请检查相机访问权限是否开启");
        }

    }

    public static void chooseImage(Activity activity, int REQUEST_CHOOSE_IMAGE) {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            activity.startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
        } catch (Exception e) {
            LogUtil.w("chooseImage1", e);
            openPhotosBrowser(activity, REQUEST_CHOOSE_IMAGE);
        }
    }


    private static void openPhotosBrowser(Activity activity, int REQUEST_CHOOSE_IMAGE) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
            intent.setType("image/*"); //
            Intent wrapperIntent = Intent.createChooser(intent, null);
            activity.startActivityForResult(wrapperIntent, REQUEST_CHOOSE_IMAGE);
        } catch (Exception e) {
            LogUtil.w("chooseImage2", e);
            ToastUtil.showToast("打开图库失败，请稍候重试");
        }
    }


/*    public static void showDialog(final Fragment fragment, final File file, final int REQUEST_CAMERA,
                                  final int REQUEST_CHOOSE_IMAGE) {
        String[] items = fragment.getResources().getStringArray(R.array.choose_image);

        ListAdapter listAdapter = new ArrayAdapter<String>(fragment.getContext(), R.layout.item_text_view, items);
        new AlertDialogBuilder(fragment.getContext()).setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (PermissionUtil.requestPermission(fragment, Manifest.permission.CAMERA)) {
                        startCamera(fragment, file, REQUEST_CAMERA);
                    }
                } else if (which == 1)
                    chooseImage(fragment, REQUEST_CHOOSE_IMAGE);
                else {
                    dialog.dismiss();
                }
            }
        }).show();
    }*/


    public static void startCamera(Fragment fragment, File file, int REQUEST_CAMERA) {
//        if (!FileUtil.existsSdcard()) {
//            return;
//        }
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                uri = Uri.fromFile(file);
            } else {
                uri = FileProvider.getUriForFile(fragment.getContext(), fragment.getContext().getPackageName() + ".provider", file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            fragment.startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            ToastUtil.showToast("相机打开出错，请检查相机访问权限是否开启");
        }

    }

    public static void chooseImage(Fragment fragment, int REQUEST_CHOOSE_IMAGE) {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            fragment.startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
        } catch (Exception e) {
            LogUtil.w("chooseImage1", e);
            openPhotosBrowser(fragment, REQUEST_CHOOSE_IMAGE);
        }
    }


    private static void openPhotosBrowser(Fragment fragment, int REQUEST_CHOOSE_IMAGE) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
            intent.setType("image/*"); //
            Intent wrapperIntent = Intent.createChooser(intent, null);
            fragment.startActivityForResult(wrapperIntent, REQUEST_CHOOSE_IMAGE);
        } catch (Exception e) {
            LogUtil.w("chooseImage2", e);
            ToastUtil.showToast("打开图库失败，请稍候重试");
        }
    }

}
