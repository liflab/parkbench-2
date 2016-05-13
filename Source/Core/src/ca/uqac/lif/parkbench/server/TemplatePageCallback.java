/*
  ParkBench, a versatile benchmark environment
  Copyright (C) 2015-2016 Sylvain Hallé

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
package ca.uqac.lif.parkbench.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.CallbackResponse.ContentType;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.LabAssistant;
import ca.uqac.lif.parkbench.Laboratory;

/**
 * Callback for a template page. This callback loads an internal HTML
 * file with the same name as the HTTP request, and replaces all
 * instructions of the form <tt>{!file.html!}</tt> by the contents of
 * <tt>file.html</tt> at that location (thus providing a basic templating
 * mechanism).
 * 
 * @author Sylvain Hallé
 *
 */
public class TemplatePageCallback extends ParkBenchCallback
{
	protected static final transient String s_path = "resource";
	
	protected static final transient Pattern s_pattern = Pattern.compile("\\{!(.*?)!\\}");

	public TemplatePageCallback(String prefix, Laboratory lab, LabAssistant assistant)
	{
		super(prefix, lab, assistant);
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		CallbackResponse response = new CallbackResponse(t);
		response.disableCaching();
		response.setContentType(ContentType.HTML);
		Map<String,String> params = getParameters(t);
		//Give the right content-type to the browser by giving it what it's looking for
		Headers headers = t.getRequestHeaders();
		String accept_Header = headers.get("Accept").get(0);
		response.setContentType(accept_Header.split(",")[0]);
		// Read file and put into response
		String filename = s_path + m_path + ".html";
		String file_contents = FileHelper.internalFileToString(ParkbenchServer.class, filename);
		
		if (file_contents == null)
		{
			response.setCode(CallbackResponse.HTTP_NOT_FOUND);
			return response;
		}
		String contents = render(file_contents, params);
		if (contents == null)
		{
			response.setCode(CallbackResponse.HTTP_NOT_FOUND);
		}
		else
		{
			response.setContents(contents);
			response.setCode(CallbackResponse.HTTP_OK);
		}
		return response;
	}
	
	public static final String resolve(String s)
	{
		Matcher mat = s_pattern.matcher(s);
		Set<String> includes = new HashSet<String>();
		while (mat.find())
		{
			includes.add(mat.group(1));
		}
		for (String filename : includes)
		{
			String file_contents = FileHelper.internalFileToString(ParkbenchServer.class, s_path + "/" + filename);
			if (file_contents == null)
				continue;
			s = s.replace("{!" + filename + "!}", file_contents);
		}
		return s;
	}
	
	public final String render(String s, Map<String,String> params)
	{
		s = resolve(s);
		s = fill(s, params);
		if (s == null)
		{
			return null;
		}
		s = s.replaceAll("\\{%VERSION_STRING%\\}", Laboratory.s_versionString);
		s = s.replaceAll("\\{%LAB_NAME%\\}", m_lab.getTitle());
		s = s.replaceAll("\\{%.*?%\\}", "");
		return s;
	}
	
	protected String fill(String s, Map<String,String> params)
	{
		return s;
	}

}
