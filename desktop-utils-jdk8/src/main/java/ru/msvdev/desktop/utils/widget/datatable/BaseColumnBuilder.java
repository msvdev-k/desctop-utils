package ru.msvdev.desktop.utils.widget.datatable;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import lombok.Setter;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;


@Setter
public abstract class BaseColumnBuilder<ROW, CELL extends CellModel<?>> {

    private String name;
    private double prefWidth;
    private boolean editable;


    public TableColumn<ROW, CELL> build() {
        TableColumn<ROW, CELL> column = new TableColumn<>(name);

        column.setPrefWidth(prefWidth);
        column.setEditable(editable);

        column.setCellValueFactory(this::cellValueFactory);
        column.setCellFactory(this::cellFactory);

        EditEvent<ROW, CELL> editEvent = new EditEvent<>(this::modelValueSetter);
        column.setOnEditCommit(editEvent);
        column.setOnEditCancel(editEvent);

        return column;
    }

    protected abstract void modelValueSetter(ROW row, CELL cell);

    protected abstract ObservableValue<CELL> cellValueFactory(TableColumn.CellDataFeatures<ROW, CELL> cellDataFeatures);

    protected abstract TableCell<ROW, CELL> cellFactory(TableColumn<ROW, CELL> tableColumn);

}
