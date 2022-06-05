package src.Face_Identification_Diplom;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.videoio.VideoCapture;

import java.util.Map;


public class Window {

    //------------------
    //Объявление некоторых переменных:

    Face face = new Face();
    Mat frame = new Mat();
    Frames frames = new Frames();
    Mat src_frame = new Mat();
    int colOfPeople;
    MatOfRect rect = new MatOfRect();

    //------------------
    //Инициализация используемых классов:

    CascadePoint cascade = new CascadePoint();
    VideoCapture inputVideo = new VideoCapture();
    DataBase dataBase = new DataBase();

    //------------------

    public Window() {
        inputVideo.open("/dev/video0"); //Получем видеопоток с камеры
        cascade.cascadeLibrary(); //Загружаем каскады Хаара
    }

    public void windowRun() {
        dataBase.loadFeatureLibraryByJPG(); //Загружаем эталонные изображения которые уже на устройстве
        if (inputVideo.isOpened()) { //Проверка есть ли видеопоток
            try {
                inputVideo.read(frame); //Берем фрейм
                if (!frame.empty()) {
                    System.out.println(frame.width() + ", " + frame.height());
                }
            } catch (Exception e) {
                System.err.println("CHECK_FRAME_IN_WINDOW_RUN: " + e.getMessage());
            }
        }
    }

    public void convertVideo() {
        while (true) { //Цикл
            if (inputVideo.isOpened()) { //Проверка на получение потока
                try {
                    inputVideo.read(frame); //Забираем фрейм из видепотока
                    if (!frame.empty()) {
                        frame.copyTo(src_frame);
                        frames.frameColorConvert(frame); //Делаем матрицу с 1 слоем
                        frames.scaleBlur(frame, 1); //Размываем изображение
                        rect = cascade.findStartPoint(frame); //Ищем лицо нна изображении
                        if ((cascade.colOfPeople(rect)) >= 1) {
                            Mat feature = face.getFaceRecognitionFuture(src_frame, 0, rect); //Получаем характеристики лица в матричной форме 1x128
                            for (Map.Entry<String, Mat> entry : dataBase.hashmap.entrySet()) { //прогоняем по эталонной базе
                                if (face.featureMatch(entry.getValue(), feature, 0) > 0.363 &&
                                        face.featureMatch(entry.getValue(), feature, 1) < 1.128) { //сверка по 2ум коэфам
                                    System.out.println(entry.getKey()); //вывод имени кого распознала система
                                    //dataBase.switchRelay(); //(управляющее напряжение на реле (открытие замка)
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("MAIN_CYCLE_ERROR_IN_ConvertVideo: " + e.getMessage());
                }
            }
        }
    }
}
