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

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.mockito.Mock;

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
        page = spy( new OperatorWizardPage() {{
            presenter = OperatorWizardPageTest.this.presenter;
        }} );

        when( oracle.getFieldType( any(), any() ) ).thenReturn( "fieldType" );
        when( presenter.getDataModelOracle() ).thenReturn( oracle );
        when( pattern52.getFactType() ).thenReturn( "factType" );
        when( plugin.getEditingPattern() ).thenReturn( pattern52 );
        when( plugin.getEditingCol() ).thenReturn( editingCol );
        when( page.plugin() ).thenReturn( plugin );
    }

    @Test
    public void testForEachOperator() {

    }

    @Test
    public void testOperatorOptionsWhenConstraintValueIsPredicate() {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_PREDICATE );

        assertTrue( page.isConstraintValuePredicate() );
    }

    @Test
    public void testOperatorOptionsWhenConstraintValueIsLiteral() {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_LITERAL );

//        when( oracle.getOperatorCompletions( "factType", "fieldType", any() ) ).thenReturn()

        List<String> strings = page.operatorOptions();

        System.out.println( "" );
//        assertTrue(  );
    }

    @Test
    public void testIsConstraintValuePredicateWhenConstraintValueIsPredicate() {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_PREDICATE );

        assertTrue( page.isConstraintValuePredicate() );
    }

    @Test
    public void testIsConstraintValuePredicateWhenConstraintValueIsNotPredicate() {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_LITERAL );

        assertFalse( page.isConstraintValuePredicate() );
    }

    @Test
    public void testCanSetOperatorWhenEditingColIsNull() {
        when( plugin.getEditingCol() ).thenReturn( null );

        assertFalse( page.canSetOperator() );
    }

    @Test
    public void testCanSetOperatorWhenEditingColIsNotNull() {

        assertTrue( page.canSetOperator() );
    }

    @Test
    public void testGetOperatorWhenOperatorIsNull() {
        when( editingCol.getOperator() ).thenReturn( "==" );

        assertEquals( "==", page.getOperator() );
    }

    @Test
    public void testGetOperatorWhenOperatorIsNotNull() {
        when( editingCol.getOperator() ).thenReturn( null );

        assertEquals( "", page.getOperator() );
    }

    @Test
    public void testSetOperator() {

    }
}