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

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class FieldPageTest {

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

    @Mock
    private GuidedDecisionTable52 model;

    private FieldPage page;

    @Before
    public void setup() {
        page = spy( new FieldPage() {{
            presenter = FieldPageTest.this.presenter;
        }} );

        when( oracle.getFieldType( any(), any() ) ).thenReturn( "fieldType" );
        when( presenter.getDataModelOracle() ).thenReturn( oracle );
        when( presenter.getModel() ).thenReturn( model );
        when( pattern52.getFactType() ).thenReturn( "factType" );
        when( plugin.getEditingPattern() ).thenReturn( pattern52 );
        when( plugin.getEditingCol() ).thenReturn( editingCol );
        when( page.plugin() ).thenReturn( plugin );
    }

    @Test
    public void testFactFields() throws Exception {
        final List<String> factFields = page.factFields();

        verify( oracle ).getFieldCompletions( eq( "factType" ), eq( FieldAccessorsAndMutators.ACCESSOR ), any() );

        assertNotNull( factFields );
    }

    @Test
    public void testHasEditingPatternWhenEditingPatternIsNull() throws Exception {
        when( plugin.getEditingPattern() ).thenReturn( null );

        assertFalse( page.hasEditingPattern() );
    }

    @Test
    public void testHasEditingPatternWhenEditingPatternIsNotNull() throws Exception {
        assertTrue( page.hasEditingPattern() );
    }

    @Test
    public void testIsConstraintValuePredicateWhenItIs() throws Exception {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_PREDICATE );

        assertTrue( page.isConstraintValuePredicate() );
    }

    @Test
    public void testIsConstraintValuePredicateWhenItIsNot() throws Exception {
        when( plugin.getConstraintValue() ).thenReturn( BaseSingleFieldConstraint.TYPE_LITERAL );

        assertFalse( page.isConstraintValuePredicate() );
    }

    @Test
    public void testGetEditingCol() throws Exception {
        page.getEditingCol();

        verify( plugin ).getEditingCol();
    }

    @Test
    public void testSetEditingCol() throws Exception {
        when( model.getTableFormat() ).thenReturn( GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY );

        page.setEditingCol( "selectedValue" );

        verify( plugin ).setEditingCol( any( ConditionCol52.class ) );
    }

    @Test
    public void testNewConditionColumnWhenTableFormatIsExtendedEntry() throws Exception {
//        when( model.getTableFormat() ).thenReturn( GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY );
//
//        final ConditionCol52 column = page.newConditionColumn( "factType" );
//
//        assertEquals( "factType", column.getFactField() );
//        assertEquals( "fieldType", column.getFieldType() );
//        assertThat( column, instanceOf( ConditionCol52.class ) );
    }

    @Test
    public void testNewConditionColumnWhenTableFormatIsLimitedEntry() throws Exception {
//        when( model.getTableFormat() ).thenReturn( GuidedDecisionTable52.TableFormat.LIMITED_ENTRY );
//
//        final ConditionCol52 column = page.newConditionColumn( "factType" );
//
//        assertEquals( "factType", column.getFactField() );
//        assertEquals( "fieldType", column.getFieldType() );
//        assertThat( column, instanceOf( LimitedEntryConditionCol52.class ) );
    }

    @Test
    public void testNewConditionColumnWhenSelectedValueIsNull() throws Exception {
//        final ConditionCol52 conditionCol52 = page.newConditionColumn( null );
//
//        assertNull( conditionCol52 );
    }

    @Test
    public void testNewConditionColumnWhenSelectedValueIsBlank() throws Exception {
//        final ConditionCol52 conditionCol52 = page.newConditionColumn( "" );
//
//        assertNull( conditionCol52 );
    }

    private ModelField modelField( final String name ) {
        return new ModelField( name, null, null, null, null, null );
    }
}
