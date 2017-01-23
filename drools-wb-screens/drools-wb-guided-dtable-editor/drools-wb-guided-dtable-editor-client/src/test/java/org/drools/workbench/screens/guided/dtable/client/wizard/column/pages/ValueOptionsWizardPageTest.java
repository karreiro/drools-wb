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

import java.util.HashMap;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.Validator;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.uberfire.client.callbacks.Callback;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ValueOptionsWizardPageTest {

    @Mock
    private Validator validator;

    @Mock
    private ConditionColumnWizardPlugin plugin;

    @Mock
    private AsyncPackageDataModelOracle oracle;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private Pattern52 pattern52;

    @Mock
    private ConditionCol52 editingCol;

    @Mock
    private GuidedDecisionTable52 model;

    @Mock
    private DTCellValue52 defaultValue;

    @Mock
    private DTCellValueWidgetFactory factory;

    @InjectMocks
    private ValueOptionsWizardPage page = spy(new ValueOptionsWizardPage() {

        @Override
        DTCellValueWidgetFactory factory() {
            return factory;
        }

        @Override
        Validator validator() {
            return validator;
        }
    });

    @BeforeClass
    public static void staticSetup() {
        // Prevent runtime GWT.create() error at 'content = new SimplePanel()'
        GWTMockUtilities.disarm();

        ApplicationPreferences.setUp(new HashMap<String, String>() {{
            put(ApplicationPreferences.DATE_FORMAT,
                "dd-MM-yyyy");
        }});
    }

    @Before
    public void setup() {
        when(defaultValue.getDataType()).thenReturn(DataType.DataTypes.STRING);
        when(editingCol.getDefaultValue()).thenReturn(defaultValue);
        when(presenter.getModel()).thenReturn(model);
        when(presenter.getDataModelOracle()).thenReturn(oracle);
        when(plugin.getEditingPattern()).thenReturn(pattern52);
        when(plugin.getEditingCol()).thenReturn(editingCol);
        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testIsReadOnly() throws Exception {
        page.isReadOnly();

        verify(presenter).isReadOnly();
    }

    @Test
    public void testNewDefaultValueWidget() throws Exception {
        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY);
        when(page.factory()).thenReturn(factory);

        page.newDefaultValueWidget();

        verify(factory).getWidget(pattern52,
                                  editingCol,
                                  defaultValue);
    }

    @Test
    public void testNewLimitedValueWidget() throws Exception {
        final LimitedEntryConditionCol52 limitedEntryConditionCol52 = mock(LimitedEntryConditionCol52.class);

        when(plugin.getEditingCol()).thenReturn(limitedEntryConditionCol52);

        page.newLimitedValueWidget();

        verify(factory).getWidget(pattern52,
                                  limitedEntryConditionCol52,
                                  limitedEntryConditionCol52.getValue());
    }

    @Test
    public void testCanSetupCepOperatorsWhenEditingPatternIsNotNull() throws Exception {
        assertTrue(page.canSetupCepOperators());
    }

    @Test
    public void testCanSetupCepOperatorsWhenEditingPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        assertFalse(page.canSetupCepOperators());
    }

    @Test
    public void testCanSetupDefaultValueWhenEditingColIsNull() throws Exception {
        when(plugin.getEditingCol()).thenReturn(null);

        assertFalse(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupDefaultValueWhenEditingPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        assertFalse(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupDefaultValueWhenEditingColHasAnEmptyFactType() throws Exception {
        when(editingCol.getFactField()).thenReturn("");

        assertFalse(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupDefaultValueWhenOperatorNeedsAValue() throws Exception {
        when(editingCol.getFactField()).thenReturn("factField");
        when(validator.doesOperatorNeedValue(any())).thenReturn(false);

        assertFalse(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupDefaultValueWhenTableFormatIsNotExtendedEntry() throws Exception {
        when(editingCol.getFactField()).thenReturn("factField");
        when(validator.doesOperatorNeedValue(any())).thenReturn(true);
        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.LIMITED_ENTRY);

        assertFalse(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupDefaultValueWhenCanSetup() throws Exception {
        when(editingCol.getFactField()).thenReturn("factField");
        when(validator.doesOperatorNeedValue(any())).thenReturn(true);
        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY);

        assertTrue(page.canSetupDefaultValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenEditingColIsNull() throws Exception {
        when(plugin.getEditingCol()).thenReturn(null);

        assertFalse(page.canSetupLimitedValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenEditingPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        assertFalse(page.canSetupLimitedValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenEditingColIsNotAnInstanceOfLimitedEntryConditionCol52() throws Exception {
        when(plugin.getEditingCol()).thenReturn(new ConditionCol52());

        assertFalse(page.canSetupLimitedValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenOperatorNeedsAValue() throws Exception {
        when(plugin.getEditingCol()).thenReturn(new LimitedEntryConditionCol52());
        when(validator.doesOperatorNeedValue(any())).thenReturn(false);

        assertFalse(page.canSetupLimitedValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenTableFormatIsNotLimitedEntry() throws Exception {
        when(plugin.getEditingCol()).thenReturn(new LimitedEntryConditionCol52());
        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY);
        when(validator.doesOperatorNeedValue(any())).thenReturn(true);

        assertFalse(page.canSetupLimitedValue());
    }

    @Test
    public void testCanSetupLimitedValueWhenCanSetup() throws Exception {
        when(plugin.getEditingCol()).thenReturn(new LimitedEntryConditionCol52());
        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.LIMITED_ENTRY);
        when(validator.doesOperatorNeedValue(any())).thenReturn(true);

        assertTrue(page.canSetupLimitedValue());
    }

    @Test
    public void testIsFactTypeAnEvent() throws Exception {
        final Callback<Boolean> callback = (b) -> {
        };

        when(pattern52.getFactType()).thenReturn("factType");

        page.isFactTypeAnEvent(callback);

        verify(oracle).isFactTypeAnEvent("factType",
                                         callback);
    }

    @Test
    public void testCanSetupBindingWhenConstraintValueIsLiteral() throws Exception {
        when(plugin.getConstraintValue()).thenReturn(BaseSingleFieldConstraint.TYPE_LITERAL);

        assertTrue(page.canSetupBinding());
    }

    @Test
    public void testCanSetupBindingWhenConstraintValueIsNotLiteral() throws Exception {
        when(plugin.getConstraintValue()).thenReturn(BaseSingleFieldConstraint.TYPE_PREDICATE);

        assertFalse(page.canSetupBinding());
    }
}
