package utilities;

/**
 * Contains the attributes of a graphmlKey as Strings
 * @author jsalam
 *
 */
public class GraphmlKey {
	String name;
	String type;
	String element;
	String id;

	public GraphmlKey() {

	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getElement() {
		return element;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNodeKey() {
		if (element.equals("node")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEdgeKey() {
		if (element.equals("edge")) {
			return true;
		} else {
			return false;
		}
	}
}
