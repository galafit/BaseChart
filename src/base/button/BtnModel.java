package base.button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdablin on 11.08.17.
 */
public class BtnModel {
    private List<StateListener> selectionListeners = new ArrayList<StateListener>();
    private BtnGroup group;

    public void setGroup(BtnGroup group) {
        this.group = group;
    }

    public void addListener(StateListener listener) {
       selectionListeners.add(listener);
    }

    public void setSelected(boolean isSelected) {
        if (group != null) {
            // use the group model instead
            boolean oldSelection = isSelected();
            if (oldSelection != isSelected) {
                group.setSelected(this, isSelected);
            }
            if (oldSelection != isSelected()) {
                for (StateListener listener : selectionListeners) {
                    listener.stateChanged(isSelected);
                }
            }
        }
    }

    public boolean isSelected() {
        if(group != null) {
            return group.isSelected(this);
        }
        return false;
    }
}
