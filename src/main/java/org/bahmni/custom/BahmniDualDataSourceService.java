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

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;

import com.jaspersoft.jasperserver.api.metadata.common.service.RepositoryService;
import com.jaspersoft.jasperserver.api.metadata.jasperreports.service.ReportDataSourceService;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author swood
 *
 */
public class BahmniDualDataSourceService implements ReportDataSourceService {

	private String erpDriver;
	private String mrsDriver;
	private String erpConnectionUrl;
	private String mrsConnectionUrl;
	private String erpUser;
	private String mrsUser;
	private String erpPassword;
	private String mrsPassword;
	private DataSource erpDataSource;
	private DataSource mrsDataSource;

	@Override
	public String toString() {
		return "BahmniDualDataSourceService{" +
				"erpDriver='" + erpDriver + '\'' +
				", mrsDriver='" + mrsDriver + '\'' +
				", erpConnectionUrl='" + erpConnectionUrl + '\'' +
				", mrsConnectionUrl='" + mrsConnectionUrl + '\'' +
				", erpUser='" + erpUser + '\'' +
				", mrsUser='" + mrsUser + '\'' +
				", erpPassword='" + erpPassword + '\'' +
				", mrsPassword='" + mrsPassword + '\'' +
				", erpDataSource=" + erpDataSource +
				", mrsDataSource=" + mrsDataSource +
				'}';
	}

	public String getErpDriver() {
		return erpDriver;
	}

	public void setErpDriver(String erpDriver) {
		this.erpDriver = erpDriver;
	}

	public String getMrsDriver() {
		return mrsDriver;
	}

	public void setMrsDriver(String mrsDriver) {
		this.mrsDriver = mrsDriver;
	}

	public String getErpConnectionUrl() {
		return erpConnectionUrl;
	}

	public void setErpConnectionUrl(String erpConnectionUrl) {
		this.erpConnectionUrl = erpConnectionUrl;
	}

	public String getMrsConnectionUrl() {
		return mrsConnectionUrl;
	}

	public void setMrsConnectionUrl(String mrsConnectionUrl) {
		this.mrsConnectionUrl = mrsConnectionUrl;
	}

	public String getErpUser() {
		return erpUser;
	}

	public void setErpUser(String erpUser) {
		this.erpUser = erpUser;
	}

	public String getMrsUser() {
		return mrsUser;
	}

	public void setMrsUser(String mrsUser) {
		this.mrsUser = mrsUser;
	}

	public String getErpPassword() {
		return erpPassword;
	}

	public void setErpPassword(String erpPassword) {
		this.erpPassword = erpPassword;
	}

	public String getMrsPassword() {
		return mrsPassword;
	}

	public void setMrsPassword(String mrsPassword) {
		this.mrsPassword = mrsPassword;
	}

	public BahmniDualDataSourceService() {
	}
	
	public BahmniDualDataSourceService(JRDataSource ds) {
	}
	
	/* (non-Javadoc)
	 * @see com.jaspersoft.jasperserver.api.metadata.jasperreports.service.ReportDataSourceService#closeConnection()
	 */
	public void closeConnection() {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see com.jaspersoft.jasperserver.api.metadata.jasperreports.service.ReportDataSourceService#setReportParameterValues(java.util.Map)
	 */
	public void setReportParameterValues(Map parameterValues) {

		erpDataSource = createErpDataSource();
		mrsDataSource = createMrsDataSource();
		parameterValues.put(JRParameter.REPORT_DATA_SOURCE, new BahmniDualDataSource(erpDataSource, mrsDataSource, parameterValues));
		// these are for the benefit of the cache
		parameterValues.put("erpDS", erpDataSource);
		parameterValues.put("mrsDS", mrsDataSource);
	}

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
