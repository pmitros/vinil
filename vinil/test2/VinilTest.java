import java.applet.Applet;
import java.awt.*;
import vinil.*;

public class VinilTest extends Applet {
    TextField textField;
    TextArea textArea;
	VinilEnvironment env;

    public void init() {
		env = new VinilEnvironment();
        textField = new TextField(20);
        textArea = new TextArea(5, 20);
        textArea.setEditable(false);

        //Add Components to the Applet. 
        GridBagLayout gridBag = new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(textField, c);
        add(textField);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridBag.setConstraints(textArea, c);
        add(textArea);

        validate();
    }

    public boolean action(Event evt, Object arg) {
        String text = textField.getText();
		try
		{
			VinilElement e = VinilEnvironment.parse(text);
			if (e == null)
				textArea.setText("(null)");
			else
			{
				VinilElement f = env.interpret(e);
				if (f != null)
					textArea.setText(f.toString());
				else
					textArea.setText("(null)");
			}
		}
		catch (VinilException ex)
		{
			textArea.setText(ex.reason);
		}
        return true;
    }
}