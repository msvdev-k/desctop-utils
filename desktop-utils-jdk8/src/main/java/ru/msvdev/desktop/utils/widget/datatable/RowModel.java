package ru.msvdev.desktop.utils.widget.datatable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Setter;
import ru.msvdev.desktop.utils.task.TaskBuilder;

import java.util.function.Consumer;


/**
 * Базовый класс, описывающий строку данных
 *
 * @param <ROW> класс описывающий одну строку данных
 * @param <ID>  тип идентификатора данных в БД
 */
public abstract class RowModel<ROW extends RowModel<? super ROW, ID>, ID> {

    @Setter
    protected TaskBuilder taskBuilder;
    @Setter
    protected Consumer<ROW> saveRowEventListener;

    protected final ObjectProperty<ID> id = new SimpleObjectProperty<>();

    public ObjectProperty<ID> idProperty() {
        return id;
    }

}
