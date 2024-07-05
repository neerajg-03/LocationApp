package com.example.locationapp

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:LocationViewModel1=viewModel()
            LocationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)

                }
            }
        }
    }

}
@Composable
fun MyApp(viewModel:LocationViewModel1){
    val context= LocalContext.current
    val locationUtils=LocationPerm(context)
    LocationDisplay(locationPerm=locationUtils,viewModel,context=context)
}
@Composable
fun LocationDisplay(locationPerm: LocationPerm,viewModel:LocationViewModel1
                    , context: Context){
    val location=viewModel.location.value
    val address=location?.let{
        locationPerm.reverseGeocodeLocation(location)
    }
    val requestPermissionLauncher= rememberLauncherForActivityResult(
        contract =ActivityResultContracts.RequestMultiplePermissions()
        , onResult ={permissions->
            if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true ){
                //HAS PERMISSIONS
                locationPerm.requestLocationUpdates(viewModel=viewModel)
            }else{
                val rationaleRequires=ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)&&
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context as MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION)

                if (rationaleRequires){
                    Toast.makeText(context,"Location is required for this feature to work",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"Location needed...change ur settings",Toast.LENGTH_LONG).show()
                }
            }

        }
    )






    Column(
        modifier=Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (location!=null){
            Text(text = "Address:${location.latitude} ${location.longitude} \n $address")
        }else{
        Text(text = "Location Not Available")}
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Get the Location")
            if(locationPerm.hasLocationPermission(context)){
                locationPerm.requestLocationUpdates(viewModel=viewModel)
                //Location Granted
            }else{

                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))

            }

        }


    }

}