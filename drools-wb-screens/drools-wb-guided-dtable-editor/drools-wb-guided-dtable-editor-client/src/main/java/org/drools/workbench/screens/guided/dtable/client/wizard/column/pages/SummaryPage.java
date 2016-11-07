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

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.GuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.AbstractDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.AttributeColumnWizardPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.MetaDataColumnWizardPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

/**
 * A summary page for the guided Decision Table Wizard
 */
@Dependent
public class SummaryPage extends AbstractDecisionTableColumnPage {

    private static String SECTION_SEPARATOR = "...............................................";

    @Inject
    private MetaDataColumnWizardPlugin metaDataColumnWizardPlugin;

    @Inject
    private AttributeColumnWizardPlugin attributeColumnWizardPlugin;

    private enum NewColumnTypes {
        METADATA_COLUMN,
        ATTRIBUTE_COLUMN,
        CONDITION_SIMPLE,
        CONDITION_BRL_FRAGMENT,
        ACTION_UPDATE_FACT_FIELD,
        ACTION_INSERT_FACT_FIELD,
        ACTION_RETRACT_FACT,
        ACTION_WORKITEM,
        ACTION_WORKITEM_UPDATE_FACT_FIELD,
        ACTION_WORKITEM_INSERT_FACT_FIELD,
        ACTION_BRL_FRAGMENT;

    }

    private FlowPanel content = new FlowPanel();

    private ListBox columnTypeSelector;

    private CheckBox checkBox;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewColumn();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void init( final GuidedDecisionTableColumnWizard wizard ) {
        super.init( wizard );
    }

    @Override
    public void initialise() {
        columnTypeSelector = getColumnTypeSelector();
        checkBox = getCheckBox();

        content = new FlowPanel() {{
            add( columnTypeSelector );
            add( checkBox );
        }};
    }

    @Override
    public void prepareView() {
    }

    private CheckBox getCheckBox() {
        return new CheckBox( SafeHtmlUtils.fromString( GuidedDecisionTableConstants.INSTANCE.IncludeAdvancedOptions() ) ) {{
            setValue( false );

            addClickHandler( event -> {
                if ( getValue() ) {
                    addExtraItens();
                } else {
                    removeExtraItens();
                }
            } );
        }};
    }

    private ListBox getColumnTypeSelector() {
        return new ListBox() {{
            setVisibleItemCount( NewColumnTypes.values().length );
            setWidth( "100%" );

            addItem( GuidedDecisionTableConstants.INSTANCE.AddNewMetadata(), NewColumnTypes.METADATA_COLUMN.name() );
            addItem( GuidedDecisionTableConstants.INSTANCE.AddNewAttributeColumn(), NewColumnTypes.ATTRIBUTE_COLUMN.name() );
            addItem( SECTION_SEPARATOR );
            addItem( GuidedDecisionTableConstants.INSTANCE.AddNewConditionSimpleColumn(), NewColumnTypes.CONDITION_SIMPLE.name() );
            addItem( SECTION_SEPARATOR );
            addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAField(), NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() );
            addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAFieldOnANewFact(), NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() );
            addItem( GuidedDecisionTableConstants.INSTANCE.DeleteAnExistingFact(), NewColumnTypes.ACTION_RETRACT_FACT.name() );

            addChangeHandler( changeEvent -> openPage( getValue( getSelectedIndex() ) ) );
        }};
    }

    private void openPage( final String type ) {
        final AbstractDecisionTableColumnPlugin plugin;

        if ( type.equals( NewColumnTypes.METADATA_COLUMN.name() ) ) {
            plugin = metaDataColumnWizardPlugin;
        } else if ( type.equals( NewColumnTypes.ATTRIBUTE_COLUMN.name() ) ) {
            plugin = attributeColumnWizardPlugin;
        } else if ( type.equals( NewColumnTypes.CONDITION_SIMPLE.name() ) ) {
            plugin = null;
//            pages.addAll( new ConditionColumnWizardPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() ) ) {
            plugin = null;
//            pages.addAll( new ActionUpdateFactPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() ) ) {
            plugin = null;
//            pages.addAll( new ActionInsertFactPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_RETRACT_FACT.name() ) ) {
            plugin = null;
//            pages.addAll( new ActionRetractFactPlugin( wizard, presenter ).getPages() );
        } else {
            plugin = null;
        }

        plugin.init( wizard );

        wizard.start( new ArrayList<WizardPage>() {{
            addAll( wizard.defaultPages() );
            addAll( plugin.getPages() );
        }} );

        wizard.goTo( 1 );
    }

    private void removeExtraItens() {
        removeItem( NewColumnTypes.CONDITION_BRL_FRAGMENT.name() );
        removeItem( NewColumnTypes.ACTION_WORKITEM.name() );
        removeItem( NewColumnTypes.ACTION_WORKITEM_UPDATE_FACT_FIELD.name() );
        removeItem( NewColumnTypes.ACTION_WORKITEM_INSERT_FACT_FIELD.name() );
        removeItem( NewColumnTypes.ACTION_BRL_FRAGMENT.name() );
    }

    private void addExtraItens() {
        addItem( 3, GuidedDecisionTableConstants.INSTANCE.AddNewConditionBRLFragment(), NewColumnTypes.CONDITION_BRL_FRAGMENT.name() );
        addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemAction(), NewColumnTypes.ACTION_WORKITEM.name() );
        addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemActionSetField(), NewColumnTypes.ACTION_WORKITEM_UPDATE_FACT_FIELD.name() );
        addItem( GuidedDecisionTableConstants.INSTANCE.WorkItemActionInsertFact(), NewColumnTypes.ACTION_WORKITEM_INSERT_FACT_FIELD.name() );
        addItem( GuidedDecisionTableConstants.INSTANCE.AddNewActionBRLFragment(), NewColumnTypes.ACTION_BRL_FRAGMENT.name() );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    private void addItem( final int index,
                          final String item,
                          final String value ) {

        if ( columnTypeSelectorContainsValue( value ) ) {
            return;
        }

        columnTypeSelector.insertItem( item, value, index );
    }

    private void addItem( final String item,
                          final String value ) {

        if ( columnTypeSelectorContainsValue( value ) ) {
            return;
        }

        columnTypeSelector.addItem( item, value );
    }

    private void removeItem( String value ) {
        for ( int itemIndex = 0; itemIndex < columnTypeSelector.getItemCount(); itemIndex++ ) {
            if ( columnTypeSelector.getValue( itemIndex ).equals( value ) ) {
                columnTypeSelector.removeItem( itemIndex );
                break;
            }
        }
    }

    private boolean columnTypeSelectorContainsValue( final String value ) {
        for ( int itemIndex = 0; itemIndex < columnTypeSelector.getItemCount(); itemIndex++ ) {
            if ( columnTypeSelector.getValue( itemIndex ).equals( value ) ) {
                return true;
            }
        }
        return false;
    }
}
