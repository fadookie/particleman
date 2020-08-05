package com.eliotlash.particlelib.mcwrapper;

public class ResourceLocation {
    public String namespace;
    public String path;

    public ResourceLocation(String id) {
        String[] split = id.split(":");
        if (split.length != 2) throw new IllegalArgumentException("Invalid resource ID:" + id);
        namespace = split[0];
        path = split[1];
    }

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }
}
