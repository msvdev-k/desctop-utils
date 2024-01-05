package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;


/**
 * Базовый класс, описывающий ячейки таблицы
 *
 * @param <ROW>  тип данных, описывающий строку
 * @param <CELL> тип данных, описывающих ячейку
 */
public abstract class BaseTableCell<ROW, CELL extends CellModel<?>> extends TableCell<ROW, CELL> {

    private final static String ERROR_STYLE = "-fx-text-fill: rgb(156,0,6); -fx-background-color: rgb(255,199,206);";
    private final static String WARNING_STYLE = "-fx-text-fill: rgb(156,101,0); -fx-background-color: rgb(255,235,156);";


    /**
     * Получить узел для редактирования ячейки
     *
     * @return узел для редактирования ячейки
     */
    protected abstract Node getEditor();

    /**
     * Получить узел для отображения ячейки
     *
     * @return узел для отображения ячейки
     */
    protected abstract Node getGraphicToView();

    /**
     * Получить текст для отображения данных
     *
     * @return текст для отображения данных
     */
    protected abstract String getTextToView();

    /**
     * Положение выравнивания содержимого ячейки
     *
     * @return описание вертикального и горизонтального положения
     */
    protected abstract Pos getAlignmentToView();


    private void resetView() {
        setText(null);
        setGraphic(null);
        setStyle(null);
        setTooltip(null);
    }


    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            resetView();

            Node editor = getEditor();
            setGraphic(editor);
            editor.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        resetView();
        setText(getTextToView());
        setGraphic(getGraphicToView());
    }


    @Override
    public void updateItem(CELL item, boolean empty) {
        super.updateItem(item, empty);
        resetView();

        if (empty || item == null) {
            return;
        }

        if (item.isError()) {
            setText(item.getErrorText());
            setStyle(ERROR_STYLE);
            setTooltip(new Tooltip(item.getErrorDescription()));
            setAlignment(Pos.TOP_CENTER);
            return;
        }

        if (item.isWarning()) {
            setStyle(WARNING_STYLE);
            setTooltip(new Tooltip(item.getWarningDescription()));
        }

        setText(getTextToView());
        setGraphic(getGraphicToView());
        setAlignment(getAlignmentToView());
    }
}
