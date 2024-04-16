package com.chw.application.util;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chw.application.R;

import java.util.Objects;
import java.util.UUID;

public class ServiceLogicUtils {

    /**
     * @param content 上下文
     * @param iv      目标视图
     * @param path    图片路径
     */
    public static void loadImage(Context content, ImageView iv, String path) {
        RequestOptions options = RequestOptions.circleCropTransform();
        if (!Objects.equals(path, "") && path != null) {
            byte[] bytes = FileEncryptUtils.fileLoad(path);
            Glide.with(content)
                    .load(bytes)
                    .apply(options)
                    .into(iv);
        } else {
            Glide.with(content)
                    .load(R.mipmap.ic_launcher)
                    .apply(options)
                    .into(iv);
        }
    }

    /**
     * 获取随机字符串
     */
    public static String getRandomStr() {
        return UUID.randomUUID().toString().replace("_", "");
    }
}
