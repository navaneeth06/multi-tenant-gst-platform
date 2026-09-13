package com.navaneeth.multitenantgstplatform.infrastructure.seeder;

import com.navaneeth.multitenantgstplatform.feature.master.entity.State;
import com.navaneeth.multitenantgstplatform.feature.master.entity.State.StateType;
import com.navaneeth.multitenantgstplatform.feature.master.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateSeeder {

    private final StateRepository stateRepository;

    @Transactional
    public void seed() {

        List<State> states = List.of(
                // States
                createState("Andhra Pradesh", "AP", "37", StateType.STATE),
                createState("Arunachal Pradesh", "AR", "12", StateType.STATE),
                createState("Assam", "AS", "18", StateType.STATE),
                createState("Bihar", "BR", "10", StateType.STATE),
                createState("Chhattisgarh", "CG", "22", StateType.STATE),
                createState("Goa", "GA", "30", StateType.STATE),
                createState("Gujarat", "GJ", "24", StateType.STATE),
                createState("Haryana", "HR", "06", StateType.STATE),
                createState("Himachal Pradesh", "HP", "02", StateType.STATE),
                createState("Jharkhand", "JH", "20", StateType.STATE),
                createState("Karnataka", "KA", "29", StateType.STATE),
                createState("Kerala", "KL", "32", StateType.STATE),
                createState("Madhya Pradesh", "MP", "23", StateType.STATE),
                createState("Maharashtra", "MH", "27", StateType.STATE),
                createState("Manipur", "MN", "14", StateType.STATE),
                createState("Meghalaya", "ML", "17", StateType.STATE),
                createState("Mizoram", "MZ", "15", StateType.STATE),
                createState("Nagaland", "NL", "13", StateType.STATE),
                createState("Odisha", "OD", "21", StateType.STATE),
                createState("Punjab", "PB", "03", StateType.STATE),
                createState("Rajasthan", "RJ", "08", StateType.STATE),
                createState("Sikkim", "SK", "11", StateType.STATE),
                createState("Tamil Nadu", "TN", "33", StateType.STATE),
                createState("Telangana", "TS", "36", StateType.STATE),
                createState("Tripura", "TR", "16", StateType.STATE),
                createState("Uttar Pradesh", "UP", "09", StateType.STATE),
                createState("Uttarakhand", "UK", "05", StateType.STATE),
                createState("West Bengal", "WB", "19", StateType.STATE),

                // Union Territories
                createState(
                        "Andaman and Nicobar Islands",
                        "AN",
                        "35",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Chandigarh",
                        "CH",
                        "04",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Dadra and Nagar Haveli and Daman and Diu",
                        "DH",
                        "26",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Delhi",
                        "DL",
                        "07",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Jammu and Kashmir",
                        "JK",
                        "01",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Ladakh",
                        "LA",
                        "38",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Lakshadweep",
                        "LD",
                        "31",
                        StateType.UNION_TERRITORY
                ),
                createState(
                        "Puducherry",
                        "PY",
                        "34",
                        StateType.UNION_TERRITORY
                )
        );

        states.forEach(this::saveIfNotExists);
    }

    private void saveIfNotExists(State state) {

        if (!stateRepository.existsByCode(state.getCode())) {
            stateRepository.save(state);
        }
    }

    private State createState(
            String name,
            String code,
            String gstCode,
            StateType type
    ) {

        State state = new State();

        state.setName(name);
        state.setCode(code);
        state.setGstCode(gstCode);
        state.setType(type);

        return state;
    }
}