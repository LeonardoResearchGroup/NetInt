package executable;

import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphController;
import org.gephi.project.api.ProjectController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.io.importer.api.Container;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.gephi.io.importer.api.ImportController;
import java.io.File;

public class ArcDiagram {

	private GraphModel graphModel;

	public void setUp() {
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		projectController.newWorkspace(projectController.getCurrentProject());

		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container;
		try {
			File file = new File(getClass().getResource("/org/gephi/dynamic/test_graph.gexf").toURI());
			container = importController.importFile(file);
		} catch (Exception ex) {
			Exceptions.printStackTrace(ex);
			return;
		}

		importController.process(container, new DefaultProcessor(), projectController.getCurrentWorkspace());

		GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
		graphModel = graphController.getGraphModel();
	}

	public static void main(String[] args) {

	}

}
