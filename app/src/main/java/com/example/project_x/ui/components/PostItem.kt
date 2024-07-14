package com.example.project_x.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.data.model.PostResponse
import com.example.project_x.utils.getRelativeTimeSpanString

@Composable
fun PostItem(modifier: Modifier = Modifier, post: PostResponse) {
    val timeAgo = remember { getRelativeTimeSpanString(post.createdAt!!) }
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        if (post.imageUrl.isNullOrEmpty()) {
            Row {
                if (post.createdBy?.profileImage.isNullOrEmpty())
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                          .padding(10.dp)
                          .clip(CircleShape)
                          .size(50.dp),
                    )
                else {
                    AsyncImage(
                        model = post.createdBy?.profileImage,
                        contentDescription = "profileImage",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                          .padding(10.dp)
                          .clip(CircleShape)
                          .size(50.dp),
                    )
                }
                Column(modifier = Modifier.padding(bottom = 5.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = post.createdBy?.name!!,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " $timeAgo", fontSize = 12.sp, fontWeight = FontWeight.Light)
                    }
                    Text(text = "@${post.createdBy?.username!!}")
                    Text(text = post.content!!, fontSize = 14.sp, fontWeight = FontWeight.Light)
                }
            }
        } else {
            Row {
                if (post.createdBy?.profileImage.isNullOrEmpty())
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                          .padding(10.dp)
                          .clip(CircleShape)
                          .size(50.dp),
                    )
                else {
                    AsyncImage(
                        model = post.createdBy?.profileImage,
                        contentDescription = "profileImage",
                        contentScale = ContentScale.Crop,
                        modifier =
                        Modifier
                          .padding(10.dp)
                          .clip(CircleShape)
                          .size(50.dp)
                          .clickable {
                            showDialog.value = true
                          },
                    )
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = post.createdBy?.name!!,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " $timeAgo", fontSize = 12.sp, fontWeight = FontWeight.Light)
                    }
                    Text(text = "@${post.createdBy?.username!!}")
                    Text(text = post.content!!, fontSize = 14.sp, fontWeight = FontWeight.Light)
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = "post_image",
                        contentScale = ContentScale.Crop,
                        modifier =
                        Modifier
                          .fillMaxWidth()
                          .padding(top = 5.dp, bottom = 10.dp)
                          .clickable { showDialog.value = true }
                          .clip(RoundedCornerShape(12.dp)),
                    )
                }
            }
        }
    }

    // Image Popup Dialog
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Column {
                ZoomableImage(imageUrl = post.imageUrl)
                Button(
                    onClick = { showDialog.value = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close_btn",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
    HorizontalDivider(color = Color.Gray, thickness = 0.25.dp)
}

@Composable
fun ZoomableImage(imageUrl: String?) {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale = remember { Animatable(1f) }

    LaunchedEffect(scale) {
        animatedScale.snapTo(scale) // Update the animated scale
    }

    Box(
        modifier =
        Modifier
          .pointerInput(Unit) { detectTransformGestures { _, _, zoom, _ -> scale *= zoom } }
          .graphicsLayer(
            scaleX = animatedScale.value,
            scaleY = animatedScale.value,
            translationX = 0f, // Keep the image centered
            translationY = 0f, // Keep the image centered
          )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "popup_post_image",
            contentScale = ContentScale.Inside,
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp),
        )

        // Reset scale when not interacting
        LaunchedEffect(scale) {
            if (scale != 1f) {
                animatedScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300)
                )
                scale = 1f // Reset scale after animation
            }
        }
    }
}
