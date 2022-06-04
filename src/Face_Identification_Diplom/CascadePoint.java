package src.Face_Identification_Diplom;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class CascadePoint {

    CascadeClassifier cascade = new CascadeClassifier();
    Point pt1 = new Point();
    Point pt2 = new Point();
    MatOfRect matofrect = new MatOfRect();
    private static final String pathModel = "/home/daimon/orange_pi/haarcascade_frontalface_default.xml";

    public final void cascadeLibrary () {
        if (!cascade.load(pathModel)) {
            System.out.println("Can't load " + "haarcascade_frontalface_default.xml");
        }
    } //Загружаем каскады Хаара

    public final MatOfRect findStartPoint (Mat frame) {
        cascade.detectMultiScale(frame, matofrect, 1.1, 1); //Функция детекции лица при помощи каскадов Хаара
        //1.1 - коэф. увелечения каждого последующего прохода
        //1 - сколько лиц ищем
        if (matofrect.toArray().length <= 0) {
            System.out.println("MatOfRect is Empty!, no face detected on image");
            return matofrect;
        } else {
            System.out.println("Face detected");
        }
        return matofrect;
    }

    public final int colOfPeople (MatOfRect matofrect) {
        int numberOfFace = matofrect.toArray().length;
        return numberOfFace;
    } //Колличество людей на фрейме

    public final Point getPt1 (MatOfRect matofrect, int numberOfFace) {
        Rect[] rect = matofrect.toArray();
        pt1.x = rect[numberOfFace].x;
        pt1.y = rect[numberOfFace].y;
        return pt1;
    } //Определяем первую координату

    public final Point getPt2 (MatOfRect matofrect, int numberOfFace) {
        Rect[] rect = matofrect.toArray();
        pt2.x = rect[numberOfFace].x + rect[numberOfFace].width;
        pt2.y = rect[numberOfFace].y + rect[numberOfFace].height;
        return pt2;
    } //Определяем вторую координнату

    public final Mat inputText (Mat frame, int colOfPeople, String text) {
        Point Pt = getPt1(matofrect, colOfPeople);
        Rect[] rect = matofrect.toArray();
        Pt.x = Pt.x + (rect[colOfPeople].width)/10;
        Pt.y = Pt.y + rect[colOfPeople].height;
        Imgproc.putText(frame, text, Pt, 0, 1, new Scalar(0, 255, 0), 2, -1, false);
        return frame;
    } //Вписываем текст в окно лица
}
