package ru.msvdev.desktop.utils.widget.datatable.cell.model;

import java.time.LocalDate;


public class DateCellModel extends CellModel<LocalDate> implements Cloneable {

    @Override
    public DateCellModel clone() {
        try {
            return (DateCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
