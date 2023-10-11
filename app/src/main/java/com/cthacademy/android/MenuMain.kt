package com.cthacademy.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MenuHome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                UpperMainScreenRectangle(shape = RectangleShape,"Linh Chi", R.drawable.defautprofile)

            }
        }
    }
}

@Composable
fun BackgroundColor() {
    Box(modifier =
    Modifier
        .fillMaxSize()
        .background(color = Color(0xFFFFFFFF))
    )
}
@Composable
fun UpperMainScreenRectangle(shape: Shape, profileName: String, profileImage: Int) {
    val mContext = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var text by remember { mutableStateOf("")}

    text = "HJJKSKKN98JD"

    val targetHeight = (screenHeight * 2f / 5f)
    val rowHeight = 35
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopCenter)
            .background(color = Color(0xFF01081E))) {
        Column(
            modifier = Modifier
                .width(screenWidth)
                .height(targetHeight)
                .padding(horizontal = 15.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Row(
                modifier = Modifier
                    .height(rowHeight.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextString(profileName,"FFFFFFFF",rowHeight-5)
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement  = Arrangement.End
                ){
                    ProfileCircularShapeWithOutline(CircleShape,rowHeight-5,profileImage)
                }

            }

            Row(
                modifier = Modifier
                    .height((rowHeight).dp)
            ) {
                Column(
                    modifier = Modifier
                        .height((rowHeight).dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString((text)))
                            Toast
                                .makeText(mContext, " Đã copy mã giới thiệu", Toast.LENGTH_SHORT)
                                .show()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .height((rowHeight-20).dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextString("Mã giới thiệu","FFFFFF",10)
                        Image(
                            painter = painterResource(id = R.drawable.ic_copy_white),
                            contentDescription = "Copy"
                        )
                    }
                    TextString(str = "HJJKSKKN98JD", color = "FFFFFF", size = 14)
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End

                ) {
                    Box(
                        modifier = Modifier
                            .width(122.dp)
                            .height(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            onClick =
                            {
                                Toast.makeText(mContext, " Đã copy mã giới thiệu", Toast.LENGTH_SHORT)
                                    .show()
                            },
                        ) {

                        }
                        TextString(str = "Liên hệ hỗ trợ", color = "000000", size = 14, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }
            }


        }
    }
}

fun colorPraise(color: String) = android.graphics.Color.parseColor("#"+color)

@Composable
fun ProfileCircularShapeWithOutline(shape: Shape, size: Int, image: Int,modifier: Modifier = Modifier){
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(shape)
            .background(Color.White)
    ){
        Box(modifier = Modifier
            .size((size - 2).dp)
            .clip(shape)
            .align(Alignment.Center)


        ){
            Image(
                painter = painterResource(image),
                contentDescription = "Profile",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun TextString(str: String, color: String, size: Int,modifier: Modifier = Modifier){
    Text(
        text = str,
        modifier = Modifier.padding(top = 2.dp),
        style = TextStyle(
            fontSize = size.sp,
            fontFamily = FontFamily(Font(R.font.baloo2regular)),
            color = Color(colorPraise(color))
        )
    )
}

@Preview
@Composable
fun DefaultPreview() {
    BackgroundColor()
    UpperMainScreenRectangle(shape = RectangleShape,"Linh Chi", R.drawable.defautprofile)
    Image(
        painter = painterResource(R.drawable.number0),
        contentDescription = "Profile",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
    )
}
