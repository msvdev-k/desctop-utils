package ru.msvdev.desktop.utils.widget.datatable;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;

import java.util.function.BiConsumer;


/**
 * Основной обработчик события изменения значения в ячейке
 *
 * @param <ROW>  тип описывающий строку таблицы
 * @param <CELL> тип описывающий ячейку в данной строке
 */
public class EditEvent<ROW, CELL extends CellModel<?>> implements EventHandler<TableColumn.CellEditEvent<ROW, CELL>> {

    private final BiConsumer<ROW, CELL> modelValueSetter;

    protected EditEvent(BiConsumer<ROW, CELL> modelValueSetter) {
        this.modelValueSetter = modelValueSetter;
    }


    @Override
    public void handle(TableColumn.CellEditEvent<ROW, CELL> event) {
        CELL newCellModel = event.getNewValue();

        if (newCellModel != null) {
            modelValueSetter.accept(event.getRowValue(), newCellModel);

        } else {
            event.getTableView().refresh();
        }

        event.getTableView().requestFocus();
    }

}
