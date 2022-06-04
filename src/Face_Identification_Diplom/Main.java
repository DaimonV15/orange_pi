package src.Face_Identification_Diplom;

import org.opencv.core.Core;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Window mainCycle = new Window(); //Вызов инициализации камеры
        mainCycle.windowRun(); //Загрузка библиотеки/проверка работы
        mainCycle.convertVideo(); //Вызов основного цикла
    }
}
