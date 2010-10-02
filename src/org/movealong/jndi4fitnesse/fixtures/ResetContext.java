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

import org.movealong.jndi4fitnesse.FitnesseTestContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by IntelliJ IDEA.
 * User: inkblot
 * Date: Oct 2, 2010
 * Time: 8:45:01 AM
 */
public class ResetContext {
    public boolean execute() throws NamingException {
        Context initial = new InitialContext();
        Object _j4f = initial.lookup("");
        assert _j4f instanceof FitnesseTestContext;
        ((FitnesseTestContext) _j4f).clear();
        return true;
    }
}
