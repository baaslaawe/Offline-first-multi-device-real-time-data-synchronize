package io.moka.base.module

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.moka.syncdemo.R


@GlideModule
class ImageLoadModule : AppGlideModule()

fun ImageView.load(
        context: Context,
        resUri: String?,
        duration: Int = 0,
        width: Int = 0,
        height: Int = 0,
        failCallback: (() -> Unit)? = null) {

    GlideApp.with(context)
            .load(resUri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    failCallback?.invoke()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .apply {

                if (duration != 0)
                    transition(DrawableTransitionOptions.withCrossFade(duration))
                else
                    dontAnimate()

                if (width != 0 && height != 0)
                    override(width, height)
            }
            .into(this)
}

fun ImageView.load(
        fragment: Fragment,
        resUri: String?,
        duration: Int = 0,
        width: Int = 0,
        height: Int = 0,
        failCallback: (() -> Unit)? = null) {

    if (null != fragment.context)
        this.load(fragment.context!!, resUri, duration, width, height, failCallback)
}

fun ImageView.load(
        context: Context,
        resId: Int?,
        duration: Int = 0,
        width: Int = 0,
        height: Int = 0,
        failCallback: (() -> Unit)? = null) {

    GlideApp.with(context)
            .load(resId)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    failCallback?.invoke()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .apply {
                if (duration != 0)
                    transition(DrawableTransitionOptions.withCrossFade(duration))
                else
                    dontAnimate()

                if (width != 0 && height != 0)
                    override(width, height)
            }
            .into(this)
}

fun ImageView.load(
        fragment: Fragment,
        uri: Uri?,
        duration: Int = 0,
        width: Int = 0,
        height: Int = 0,
        failCallback: (() -> Unit)? = null) {

    if (null != fragment.context) {
        GlideApp.with(fragment.context!!)
                .load(uri)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        failCallback?.invoke()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .apply {

                    if (duration != 0)
                        transition(DrawableTransitionOptions.withCrossFade(duration))
                    else
                        dontAnimate()

                    if (width != 0 && height != 0)
                        override(width, height)
                }
                .into(this)
    }
}

fun ImageView.load(
        glide: GlideRequests,
        resUri: String?,
        duration: Int = 0,
        width: Int = 0,
        height: Int = 0,
        failCallback: (() -> Unit)? = null) {

    glide
            .load(resUri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    failCallback?.invoke()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }


            })
            .apply {

                if (duration != 0)
                    transition(DrawableTransitionOptions.withCrossFade(duration))
                else
                    dontAnimate()

                if (width != 0 && height != 0)
                    override(width, height)
            }
            .into(this)
}

fun ImageView.circle(glide: GlideRequests, url: String?) {
    glide
            .load(url)
            .centerCrop()
            .apply(RequestOptions().circleCrop())
            .into(this)
}

fun ImageView.radius(context: Context, url: String?, radius: Int = 10) {
    GlideApp
            .with(context)
            .load(url)
            .transform(RoundedCorners(radius))
            .into(this)
}

fun ImageView.radius(context: Context, resId: Int?, radius: Int = 10) {
    GlideApp
            .with(context)
            .load(resId)
            .transform(RoundedCorners(radius))
            .into(this)
}
