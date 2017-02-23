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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.oracle.OperatorsOracle;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.datamodel.OracleUtils;
import org.mockito.Mock;
import org.uberfire.client.callbacks.Callback;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class OperatorWizardPageTest {

    @Mock
    private ConditionColumnWizardPlugin plugin;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private AsyncPackageDataModelOracle oracle;

    @Mock
    private Pattern52 pattern52;

    @Mock
    private ConditionCol52 editingCol;

    private OperatorWizardPage page;

    @Before
    public void setup() {
        page = spy(new OperatorWizardPage() {
            {
                presenter = OperatorWizardPageTest.this.presenter;
            }

            @Override
            void getOperatorCompletions(final Callback<String[]> callback) {
                callback.callback(OracleUtils.joinArrays(OperatorsOracle.STANDARD_OPERATORS,
                                                         OperatorsOracle.SIMPLE_CEP_OPERATORS,
                                                         OperatorsOracle.COMPLEX_CEP_OPERATORS,
                                                         OperatorsOracle.EXPLICIT_LIST_OPERATORS));
            }
        });

        when(oracle.getFieldType(any(),
                                 any())).thenReturn("fieldType");
        when(presenter.getDataModelOracle()).thenReturn(oracle);
        when(pattern52.getFactType()).thenReturn("factType");
        when(plugin.getEditingPattern()).thenReturn(pattern52);
        when(plugin.getEditingCol()).thenReturn(editingCol);
        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testForEachOperator() {
        final Map<String, String> map = new HashMap<>();

        page.forEachOperator(map::put);

        final Set<String> humanizedValues = map.keySet();
        final Collection<String> regularValues = map.values();

        assertTrue(humanizedValues.contains("isEqualToNull"));
        assertTrue(regularValues.contains("== null"));
    }

    @Test
    public void testOperatorOptionsWhenConstraintValueTypeIsPredicate() {
        when(editingCol.getConstraintValueType()).thenReturn(BaseSingleFieldConstraint.TYPE_PREDICATE);

        final List<String> operators = page.operatorOptions();

        assertFalse(operators.contains("in"));
        assertFalse(operators.contains("not in"));
        assertEquals(17,
                     operators.size());
    }

    @Test
    public void testOperatorOptionsWhenConstraintValueTypeIsLiteral() {
        when(editingCol.getConstraintValueType()).thenReturn(BaseSingleFieldConstraint.TYPE_LITERAL);

        final List<String> operators = page.operatorOptions();

        assertTrue(operators.contains("in"));
        assertTrue(operators.contains("not in"));
        assertEquals(19,
                     operators.size());
    }

    @Test
    public void testIsConstraintValuePredicateWhenConstraintValueIsPredicate() {
        when(plugin.getConstraintValue()).thenReturn(BaseSingleFieldConstraint.TYPE_PREDICATE);

        assertTrue(page.isConstraintValuePredicate());
    }

    @Test
    public void testIsConstraintValuePredicateWhenConstraintValueIsNotPredicate() {
        when(plugin.getConstraintValue()).thenReturn(BaseSingleFieldConstraint.TYPE_LITERAL);

        assertFalse(page.isConstraintValuePredicate());
    }

    @Test
    public void testCanSetOperatorWhenEditingColIsNull() {
        when(plugin.getEditingCol()).thenReturn(null);

        assertFalse(page.canSetOperator());
    }

    @Test
    public void testCanSetOperatorWhenEditingColIsNotNull() {
        assertTrue(page.canSetOperator());
    }

    @Test
    public void testGetOperatorWhenOperatorIsNull() {
        when(editingCol.getOperator()).thenReturn("==");

        assertEquals("==",
                     page.getOperator());
    }

    @Test
    public void testGetOperatorWhenOperatorIsNotNull() {
        when(editingCol.getOperator()).thenReturn(null);

        assertEquals("",
                     page.getOperator());
    }

    @Test
    public void testSetOperator() {
        page.setOperator("==");

        verify(plugin).setEditingColOperator("==");
    }
}
