package me.philcali.rss.module.asset;

import java.util.Arrays;
import java.util.List;

import me.philcali.rss.module.DaggerPetroniusWebsiteAssetModule;
import me.philcali.service.reflection.IModule;

public class Bootstrap implements IModule {
    @Override
    public List<Object> getComponents() {
        return Arrays.asList(DaggerPetroniusWebsiteAssetModule.create());
    }
}
