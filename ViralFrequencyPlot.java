
import java.util.ArrayList;
import org.math.plot.*;
import javax.swing.JFrame;

public class ViralFrequencyPlot {

	Plot2DPanel plot;

	public ViralFrequencyPlot()
	{
		// create your PlotPanel (you can use it as a JPanel)
		plot = new Plot2DPanel();
	}

	public void addPlot(String title, double[] x, double[] y) {
		// Add line plot to the plot panel
		plot.addLinePlot(title, x, y);
	}

	public void showPlot() {
		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("Frequency plot");
		frame.setSize(1024, 768);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		double[] x = {1.0, 2.0, 3.0};
		double[] y = {5.0, 15.0, 5.0};

		FrequencyPlot plot = new FrequencyPlot(x, y);

	}
}