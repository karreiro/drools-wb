/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class CalculationTypeWizardPageTest {

    @Mock
    private ConditionColumnWizardPlugin plugin;

    private CalculationTypeWizardPage page;

    @Before
    public void setup() {
        page = spy( new CalculationTypeWizardPage() );

        when( page.plugin() ).thenReturn( plugin );
    }

    @Test
    public void testGetConstraintValue() throws Exception {
        page.getConstraintValue();

        verify( plugin ).getConstraintValue();
    }

    @Test
    public void testSetConstraintValue() throws Exception {
        page.setConstraintValue( BaseSingleFieldConstraint.TYPE_LITERAL );

        verify( plugin ).setConstraintValue( BaseSingleFieldConstraint.TYPE_LITERAL );
    }
}
