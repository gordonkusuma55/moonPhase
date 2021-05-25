package com.example.moonphase

import android.icu.util.Calendar
import android.telecom.Call
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId

class bulan {
}

var synmonth= 29.53058868.toDouble()// Synodic month (new Moon to new Moon) (dalam 1 bulan menurut astronomi)

fun New(t: Calendar) : MainActivity.Moon {

    val moonP = MainActivity.Moon()

    // Astronomical constants
    var epoch  = 2444238.5.toFloat() // 1989 January 0.0

    //Constants defining the Sun's apparent orbit
    var elonge = 278.833540.toFloat()  // Ecliptic longitude of the Sun at epoch 1980.0
    var elongp = 282.596403. toFloat() // Ecliptic longitude of the Sun at perigee
    var eccent = 0.016718 .toFloat()    // Eccentricity of Earth's orbit
    var sunsmax = 1.495985e8.toDouble() // Sun's angular size, degrees, at semi-major axis distance
    var sunangsiz = 0.533128.toDouble()

    // Elements of the Moon's orbit, epoch 1980.0
    var mmlong  = 64.975464.toDouble()   // Moon's mean longitude at the epoch
    var mmlongp  = 349.383063.toDouble()// Mean longitude of the perigee at the epoch
    var mecc = 0.054900.toDouble()      // Eccentricity of the Moon's orbit
    var mangsiz = 0.5181. toDouble()     // Moon's angular size at distance a from Earth
    var msmax = 384401.toDouble()       // Semi-major axis of Moon's orbit in km
    var unixTime = System.currentTimeMillis() / 1000L;
    moonP.timespace = unixTime.toDouble()
    moonP.pdata = utcToJulian((unixTime).toDouble()).toDouble()
    // Calculation of the Sun's position
    var day:Double = (moonP.pdata - epoch).toDouble() // Date within epoch

    var temp=360/365.2422.toDouble()
    var n:Double = fixangle(temp.toFloat() * day.toFloat()) // Mean anomaly of the Sun
    var m:Double  = fixangle(n.toFloat() + elonge.toFloat() - elongp.toFloat())  // Convert from perigee co-orginates to epoch 1980.0
    var ec:Double = kepler(m.toFloat(), eccent)                       // Solve equation of Kepler
    ec = Math.sqrt((1 + eccent.toDouble()) / (1 - eccent)) * Math.tan(ec / 2)
//        ec = Math.sqrt((1+eccent.toDouble())/(1-eccent))
//        * tan(ec/2)
    ec = 2 * rad2deg(Math.atan(ec))               // True anomaly
    var lambdasun = fixangle(ec.toFloat() + elongp.toFloat()) // Sun's geocentric ecliptic longitude

    var f  = ((1 + eccent*Math.cos(deg2rad(ec.toDouble()))) / (1 - eccent*eccent)) // Orbital distance factor
    var sunDist  = sunsmax / f                                     // Distance to Sun in km
    var sunAng  = f * sunangsiz                                    // Sun's angular size in degrees


    // Calsulation of the Moon's position
    var ml  = fixangle((13.1763966 * day + mmlong).toFloat())   // Moon's mean longitude
    var mm  = fixangle((ml - 0.1114041 * day - mmlongp).toFloat())    // Moon's mean anomaly
    var ev  = 1.2739 * Math.sin(deg2rad(2 * (ml - lambdasun) - mm)) // Evection
    var ae  = 0.1858 * Math.sin(deg2rad(m))                   // Annual equation
    var a3  = 0.37 * Math.sin(deg2rad(m))                     // Correction term
    var mmP = mm + ev - ae - a3                         // Corrected anomaly
    var mec = 6.2886 * Math.sin(deg2rad(mmP))                // Correction for the equation of the centre
    var a4  = 0.214 * Math.sin(deg2rad(2 * mmP))                // Another correction term
    var lP = ml + ev + mec - ae + a4                    // Corrected longitude
    var v  = 0.6583 * Math.sin(deg2rad(2 * (lP - lambdasun)))     // Variation
    var lPP  = lP + v                                    // True longitude


    // Calculation of the phase of the Moon
    var moonAge :Double = (lPP - lambdasun).toDouble()                   // Age of the Moon in degrees
    var moonPhase :Double = (1 - Math.cos(deg2rad(moonAge.toDouble()))).toDouble() / 2 // Phase of the Moon

    // Distance of moon from the centre of the Earth
    var moonDist :Double = ((msmax * (1 - mecc*mecc)) / (1 + mecc*Math.cos(deg2rad(mmP.toDouble() + mec.toDouble())))).toDouble()

    var moonDFrac:Double = (moonDist / msmax).toDouble()
    var moonAng :Double = (mangsiz / moonDFrac).toDouble() // Moon's angular diameter

    // store result
    moonP.phase = (fixangle(moonAge.toFloat()) / 360).toDouble() // Phase (0 to 1)
    moonP.illum = moonPhase.toDouble()               // Illuminated fraction (0 to 1)
    moonP.age = (synmonth * moonP.phase).toDouble()    // Usia bulan (days)
    moonP.dist = moonDist.toDouble()                // Jarak (kilometres)
    moonP.angdia = moonAng.toDouble()               // Diameter sudut(degreees)
    moonP.sundist = sunDist               // Jarak dengan matahari (kilometres)
    moonP.sunangdia = sunAng              // Diameter sudut matahari (degrees)
    moonP.longitude = lPP                 // Moon's true longitude
//        moonP.phaseHunt()
    //---------------------------------------------------------------------
    var sdate :Double = utcToJulian(moonP.timespace)
    var adate :Double = sdate - 45
    var ats :Double = moonP.timespace - 86400*45
    //t = unixTime
    //t := time.Unix(int64(ats), 0)
    var yy = SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toDouble() //bulan
    var mmm = SimpleDateFormat("MM").format(System.currentTimeMillis()).toDouble()  //Tahun
    //var dyy = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(yy.toLong())).toDouble()
    //var dmm = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(mmm.toLong())).toDouble()

    var k1  = Math.floor((yy+((mmm-1)*(1/12))-1900) * 12.3685)
    var nt1  = meanPhase(adate, k1)
    adate = nt1
    var nt2 :Double
    var k2  :Double

    while (true) {
        adate += synmonth
        k2 = k1 + 1
        nt2 = meanPhase(adate, k2)
        if (Math.abs(nt2-sdate) < 0.75) {
            nt2 = truePhase(k2, 0.0)
        }
        if (nt1 <= sdate && nt2 > sdate) {
            break
        }
        nt1 = nt2
        k1 = k2
    }

    var data:DoubleArray=DoubleArray(8)

    data[0] = truePhase(k1, 0.0)
    data[1] = truePhase(k1, 0.25)
    data[2] = truePhase(k1, 0.5)
    data[3] = truePhase(k1, 0.75)
    data[4] = truePhase(k2, 0.0)
    data[5] = truePhase(k2, 0.25)
    data[6] = truePhase(k2, 0.5)
    data[7] = truePhase(k2, 0.75)

    var i=0

    for (  i in 0..7 ) {
        moonP.quarters[i] = (data[i] - 2440587.5) * 86400 // convert to UNIX time
    }

    return moonP


}


