package ru.msvdev.desktop.utils.widget.datatable;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import lombok.Setter;


@Setter
public abstract class BaseIdColumnBuilder<ROW, CELL extends Number> {

    private String name;
    private double prefWidth;


    public TableColumn<ROW, CELL> build() {
        TableColumn<ROW, CELL> column = new TableColumn<>(name);

        column.setPrefWidth(prefWidth);
        column.setEditable(false);

        column.setCellValueFactory(this::cellValueFactory);
        column.setCellFactory(this::cellFactory);

        column.setEditable(false);

        return column;
    }

    protected abstract ObservableValue<CELL> cellValueFactory(TableColumn.CellDataFeatures<ROW, CELL> cellDataFeatures);

    protected TableCell<ROW, CELL> cellFactory(TableColumn<ROW, CELL> tableColumn) {
        return new TableCell<ROW, CELL>() {

            {
                setAlignment(Pos.TOP_CENTER);
            }

            @Override
            protected void updateItem(CELL item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                }
            }
        };
    }

}
