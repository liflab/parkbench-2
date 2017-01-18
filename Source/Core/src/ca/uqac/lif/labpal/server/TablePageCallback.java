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
package ca.uqac.lif.labpal.server;

import java.util.Map;
import java.util.regex.Matcher;

import ca.uqac.lif.labpal.LabAssistant;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.table.DataTable;
import ca.uqac.lif.labpal.table.Table;
import ca.uqac.lif.labpal.table.rendering.HtmlTableNodeRenderer;

/**
 * Callback producing an image from one of the lab's plots, in various
 * formats.
 * <p>
 * The HTTP request accepts the following parameters:
 * <ul>
 * <li><tt>dl=1</tt>: to download the image instead of displaying it. This
 *   will prompt the user to save the file in its browser</li>
 * <li><tt>id=x</tt>: mandatory; the ID of the plot to display</li>
 * <li><tt>format=x</tt>: the requested image format. Currenly supports
 *   pdf, dumb (text), png and gp (raw data file for Gnuplot).
 * </ul>
 * 
 * @author Sylvain Hallé
 *
 */
public class TablePageCallback extends TemplatePageCallback
{
	/**
	 * A renderer for tables into beautified HTML
	 */
	protected static final HtmlTableNodeRenderer s_renderer = new HtmlTableNodeRenderer();
	
	public TablePageCallback(Laboratory lab, LabAssistant assistant)
	{
		super("/table", lab, assistant);
	}

	@Override
	public String fill(String s, Map<String,String> params)
	{
		int tab_id = Integer.parseInt(params.get("id"));
		Table tab = m_lab.getTable(tab_id);
		if (tab == null)
		{
			return null;
		}
		DataTable tbl = tab.getDataTable();
		s = s.replaceAll("\\{%TITLE%\\}", Matcher.quoteReplacement(tab.getTitle()));
		s = s.replaceAll("\\{%TABLE%\\}", Matcher.quoteReplacement(s_renderer.render(tbl.getTree(), tbl.getColumnNames())));
		String desc = tab.getDescription();
		if (desc != null && !desc.isEmpty())
		{
			s = s.replaceAll("\\{%DESCRIPTION%\\}", Matcher.quoteReplacement(desc));
		}
		s = s.replaceAll("\\{%FAVICON%\\}", getFavicon(IconType.TABLE));
		return s;
	}
}