//Manggil fungsi
fun Phase (m: MainActivity.Moon):Double{
    return  m.phase
}
fun  PhaseName(m: MainActivity.Moon) :String {
    val names= listOf(
            "New Moon",         //0
            "Waxing Crescent",  //1
            "First Quarter",    //2
            "Waxing Gibbous",   //3
            "Full Moon",        //4
            "Waning Gibbous",   //5
            "Third Quarter",    //6
            "Waning Crescent",  //7
            "New Moon"          //8
    )

    val i = (Math.floor((m.phase + 0.0625) * 8)).toInt()
    return names[i]
}
fun Age(m: MainActivity.Moon):Double{
    return m.age
}

fun Illumination(m: MainActivity.Moon) :Double {
    return m.illum
}

fun NextNewMoon(m :MainActivity.Moon) :Double{
    return m.quarters[4]
}
//--------------------------------------------------------
fun rad2deg(r: Double) :Double{
    return (r * 180) /Math.PI
}

fun deg2rad(d: Double):Double  {
    return (d * Math.PI) / 180
}

fun fixangle(a: Float) :Double{
    return (a - 360*Math.floor(a / 360.0))
}

fun kepler(m: Float, ecc: Float) :Double {
    var epsilon = 0.000001.toFloat()
    var m = deg2rad(m.toDouble())
    var e = m
    var delta= (e - ecc*Math.sin(e) - m).toFloat()
    e -= delta / (1 - ecc*Math.cos(e))
    while (Math.abs(delta) > epsilon) {
        delta = (e.toFloat() - ecc.toFloat()*Math.sin(e.toDouble()) - m.toFloat()).toFloat()
        e -= delta / (1 - ecc*Math.cos(e))
    }
    return e
}

