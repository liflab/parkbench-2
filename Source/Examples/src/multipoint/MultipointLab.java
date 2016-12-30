/*
  LabPal, a versatile environment for running experiments on a computer
  Copyright (C) 2015-2017 Sylvain Hallé

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
package multipoint;

import java.util.List;

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.CliParser.ArgumentMap;
import ca.uqac.lif.labpal.plot.gral.Scatterplot;
import ca.uqac.lif.labpal.server.WebCallback;
import ca.uqac.lif.labpal.table.ExperimentTable;

public class MultipointLab extends Laboratory 
{
	public static void main(String[] args)
	{
		initialize(args, MultipointLab.class);
	}

	@Override
	public void setupExperiments(ArgumentMap map, List<WebCallback> callbacks) 
	{
		ExperimentTable table = new ExperimentTable("a", "b");
		Scatterplot plot = new Scatterplot(table);
		add(plot);
		add(new MultipointExperiment(), table);
	}

}
