package src.Face_Identification_Diplom;

import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.*;
import java.util.ArrayList;

public class Frames {

    public final Image MatToImage (Mat frame) {
        return HighGui.toBufferedImage(frame);
    } //Представление матрицы в изображение

    public final Mat ImageToMat (String links) {
        Mat returnMat = new Mat();
        try
        {
            if (links.equals("")) {
                throw new Exception("String is Empty");
            }
            boolean temp = Imgcodecs.haveImageReader(links);
            if (temp) {
                returnMat = Imgcodecs.imread(links); //конвертация
            } else {
                System.err.println("image missing");
            }
            if (returnMat.empty()) {
                throw new Exception("link is wrong! Change folders/fileName");
            }
        }
        catch (Exception e) {
            System.err.println("IMAGE_TO_MAT: " + e.getMessage());
        }
        return returnMat;
    } //Представление изображения в матричной форме

    public final Mat frameColorConvert (Mat frameColorConvertFrame) {
        Imgproc.cvtColor(frameColorConvertFrame, frameColorConvertFrame, Imgproc.COLOR_BGR2GRAY);
        return frameColorConvertFrame;
    } //RGB -> G // 3 -> 1

    public final Mat drawRectangle (Mat drawRectangleFrame, Point pt1, Point pt2, Scalar color, int thickness) {
        Imgproc.rectangle(drawRectangleFrame, pt1, pt2, color, thickness);
        return drawRectangleFrame;
    } //Отрисовка квадрата вокруг полученных координат лица

    public final Mat scaleBlur (Mat scaleBlurFrame, int degree) {
        Size size = new Size();
        switch (degree) {
            case 1:
                size.height = 3;
                size.width = 3;
                break;
            case 2:
                size.height = 5;
                size.width = 5;
                break;
            case 3:
                size.height = 7;
                size.width = 7;
                break;
            case 4:
                size.height = 9;
                size.width = 9;
                break;
            case 5:
                size.height = 11;
                size.width = 11;
                break;
            default:
                size.height = 1;
                size.width = 1;
                break;
        }
        Imgproc.GaussianBlur(scaleBlurFrame, scaleBlurFrame, size, 0);
        return scaleBlurFrame;
    } //Размытие изображение для меньшего времени распознования + убирает некоторые проблемы с освещенностью

    public final Mat scaleCanny (Mat scaleCannyFrame, int min, int max) {
        Imgproc.Canny(scaleCannyFrame, scaleCannyFrame, min, max);
        return scaleCannyFrame;
    } //Выделяет границы объекта

    public final void resize (Mat frame) {
        Imgproc.resize(frame, frame, new Size(112, 112));
    } //Преобразование марицы в одинаковый для всех размер
}
