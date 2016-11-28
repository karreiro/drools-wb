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

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.AbstractDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.*;

@Dependent
public class FactFieldWizardPage extends AbstractDecisionTableColumnPage<ConditionColumnWizardPlugin> {

    @Inject
    private View view;

    @Inject
    private Event<WizardPageStatusChangeEvent> changeEvent;

    private SimplePanel content = new SimplePanel();

    private ConditionCol52 editingCol = new ConditionCol52();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.FactType();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( plugin().getEditingCol() != null );
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    List<String> factFields() {
        final List<String> fields = new ArrayList<>();

        if ( canSetFactField() ) {
            final Pattern52 pattern52 = plugin().getEditingPattern();
            final String factType = pattern52.getFactType();
            final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

            oracle.getFieldCompletions( factType,
                                        FieldAccessorsAndMutators.ACCESSOR,
                                        modelFields -> {

                                            for ( final ModelField field : modelFields ) {
                                                final String fieldName = field.getName();

                                                switch ( editingCol.getConstraintValueType() ) {
                                                    case BaseSingleFieldConstraint.TYPE_LITERAL:
                                                        fields.add( fieldName );
                                                        break;

                                                    case BaseSingleFieldConstraint.TYPE_RET_VALUE:
                                                        final boolean fieldHasEnumerations = oracle.hasEnums( factType,
                                                                                                              fieldName );
                                                        if ( !fieldHasEnumerations ) {
                                                            fields.add( fieldName );
                                                        }
                                                        break;

                                                    case BaseSingleFieldConstraint.TYPE_PREDICATE:
                                                        throw new UnsupportedOperationException( "Predicates don't need a field" );
                                                }
                                            }
                                        } );
        }

        return fields;
    }

    boolean canSetFactField() {
        final Pattern52 pattern52 = plugin().getEditingPattern();

        return pattern52 != null;
    }

    private ConditionCol52 newConditionColumn( final String selectedValue ) {
        if ( nil( selectedValue ) ) {
            return null;
        }

        final ConditionCol52 conditionCol52 = newConditionColumn();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        conditionCol52.setFactField( selectedValue );
        conditionCol52.setFieldType( oracle.getFieldType( plugin().getEditingPattern().getFactType(),
                                                          conditionCol52.getFactField() ) );
        return conditionCol52;
    }

    private ConditionCol52 newConditionColumn() {
        switch ( presenter.getModel().getTableFormat() ) {
            case EXTENDED_ENTRY:
                return new ConditionCol52();
            case LIMITED_ENTRY:
                return new LimitedEntryConditionCol52();
            default:
                throw new UnsupportedOperationException( "Unsupported table format: " + presenter.getModel().getTableFormat() );
        }
    }

    private DTCellValue52 cloneDTCellValue( DTCellValue52 dtCellValue52 ) {
        if ( dtCellValue52 == null ) {
            return null;
        }

        return new DTCellValue52( dtCellValue52 );
    }

    public ConditionCol52 getEditingCol() {
        return plugin().getEditingCol();
    }

    void setEditingCol( final String selectedValue ) {
        final ConditionCol52 conditionCol52 = newConditionColumn( selectedValue );

        plugin().setEditingCol( conditionCol52 );

        changeEvent.fire( new WizardPageStatusChangeEvent( this ) );
        changeEvent.fire( new WizardPageStatusChangeEvent( new OperatorWizardPage() ) );
    }

    public interface View extends UberView<FactFieldWizardPage> {

    }
}
