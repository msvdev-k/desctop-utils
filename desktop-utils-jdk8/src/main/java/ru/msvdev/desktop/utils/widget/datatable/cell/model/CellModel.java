package ru.msvdev.desktop.utils.widget.datatable.cell.model;

import lombok.Getter;
import lombok.Setter;


/**
 * Базовый класс, описывающий ячейку таблицы данных
 *
 * @param <T> тип данных ассоциированных с ячейкой
 */
@Getter
@Setter
public abstract class CellModel<T> {

    private T value;

    private CellWarning warning;
    private CellError error;

    {
        value = null;
        warning = null;
        error = null;
    }


    public void resetWarning() {
        warning = null;
    }

    public boolean isWarning() {
        return warning != null;
    }

    public String getWarningDescription() {
        return warning.warningDescription;
    }

    public void resetError() {
        this.error = null;
    }

    public boolean isError() {
        return error != null;
    }

    public String getErrorText() {
        return error.errorText;
    }

    public String getErrorDescription() {
        return error.errorDescription;
    }


    /**
     * Список ошибок, которые могут возникать при изменении данных в ячейке таблицы
     */
    public enum CellError {

        ERROR("ОШИБКА", "Неизвестна ошибка"),
        REQUIRED("*", "Обязательное значение"),
        NUMBER("ЧИСЛО", "Недопустимое числовое значение"),
        NAME("ИМЯ", "Недопустимое строковое значение"),
        DATE("ДАТА", "Недопустимое значение даты"),
        UNIQUE("УНИК", "Значение должно быть уникальным в пределах текущего столбца"),
        EMPTY("ПУСТО", "Строка не может быть пустой или состоять из пробельных символов");

        public final String errorText;
        public final String errorDescription;

        CellError(String errorText, String errorDescription) {
            this.errorText = errorText;
            this.errorDescription = errorDescription;
        }
    }


    /**
     * Список предупреждений, которые могут возникать при изменении данных в ячейке таблицы
     */
    public enum CellWarning {

        NO_SYNC("Значение не синхронизовано с БД");

        public final String warningDescription;

        CellWarning(String warningDescription) {
            this.warningDescription = warningDescription;
        }
    }
}
