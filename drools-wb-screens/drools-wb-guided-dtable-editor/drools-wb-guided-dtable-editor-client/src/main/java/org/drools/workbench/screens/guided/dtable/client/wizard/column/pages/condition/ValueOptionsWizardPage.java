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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.AbstractDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.BindingTextBox;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class ValueOptionsWizardPage extends AbstractDecisionTableColumnPage {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return "Additional info";
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    IsWidget newDefaultValueWidget() {
        final Pattern52 editingPattern = getConditionColumnWizardPlugin().getEditingPattern();
        final ConditionCol52 editingCol = getConditionColumnWizardPlugin().getEditingCol();
//        final DTCellValue52 defaultValue = editingCol.getDefaultValue();
        final DTCellValue52 defaultValue = new DTCellValue52() {
            @Override
            public String getStringValue() {
                return "";
            }
        };

        return factory().getWidget( editingPattern,
                                    editingCol,
                                    defaultValue );
    }

    IsWidget newLimitedValueWidget() {
        final Pattern52 editingPattern = getConditionColumnWizardPlugin().getEditingPattern();
        final ConditionCol52 editingCol = getConditionColumnWizardPlugin().getEditingCol();

//        if ( ( editingCol instanceof LimitedEntryConditionCol52 ) ) {
//            final LimitedEntryConditionCol52 lec = (LimitedEntryConditionCol52) editingCol;
//        }
//
//        final DTCellValue52 defaultValue = lec.getValue();

        final DTCellValue52 defaultValue = new DTCellValue52() {
            @Override
            public String getStringValue() {
                return "";
            }
        };

        return factory().getWidget( editingPattern,
                                    editingCol,
                                    defaultValue );
    }

    CEPWindowOperatorsDropdown newCEPWindowOperatorsDropdown() {
        final Pattern52 editingPattern = getConditionColumnWizardPlugin().getEditingPattern();

        return new CEPWindowOperatorsDropdown( editingPattern, isReadOnly() );
    }

    TextBox newBindingTextBox() {
        return new BindingTextBox();
    }

    private DTCellValueWidgetFactory factory() {
        final GuidedDecisionTable52 model = presenter.getModel();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();
        final boolean allowEmptyValues = model.getTableFormat() == GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY;

        return DTCellValueWidgetFactory.getInstance( model,
                                                     oracle,
                                                     isReadOnly(),
                                                     allowEmptyValues );
    }

    private ConditionColumnWizardPlugin getConditionColumnWizardPlugin() {
        return (ConditionColumnWizardPlugin) plugin;
    }

    public interface View extends UberView<ValueOptionsWizardPage> {

    }
}
