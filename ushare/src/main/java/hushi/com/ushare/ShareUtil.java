package hushi.com.ushare;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * 微信支付工具类
 *
 * @author JH
 * @since 2017/8/4
 */
public class ShareUtil {

    public static final String WX_KEY = "wxe9f13bda739084bc";
    public static final String WX_SECRET = "9050e8c8fcb013a42848fe7098a3339b";
//    public static final String WX_MCH_ID = "1383172702";         //  商户号

    private static final String QQ_ID = "1105506552";
    private static final String QQ_KEY = "dm4qIWt3ikVlqMmZ";

    private static final String WX_MIN = "gh_8ab1ade13870";

    //https://testeduwxopen.veehui.com/   https://eduwxopen.veehui.com/
    public static String getShareVideoPath(boolean isOnLine, long userId) {
        String next = "?fromUserId=" + (userId == 0 ? "null" : userId);
        return isOnLine ? "pages/share/share" + next : "pages/share/share" + next;
    }

    public static String getShareVideoPath(long userId, long videoId) {
        String next = "?fromUserId=" + (userId == 0 ? "null" : userId) + "&videoId=" + videoId;
        return "pages/share/share" + next;
    }

    public static String getSharePreviewPath(boolean isOnLine, long userId) {
        String next = "?fromUserId=" + (userId == 0 ? "null" : userId);
        return isOnLine ? "pages/index/index" + next : "pages/index/index" + next;
    }

    private static final String START_URL = "https://eduwxopen.veehui.com/";

    /**
     * 微信分享
     */
    public static void share(final Activity activity, String title, String description, String webUrl, UMImage image, UMShareListener listener) {
        PlatformConfig.setWeixin(WX_KEY, WX_SECRET);
//        PlatformConfig.setQQZone(QQ_ID, QQ_KEY);
        UMWeb web = new UMWeb(webUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        final ShareAction shareAction = new ShareAction(activity);
        shareAction.withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    boolean install = UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN);
                    if (!install) {
                        showToast(activity, R.string.un_install_wx);
                        return;
                    }
                    shareAction.setPlatform(share_media);
                    shareAction.share();
                }
            }
        }).setCallback(listener).open();
    }

    /**
     * 微信分享
     */
    public static void shareImg(final Activity activity, UMImage image, UMShareListener listener) {
        PlatformConfig.setWeixin(WX_KEY, WX_SECRET);
//        PlatformConfig.setQQZone(QQ_ID, QQ_KEY);
        final ShareAction shareAction = new ShareAction(activity);
        shareAction.withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    boolean install = UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN);
                    if (!install) {
                        showToast(activity, R.string.un_install_wx);
                        return;
                    }
                    shareAction.setPlatform(share_media);
                    shareAction.share();
                }
            }
        }).setCallback(listener).open();
    }


    private static void showToast(Context context, int stringId) {
        Toast toast = Toast.makeText(context, stringId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void shareWx(Activity activity, boolean isWx,
                               String title, String description,
                               String webUrl, UMImage image, UMShareListener listener) {
        PlatformConfig.setWeixin(WX_KEY, WX_SECRET);
        boolean install = UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN);
        if (!install) {
            showToast(activity, R.string.un_install_wx);
            return;
        }
        UMWeb web = new UMWeb(webUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        new ShareAction(activity).withMedia(web).setPlatform(isWx ? SHARE_MEDIA.WEIXIN : SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(listener).share();
    }


    /**
     * 微信分享小程序 带弹出框
     */
    public static void shareWxMinPlatform(final Activity activity, String title, String description, String webUrl, UMImage image, UMShareListener listener) {
        PlatformConfig.setWeixin(WX_KEY, WX_SECRET);
        final ShareAction shareAction = new ShareAction(activity);
        UMMin umMin = new UMMin(START_URL + webUrl);
        image.compressStyle = UMImage.CompressStyle.QUALITY;
        umMin.setThumb(image);
        umMin.setTitle(title);
        umMin.setDescription(description);
        umMin.setPath(webUrl);
        umMin.setUserName(WX_MIN);
        shareAction.withMedia(umMin).setDisplayList(SHARE_MEDIA.WEIXIN/*, SHARE_MEDIA.WEIXIN_CIRCLE*/).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.WEIXIN /*|| share_media == SHARE_MEDIA.WEIXIN_CIRCLE*/) {
                    boolean install = UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN);
                    if (!install) {
                        showToast(activity, R.string.un_install_wx);
                        return;
                    }
                    shareAction.setPlatform(share_media);
                    shareAction.share();
                }
            }
        }).setCallback(listener).open();
    }


    /**
     * 分享到微信小程序
     */
    public static void shareWxMin(Activity activity, String title, String description,
                                  String webUrl, UMImage image, UMShareListener listener) {
        PlatformConfig.setWeixin(WX_KEY, WX_SECRET);
        boolean install = UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN);
        if (!install) {
            showToast(activity, R.string.un_install_wx);
            return;
        }
        UMMin umMin = new UMMin(START_URL + webUrl);
        image.compressStyle = UMImage.CompressStyle.QUALITY;
        umMin.setThumb(image);
        umMin.setTitle(title);
        umMin.setDescription(description);
        umMin.setPath(webUrl);
        umMin.setUserName(WX_MIN);
        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(listener)
                .share();
    }

}
