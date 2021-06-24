import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.time.Instant
import javax.imageio.ImageIO
import kotlin.random.Random

private val chars = "qwertyuiopasdfghjklzxcvbnm"

val width = 6000
val height = 6000
fun main(args:Array<String>) {
    val drawer = SunPatternBackground()
    val img = createImage(width, height)
    val finito = drawer.drawMeAnImage(img)
    writeToDisk(finito)
}

private fun writeToDisk(img:BufferedImage):Boolean {
    var fileName = Instant.now().toString().replace('-', '_').replace(':', '-')
    System.out.println("file name: $fileName")
    try {
        while(File(fileName+".png").exists()) {
            fileName += chars[Random.nextInt()]
        }
        val outputFile =  File(fileName+".png")
        outputFile.createNewFile()
        ImageIO.write(img, "png", outputFile)
    } catch ( e:IOException) {
        e.printStackTrace()
        return false
    }
    return true
}

private fun createImage(width:Int, height:Int):BufferedImage {
    return BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
}