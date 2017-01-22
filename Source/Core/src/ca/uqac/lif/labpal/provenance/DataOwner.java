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

public interface DataOwner
{
	/**
	 * Gets an instance of the actual object who is encapsulated by this
	 * owner
	 * @return The object. Must not be null.
	 */
	public Object getOwner();
	
	/**
	 * Gets the current value of the data point with given ID
	 * @param id The ID
	 * @return A value
	 */
	public Object getValue(String id);
	
	/**
	 * For a given data point, gets the set of data points it depends on.
	 * @param id The data point
	 * @return A provenance node
	 */
	public ProvenanceNode dependsOn(String id);
}
