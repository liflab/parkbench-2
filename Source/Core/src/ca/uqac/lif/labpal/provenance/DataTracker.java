/*
  LabPal, a versatile environment for running experiments on a computer
  Copyright (C) 2014-2017 Sylvain Hallé

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.labpal.provenance;

import java.util.LinkedList;
import java.util.List;

import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.table.Table;
import ca.uqac.lif.labpal.table.TableCellNode;

public class DataTracker
{
	/**
	 * The lab
	 */
	protected Laboratory m_lab;
	
	/**
	 * Creates a new data tracker
	 */
	public DataTracker(Laboratory lab)
	{
		super();
		m_lab = lab;
	}
	
	public Object getOwner(String id)
	{
		if (id.startsWith("T"))
		{
			// Is it a table?
			Table t = TableCellNode.getOwner(m_lab, id);
			if (t != null)
			{
				return t;
			}
		}
		if (id.startsWith("E"))
		{
			// Is it an experiment?
			Experiment e = ExperimentValue.getOwner(m_lab, id);
			if (e != null)
			{
				return e;
			}
		}
		return null;
	}
	
	public NodeFunction getNodeFunction(String datapoint_id)
	{
		Object owner = getOwner(datapoint_id);
		if (owner == null)
		{
			return null;
		}
		if (owner instanceof Table)
		{
			return TableCellNode.dependsOn((Table) owner, datapoint_id);
		}
		if (owner instanceof Experiment)
		{
			return ExperimentValue.dependsOn((Experiment) owner, datapoint_id);
		}
		return null;
	}
	
	public ProvenanceNode explain(String datapoint_id)
	{
		NodeFunction nf = getNodeFunction(datapoint_id);
		if (nf == null)
		{
			return new BrokenChain();
		}
		return explain(nf);
	}

	/**
	 * Builds a provenance tree for a given data point 
	 * @param id The ID of the data point
	 * @param added_ids A set of IDs already included in the tree. This
	 *   is to avoid infinite looping due to possible circular dependencies.
	 * @param factory A factory for creating instances of provenance nodes
	 *   with unique IDs
	 * @return The root of the provenance tree
	 */
	public ProvenanceNode explain(NodeFunction nf)
	{
		List<ProvenanceNode> nodes = new LinkedList<ProvenanceNode>();
		if (nf instanceof ExperimentValue)
		{
			// Leaf
			ProvenanceNode pn = new ProvenanceNode(nf);
			nodes.add(pn);
			return pn;
		}
		if (nf instanceof TableCellNode)
		{
			// Table cell; does it depend on something else?
			ProvenanceNode pn = new ProvenanceNode(nf);
			TableCellNode tcn = (TableCellNode) nf;
			Table t = tcn.getOwner();
			NodeFunction nf_dep = t.getDependency(tcn.getRow(), tcn.getCol());
			pn.addParent(explain(nf_dep));
			return pn;
		}
		if (nf instanceof AggregateFunction)
		{
			ProvenanceNode pn = new ProvenanceNode(nf);
			AggregateFunction af = (AggregateFunction) nf;
			List<NodeFunction> dependencies = af.m_nodes;
			List<ProvenanceNode> parents = new LinkedList<ProvenanceNode>();
			for (NodeFunction par_nf : dependencies)
			{
				ProvenanceNode par_pn = explain(par_nf);
				parents.add(par_pn);
			}
			pn.addParents(parents);
			return pn;
		}
		return new BrokenChain();
	}
}