package client;

import javax.swing.*;
import java.awt.*;

public class SearchTextLine extends DefaultListCellRenderer {

    String searchingText;
    public SearchTextLine(String searchingText){
        this.searchingText = searchingText;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value.toString().contains(searchingText)) {
            Color fg = Color.GREEN;
            setForeground(fg);
        }
        return this;
    }
}