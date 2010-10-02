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
package org.movealong.jndi4fitnesse;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: inkblot
 * Date: Oct 1, 2010
 * Time: 5:28:59 PM
 */
public class FitnesseInitialContextFactory implements InitialContextFactory {
    private static FitnesseTestContext context;

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        if (context == null) {
            context = new FitnesseTestContext((Hashtable<String, Object>) environment);
        }
        return context; 
    }
}
