package src.Face_Identification_Diplom;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.FaceRecognizerSF;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Face {

    Map<String, Mat> hashmap = new HashMap<String, Mat>();

    public static final double cos_thresh = 0.363;
    public static final double L2_thresh = 1.128;

    private static final String pathModel = "/home/daimon/orange_pi/orange_pi/face_recognizer_fast.onnx";
    CascadePoint cascadePoint = new CascadePoint();

    public final Mat getFaceRecognitionFuture (Mat frame, int colOfPeople) {
        Mat aligned_image = new Mat();
        Mat feature = new Mat();
        FaceRecognizerSF faceRecognizerSF = FaceRecognizerSF.create(pathModel, new String(""));
        Frames frames = new Frames();
        Mat box_image = repaintFace(frame, colOfPeople);
        frames.resize(box_image);
        //faceRecognizerSF.alignCrop(frame, box_image, aligned_image);
        faceRecognizerSF.feature(box_image, feature);
        //faceRecognizerSF.feature(aligned_image, feature);
        return feature;
    }

    public final Mat repaintFace (Mat frame, int colOfPeople) {
        cascadePoint.cascadeLibrary();
        MatOfRect matofrect = cascadePoint.findStartPoint(frame);
        Rect[] rect = matofrect.toArray();
        Mat faceMat = frame.submat(rect[colOfPeople]);
        return faceMat;
    }

    public final double featureMatch (Mat feature1, Mat feature2, int type) {
        FaceRecognizerSF faceRecognizerSF = FaceRecognizerSF.create(pathModel, new String(""));
        return faceRecognizerSF.match(feature1, feature2, type);
    }
}