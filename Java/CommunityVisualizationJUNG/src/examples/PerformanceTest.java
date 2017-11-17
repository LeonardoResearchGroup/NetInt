package examples;

import netInt.GraphPad_Workbench;
import netInt.canvas.Canvas;
import processing.core.PApplet;


/**
 * Aqui tenemos tres formas de pintar 8000 elipses. Se crea una clase circulo y
 * se prueba en los siguientes escenarios
 * 
 * 1. Cada circulo dibuja su propia geometría utilizando el metodo ellipse
 * 
 * 2. Cada circulo tiene referencia a un único PShape
 * 
 * 3.Cada circulo tiene su propio PShape
 * 
 * #### Resultados
 * 
 * 1. es el mas rápido en framerate y el segundo en términos de memoria
 * 
 * 2. es ligeramente mas lento que 1, pero ocupa menos memoria que todos
 * 
 * 3. Se demora en cargar, ocupa muchisima mas memoria y es el mas lento
 * 
 * #### Conclusion
 * 
 * Es mejor usar objetos gráficos muy ligeros, con la menor cantidad de
 * atributos para reducir el espacio en memoria. Definititivamente es mas rápido
 * pintar cada objeto directamente con los metodos primitivos de processing. La
 * opción de PShape impacta el desempeño.
 * 
 * @author jsalam
 *
 */
public class PerformanceTest extends PApplet {

	private GraphPad_Workbench pad;
	GraphicsMaker graphics;

	/**
	 * Required method from parent class. Define here the size of the
	 * visualization pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(1000, 800, P2D);
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		pad = new GraphPad_Workbench(this);
		graphics = new GraphicsMaker();
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(70);
		pad.show(graphics.getPGraphic());
		//graphics.show();
	}

	public static void main(String[] args) {
		PApplet.main("examples.PerformanceTest");
	}

}
