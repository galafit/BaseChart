package configuration;

/**
 * Created by galafit on 5/9/17.
 */
public class AxisConfig {
    public Orientation orientation;
    public boolean isAxisVisible = true;

    public String name;
    public TextStyle nameTextStyle = new TextStyle();
    public int namePadding = (int)(0.8 * nameTextStyle.fontSize);
    public boolean isNameVisible = false;

    public LinesConfig linesConfig = new LinesConfig();
    public TicksConfig ticksConfig = new TicksConfig();

}
