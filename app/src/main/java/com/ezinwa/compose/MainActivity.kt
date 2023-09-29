package com.ezinwa.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ezinwa.circular_card_image.Scale
import com.ezinwa.circular_card_image.ScaleStyle
import com.ezinwa.compose.ui.theme.ComposeXampleLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeXampleLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoadScale()
                }
            }
        }
    }
}

@Composable
private fun LoadScale() {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        var weight by remember { mutableStateOf(0) }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "$weight kg",
            fontSize = 45.sp)

        Scale(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter),
            style = ScaleStyle()
        ) {
            weight = it
        }
    }
}
