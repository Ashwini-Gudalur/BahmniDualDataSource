/*
 * Copyright (C) 2005 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bahmni.custom;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;
import org.bahmni.custom.data.BahmniReportObject;

import javax.sql.DataSource;
import javax.xml.xpath.XPath;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author bob
 * 
 */
public class BahmniDualDataSource implements JRDataSource {
	public static final String REPORT_CLASS = "bahmnirep_reportclass";
	public static final String START_DATE = "bahmnirep_startdate";
	public static final String END_DATE = "bahmnirep_enddate";
	private final DataSource erpDS;
	private final DataSource emrDS;
	private List<? extends BahmniReportObject> nodes;
	private int index = -1;
	private String urlValue;
	private String path;
	private Map params = new HashMap();
	private XPath xpath;


	public BahmniDualDataSource(DataSource erpDS, DataSource emrDS, Map parameterValues) {
		this.erpDS = erpDS;
		this.emrDS = emrDS;
		Iterator pvi = parameterValues.entrySet().iterator();
		while (pvi.hasNext()) {
			Entry pv = (Entry) pvi.next();
			String key = (String) pv.getKey();
			params.put(key, pv.getValue());
		}
	}

	private void init() throws JRException {
		if (nodes != null) {
			return;
		}
		try {
			String reportClass = (String)params.get(REPORT_CLASS);
			Date startDate = Util.getFormattedDate((String) params.get(START_DATE));
			Date endDate = Util.getFormattedDate((String) params.get(END_DATE));
			Class<?> clazz = Class.forName(reportClass);
			Object classInstance = Util.getObjectFromClassName(reportClass);
			AbstractBahmniReport report = (AbstractBahmniReport) classInstance;
			report.setErpDS(erpDS);
			report.setEmrDS(emrDS);
			report.setStartDate(startDate);
			report.setEndDate(endDate);
			report.setParams(params);
			nodes = report.getReportData();
		} catch (Exception e) {
			throw new JRException("Exception getting web page " + urlValue+ " with node path " + path, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	public Object getFieldValue(JRField jrField) throws JRException {
		init();
		/*String itemPath = jrField.getPropertiesMap().getProperty(WEBSCRAPE_PATH);
		if (itemPath == null) {
			throw new JRException("Missing " + WEBSCRAPE_PATH + " property on field " + jrField.getName());
		}*/
		try {
			BahmniReportObject map = nodes.get(index);
			Object value = PropertyUtils.getProperty(map, jrField.getName());
			System.out.println("Sandeep:value for object:" + value + "  " + jrField.getName());
			return value;
		} catch (Exception e) {
			throw new JRException("Exception getting field with for " + jrField.getName(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() throws JRException {
		init();
		index++;
		return nodes != null && index < nodes.size();
	}

}
