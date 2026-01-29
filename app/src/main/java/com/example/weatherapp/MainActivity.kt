package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import android.Manifest
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.Blue
import com.example.weatherapp.ui.theme.DarkBlue
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherScreen()
        }
    }
}
/*@Composable
fun WeatherScreen(){
    val viewModel: MainViewModel = viewModel()
    val Jsondata by viewModel.Jsondata.collectAsState()
    var city by remember { mutableStateOf("") }
    val apiKey = "10a6bed5ee3beb639cef1a5e806a22ad"

    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painterResource(id = R.drawable.weatherbg),
            contentScale = ContentScale.FillBounds
        )) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(180.dp))
            OutlinedTextField(value = city,
                onValueChange = { city = it },
                label = { Text("Enter City")},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Blue,
                    focusedIndicatorColor = Color.Blue,
                    focusedLabelColor = DarkBlue,
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.fetchWeather(city, apiKey) },
                colors = ButtonDefaults.buttonColors(DarkBlue)){
                Text(text = "Get Weather")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Jsondata?.let {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    WeatherCard(label = "City", value = it.name, icon = Icons.Default.Place)
                    //val message = "This is a debug message city"
                   // Log.d("MyCity", message)

                    WeatherCard(label = "Temperature", value = "${it.main.temp}C", icon = Icons.Default.Star)
                    //val message2 = "This is a debug message temperature"
                   // Log.d("MyTemp", message2)
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    WeatherCard(label = "Humidity", value = "${it.main.humidity}%", icon = Icons.Default.Warning)
                   // val message3 = "This is a debug message humidity"
                   // Log.d("MyHumidity", message3)

                    WeatherCard(label = "Description", value = it.weather[0].description, icon = Icons.Default.Info)
                    //val message4 = "This is a debug message description"
                   // Log.d("MyDescription", message4)
                }
            }
        }
    }
}*/
@Composable
fun WeatherScreen() {
    val viewModel: MainViewModel = viewModel()
    val Jsondata by viewModel.Jsondata.collectAsState()
    var city by remember { mutableStateOf("") }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    val apiKey = "10a6bed5ee3beb639cef1a5e806a22ad"

    // Check for location permission
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            locationPermissionGranted = true
        }
    }

    // Get user's current location
    if (locationPermissionGranted) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        LaunchedEffect(Unit) {
            val locationResult: Task<Location> = fusedLocationClient.lastLocation
            locationResult.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painterResource(id = R.drawable.weatherbg),
            contentScale = ContentScale.FillBounds
        )) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(180.dp))
            OutlinedTextField(value = city,
                onValueChange = { city = it },
                label = { Text("Enter City") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Blue,
                    focusedIndicatorColor = Color.Blue,
                    focusedLabelColor = DarkBlue,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.fetchWeather(city, apiKey) },
                colors = ButtonDefaults.buttonColors(DarkBlue)) {
                Text(text = "Get Weather")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // New button for getting weather based on location
            Button(
                onClick = {
                    currentLocation?.let { location ->
                        val lat = location.latitude
                        val lon = location.longitude
                        viewModel.fetchWeatherByLocation(lat, lon, apiKey)
                    }
                },
                colors = ButtonDefaults.buttonColors(DarkBlue),
                enabled = currentLocation != null
            ) {
                Text(text = "Get Weather by Location")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Jsondata?.let {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherCard(label = "City", value = it.name, icon = Icons.Default.Place)
                    WeatherCard(label = "Temperature", value = "${it.main.temp}C", icon = Icons.Default.Star)
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherCard(label = "Humidity", value = "${it.main.humidity}%", icon = Icons.Default.Warning)
                    WeatherCard(label = "Description", value = it.weather[0].description, icon = Icons.Default.Info)
                }
            }
        }
    }
}

const val LOCATION_PERMISSION_REQUEST_CODE = 1

@Composable
fun WeatherCard(label: String, value: String,icon:ImageVector) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            )
            {
                Icon(
                    imageVector = icon, contentDescription = null,
                    tint = DarkBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, fontSize = 14.sp, color = DarkBlue)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
            ) {
                Text(text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = DarkBlue)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WeatherPreview(){
    WeatherAppTheme {
        WeatherScreen()
    }
}