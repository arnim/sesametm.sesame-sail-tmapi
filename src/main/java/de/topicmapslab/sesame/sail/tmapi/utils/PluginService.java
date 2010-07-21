/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;

import de.topicmapslab.sesame.sail.tmapi.SailTmapiPlugin;

/**
 * @author Arnim Bleier
 *
 */
public class PluginService {

    private static PluginService service;
    private ServiceLoader<SailTmapiPlugin> loader;


    private PluginService() {
        loader = ServiceLoader.load(SailTmapiPlugin.class);
        loader.reload();
    }


    public static PluginService getInstance() {
        if (service == null) {
            service = new PluginService();
        }
        return service;
    }


    public void evaluate(Locator subj, Locator pred, Locator obj, TopicMap tm, TmapiStatementIterator<?> other ) {
        try {
            Iterator<SailTmapiPlugin> plugins = loader.iterator();
            while (plugins.hasNext()) {
            	plugins.next().evaluate(subj, pred, obj, tm, other);
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();

        }
    }
}