fun utcToJulian(t: Double):Double {
    return t/86400 + 2440587.5
}

fun julianToUtc(t: Double) :Double {
    return t*86400 + 2440587.5
}

/**
Calculates time of the mean new Moon for a given
base date. This argument K to this function is the
precomputed synodic month index, given by:
K = (year - 1900) * 12.3685
where year is expressed as a year aand fractional year
 */
fun meanPhase(sdate: Double, k: Double) :Double {
    // Time in Julian centuries from 1900 January 0.5
    var t  = ((sdate - 2415020.0) / 36525).toDouble()
    var t2:Double  = t * t
    var t3 :Double = t2 * t

    val nt :Double
    nt = 2415020.75933 + synmonth*k +
            0.0001178*t2 -
            0.000000155*t3 +
            0.00033* Math.sin(deg2rad(166.56 + 132.87 * t - 0.009173 * t2))

    return nt
}

fun truePhase(k :Double, phase :Double) :Double {
    var k = phase                  // Add phase to new moon time
    var t :Double = k / 1236.85 // Time in Julian centures from 1900 January 0.5
    var t2 :Double = t * t
    var t3 :Double = t2 * t
    var pt :Double
    pt = 2415020.75933 + synmonth*k +
            0.0001178*t2 -
            0.000000155*t3 +
            0.00033*Math.sin(deg2rad(166.56+132.87*t-0.009173*t2))

    var m:Double
    var mprime:Double
    var f :Double
    m = 359.2242 + 29.10535608*k - 0.0000333*t2 - 0.00000347*t3       // Sun's mean anomaly
    mprime = 306.0253 + 385.81691806*k + 0.0107306*t2 + 0.00001236*t3 // Moon's mean anomaly
    f = 21.2964 + 390.67050646*k - 0.0016528*t2 - 0.00000239*t3       // Moon's argument of latitude

    if (phase < 0.01 || Math.abs(phase-0.5) < 0.01) {
        // Corrections for New and Full Moon
        pt += (0.1734-0.000393*t)*Math.sin(deg2rad(m)) +
                0.0021*Math.sin(deg2rad(2*m)) -
                0.4068*Math.sin(deg2rad(mprime)) +
                0.0161*Math.sin(deg2rad(2*mprime)) -
                0.0004*Math.sin(deg2rad(3*mprime)) +
                0.0104*Math.sin(deg2rad(2*f)) -
                0.0051*Math.sin(deg2rad(m+mprime)) -
                0.0074*Math.sin(deg2rad(m-mprime)) +
                0.0004*Math.sin(deg2rad(2*f+m)) -
                0.0004*Math.sin(deg2rad(2*f-m)) -
                0.0006*Math.sin(deg2rad(2*f+mprime)) +
                0.0010*Math.sin(deg2rad(2*f-mprime)) +
                0.0005*Math.sin(deg2rad(m+2*mprime))
    } else if (Math.abs(phase-0.25) < 0.01 || Math.abs(phase-0.75) < 0.01) {
        pt += (0.1721-0.0004*t)*Math.sin(deg2rad(m)) +
                0.0021*Math.sin(deg2rad(2*m)) -
                0.6280*Math.sin(deg2rad(mprime)) +
                0.0089*Math.sin(deg2rad(2*mprime)) -
                0.0004*Math.sin(deg2rad(3*mprime)) +
                0.0079*Math.sin(deg2rad(2*f)) -
                0.0119*Math.sin(deg2rad(m+mprime)) -
                0.0047*Math.sin(deg2rad(m-mprime)) +
                0.0003*Math.sin(deg2rad(2*f+m)) -
                0.0004*Math.sin(deg2rad(2*f-m)) -
                0.0006*Math.sin(deg2rad(2*f+mprime)) +
                0.0021*Math.sin(deg2rad(2*f-mprime)) +
                0.0003*Math.sin(deg2rad(m+2*mprime)) +
                0.0004*Math.sin(deg2rad(m-2*mprime)) -
                0.0003*Math.sin(deg2rad(2*m+mprime))
        if (phase < 0.5) { // First quarter correction
            pt += 0.0028 - 0.0004*Math.cos(deg2rad(m)) + 0.0003*Math.cos(deg2rad(mprime))
        } else { // Last quarter correction
            pt += -0.0028 + 0.0004*Math.cos(deg2rad(m)) - 0.0003*Math.cos(deg2rad(mprime))
        }
    }

    return pt
}

