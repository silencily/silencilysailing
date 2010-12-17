package net.silencily.sailing.utils;

/**
 * @author zhaoyf
 *
 */
import java.util.ArrayList;
import java.util.List;

public class PoemBeana {

    private List lines = new ArrayList();
    
    public List getLines() {
        return lines;
    }
    
    public void addLine(String line) {
        lines.add(line);
    }
}

