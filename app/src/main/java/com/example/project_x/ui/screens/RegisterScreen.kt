package com.example.project_x.ui.screens

import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.data.model.UserRequest
import com.example.project_x.ui.theme.ButtonColor
import com.example.project_x.ui.theme.LeadingIconColor
import com.example.project_x.ui.theme.SFDisplayFont
import com.example.project_x.ui.viewmodel.AuthViewModel
import java.io.InputStream

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
  val userState = authViewModel.userStateHolder.collectAsState().value
  val context = LocalContext.current
  var email by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var age by rememberSaveable { mutableStateOf("") }
  var name by rememberSaveable { mutableStateOf("") }
  var username by rememberSaveable { mutableStateOf("") }
  var bio by rememberSaveable { mutableStateOf("") }

  var profileImageUri: Uri? by remember { mutableStateOf(null) }
  var profileImageBase64: String? by remember { mutableStateOf(null) }

  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      profileImageUri = uri
      uri?.let {
        context.contentResolver.openInputStream(it)?.use { inputStream ->
          profileImageBase64 = inputStream.toBase64()
        }
      }
    }

  if (userState.isLoading) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator()
    }
  } else {

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .imePadding()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
          text = "Welcome Slidee 👋",
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = SFDisplayFont,
        )
        Text(
          text = "Sign Up and enjoy our community",
          fontWeight = FontWeight.Light,
          fontFamily = SFDisplayFont,
          fontSize = 18.sp,
          color = Color.Gray,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
          modifier =
          Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable {
              launcher.launch(
                PickVisualMediaRequest(
                  mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
              )
            },
          contentAlignment = Alignment.Center,
        ) {
          if (profileImageUri == null) {
            Image(
              painter = painterResource(id = R.drawable.profile),
              contentDescription = "profile_image",
              modifier = Modifier.fillMaxSize(),
            )
          } else {
            AsyncImage(
              model = profileImageUri,
              contentDescription = "profile_image",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop,
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Name") },
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.profile_icon),
              contentDescription = "name_icon",
              tint = LeadingIconColor,
              modifier = Modifier.size(24.dp),
            )
          },
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onSearch),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email") },
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.email_icon),
              contentDescription = "email_icon",
              tint = LeadingIconColor,
              modifier = Modifier.size(24.dp),
            )
          },
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onNext),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text("Password") },
          modifier = Modifier.fillMaxWidth(),
          visualTransformation = PasswordVisualTransformation(),
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.password_icon),
              contentDescription = "password_icon",
              tint = LeadingIconColor,
              modifier = Modifier.size(28.dp),
            )
          },
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onNext),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = username,
          onValueChange = { username = it },
          label = { Text("Username") },
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.username),
              contentDescription = "name_icon",
              tint = LeadingIconColor,
              modifier = Modifier.size(24.dp),
            )
          },
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onNext),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = bio,
          onValueChange = { bio = it },
          label = { Text("Bio") },
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.bio),
              contentDescription = "bio",
              tint = LeadingIconColor,
              modifier = Modifier.size(24.dp),
            )
          },
          placeholder = {
            Text(text = "Tell us something about yourself", fontSize = 12.sp, color = Color.Gray)
          },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onNext),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = age,
          onValueChange = { age = it },
          label = { Text("Age") },
          leadingIcon = {
            Icon(
              painter = painterResource(id = R.drawable.age),
              contentDescription = "age",
              tint = LeadingIconColor,
              modifier = Modifier.size(24.dp),
            )
          },
          modifier = Modifier.fillMaxWidth(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          maxLines = 1,
          keyboardActions = KeyboardActions(onDone = KeyboardActions.Default.onDone),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
          Text(text = "By continuing you agree to our")
        }
        Row {
          Text(text = "and ")
          Text(
            text = "Terms & Conditions & Privacy Policy",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = SFDisplayFont,
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(ButtonColor),
          onClick = {
            val user =
              UserRequest(
                name = name,
                email = email,
                age = age.toInt(),
                username = username,
                password = password,
                bio = bio,
              )
            authViewModel.registerUser(user)
            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
          },
        ) {
          Text(
            "Create Account",
            fontFamily = SFDisplayFont,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 16.sp,
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
          Text(text = "Already Have an Account? ")
          Text(
            text = "Sign In",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ButtonColor,
          )
        }

        if (userState.error?.isNotBlank() == true) {
          Spacer(modifier = Modifier.height(16.dp))
          Text(text = userState.error, color = Color.Red, textAlign = TextAlign.Center)
        }
      }
    }
  }
}

fun InputStream.toBase64(): String {
  val byteArray = this.readBytes()
  return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
