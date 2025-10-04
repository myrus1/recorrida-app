package com.example.myapplication.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreen(){
    Box(Modifier
        .fillMaxSize()
        .padding(16.dp)){
    Login(Modifier.align (Alignment.Center))
    }
}

@Composable
fun Login(modifier: Modifier) {
    Column ( modifier = modifier){
        HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp))
        UserField()
        Spacer(modifier = Modifier.padding(10.dp))
        PaswordField()
    }
}

@Composable
fun PaswordField() {
    TextField(
        value ="",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder ={ Text("Contraseña" )},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(unfocusedTextColor = Color.Red)    )
}

@Composable
fun UserField() {
    TextField(
        value ="",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Usuario")} ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.foto_inevh),
        contentDescription = "Header",
        modifier = Modifier)


}
