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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProvenanceNode
{	
	/**
	 * The ID of the data point
	 */
	protected final NodeFunction m_nodeFunction;
	
	/**
	 * A set of points this data point depends on
	 */
	protected final List<ProvenanceNode> m_parents;
	
	/**
	 * Creates a new provenance node
	 * @param datapoint_id The ID of the data point
	 * @param owner The owner of the data point
	 */
	public ProvenanceNode(NodeFunction node)
	{
		super();
		m_nodeFunction = node;
		m_parents = new ArrayList<ProvenanceNode>();
	}
	
	/**
	 * Adds a parent to this node
	 * @param p The parent
	 */
	public void addParent(ProvenanceNode p)
	{
		m_parents.add(p);
	}
	
	/**
	 * Replaces a parent node by another
	 * @param position The position where to perform the replacement. The
	 * operation is ignored is this value is out of bounds.
	 * @param p The parent
	 */
	public void replaceParent(int position, ProvenanceNode p)
	{
		m_parents.set(position, p);
	}
	
	public NodeFunction getNodeFunction()
	{
		return m_nodeFunction;
	}
	
	public List<ProvenanceNode> getParents()
	{
		return m_parents;
	}
	
	@Override
	public String toString()
	{
		if (m_nodeFunction != null)
		{
			return m_nodeFunction.toString();
		}
		return "?";
	}

	public void addParents(Collection<ProvenanceNode> parents)
	{
		m_parents.addAll(parents);
		
	}
}