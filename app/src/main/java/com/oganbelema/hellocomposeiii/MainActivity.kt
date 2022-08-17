package com.oganbelema.hellocomposeiii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oganbelema.hellocomposeiii.ui.theme.HelloComposeIIITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloComposeIIITheme {
                MyApp {
                    Text(text = "Hello World!")
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        content()
    }
}

@Preview
@Composable
fun TopHeader() {
    Surface(modifier = Modifier.fillMaxWidth()
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
    color = Color.Magenta
    ) {

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloComposeIIITheme {
        MyApp {
            Text(text = "Hello World!")
        }
    }
}