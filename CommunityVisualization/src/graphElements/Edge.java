package graphElements;

import java.util.Observable;
import java.util.Observer;

import utilities.HopMessenger;

/**
 * @author jsalam
 *
 */
public class Edge implements Observer {

	private Node source;
	private Node target;
	private boolean directed;
	private float weight;
	private String name;
	private boolean loop;

	private int mark;


	public Edge(Node source, Node target, boolean directed) {
		this.source = source;
		this.target = target;
		this.directed = directed;
		// Observer
		this.source.addObserver(this);
		//this.target.addObserver(this);

		if (source.equals(target))
			loop = true;

	}

	// **** Getters and Setters

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	//
	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public boolean isDirected() {
		return directed;
	}

	public float getWeight() {
		return weight;
	}

	public String getName() {
		return name;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoop() {
		return loop;
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof HopMessenger) {

			// Node test = (Node)o;
			// System.out.println("node: " + test.getId());
			HopMessenger heapMsn = ((HopMessenger) arg);

			// If mouse over
//			System.out.println("current value: " + heapMsn.getCurrentHop());
//			System.out.println("-----------LOG-----------");
//			System.out.println(heapMsn.getLog());
//			System.out.println("-------------------------");
			if (heapMsn.getMessage().contains("selected")) {
				mark = 1;
				// if source
				if (source.equals(o)) {
					mark = 2;
					// if target
				} else if (target.equals(o)) {
					mark = 3;
				}

				// if(o instanceof Node){
				// Node t = (Node)o;
				//
				int currentValue = heapMsn.getCurrentHop();
				if (currentValue < heapMsn.getHops()) {
					currentValue++;
					heapMsn.setCurrentHope(currentValue);
					heapMsn.addToLog("ID:" + target.getId() + "val: " + currentValue + " // " + this);
					target.change();
					target.notifyObservers(heapMsn);
					// target.clear();
				} else {
//					System.out.println("FIN PROCESO");
				}
				// }

				// if mouse not over
			} else if (heapMsn.getMessage().contains("free")) {
				mark = 0;

				int currentValue = heapMsn.getCurrentHop();
				if (currentValue < heapMsn.getHops()) {
					currentValue++;
					heapMsn.setCurrentHope(currentValue);
					heapMsn.addToLog("ID:" + target.getId() + "val: " + currentValue + " // " + this);
					target.change();
					target.notifyObservers(heapMsn);
					// target.clear();
				} else {
//					System.out.println("FIN PROCESO");
				}
			}
			// System.out.println(value);

		}

	}

}
