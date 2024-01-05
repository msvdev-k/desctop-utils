package ru.msvdev.desktop.utils.widget.datatable;

import javafx.scene.control.TableView;
import lombok.Setter;
import ru.msvdev.desktop.utils.task.TaskBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Базовый контроллер, управляющий таблицей данных
 *
 * @param <ROW> тип данных, представляющих строку таблицы
 * @param <ID>  тип идентификатора данных в БД
 */
public abstract class AbstractTableController<ROW extends RowModel<?, ID>, ID> {

    protected final TaskBuilder taskBuilder;

    protected final Map<ID, ROW> rows;

    /**
     * Новая строка в таблице. Строка не сохранена в БД и отсутствует в списке rows
     */
    protected ROW newRow;

    /**
     * Строка в таблице которая считается новой, но она сохранена в БД и в списке rows
     */
    protected ROW savedNewRow;

    @Setter
    protected TableView<ROW> tableView;

    {
        rows = new ConcurrentHashMap<>();
    }

    public AbstractTableController(TaskBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }


    public void initTable() {
        rows.clear();
        newRow = null;
        savedNewRow = null;

        tableView.getColumns().clear();
    }

    public void refresh() {
        rows.clear();
        newRow = null;
        savedNewRow = null;

        tableView.getItems().clear();
    }

    protected void saveRowEventListener(ROW rowModel) {
        if (rowModel == newRow) {
            savedNewRow = newRow;
            newRow = null;
            rows.put(rowModel.idProperty().get(), rowModel);
        }
    }

    public int getSelectedCount() {
        return tableView.getSelectionModel().getSelectedIndices().size();
    }

    abstract public void addNewRow();

    abstract public void removeSelected();
}
