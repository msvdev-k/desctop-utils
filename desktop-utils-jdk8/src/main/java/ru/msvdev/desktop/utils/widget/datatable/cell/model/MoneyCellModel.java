package ru.msvdev.desktop.utils.widget.datatable.cell.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class MoneyCellModel extends CellModel<BigDecimal> implements Cloneable {

    public static final String RUBLE_SIGN = "₽";// \u20BD

    private String sign = "";

    private Currency currency;


    @Override
    public MoneyCellModel clone() {
        try {
            return (MoneyCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Валюта
     */
    public enum Currency {

        RUBLE("₽");

        public final String sign;

        Currency(String sign) {
            this.sign = sign;
        }
    }
}
