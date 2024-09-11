package com.montanhajr.seeforme.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.montanhajr.seeforme.BuildConfig
import com.montanhajr.seeforme.R

@Composable
fun HomeScreen(
    navController: NavController = NavController(context = LocalContext.current)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.see_for_me_logo),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.accessibility_text_logo),
                modifier = Modifier.size(300.dp)
            )

            HomeButton(
                text = stringResource(id = R.string.see_for_me_button_home),
                backgroundColor = Color(0xFFCA530B),
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_cameraFragment)
                }
            )

            HomeButton(
                text = stringResource(id = R.string.read_for_me_button_home),
                backgroundColor = Color(0xFFE76F26),
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_readFragment)
                }
            )

            HomeButton(
                text = stringResource(id = R.string.find_for_me_button_home),
                backgroundColor = Color(0xFFCA530B),
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_findFragment)
                }
            )

//            HomeButton(
//                text = "DESCRIBE FOR ME",
//                backgroundColor = Color(0xFFF48440),
//                onClick = {
//                    // Ação do botão 4
//                }
//            )
        }
        AdMobBanner(modifier = Modifier.fillMaxWidth().padding(top = 16.dp).align(Alignment.BottomCenter))
    }
}

@Composable
fun AdMobBanner(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BuildConfig.ADMOB_BANNER_UNIT_ID
            loadAd(AdRequest.Builder().build())
        }
    })
}

@Composable
fun HomeButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(70.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFFFFFFFF),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomHomeScreen() {
    HomeScreen()
}