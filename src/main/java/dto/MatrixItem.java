package dto;

public class MatrixItem {
    boolean hasValue;
    boolean isValidCell;
    String value;
    String position;

    public boolean isHasValue() {
        return hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    public boolean isValidCell() {
        return isValidCell;
    }

    public void setValidCell(boolean validCell) {
        isValidCell = validCell;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
