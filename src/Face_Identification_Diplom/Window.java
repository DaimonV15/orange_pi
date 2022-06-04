package src.Face_Identification_Diplom;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.videoio.VideoCapture;

import java.util.Map;


public class Window {

    Face face = new Face();
    Mat frame = new Mat();
    Frames frames = new Frames();
    Mat src_frame = new Mat();
    int colOfPeople;
    MatOfRect rect = new MatOfRect();

    CascadePoint cascade = new CascadePoint();
    VideoCapture inputVideo = new VideoCapture();
    DataBase dataBase = new DataBase();

    //------------------

    public Window(int camera) {
        inputVideo.open(camera);
        cascade.cascadeLibrary();
    }

    public void windowRun() {
        dataBase.loadFeatureLibraryByJPG();
        if (inputVideo.isOpened()) {
            try {
                inputVideo.read(frame);
                if (!frame.empty()) {
                    System.out.println(frame.width() + ", " + frame.height());
                }
            } catch (Exception e) {
                System.err.println("CONVERT_TO_GRAY_LOAD_VIDEO: " + e.getMessage());
            }
        }
    }

    public void convertVideo() {
        while (true) {
            if (inputVideo.isOpened()) {
                try {
                    inputVideo.read(frame);
                    if (!frame.empty()) {
                        frame.copyTo(src_frame);
                        frames.frameColorConvert(frame);
                        frames.scaleBlur(frame, 1);
                        rect = cascade.findStartPoint(frame);
                        if ((colOfPeople = cascade.colOfPeople(rect)) >= 1) {
                            Mat feature = face.getFaceRecognitionFuture(src_frame, 0);
                            for (Map.Entry<String, Mat> entry : dataBase.hashmap.entrySet()) {
                                if (face.featureMatch(entry.getValue(), feature, 0) > 0.363 &&
                                        face.featureMatch(entry.getValue(), feature, 1) < 1.128) {
                                    System.out.println(entry.getKey());
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("CONVERT_TO_GRAY_LOAD_VIDEO: " + e.getMessage());
                }
            }
        }
    }
}
