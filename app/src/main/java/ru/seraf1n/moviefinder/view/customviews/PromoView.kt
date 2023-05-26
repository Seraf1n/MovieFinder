package ru.seraf1n.moviefinder.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.seraf1n.moviefinder.databinding.MergePromoBinding
import ru.seraf1n.remote_module.entity.ApiConstants

private const val CORNER_RADIUS = 55

private const val IMAGE_SIZE = "w500"

class PromoView(context: Context, attributeSet: AttributeSet?) : FrameLayout(context, attributeSet) {
    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val watchButton = binding.watchButton

    fun setLinkForPoster(link: String) {
        val into = Glide.with(binding.root)
            .load(ApiConstants.IMAGES_URL + IMAGE_SIZE + link)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(CORNER_RADIUS)))
            .into(binding.poster)
    }
}