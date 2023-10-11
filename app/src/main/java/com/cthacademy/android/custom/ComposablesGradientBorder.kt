package com.cthacademy.android.custom

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cthacademy.android.R


class ComposablesGradientBorder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Call the wrapper function here
            SetRingPreview()
        }
    }
}
@Composable
@Preview
fun SetRingPreview() {
    SetRing(R.drawable.ic_avatar)
}

@Composable
fun SetRing(imageResource: Int) {
    Box(
        modifier = Modifier
            .border(
                2.dp,
                Brush.linearGradient(
                    colors = listOf(
                        Color.Yellow,
                        Color.Blue
                    )
                ),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
