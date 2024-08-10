package com.montanhajr.seeforme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.montanhajr.seeforme.ui.viewmodels.HomeViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavController = NavController(context = LocalContext.current)
) {
    // Background da tela
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentScale = ContentScale.Crop,
            contentDescription = null, // Descrição acessível opcional
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
                contentDescription = "Logo do App com a escrita See For Me",
                modifier = Modifier.size(300.dp)
            )

            // Botão 1
            Button(
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_cameraFragment)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC1CC)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "SEE FOR ME", color = Color(0xFF2D004E), fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Botão 2
            Button(
                onClick = { /* Ação do botão 2 */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF5B7)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "READ FOR ME", color = Color(0xFF2D004E), fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Botão 3
            Button(
                onClick = { /* Ação do botão 3 */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2F2BB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "FIND FOR ME", color = Color(0xFF2D004E), fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

@Composable
fun CustomHomeScreen() {
    // Background da tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C54))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nome do app
            Text(
                text = "Your App Name",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botão 1: See For Me
            Button(
                onClick = { /* Ação do botão See For Me */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34E7E4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "See For Me",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "See For Me", color = Color.Black, fontSize = 18.sp)
            }

            // Botão 2: Read For Me
            Button(
                onClick = { /* Ação do botão Read For Me */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEA47F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Read For Me",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Read For Me", color = Color.Black, fontSize = 18.sp)
            }

            // Botão 3: Find For Me
            Button(
                onClick = { /* Ação do botão Find For Me */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9AECDB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Find For Me",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Find For Me", color = Color.Black, fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomHomeScreen() {
    CustomHomeScreen()
}