//TIME moon

var rad:Double= Math.PI/180
var dayMs = 1000 * 60 * 60 * 24
var J1970 = 2440588
var J2000:Double = 2451545.0

fun toJulian(date:Date) :Double{
    return date.hashCode() / dayMs - 0.5 + J1970
}

fun toDays(date :Date) :Double {
    return toJulian(date) - J2000
}
//------
var e:Double = rad * 23.4397.toDouble()
fun rightAscension(l:Double, b:Double):Double {
    return Math.atan2(Math.sin(l) * Math.cos(e) - Math.tan(b) * Math.sin(e), Math.cos(l))
}
fun declination(l:Double, b:Double):Double{
    return Math.asin(Math.sin(b) * Math.cos(e) + Math.cos(b) * Math.sin(e) * Math.sin(l))
}
fun altitude(H:Double, phi:Double, dec:Double):Double {
    return Math.asin(Math.sin(phi) * Math.sin(dec) + Math.cos(phi) * Math.cos(dec) * Math.cos(H))
}
fun azimuth(H:Double, phi:Double, dec:Double):Double  {
    return Math.atan2(Math.sin(H), Math.cos(H) * Math.sin(phi) - Math.tan(dec) * Math.cos(phi))
}



fun astroRefraction(h:Double):Double {
    var h1 :Double = 0.0
    if (h < 0){ // the following formula works for positive altitudes only.
        h1 = 0.0
    }else  {
        h1=h
    }; // if h = -0.08901179 a div/0 would occur.

    // formula 16.4 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
    // 1.02 / tan(h + 10.26 / (h + 5.10)) h in degrees, result in arc minutes -> converted to rad:
    return 0.0002967 / Math.tan(h1 + 0.00312536 / (h1 + 0.08901179));
}

