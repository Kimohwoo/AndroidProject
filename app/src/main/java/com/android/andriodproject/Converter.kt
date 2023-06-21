import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LatXLngY {
    var lat = 0.0
    var lng = 0.0
    var x = 0.0
    var y = 0.0
}

object Converter {
    val TO_GRID = 0
//    val TO_GPS = 1

    fun xyToSido(nx: Int, ny: Int): String {
        if ((nx in 93..101) && (ny in 73..79)) {
            return "부산"
        } else if ((nx in 57..63) && (ny in 124..129)) {
            return "서울"
        } else if ((nx in 53..73) && (ny in 113..140)) {
            return "경기도"
        } else if ((nx in 66..69) && (ny in 98..103)) {
            return "대전"
        } else if ((nx in 86..91) && (ny in 86..92)) {
            return "대구"
        } else {
            return "부산"
        }
    }

    fun convertGRID_GPS(mode: Int, lat_X: Double, lng_Y: Double): LatXLngY {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //
        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD
        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        val rs = LatXLngY()
        if (mode == TO_GRID) {
            rs.lat = lat_X
            rs.lng = lng_Y
            var ra = Math.tan(Math.PI * 0.25 + lat_X * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = lng_Y * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
        } else {
            rs.x = lat_X
            rs.y = lng_Y
            val xn = lat_X - XO
            val yn = ro - lng_Y + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5
            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            rs.lat = alat * RADDEG
            rs.lng = alon * RADDEG
        }
        return rs
    }

    fun calculateSpeed(distance: String, exerciseTime: String): Double? {
        val distance = distance.toDoubleOrNull()
        val timeComponents = exerciseTime.split(":")

        if (timeComponents.size != 3) {
            // exerciseTime이 올바른 형식이 아닐 경우 null 반환
            return null
        }

        val hours = timeComponents[0].toDoubleOrNull()
        val minutes = timeComponents[1].toDoubleOrNull()
        val seconds = timeComponents[2].toDoubleOrNull()

        if (distance != null && hours != null && minutes != null && seconds != null && (hours != 0.0 || minutes != 0.0 || seconds != 0.0)) {
            val totalHours = hours + minutes / 60 + seconds / 3600
            return distance / totalHours
        }

        return null
    }
}