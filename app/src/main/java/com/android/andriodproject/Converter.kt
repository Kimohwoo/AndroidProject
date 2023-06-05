import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import kotlin.math.*

const val NX = 149 /* X축 격자점 수 */
const val NY = 253 /* Y축 격자점 수 */

data class Converter(
    val Re: Float, /* 사용할 지구반경 [ km ] */
    val grid: Float, /* 격자간격 [ km ] */
    val slat1: Float, /* 표준위도 [degree] */
    val slat2: Float, /* 표준위도 [degree] */
    val olon: Float, /* 기준점의 경도 [degree] */
    val olat: Float, /* 기준점의 위도 [degree] */
    val xo: Float, /* 기준점의 X좌표 [격자거리] */
    val yo: Float, /* 기준점의 Y좌표 [격자거리] */
    var first: Int /* 시작여부 (0 = 시작) */
)

/******************************************************************************
 *
 * MAIN
 *
 *****************************************************************************/
fun main(args: Array<String>) {
    var lon = 0f
    var lat = 0f
    var x = 0f
    var y = 0f
    val map = LamcParameter(
        Re = 6371.00877f, // 지도반경
        grid = 5.0f, // 격자간격 (km)
        slat1 = 30.0f, // 표준위도 1
        slat2 = 60.0f, // 표준위도 2
        olon = 126.0f, // 기준점 경도
        olat = 38.0f, // 기준점 위도
        xo = (210 / map.grid).toInt(), // 기준점 X좌표
        yo = (675 / map.grid).toInt(), // 기준점 Y좌표
        first = 0
    )

    //
    // 인수 확인
    //

    if (args.size != 4) {
        println("[Usage] ${args[0]} 1 <X-grid><Y-grid>")
        println(" ${args[0]} 0 <longitude><latitude>")
        return
    }

    if (args[1].toInt() == 1) {
        x = args[2].toFloat()
        y = args[3].toFloat()

        if (x < 1 || x > NX || y < 1 || y > NY) {
            println("X-grid range [1,$NX] / Y-grid range [1,$NY]")
            return
        }
    } else if (args[1].toInt() == 0) {
        lon = args[2].toFloat()
        lat = args[3].toFloat()
    }

    //
    // 단기예보 지도 정보
    //

    mapConv(lon, lat, x, y, args[1].toInt(), map)

    if (args[1].toInt() == 1)
        println("X = ${x.toInt()}, Y = ${y.toInt()} --->lon.= $lon, lat.= $lat")
    else
        println("lon.= $lon, lat.= $lat ---> X = ${x.toInt()}, Y = ${y.toInt()}")
}

/*============================================================================*
 * 좌표변환
 *============================================================================*/
fun mapConv(
    lon: Float, // 경도(degree)
    lat: Float, // 위도(degree)
    x: Float, // X격자 (grid)
    y: Float, // Y격자 (grid)
    code: Int, // 0 (격자->위경도), 1 (위경도->격자)
    map: LamcParameter
): Int {
    var lon1 = 0f
    var lat1 = 0f
    var x1 = 0f
    var y1 = 0f

    //
    // 위경도 -> (X,Y)
    //

    if (code == 0) {
        lon1 = lon
        lat1 = lat
        lamcProj(lon1, lat1, x1, y1, 0, map)
        x1 = (x1 + 1.5f).toInt().toFloat()
        y1 = (y1 + 1.5f).toInt().toFloat()
        x = x1
        y = y1
    }

    //
    // (X,Y) -> 위경도
    //

    if (code == 1) {
        x1 = x - 1
        y1 = y - 1
        lamcProj(lon1, lat1, x1, y1, 1, map)
        lon = lon1
        lat = lat1
    }
    return 0
}

/***************************************************************************
 *
 * [ Lambert Conformal Conic Projection ]
 *
 * olon, lat : (longitude,latitude) at earth [degree]
 * o x, y : (x,y) cordinate in map [grid]
 * o code = 0 : (lon,lat) --> (x,y)
 * 1 : (x,y) --> (lon,lat)
 *
 ****************************************************************************/

fun lamcProj(
    lon: Float,
    lat: Float,
    x: Float,
    y: Float,
    code: Int,
    map: LamcParameter
): Int {
    var lon1 = 0.0
    var lat1 = 0.0
    var x1 = 0.0
    var y1 = 0.0

    if (map.first == 0) {
        val PI = asin(1.0) * 2.0
        val DEGRAD = PI / 180.0
        val RADDEG = 180.0 / PI

        val re = map.Re / map.grid
        val slat1 = map.slat1 * DEGRAD
        val slat2 = map.slat2 * DEGRAD
        val olon = map.olon * DEGRAD
        val olat = map.olat * DEGRAD

        val sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
        val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
        val ro = tan(PI * 0.25 + olat * 0.5).pow(sn) * sf

        map.first = 1
    }

    if (code == 0) {
        val ra = tan(PI * 0.25 + lat * DEGRAD * 0.5).pow(sn) * (map.Re / map.grid).toFloat() / tan(PI * 0.25 + map.slat1 * DEGRAD * 0.5).pow(sn)
        val theta = lon * DEGRAD - map.olon * DEGRAD
        val xResult = ra * sin(theta)
        val yResult = map.ro - ra * cos(theta)

        x1 = xResult.toDouble()
        y1 = yResult.toDouble()

        x = (x1 + map.xo).toFloat()
        y = (y1 + map.yo).toFloat()
    } else if (code == 1) {
        val xn = x - map.xo
        val yn = map.ro - y + map.yo
        val ra = sqrt(xn.pow(2) + yn.pow(2))
        val alat = (re * sf / ra.pow(sn)).pow(1.0 / sn)
        val alatResult = 2.0 * atan(alat) - PI * 0.5
        val theta = if (xn == 0.0) {
            0.0
        } else {
            if (yn == 0.0) {
                PI * 0.5 * if (xn < 0.0) -1 else 1
            } else {
                atan2(xn, yn)
            }
        }
        val alon = theta / sn + map.olon

        lon = (alon * RADDEG).toFloat()
        lat = (alatResult * RADDEG).toFloat()
    }

    return 0
}

data class LamcParameter(
    val Re: Float, /* 사용할 지구반경 [ km ] */
    val grid: Float, /* 격자간격 [ km ] */
    val slat1: Float, /* 표준위도 [degree] */
    val slat2: Float, /* 표준위도 [degree] */
    val olon: Float, /* 기준점의 경도 [degree] */
    val olat: Float, /* 기준점의 위도 [degree] */
    val xo: Float, /* 기준점의 X좌표 [격자거리] */
    val yo: Float, /* 기준점의 Y좌표 [격자거리] */
    var first: Int /* 시작여부 (0 = 시작) */
)