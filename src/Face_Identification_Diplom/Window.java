package src.Face_Identification_Diplom;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.util.Map;


public class Window {

    Face face = new Face();
    Mat frame = new Mat();
    Frames frames = new Frames();
    Mat src_frame = new Mat();
    int colOfPeople;
    MatOfRect rect = new MatOfRect();
    Scalar colorOfRectangle = new Scalar(0, 255, 0);

    CascadePoint cascade = new CascadePoint();
    VideoCapture inputVideo = new VideoCapture();
    DataBase dataBase = new DataBase();

    JFrame jframe = new JFrame("");
    JLabel jpanel = new JLabel();

    //------------------
    Mat dataBaseFuture = face.getFaceRecognitionFuture(frames.ImageToMat("C:/Users/dimit/IdeaProjects/orange_pi/src/PhotoBase/ME.jpg"), colOfPeople);
    String name = "Daimon";
    //------------------

    public Window(int camera) {
        inputVideo.open(camera);
        cascade.cascadeLibrary();
    }

    public void windowRun() {
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setContentPane(jpanel);
        jframe.setVisible(true);
        dataBase.loadFeatureLibraryByJPG();
        if (inputVideo.isOpened()) {
            try {
                inputVideo.read(frame);
                if (!frame.empty()) {
                    jframe.setSize(frame.width(), frame.height());
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
                            src_frame = frames.drawRectangle(src_frame, cascade.getPt1(rect, 0), cascade.getPt2(rect, 0), colorOfRectangle, 1);
                            for (Map.Entry<String, Mat> entry : dataBase.hashmap.entrySet()) {
                                if (face.featureMatch(entry.getValue(), feature, 0) > 0.363 &&
                                        face.featureMatch(entry.getValue(), feature, 1) < 1.128) {
                                    cascade.inputText(src_frame, 0, entry.getKey());
                                    break;
                                }
                            }
                            ImageIcon image = new ImageIcon(frames.MatToImage(src_frame));
                            jpanel.setIcon(image);
                            jpanel.repaint();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("CONVERT_TO_GRAY_LOAD_VIDEO: " + e.getMessage());
                }
            }
        }
    }
}
