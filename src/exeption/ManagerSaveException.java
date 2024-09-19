package exeption;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ManagerSaveException saveException(IOException e) {
        return new ManagerSaveException("Ошибка при сохранении файла", e);
    }

    public static ManagerSaveException loadException(IOException e) {
        return new ManagerSaveException("Ошибка при загрузке данных из файла", e);
    }

}
