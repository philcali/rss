package me.philcali.rss.gateway;

import java.util.List;

import me.philcali.rss.module.DaggerSmartRSSModule;
import me.philcali.service.reflection.IModule;

public class Bootstrap implements IModule {
    @Override
    public List<Object> getComponents() {
        return DaggerSmartRssModule.create();
    }
}
