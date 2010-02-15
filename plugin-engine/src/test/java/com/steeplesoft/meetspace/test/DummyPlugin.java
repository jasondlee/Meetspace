package com.steeplesoft.meetspace.test;

import com.steeplesoft.meetspace.plugins.Plugin;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 12, 2010
 * Time: 9:33:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Plugin(name=DummyPlugin.NAME, type=DummyPlugin.TYPE, description=DummyPlugin.DESCRIPTION)
public class DummyPlugin {
    public static final String NAME = "Dummy";
    public static final String TYPE = "dummy";
    public static final String DESCRIPTION="Dummy Plugin";
}
