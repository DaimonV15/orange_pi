package src.Face_Identification_Diplom;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class CascadePoint {

    Frames frames = new Frames();
    CascadeClassifier cascade = new CascadeClassifier();
    Point pt1 = new Point();
    Point pt2 = new Point();
    MatOfRect matofrect = new MatOfRect();
    private static final String pathModel = "C:/Users/dimit/IdeaProjects/orange_pi/haarcascade_frontalface_default.xml";

    public final void cascadeLibrary (String link) {
        if (!cascade.load(link)) {
            System.out.println("Can't load " + link);
        }
    }

    public final void cascadeLibrary () {
        if (!cascade.load(pathModel)) {
            System.out.println("Can't load " + "haarcascade_frontalface_default.xml");
        }
        //src/haarcascade_frontalface_default.xml
    }

    public final MatOfRect findStartPoint (Mat frame) {
        cascade.detectMultiScale(frame, matofrect, 1.1, 1);
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
    }

    public final Point getPt1 (MatOfRect matofrect, int numberOfFace) {
        Rect[] rect = matofrect.toArray();
        pt1.x = rect[numberOfFace].x;
        pt1.y = rect[numberOfFace].y;
        return pt1;
    }

    public final Point getPt2 (MatOfRect matofrect, int numberOfFace) {
        Rect[] rect = matofrect.toArray();
        pt2.x = rect[numberOfFace].x + rect[numberOfFace].width;
        pt2.y = rect[numberOfFace].y + rect[numberOfFace].height;
        return pt2;
    }

    public final Mat inputText (Mat frame, int colOfPeople, String text) {
        Point Pt = getPt1(matofrect, colOfPeople);
        Rect[] rect = matofrect.toArray();
        Pt.x = Pt.x + (rect[colOfPeople].width)/10;
        Pt.y = Pt.y + rect[colOfPeople].height;
        Imgproc.putText(frame, text, Pt, 0, 1, new Scalar(0, 255, 0), 2, -1, false);
        return frame;
    }

}
