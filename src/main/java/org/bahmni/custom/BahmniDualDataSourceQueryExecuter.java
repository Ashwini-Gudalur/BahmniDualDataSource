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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author bob
 *
 */
public class BahmniDualDataSourceQueryExecuter extends JRAbstractQueryExecuter {

	private String path;
	private String url;

	/**
	 * @param dataset
	 * @param parameters
	 */
	public BahmniDualDataSourceQueryExecuter(JRDataset dataset, Map parameters) {
		super(dataset, parameters);
		parseQuery();
	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.query.JRQueryExecuter#cancelQuery()
	 */
	public boolean cancelQuery() throws JRException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.query.JRQueryExecuter#close()
	 */
	public void close() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.query.JRQueryExecuter#createDatasource()
	 */
	public JRDataSource createDatasource() throws JRException {
		String[] qarray = getQueryString().trim().split(" ");
		Map<String,String> args = new HashMap<String, String>();
		args.put(BahmniDualDataSource.REPORT_CLASS,qarray[0]);
		if (qarray.length > 1) {
			args.put(BahmniDualDataSource.START_DATE,qarray[1]);
		}
		if (qarray.length > 2) {
			args.put(BahmniDualDataSource.END_DATE,qarray[2]);
		}
		return new BahmniDualDataSource(createErpDataSource(), createMrsDataSource(), args);
	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.query.JRAbstractQueryExecuter#getParameterReplacement(java.lang.String)
	 */
	protected String getParameterReplacement(String parameterName) {
		// TODO Auto-generated method stub
		return null;
	}
	private String erpDriver="org.postgresql.Driver";
	private String mrsDriver="com.mysql.jdbc.Driver";
	private String erpConnectionUrl="jdbc:postgresql://192.168.57.103:5432/openerp";
	private String mrsConnectionUrl="jdbc:mysql://192.168.57.103:3306/openmrs";
	private String erpUser="openerp";
	private String mrsUser="openmrs-user";
	private String erpPassword="";
	private String mrsPassword="password";

	private DataSource createErpDataSource(){
		DriverManagerDataSource ds=new DriverManagerDataSource();
		ds.setDriverClassName(erpDriver);
		ds.setUrl(erpConnectionUrl);
		ds.setUsername(erpUser);
		ds.setPassword(erpPassword);
		return ds;
	}
	private DataSource createMrsDataSource(){
		DriverManagerDataSource ds=new DriverManagerDataSource();
		ds.setDriverClassName(mrsDriver);
		ds.setUrl(mrsConnectionUrl);
		ds.setUsername(mrsUser);
		ds.setPassword(mrsPassword);
		return ds;
	}

}
