/*
 * (c) Copyright 2010 Nate Riffe <inkblot@movealong.org>
 *
 * This file is part of jndi4fitnesse.
 *
 * jndi4fitnesse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * jndi4fitnesse is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with jndi4fitnesse.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.movealong.jndi4fitnesse.fixtures;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: inkblot
 * Date: Oct 1, 2010
 * Time: 5:02:49 PM
 */
public class UsingTheseDatabases {
    private String url;

    public void setURL(String url) {
        this.url = url;
    }

    public void setName(String name) throws SQLException, NamingException {
        DataSource dataSource = new DriverDataSource(url);
        Context context = new InitialContext();
        context.bind(name, dataSource);
    }

    private static class DriverDataSource implements DataSource {
        private final String url;
        private final Driver driver;

        public DriverDataSource(String url) throws SQLException {
            this.url = url;
            this.driver = DriverManager.getDriver(url);
        }

        @Override
        public Connection getConnection() throws SQLException {
            return driver.connect(url, new Properties());
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            Properties properties = new Properties();
            properties.put("username", username);
            properties.put("password", password);
            return driver.connect(url, properties);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }
}
