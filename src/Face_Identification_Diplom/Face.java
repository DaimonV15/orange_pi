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

    private static final String pathModel = "/home/daimon/orange_pi/orange_pi/face_recognizer_fast.onnx"; //Расположение onnx файла
    CascadePoint cascadePoint = new CascadePoint(); //Объявляеся класс

    public final Mat getFaceRecognitionFuture (Mat frame, int colOfPeople) {
        Mat aligned_image = new Mat();
        Mat feature = new Mat();
        FaceRecognizerSF faceRecognizerSF = FaceRecognizerSF.create(pathModel, new String("")); //Вызываем библиотеку (Необходимо переопределять её)
        Frames frames = new Frames();
        Mat box_image = repaintFace(frame, colOfPeople); //Получаем матрицу обнаруженного лица
        frames.resize(box_image); //Изменяем её размер
        //faceRecognizerSF.alignCrop(frame, box_image, aligned_image); //Выравниваем лицо
        faceRecognizerSF.feature(box_image, feature); //Получаем 128х1 матрицу характеристик лица
        //faceRecognizerSF.feature(aligned_image, feature);
        return feature;
    } //Получем матричное представление характеристик лица

    public final Mat repaintFace (Mat frame, int colOfPeople) {
        cascadePoint.cascadeLibrary();
        MatOfRect matofrect = cascadePoint.findStartPoint(frame); //Получаем координаты лица
        Rect[] rect = matofrect.toArray(); //Представление матрицы в виде массива
        Mat faceMat = frame.submat(rect[colOfPeople]); //Удобное копирование матрицы из массива :)
        return faceMat;
    } //Получаем матрицу лица

    public final double featureMatch (Mat feature1, Mat feature2, int type) {
        FaceRecognizerSF faceRecognizerSF = FaceRecognizerSF.create(pathModel, new String("")); //Необходимо переопределить библиотеку
        return faceRecognizerSF.match(feature1, feature2, type); //Сравнение
    } //Функция сравнненния
}