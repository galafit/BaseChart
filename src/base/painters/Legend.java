package base.painters;

import base.button.BaseButton;
import base.button.BtnGroup;
import base.config.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hdablin on 11.08.17.
 */
public class Legend {
    private BtnGroup buttonGroup;
    private List<BaseButton> buttons = new ArrayList<BaseButton>();
    private LegendConfig legendConfig;
    private Rectangle area;
    private boolean isDirty = true;

    public Legend(LegendConfig legendConfig, BtnGroup buttonGroup) {
        this.legendConfig = legendConfig;
        this.buttonGroup = buttonGroup;
    }

    public boolean toggle(int x, int y) {
        for (BaseButton button : buttons) {
           if(button.contains(x, y)) {
               button.toggle();
               return true;
           }
        }
        return false;
    }

    public  void setArea(Rectangle area) {
        this.area = area;
    }

    public void add(BaseButton legendButton) {
        buttons.add(legendButton);
        buttonGroup.add(legendButton.getModel());
        legendButton.setBackground(legendConfig.getBackground());
        legendButton.setTextStyle(legendConfig.getTextStyle());
    }

    private void createAreas(Graphics2D g2) {
        Rectangle[] itemAreas = new Rectangle[buttons.size()];
        for (int i = 0; i < buttons.size(); i++) {
           itemAreas[i] = buttons.get(i).getBounds(g2);
        }
        int x = area.x;
        int y = area.y;
        for (int i = 0; i < buttons.size(); i++) {
            Rectangle itemArea = itemAreas[i];
            if(x + itemArea.width >= area.x + area.width) {
                x = area.x;
                y += itemArea.height;
                buttons.get(i).setLocation(x, y, g2);
            } else {
                buttons.get(i).setLocation(x, y, g2);
                x += itemArea.width + getInterItemSpace();
            }
        }
        isDirty = false;
    }


    public void draw(Graphics2D g2) {
        if (!legendConfig.isVisible() || buttons.size() == 0) {
            return;
        }
        if(isDirty) {
            createAreas(g2);
        }
        g2.setFont(legendConfig.getTextStyle().getFont());
        for (int i = 0; i < buttons.size(); i++) {
           buttons.get(i).draw(g2);
        }
    }

    private int getInterItemSpace() {
        return (int) (legendConfig.getTextStyle().getFontSize() * 0);
    }
}

