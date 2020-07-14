package com.bytedance.videoplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class MyGride {
    static RequestBuilder<Drawable> GlideWithPlaceHolder(Context context, Object object) {
        return Glide.with(context)
                .load(object)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .dontAnimate()
                        .transform(new GlideRoundTransorm(15)));
    }

}