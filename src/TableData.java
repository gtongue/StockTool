import javafx.beans.property.SimpleStringProperty;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Gtongue on 3/26/2017.
 */
public class TableData {
    private SimpleStringProperty text;

    public TableData()
    {
        text = new SimpleStringProperty("workPlease");
    }
    public TableData(String s)
    {
        text = new SimpleStringProperty(s);
    }
    public void setText(String s)
    {
        text.set(s);
    }
    public String getText()
    {
        return text.get();
    }
}
