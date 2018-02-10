package de.adorsys.tppserver.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import de.adorsys.tppserver.Server;

@Component
@ConfigurationProperties(prefix="my")
public class EnvConfig {


    private List<Server> servers = new ArrayList<Server>();

    public List<Server> getServers() {
        return this.servers;
    }

}