fun moonCoords(d:Double) :Triple<Double,Double,Double> { // geocentric ecliptic coordinates of the moon

    var L = rad * (218.316 + 13.176396 * d) // ecliptic longitude
    var  M = rad * (134.963 + 13.064993 * d) // mean anomaly
    var F = rad * (93.272 + 13.229350 * d)  // mean distance

    var l :Double = L + rad * 6.289 * Math.sin(M) // longitude
    var b :Double = rad * 5.128 * Math.sin(F)     // latitude
    var dt:Double = 385001 - 20905 * Math.cos(M)  // distance to the moon in km
    var ra :Double = rightAscension(l,b)
    var dec :Double = declination(l,b)
    var dist :Double = dt
    return Triple(ra,dec,dist)

}
fun siderealTime(d:Double, lw:Double):Double {
    return rad * (280.16 + 360.9856235 * d) - lw
}

 fun getMoonPosition(date:Date, lat :Double, lng :Double) :Array<Double> {

    var lw  = rad * -lng
    var phi = rad * lat
    var d   = toDays(date),

    var c = moonCoords(d)
    var H:Double = siderealTime(d, lw) - c.first
    var h:Double = altitude(H, phi, c.second)
    // formula 14.1 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
    var pa = Math.atan2(Math.sin(H), Math.tan(phi) * Math.cos(c.second) - Math.sin(c.second) * Math.cos(H));

     h = h + astroRefraction(h); // altitude correction for refraction
     var azimuth :Double = azimuth(H, phi,c.second)
     var altitude:Double = h
     var distance :Double=c.third
     var  parallacticAngle:Double= pa
    return arrayOf(azimuth,altitude,distance,parallacticAngle)
}

fun hoursLater(date:Calendar, h:Double) :Date {
    return  date.add((h * dayMs / 24).toInt())
}

fun getMoonTimes (date :Calendar, lat:Double, lng:Double, inUTC:Boolean=false) {
    var t :Calendar = date
    t.set(Calendar.HOUR,0)
    t.set(Calendar.MINUTE,0)
    t.set(Calendar.SECOND,0)
    t.set(Calendar.MILLISECOND,0)
    var hc = 0.133 * rad
    var h0 = getMoonPosition(t, lat, lng)[1] - hc
    var h1 :Double =0.0
    var h2 :Double =0.0
    var rise :Double =0.0
    var set:Double =0.0
    var a :Double =0.0
    var b:Double =0.0
    var xe:Double =0.0
    var ye:Double =0.0
    var d:Double =0.0
    var roots:Double =0.0
    var x1:Double =0.0
    var x2:Double =0.0
    var dx:Double =0.0

    // go in 2-hour chunks, each time seeing if a 3-point quadratic curve crosses zero (which means rise or set)
    for (  i in 1..24 step 2) {
        h1 = getMoonPosition(hoursLater(t, i.toDouble()), lat, lng)[1] - hc;
        h2 = getMoonPosition(hoursLater(t, i + 1.0), lat, lng)[1] - hc;

        a = (h0 + h2) / 2 - h1
        b = (h2 - h0) / 2
        xe = -b / (2 * a)
        ye = (a * xe + b) * xe + h1
        d = b * b - 4 * a * h1
        roots = 0.0

        if (d >= 0) {
            dx = Math.sqrt(d) / (Math.abs(a) * 2);
            x1 = xe - dx;
            x2 = xe + dx;
            if (Math.abs(x1) <= 1) {
                roots++
            }
            if (Math.abs(x2) <= 1){
                roots++
            }
            if (x1 < -1) {
                x1 = x2
            }
        }

        if (roots == 1.0) {
            if (h0 < 0) rise = i + x1;
            else set = i + x1;

        } else if (roots == 2.0) {
            rise = i + (ye < 0 ? x2 : x1)
            set = i + (ye < 0 ? x1 : x2)
        }

        if (rise && set) break;

        h0 = h2;
    }

    var result = {};

    if (rise) result.rise = hoursLater(t, rise);
    if (set) result.set = hoursLater(t, set);

    if (!rise && !set) result[ye > 0 ? 'alwaysUp' : 'alwaysDown'] = true;

    return result;
}


//END TIME MOON


