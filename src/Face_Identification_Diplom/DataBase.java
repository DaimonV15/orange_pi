package src.Face_Identification_Diplom;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataBase {

    Map<String, Mat> hashmap = new HashMap<String, Mat>();
    Face face = new Face();
    Frames frames = new Frames();

/*    private static  final String directorySrc = "C:/Users/dimit/IdeaProjects/Face_identification/src/Face_Identification_OpenCV";
    private static  final String directoryFeatureBase = "C:/Users/dimit/IdeaProjects/Face_identification/src/FeatureBase";*/

    private static  final String directorySrc = "C:/Users/dimit/IdeaProjects/orange_pi/src/Face_Identification_OpenCV";
    private static  final String directoryFeatureBase = "C:/Users/dimit/IdeaProjects/orange_pi/src/FeatureBase";
    private static  final String directoryPhotoBase = "C:/Users/dimit/IdeaProjects/orange_pi/src/PhotoBase";

    public final boolean checkPath(String pathName) {
        File path = new File(pathName);
        if (pathName.isEmpty()) {
            System.out.println("String is empty!");
            return true;
        }
        if (!path.exists()) {
            System.out.println("Directory is not exist!");
            return true;
        }
        if (path.length() == 0) {
            System.out.println("Directory is empty!");
            return false;
        }
        for (File item : path.listFiles()) {
            if (item.isFile()) {
                System.out.println("FILE: " + item.getName());
            }
            if (item.isDirectory()) {
                System.out.println("DIRECTORY: " + item.getName());
            }
        }
        return false;
    }

    public final boolean checkFile(String pathName, String fileName) {
        File file = new File(pathName + "/" + fileName);
        if (!file.exists()) {
            System.out.println("File not found");
            return false;
        }
        return true;
    }

    public final void saveImage (Mat frame, String fileName, String directory) {
        Imgcodecs.imwrite(directory + "/" + fileName + ".jpg", frame);
    }

    public final void loadFeatureLibrary () throws IOException {
        if (!checkFile(directorySrc, "config.txt")) {
            return;
        }
        File file = new File(directorySrc + "/config.txt");
        FileReader reader = new FileReader(file);
        Scanner scan = new Scanner(reader);
        while (scan.hasNextLine()) {
            float[] arr = new float[128];
            int i = 0;
            String string = scan.nextLine();
            Scanner string_scan = new Scanner(string);
            String string2 = string.substring(string.indexOf("[")+1, string.lastIndexOf("]"));
            String[] values = string2.split(", ");
            for (String value : values) {
                arr[i] = Float.parseFloat(value);
                i++;
            }
            Mat frame = new Mat(1, 128, CvType.CV_32FC1);
            frame.put(0, 0, arr);
            hashmap.put(string_scan.next(), frame);
        }
        reader.close();
        System.out.println("library loaded!");
    }

    public final void addToFeatureLibrary (Mat frame, String name) throws IOException {
        if (!checkFile(directorySrc, "config.txt")) {
            return;
        }
        for (String key: hashmap.keySet()) {
            if (key.equals(name)) {
                System.out.println("This profile already exist!");
                return;
            }
        }
        hashmap.put(name, frame);
        File file = new File(directorySrc + "/config.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write(name + " = " + frame.dump() + "\n");
        writer.flush();
    }

    public final void loadFeatureLibraryByJPG () {
        Frames frames = new Frames();
        if (checkPath(directoryPhotoBase)) {
            System.out.println("Something went wrong!");
            return;
        }
        File path = new File(directoryPhotoBase);
        for (File item : path.listFiles()) {
            if (item.isFile()) {
                hashmap.put(item.getName(), face.getFaceRecognitionFuture(
                        frames.ImageToMat(path + "/" + item.getName()), 0));
            }
        }
    }

    public final void addToFeatureLibraryByJPG (Mat frame, String name) {
        if (checkFile(directoryPhotoBase, name + ".jpg")) {
            System.out.println("This profile already exist!");
            return;
        }
        for (String key: hashmap.keySet()) {
            if (key.equals(name)) {
                System.out.println("This profile already exist!");
                return;
            }
        }
        hashmap.put(name, frame);
        saveImage(frame, name, directoryPhotoBase);
        System.out.println("File was added!");
    }
}
