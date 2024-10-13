package com.test.newsapiproject.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.test.newsapiproject.ui.common.ImageScale.None.convertContentScale

@Composable
fun ImageAsync(url: String,
               contentDescription: String?,
               modifier: Modifier = Modifier,
               placeholder: Painter? = null,
               error: Painter? = null,
               imageScale: ImageScale = ImageScale.Fit,
               alignment: Alignment = Alignment.Center
){
    Box(modifier = modifier) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            placeholder = placeholder,
            error = error,
            alignment = alignment,
            contentScale = imageScale.convertContentScale()
        )
    }
}

sealed interface ImageScale {
    object Crop: ImageScale
    object Fit: ImageScale
    object FillHeight: ImageScale
    object FillWidth: ImageScale
    object Inside: ImageScale
    object FillBounds: ImageScale
    object None: ImageScale

    fun ImageScale.convertContentScale(): ContentScale = when(this) {
        Crop -> ContentScale.Crop
        FillBounds -> ContentScale.FillBounds
        FillHeight -> ContentScale.FillHeight
        FillWidth -> ContentScale.FillWidth
        Fit -> ContentScale.Fit
        Inside -> ContentScale.Inside
        None -> ContentScale.None
    }
}