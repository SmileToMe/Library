package com.p.library.utils;

/**
 * 表情 utils
 *
 * @author JH
 * @since 2017/6/29
 */
public class EMOJIFilterUtil {
    /**
     * 是否包含表情
     *
     * @return 如果不包含 返回false,包含 则返回true
     */
    private static boolean isEMOJICharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 是否存在表情
     * 已有提示语
     *
     * @return true 存在表情
     */
    public static boolean existEmoji(String source) {
        int len = source.length();
        boolean exit = false;
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEMOJICharacter(codePoint)) {// 如果不包含 则将字符append
                exit = true;
                break;
            }
        }
        if (exit)
            ToastUtil.showToast("不支持表情符号输入");
        return exit;
    }

    public static String replaceEMOJI(String source) {
        char empty = '\0';
        for (int i = 0; i < source.length(); i++) {
            char codePoint = source.charAt(i);
            if (isEMOJICharacter(codePoint)) {// 如果不包含 则将字符append
                source = source.replace(codePoint, empty);
            }
        }
        return source;
    }
}
