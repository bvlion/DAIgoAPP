package net.ambitious.daigoapp.android.compose

import android.content.res.Configuration
import android.widget.ImageView
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.databinding.AdmobBinding
import net.ambitious.daigoapp.android.ui.AppTheme

@Composable
fun NativeAdCompose() {
  val context = LocalContext.current
  val textColor = MaterialTheme.colors.onBackground.toArgb()
  val backgroundColor = MaterialTheme.colors.background.toArgb()
  AndroidViewBinding(AdmobBinding::inflate) {
    carView.setCardBackgroundColor(backgroundColor)
    AdLoader.Builder(context, BuildConfig.ADMOB_NATIVE_KEY)
      .forNativeAd { nativeAd ->
        adImage.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        nativeAd.mediaContent?.let {
          adImage.setMediaContent(it)
        }
        nativeAdView.mediaView = adImage

        nativeAdView.headlineView = adHeadline.apply {
          text = nativeAd.headline
          setTextColor(textColor)
        }

        nativeAdView.bodyView = adBody.apply {
          text = nativeAd.body
          setTextColor(textColor)
        }

        if (!nativeAd.advertiser.isNullOrEmpty()) {
          nativeAdView.advertiserView = advertiser.apply {
            text = nativeAd.advertiser
            setTextColor(textColor)
          }
        } else {
          advertiser.isVisible = false
        }

        nativeAdView.setNativeAd(nativeAd)
      }.build().loadAd(AdRequest.Builder().build())
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AdPreview() {
  AppTheme {
    NativeAdCompose()
  }
}
