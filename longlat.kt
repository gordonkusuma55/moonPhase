package com.example.moonphase

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*
import java.util.*

class longlat {
}
lateinit var fusedLocationProviderClient: FusedLocationProviderClient

lateinit var locationRequest: LocationRequest

private var PERMISSION_ID = 1010

////LONGLAT Fungsi
//fun getLastLocation() :Pair<Double,Double>{
//    var long:Double = 0.0
//    var lat:Double = 0.0
//    if (CheckPermission()){
//        if (isLocationEnabled()){
//            if (ActivityCompat.checkSelfPermission(this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//            }
//            fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
//                var location: Location? = task.result
//                if (location==null){
//                    NewLocationData()
//                }else {
//                    Log.d("Debug:" ,"Your Location:"+ location.longitude)
////                        Locationtxt.text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(location.latitude,location.longitude)
////                        tvlong.text=location.longitude.toString()
////                        tvlit.text=location.latitude.toString()
//                    long =location.longitude
//                    lat =location.latitude
//                }
//            }
//        }else {
//            Toast.makeText(this,"Please Enable Your Location servicce", Toast.LENGTH_SHORT).show()
//        }
//    }else{
//        RequestPermission()
//    }
//    return Pair(long,lat)
//}
//
//fun NewLocationData(){
//    var locationRequest =  LocationRequest()
//    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    locationRequest.interval = 0
//    locationRequest.fastestInterval = 0
//    locationRequest.numUpdates = 1
//    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//    if (ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//        return
//    }
//    if (ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//        return
//    }
//    fusedLocationProviderClient!!.requestLocationUpdates(
//        locationRequest,locationCallback, Looper.myLooper())
//}
//
//private val locationCallback = object : LocationCallback(){
//    override fun onLocationResult(locationResult: LocationResult) {
//        var lastLocation: Location = locationResult.lastLocation
////            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
////            Locationtxt.text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude,lastLocation.longitude)
//
//
//    }
//}
//
//private fun CheckPermission(): Boolean {
//    if (
//        ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED ||
//        ActivityCompat.checkSelfPermission(
//            this,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {
//        return true
//    }
//
//    return false
//}
//
//
//private fun RequestPermission() {
//    ActivityCompat.requestPermissions(
//        this,
//        arrayOf(
//            android.Manifest.permission.ACCESS_FINE_LOCATION,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        ), PERMISSION_ID
//    )
//}
//
//
//private fun isLocationEnabled(): Boolean {
//
//    var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//        LocationManager.NETWORK_PROVIDER
//    )
//}
//
//
//override fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<out String>,
//    grantResults: IntArray
//) {
//    if (requestCode == PERMISSION_ID) {
//        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.d("Debug:", "You have the Permission")
//        }
//    }
//}
//
//
//private fun getCityName(lat: Double,long: Double):String{
//    var cityName:String = ""
//    var countryName = ""
//    var geoCoder = Geocoder(this, Locale.getDefault())
//    var Adress = geoCoder.getFromLocation(lat,long,3)
//
//    cityName = Adress.get(0).locality
//    countryName = Adress.get(0).countryName
//    Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
//    return cityName
//}
//
////END Longlat fungsi

