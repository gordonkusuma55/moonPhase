package com.example.moonphase

import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.math.*
import kotlin.time.*
import com.google.android.gms.location.*
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE as LOCATION_SERVICE1
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE as LOCATION_SERVICE1


class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest

    private var PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        val cal= Calendar.getInstance()
        val MoonMic = New(Calendar.getInstance())
        val s: String = String.format("%.2f", (Illumination(MoonMic) * 100)) //0 to 1
        tvMoon.setText("Persentasi Bulan: " + s + "%")
        val tvAge:String= String.format("%.2f", (Age(MoonMic)))
        //tvMoon1.setText("Umur:"+tvAge+" Hari")
        tvMoon1.setText(PhaseName(MoonMic).toString())


//        val a= ImageView(this).apply {
//            setImageResource(R.drawable.a)
//        }
        //gambar fase bulan
        val i = (Math.floor((MoonMic.phase + 0.0625) * 8)).toInt()
        if (i==0|| i==8){
            tvImage.setImageResource(R.drawable.a)
        }else if(i==1){

        }else if(i==2){
            tvImage.setImageResource(R.drawable.bulan150)

        }else if(i==3){

        }else if(i==4){

        }else if (i==5){

        }else if (i==6){

        }else if (i==7){

        }
        //tvImage.setImageResource(R.drawable.a)
//        val dt = Instant.ofEpochSecond(NextNewMoon(MoonMic).toLong())
//              .atZone(ZoneId.systemDefault())
//                .toLocalDateTime()
        tvTes.setText(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(NextNewMoon(MoonMic).toLong()))).toString()
        var unixTime = System.currentTimeMillis() / 1000L;
        var mmm = SimpleDateFormat("MM").format(System.currentTimeMillis()).toDouble()
        //var dmm = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(mmm.toLong())).toString()

        //tvTes.setText(MoonMic.toString())

    }


//    class Moon{
//        var phase:Float= 0,
//
//    }

//    type Moon struct {
//        phase     float64
//                illum     float64
//                age       float64
//                dist      float64
//                angdia    float64
//                sundist   float64
//                sunangdia float64
//                pdata     float64
//                quarters  [8]float64
//                timespace float64
//                longitude float64
//    }
    class Moon(
        //var phase: Float = (0).toFloat(),
        var phase: Double = 0.0,
        var illum: Double = 0.0,
        var age: Double = 0.0,
        var dist: Double = 0.0,
        var angdia: Double = 0.0,
        var sundist: Double = 0.0,
        var sunangdia: Double = 0.0,
        //var pdata: Float = (0).toFloat(),
        var pdata: Double = 0.0,
        var quarters: DoubleArray = DoubleArray(8), //{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}
        var timespace: Double = 0.0,
        var longitude: Double = 0.0
)


//lanjutan

//    fun phaseHunt(m :Moon) :Moon{
//        var sdate :Double = utcToJulian(m.timespace)
//        var adate :Double = sdate - 45
//        var ats :Double = m.timespace - 86400*45
////        t = Time.unix(int64(ats), 0)
//        var yy = SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toDouble() //bulan
//        var mm =SimpleDateFormat("MM").format(System.currentTimeMillis()).toDouble()   //Tahun
//
//        var k1  = Math.floor((yy+((mm-1)*(1/12))-1900) * 12.3685)
//        var nt1  = meanPhase(adate, k1)
//        adate = nt1
//        var nt2 :Double
//        var k2  :Double
//
//        while (true) {
//            adate += synmonth
//            k2 = k1 + 1
//            nt2 = meanPhase(adate, k2)
//            if (Math.abs(nt2-sdate) < 0.75) {
//                nt2 = truePhase(k2, 0.0)
//            }
//            if (nt1 <= sdate && nt2 > sdate) {
//                break
//            }
//            nt1 = nt2
//            k1 = k2
//        }
//
//        var data:DoubleArray=DoubleArray(8)
//
//        data[0] = truePhase(k1, 0.0)
//        data[1] = truePhase(k1, 0.25)
//        data[2] = truePhase(k1, 0.5)
//        data[3] = truePhase(k1, 0.75)
//        data[4] = truePhase(k2, 0.0)
//        data[5] = truePhase(k2, 0.25)
//        data[6] = truePhase(k2, 0.5)
//        data[7] = truePhase(k2, 0.75)
//
//        var i=0
//
//        for (  i in 1..8 ) {
//            m.quarters[i] = (data[i] - 2440587.5) * 86400 // convert to UNIX time
//        }
//    }


    //LONGLAT Fungsi
    fun getLastLocation() :Pair<Double,Double>{
        var long:Double = 0.0
        var lat:Double = 0.0
        if (CheckPermission()){
            if (isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{ task ->
                    var location: Location? = task.result
                    if (location==null){
                        NewLocationData()
                    }else {
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
//                        Locationtxt.text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(location.latitude,location.longitude)
//                        tvlong.text=location.longitude.toString()
//                        tvlit.text=location.latitude.toString()
                        long =location.longitude
                        lat =location.latitude
                    }
                }
            }else {
                Toast.makeText(this,"Please Enable Your Location servicce", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
        return Pair(long,lat)
    }

    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper())
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
//            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
//            Locationtxt.text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude,lastLocation.longitude)


        }
    }

    private fun CheckPermission(): Boolean {
        if (
                ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }


    private fun RequestPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_ID
        )
    }


    private fun isLocationEnabled(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You have the Permission")
            }
        }
    }


    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }

//END Longlat fungsi

}