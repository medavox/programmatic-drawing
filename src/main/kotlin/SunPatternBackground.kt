import java.awt.Color
import java.awt.Polygon
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import kotlin.math.roundToInt


class SunPatternBackground():Artist {

    private val MY_RED = Color(0xE04444)
    private val MY_WHITE = Color(0xFFFFDD)
    override fun drawMeAnImage(img:BufferedImage): BufferedImage {
        val g = img.createGraphics()

        g.color = MY_RED
        val polygons = drawSunPattern(
            centrePoint = Point2D.Double(img.width.toDouble() /2.0, img.height.toDouble() /2.0),
            drawDistanceFromCentre = 80.0,
            barWidthDeg = 20.0,
            arcSliceDeg = 360.0,
            barLength = 3000.0
        )
        for( polygon:Polygon in polygons) {
            g.color = if(g.color == MY_RED) MY_WHITE else MY_RED
            g.fillPolygon(polygon)
        }

        return img
    }

    /**Draw a star-ish shaped pattern. I don't know the name of it.
     * @param centrePoint the place to start from
     * @param drawDistanceFromCentre pixels from the centrepoint to start drawing
     * @param barWidthDeg number of degrees each angle block lasts for
     * @param arcSliceRad how far around the circle to draw*/
    private fun drawSunPattern(centrePoint:Point2D.Double,
                               drawDistanceFromCentre:Double,
                               barWidthDeg:Double,
                               barLength:Double,
                               arcSliceDeg:Double
    ):List<Polygon> {
        var currentAngle = 0.0
        val polygons = mutableListOf<Polygon>()
        while(currentAngle < arcSliceDeg) {

            //points are numbered clockwise, starting with the innermost, lowest-angle point
            val points = Array<Polar>(4) { index ->
                when (index) {
                    0 -> Polar(angle = currentAngle, distance = drawDistanceFromCentre)
                    1 -> Polar(angle = currentAngle, distance = drawDistanceFromCentre + barLength)
                    2 -> Polar(angle = currentAngle + barWidthDeg, distance = drawDistanceFromCentre + barLength)
                    3 -> Polar(angle = currentAngle + barWidthDeg, distance = drawDistanceFromCentre)
                    else -> throw IllegalStateException()
                }
            }
            val poly = Polygon()
            for(point in points) {
                val cart = point.toCartesian() + centrePoint
                poly.addPoint(cart.x.roundToInt(), cart.y.roundToInt())
            }
            polygons.add(poly)
            currentAngle += barWidthDeg
        }
        return polygons
    }



    fun cartesianToPolar(x:Double, y:Double):Polar {
        val r = Math.sqrt(x * x + y * y)
        val theta = Math.toDegrees(Math.atan2(y, x))
        return Polar(r, theta)
    }


}

class Polar(val angle:kotlin.Double, val distance:kotlin.Double) : Point2D.Double(angle, distance) {
    fun toCartesian( reverse: Boolean = false ): Point2D.Double {
        val angleInRadians = Math.toRadians(angle)
        val x = Math.round(distance * Math.cos(angleInRadians) * 100.0) / 100.0
        val y = Math.round(distance * Math.sin(angleInRadians) * 100.0) / 100.0
        return if (reverse) {
            Point2D.Double(x, y * -1)
        } else Point2D.Double(x, y)
    }
}
private operator fun Point2D.Double.plus(other:Point2D.Double): Point2D.Double {
    return Point2D.Double(this.x + other.x, this.y + other.y)
}
class Cartesian(val x:Double, )