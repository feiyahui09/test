package com.zmax.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import com.zmax.app.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmotionUtils {

    public static final String[] EMOTIONS_STR = {"[单眼]", "[调皮]", "[嘟嘴]", "[愤怒]", "[哈哈]", "[害羞]", "[坏笑]", "[慌张]", "[惊讶]", "[开心]", "[可爱]",
            "[酷酷]", "[困困]", "[流汗]", "[魔鬼]", "[内年]", "[撇嘴]", "[气愤]", "[伤心]", "[生病]", "[失望]", "[叹气]", "[舔嘴]", "[微笑]", "[委屈]", "[喜欢]", "[笑着]",
            "[憎憎]", "[皱眉]"};
    public static final Pattern EMOTION_PATTERN = Pattern.compile("\\[(\\S+?)\\]");
    static HashMap<String, Integer> EMOTIONS_MAP = new HashMap<String, Integer>();

    static {
        EMOTIONS_MAP.put("单眼", R.drawable.e1);
        EMOTIONS_MAP.put("调皮", R.drawable.e2);
        EMOTIONS_MAP.put("嘟嘴", R.drawable.e3);
        EMOTIONS_MAP.put("愤怒", R.drawable.e4);
        EMOTIONS_MAP.put("哈哈", R.drawable.e5);
        EMOTIONS_MAP.put("害羞", R.drawable.e6);
        EMOTIONS_MAP.put("坏笑", R.drawable.e7);
        EMOTIONS_MAP.put("慌张", R.drawable.e8);
        EMOTIONS_MAP.put("惊讶", R.drawable.e9);
        EMOTIONS_MAP.put("开心", R.drawable.e10);
        EMOTIONS_MAP.put("可爱", R.drawable.e11);
        EMOTIONS_MAP.put("酷酷", R.drawable.e12);
        EMOTIONS_MAP.put("困困", R.drawable.e13);
        EMOTIONS_MAP.put("流汗", R.drawable.e14);
        EMOTIONS_MAP.put("魔鬼", R.drawable.e15);
        EMOTIONS_MAP.put("内年", R.drawable.e16);
        EMOTIONS_MAP.put("撇嘴", R.drawable.e17);
        EMOTIONS_MAP.put("气愤", R.drawable.e18);
        EMOTIONS_MAP.put("伤心", R.drawable.e19);
        EMOTIONS_MAP.put("生病", R.drawable.e20);
        EMOTIONS_MAP.put("失望", R.drawable.e21);
        EMOTIONS_MAP.put("叹气", R.drawable.e22);
        EMOTIONS_MAP.put("舔嘴", R.drawable.e23);
        EMOTIONS_MAP.put("微笑", R.drawable.e24);
        EMOTIONS_MAP.put("委屈", R.drawable.e25);
        EMOTIONS_MAP.put("喜欢", R.drawable.e26);
        EMOTIONS_MAP.put("笑着", R.drawable.e27);
        EMOTIONS_MAP.put("憎憎", R.drawable.e28);
        EMOTIONS_MAP.put("皱眉", R.drawable.e29);
    }

    public static SpannableString getSinaEmotionsString(SpannableString value, Context context) {
        Matcher localMatcher = EMOTION_PATTERN.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {

                ImageSpan localImageSpan;
                str2 = str2.substring(1, str2.length() - 1);
                if (!EMOTIONS_MAP.containsKey(str2)) {
                    Log.i("找不到该表情：   " + str2);
                    continue;
                }
                Drawable d = context.getResources().getDrawable(EMOTIONS_MAP.get(str2).intValue());
                d.setBounds(0, 0, PhoneUtil.dip2px(context , Constant.Chat.EMOTION_DIMEN),
                        PhoneUtil.dip2px(context, Constant.Chat.EMOTION_DIMEN));// 设置表情图片的显示大小
                // 这个地方要优化！！
                localImageSpan = new ImageSpan(d,
                        ImageSpan.ALIGN_BASELINE);
                value.setSpan(localImageSpan, k, m, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return value;
    }
}
