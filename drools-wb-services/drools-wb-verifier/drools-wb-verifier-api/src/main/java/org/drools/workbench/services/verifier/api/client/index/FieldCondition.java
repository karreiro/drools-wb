/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.services.verifier.api.client.index;

import java.util.ArrayList;

import org.drools.workbench.services.verifier.api.client.configuration.AnalyzerConfiguration;
import org.drools.workbench.services.verifier.api.client.index.keys.Key;
import org.drools.workbench.services.verifier.api.client.index.keys.Values;
import org.drools.workbench.services.verifier.api.client.maps.KeyDefinition;
import org.kie.soup.commons.validation.PortablePreconditions;

public class FieldCondition<T extends Comparable>
        extends Condition {

    private final Field field;
    private final String operator;

    public FieldCondition(final Field field,
                          final Column column,
                          final String operator,
                          final Values<T> values,
                          final AnalyzerConfiguration configuration) {
        super(column,
              ConditionSuperType.FIELD_CONDITION,
              values,
              configuration);

        this.field = PortablePreconditions.checkNotNull("field",
                                                        field);
        this.operator = PortablePreconditions.checkNotNull("operator",
                                                           operator);
    }

    public Field getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return field + " " + operator + " " + getValues();
    }

    @Override
    public Key[] keys() {
        final ArrayList<Key> keys = new ArrayList<>();
        for (final Key key : super.keys()) {
            keys.add(key);
        }

        return keys.toArray(new Key[keys.size()]);
    }

    public static KeyDefinition[] keyDefinitions() {
        final ArrayList<KeyDefinition> keyDefinitions = new ArrayList<>();
        for (final KeyDefinition keyDefinition : Condition.keyDefinitions()) {
            keyDefinitions.add(keyDefinition);
        }

        return keyDefinitions.toArray(new KeyDefinition[keyDefinitions.size()]);
    }
}
