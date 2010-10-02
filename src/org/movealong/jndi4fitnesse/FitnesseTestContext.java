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

import javax.naming.*;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: inkblot
 * Date: Oct 1, 2010
 * Time: 5:31:01 PM
 */
public class FitnesseTestContext implements Context {
    private static final NameParser nameParser = new DefaultNameParser();

    private Hashtable<String, Object> environment;
    private Hashtable<String, Object> bindings;
    private String nameInNamespace;
    private boolean closed;

    public FitnesseTestContext() {
        this(new Hashtable<String, Object>());
    }

    public FitnesseTestContext(Hashtable<String, Object> environment) {
        this(environment, "");
    }

    public FitnesseTestContext(Hashtable<String, Object> environment, String nameInNamespace) {
        if (environment == null) {
            this.environment = new Hashtable<String, Object>();
        } else {
            this.environment = new Hashtable<String, Object>(environment);
        }
        this.nameInNamespace = nameInNamespace;
        bindings = new Hashtable<String, Object>();
        closed = false;
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        checkClosed();
        CompositeName compositeName = checkName(name);
        switch (compositeName.size()) {
            case 0:
                return this;
            case 1:
                return bindings.get(compositeName.get(0));
            default:
                Object obj = bindings.get(compositeName.get(0));
                if (obj == null) return null;
                if (!(obj instanceof Context)) throw new NotContextException();
                return ((Context) obj).lookup(compositeName.getSuffix(1));
        }
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return lookup(getNameParser(name).parse(name));
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        checkClosed();
        CompositeName compositeName = checkName(name);
        switch (compositeName.size()) {
            case 0:
                throw new NamingException("Cannot bind to context root");

            case 1:
                bindings.put(compositeName.get(0), obj);
                break;

            default:
                Context ctx;
                Object _ctx = lookup(compositeName.getPrefix(1));
                if (_ctx == null) {
                    ctx = new FitnesseTestContext(environment, compositeName.get(0));
                    bindings.put(compositeName.get(0), ctx);
                } else if (_ctx instanceof Context) {
                    ctx = (Context) _ctx;
                } else {
                    throw new NotContextException();
                }
                ctx.bind(compositeName.getSuffix(1), obj);
        }
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        bind(getNameParser(name).parse(name), obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        unbind(name);
        bind(name, obj);
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        rebind(getNameParser(name).parse(name), obj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        checkClosed();
        CompositeName compositeName = checkName(name);
        switch (compositeName.size()) {
            case 0:
                throw new NamingException("Cannot unbind the context root");
            case 1:
                bindings.remove(compositeName.get(0));
                break;
            default:
                Object _ctx = lookup(compositeName.getPrefix(1));
                if (_ctx == null) {
                    throw new NamingException("Context not found");
                } else if (!(_ctx instanceof Context)) {
                    throw new NotContextException();
                } else {
                    Context ctx = (Context) _ctx;
                    ctx.unbind(compositeName.getSuffix(1));
                }
        }
    }

    @Override
    public void unbind(String name) throws NamingException {
        unbind(getNameParser(name).parse(name));
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        Object obj = lookup(oldName);
        unbind(oldName);
        bind(newName, obj);
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        rename(getNameParser(oldName).parse(oldName), getNameParser(newName).parse(newName));
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return list(getNameParser(name).parse(name));
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        checkClosed();
        return listBindings(getNameParser(name).parse(name));
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        destroySubcontext(getNameParser(name).parse(name));
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return createSubcontext(getNameParser(name).parse(name));
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return lookupLink(getNameParser(name).parse(name));
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        checkClosed();
        return nameParser;
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        checkClosed();
        return nameParser;
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        checkClosed();
        throw new UnsupportedOperationException("This operation is not yet implemented");
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        return composeName(getNameParser(name).parse(name), getNameParser(prefix).parse(prefix)).toString();
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        checkClosed();
        return environment.put(propName, propVal);
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        checkClosed();
        return environment.remove(propName);
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        checkClosed();
        return (Hashtable) environment.clone();
    }

    @Override
    public void close() throws NamingException {
        checkClosed();
        closed = true;
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        checkClosed();
        return nameInNamespace;
    }

    private CompositeName checkName(Name name) throws NamingException {
        if (!(name instanceof CompositeName)) throw new NamingException("Jndi4Fitnesse only supports CompositeNames");
        return (CompositeName) name;
    }

    private void checkClosed() throws NamingException {
        if (closed) throw new NamingException("Context is closed");
    }

    public void clear() {
        bindings = new Hashtable<String, Object>();
    }
}
