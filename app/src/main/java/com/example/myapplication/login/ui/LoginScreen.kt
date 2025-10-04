package com.example.myapplication.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


@Composable
fun LoginScreen(viewModel: LoginViewModel){
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Login(Modifier.align (Alignment.Center), viewModel)
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(viewModel = LoginViewModel())
}
@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {

    val user : String by viewModel.user.observeAsState(initial = "" )
    val password : String by viewModel.password.observeAsState(initial = "" )
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(false)


    Column ( modifier = modifier){
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        UserField(user,{viewModel.onLoginChanged(it, password)})
        Spacer(modifier = Modifier.padding(5.dp))
        PaswordField(password){viewModel.onLoginChanged(user,it)}
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton(Modifier.align(Alignment.CenterHorizontally),loginEnable){viewModel.onLoginSelected()}
    }
}

@Composable
fun LoginButton(modifier: Modifier, loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = {onLoginSelected()},
        modifier = modifier.width(250.dp).height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor=Color(0xFF4CAF50),
            disabledContainerColor= Color(0xFF86AF87),
            contentColor = Color.White,
            disabledContentColor =Color.White
            ), enabled = loginEnable
    ) {
        Text("Iniciar Sesión")
    }
}

@Composable
fun PaswordField(password: String, onTextFieldChanged:(String) ->  Unit) {
    TextField(
        value =password,
        onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder ={ Text("Contraseña" )},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        /***colors = TextFieldDefaults.colors(unfocusedTextColor = Color.Red***/)
}

@Composable
fun UserField(user: String, onTextFieldChanged:(String) ->  Unit) {
    TextField(
        value = user,
        onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Usuario")} ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.foto_inevh),
        contentDescription = "Header",
        modifier = Modifier)


}
