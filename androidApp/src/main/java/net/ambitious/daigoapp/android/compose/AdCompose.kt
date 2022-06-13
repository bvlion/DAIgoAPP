package net.ambitious.daigoapp.android.compose

import android.content.res.Configuration
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import net.ambitious.daigoapp.android.BuildConfig
import net.ambitious.daigoapp.android.ui.AppTheme

@Composable
fun NativeAdCompose() {
  val context = LocalContext.current
  AndroidView(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    factory = ::NativeAdView,
    update = { nativeAdView ->
      val image = MediaView(nativeAdView.context)
      nativeAdView.addView(image)
      AdLoader.Builder(context, BuildConfig.ADMOB_NATIVE_KEY)
        .forNativeAd { nativeAd ->
          image.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
          nativeAd.mediaContent?.let {
            image.setMediaContent(it)
          }
          nativeAdView.mediaView = image

          nativeAdView.setNativeAd(nativeAd)
        }.build().loadAd(AdRequest.Builder().build())
    }
  )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AdPreview() {
  AppTheme {
    NativeAdCompose()
  }
}
