package me.philcali.rss.module.api;

import java.util.Arrays;
import java.util.List;

import me.philcali.rss.module.DaggerPetroniusServiceModule;
import me.philcali.service.reflection.IModule;

public class Bootstrap implements IModule {
    @Override
    public List<Object> getComponents() {
        return Arrays.asList(DaggerPetroniusServiceModule.create());
    }
}
