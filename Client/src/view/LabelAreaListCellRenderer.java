package view;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class LabelAreaListCellRenderer extends DefaultListCellRenderer {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LabelAreaListCellRenderer(){
        super();
    }
   
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean hasFocus) {
       
        if(JLabel.class.isInstance(value))
            return (JLabel)value;
       
        return super.getListCellRendererComponent(list, value,
                index, isSelected, hasFocus);
    }
   
}
