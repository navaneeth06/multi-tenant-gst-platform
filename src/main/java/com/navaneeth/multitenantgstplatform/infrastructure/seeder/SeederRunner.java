package com.navaneeth.multitenantgstplatform.infrastructure.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeederRunner implements ApplicationRunner {

    private final AuthorizationSeeder authorizationSeeder;
    private final StateSeeder stateSeeder;

    @Override
    public void run(ApplicationArguments args) {

        if (!args.containsOption("seed")) {
            return;
        }

        if (args.getOptionValues("seed").contains("authorization")) {
            authorizationSeeder.seed();
        }

        if (args.getOptionValues("seed").contains("states")) {
            stateSeeder.seed();
        }
    }
}