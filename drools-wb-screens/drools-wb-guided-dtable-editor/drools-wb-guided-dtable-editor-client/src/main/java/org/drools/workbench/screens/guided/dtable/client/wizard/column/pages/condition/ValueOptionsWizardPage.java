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
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.Validator;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.AbstractDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.BindingTextBox;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.TextBox;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.*;

@Dependent
public class ValueOptionsWizardPage extends AbstractDecisionTableColumnPage<ConditionColumnWizardPlugin> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return "Value options";
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( false );
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

        if ( editingCol().getDefaultValue() == null ) {
            editingCol().setDefaultValue( factory().makeNewValue( editingPattern(),
                                                                  editingCol() ) );
        }

        //Ensure the Default Value has been updated to represent the column's
        //data-type. Legacy Default Values are all String-based and need to be
        //coerced to the correct type
        final DTCellValue52 defaultValue = editingCol().getDefaultValue();
        final DataType.DataTypes dataType = columnUtilities().getDataType( editingPattern(),
                                                                           editingCol() );
        cellUtilities().convertDTCellValueType( dataType,
                                                defaultValue );

        //Correct comma-separated Default Value if operator does not support it
        if ( !validator().doesOperatorAcceptCommaSeparatedValues( editingCol() ) ) {
            cellUtilities().removeCommaSeparatedValue( defaultValue );
        }

        return factory().getWidget( editingPattern(), editingCol(), defaultValue );
    }

    private CellUtilities cellUtilities() {
        return new CellUtilities();
    }

    private ColumnUtilities columnUtilities() {
        return new ColumnUtilities( presenter.getModel(), presenter.getDataModelOracle() );
    }

    IsWidget newLimitedValueWidget() {
        final LimitedEntryConditionCol52 lec = (LimitedEntryConditionCol52) editingCol();

        if ( lec.getValue() == null ) {
            lec.setValue( factory().makeNewValue( editingPattern(), editingCol() ) );
        }

        return factory().getWidget( editingPattern(), editingCol(), lec.getValue() );
    }

    CEPWindowOperatorsDropdown newCEPWindowOperatorsDropdown() {
        return new CEPWindowOperatorsDropdown( editingPattern(), isReadOnly() );
    }

    TextBox newBindingTextBox() {
        return new BindingTextBox();
    }

    boolean canSetupCepOperators() {
        return editingPattern() != null;
    }

    boolean canSetupDefaultValue() {
        if ( editingCol() == null || editingPattern() == null ) {
            return false;
        }

        if ( nil( editingCol().getFactField() ) ) {
            return false;
        }

        if ( !validator().doesOperatorNeedValue( editingCol() ) ) {
            return false;
        }

        if ( tableFormat() != GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY ) {
            return false;
        }

        return true;
    }

    boolean canSetupLimitedValue() {
        if ( editingCol() == null || editingPattern() == null ) {
            return false;
        }

        if ( !( editingCol() instanceof LimitedEntryConditionCol52 ) ) {
            return false;
        }

        if ( !validator().doesOperatorNeedValue( editingCol() ) ) {
            return false;
        }

        if ( tableFormat() != GuidedDecisionTable52.TableFormat.LIMITED_ENTRY ) {
            return false;
        }

        return true;
    }

    void isFactTypeAnEvent( final Callback<Boolean> callback ) {
        if ( canSetupCepOperators() ) {
            presenter.getDataModelOracle().isFactTypeAnEvent( editingPattern().getFactType(), callback );
        }
    }

    private Validator validator() {
        return new Validator( presenter.getModel().getConditions() );
    }

    private GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
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

    private ConditionCol52 editingCol() {
        return plugin().getEditingCol();
    }

    private Pattern52 editingPattern() {
        return plugin().getEditingPattern();
    }

    boolean canSetupBinding() {
        return plugin().getConstraintValue() == BaseSingleFieldConstraint.TYPE_LITERAL;
    }

    public interface View extends UberView<ValueOptionsWizardPage> {

    }
}
