import java.awt.image.BufferedImage

interface Artist {
    fun drawMeAnImage(img:BufferedImage): BufferedImage
}