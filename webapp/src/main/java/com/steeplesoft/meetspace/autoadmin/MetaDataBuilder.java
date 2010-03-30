package com.steeplesoft.meetspace.autoadmin;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
public class MetaDataBuilder {
    private Properties properties;
    private Set<String> modelPackages = new TreeSet<String>();
    private Set<String> modelClasses = new TreeSet<String>();
    private Map<String, ModelMetadata> metadata = new HashMap<String, ModelMetadata>();

    public MetaDataBuilder() {
        this.properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/autoadmin.properties"));
            splitStringIntoSet(properties.getProperty("autoadmin.packages"), modelPackages);
            splitStringIntoSet(properties.getProperty("autoadmin.models"), modelClasses);
        } catch (IOException e) {
        }
        loadMetaData();
    }

    public ModelMetadata getModelMetadata(String className) {
        return metadata.get(className);
    }

    public Set<String> getModelClasses() {
        return modelClasses;
    }

    protected void loadMetaData() {
        for (String model: modelClasses) {
            Class clazz = null;

            for (String pkg : modelPackages) {
                try {
                    clazz = Class.forName(pkg + "." + model);
                    ModelMetadata modelMetadata = new ModelMetadata(clazz, properties);
                    metadata.put(model, modelMetadata);
                } catch (ClassNotFoundException e) {
                    //
                }
            }
        }
    }

    private void splitStringIntoSet(String input, Set<String> set) {
        if (input != null) {
            for (String model : input.split(",")) {
                set.add(model);
            }
        }
    }
}