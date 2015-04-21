
import java.util.ArrayList;
import org.math.plot.*;
import javax.swing.JFrame;

public class FrequencyPlot {

	public FrequencyPlot(double[] x, double[] y)
	{
		// create your PlotPanel (you can use it as a JPanel)
		Plot2DPanel plot = new Plot2DPanel();

		// add a line plot to the PlotPanel
		plot.addLinePlot("my plot", x, y);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 480);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

	public static void main(String[] arg) {
		
		double[] x = {1.0, 2.0, 3.0};
		double[] y = {5.0, 15.0, 5.0};

		FrequencyPlot plot = new FrequencyPlot(x, y);

	}
}