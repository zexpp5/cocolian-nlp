/**
 * 
 */
package org.cocolian.nlp.pos.chmm.npath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cocolian.nlp.pos.chmm.AbstractProcessor;
import org.cocolian.nlp.pos.chmm.NPathGenerator;
import org.cocolian.nlp.pos.chmm.POSTerm;
import org.cocolian.nlp.pos.chmm.TermEdge;
import org.cocolian.nlp.pos.chmm.TermGraph;
import org.cocolian.nlp.pos.chmm.TermPath;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;

/**
 * @author lixf
 * 
 */
public class AbstractNPathGenerator extends AbstractProcessor implements NPathGenerator {
	private static final Log log = LogFactory.getLog(AbstractNPathGenerator.class);
	private int pathCount;

	public AbstractNPathGenerator() {
		this.pathCount = 8;
	}

	@Override
	public List<TermPath> process(TermGraph graph) {
		this.scoreEdges(graph);
		return this.findNPath(graph, pathCount);
	}

	/**
	 * 寻找N条最短的路径。
	 * 
	 * @param graph
	 * @param count
	 *            最短路径条数；
	 * @return
	 */
	protected List<TermPath> findNPath(TermGraph graph, int count) {
		KShortestPaths<POSTerm, TermEdge> alg = new KShortestPaths<POSTerm, TermEdge>(graph, count);
		List<GraphPath<POSTerm, TermEdge>> pathes = alg.getPaths(graph.getStartVertex(), graph.getEndVertex());
		List<TermPath> result = new ArrayList<TermPath>();
		if(pathes==null) {
			log.error("Error in finding path for :"+ graph.getSource());
			return result;
		}
		for (GraphPath<POSTerm, TermEdge> path : pathes) {
			//log.info(path);
			result.add(graph.createPath(path));
		}
		return result;
	}

	/**
	 * 给边打分；
	 * 
	 * @param gp
	 */
	protected void scoreEdges(TermGraph gp) {
		for (TermEdge edge : gp.edgeSet()) {
			gp.setEdgeWeight(edge, 1.0);
		}
	}

}
