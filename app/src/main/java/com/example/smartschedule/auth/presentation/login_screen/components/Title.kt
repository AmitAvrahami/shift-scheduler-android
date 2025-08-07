package com.example.smartschedule.auth.presentation.login_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Title (
    modifier : Modifier = Modifier,
    titleText : String = "Hello",
    textColor : Color = Color.Black,
    backgroundColor : Color = Color.White,
    fontSize : Int = 16,
    padding : PaddingValues = PaddingValues(16.dp),
) {
    Box(
        modifier = modifier
            .padding(padding),
    ){
        Text(
            text = titleText,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
    }
}

@Composable
fun SubTitle (
    modifier : Modifier = Modifier,
    subTitleText : String = "User",
    textColor : Color = Color.Black,
    backgroundColor : Color = Color.White,
    fontSize : Int = 16,
    padding : PaddingValues = PaddingValues(16.dp,8.dp),
) {
    Box(
        modifier = modifier
            .padding(padding)
    ){
        Text(
            subTitleText,
            color = textColor,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
        )
    }
}

@Composable
fun TitleSection(
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    Title()
    SubTitle()
    }
}


@Composable
@Preview(showBackground = true)
fun FunNamePreview(){
    Title()
}