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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ActionInsertFactPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ActionRetractFactPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ActionUpdateFactPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.MetaDataColumnWizardPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

/**
 * A summary page for the guided Decision Table Wizard
 */
public class SummaryPage implements WizardPage {

    private static String SECTION_SEPARATOR = "...............................................";

    private enum NewColumnTypes {
        METADATA_ATTRIBUTE,
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

    private GuidedDecisionTableView.Presenter presenter;

    private final FlowPanel content = new FlowPanel();

    private ListBox columnTypeSelector;

    private CheckBox checkBox;

    private NewGuidedDecisionTableColumnWizard wizard;

    public SummaryPage( final NewGuidedDecisionTableColumnWizard wizard,
                        final GuidedDecisionTableView.Presenter presenter ) {
        this.wizard = wizard;
        this.presenter = presenter;
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewColumn();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void initialise() {
        columnTypeSelector = getColumnTypeSelector();
        checkBox = getCheckBox();
    }

    @Override
    public void prepareView() {
        content.add( columnTypeSelector );
        content.add( checkBox );
    }

    private CheckBox getCheckBox() {
        CheckBox checkBox = new CheckBox( SafeHtmlUtils.fromString( GuidedDecisionTableConstants.INSTANCE.IncludeAdvancedOptions() ) );

        checkBox.setValue( false );
        checkBox.addClickHandler( event -> {
            if ( checkBox.getValue() ) {
                addExtraItens();
            } else {
                removeExtraItens();
            }
        } );

        return checkBox;
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

    private ListBox getColumnTypeSelector() {
        final ListBox columnTypeSelector = new ListBox();

        columnTypeSelector.setVisibleItemCount( NewColumnTypes.values().length );
        columnTypeSelector.setWidth( "100%" );

        columnTypeSelector.addItem( GuidedDecisionTableConstants.INSTANCE.AddNewMetadataOrAttributeColumn(), NewColumnTypes.METADATA_ATTRIBUTE.name() );
        columnTypeSelector.addItem( SECTION_SEPARATOR );
        columnTypeSelector.addItem( GuidedDecisionTableConstants.INSTANCE.AddNewConditionSimpleColumn(), NewColumnTypes.CONDITION_SIMPLE.name() );
        columnTypeSelector.addItem( SECTION_SEPARATOR );
        columnTypeSelector.addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAField(), NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() );
        columnTypeSelector.addItem( GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAFieldOnANewFact(), NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() );
        columnTypeSelector.addItem( GuidedDecisionTableConstants.INSTANCE.DeleteAnExistingFact(), NewColumnTypes.ACTION_RETRACT_FACT.name() );

        columnTypeSelector.addChangeHandler( changeEvent -> openPage( columnTypeSelector.getValue( columnTypeSelector.getSelectedIndex() ) ) );

        return columnTypeSelector;
    }

    private void openPage( final String type ) {
        final List<WizardPage> pages = wizard.defaultPages();

        if ( type.equals( NewColumnTypes.METADATA_ATTRIBUTE.name() ) ) {
            pages.addAll( new MetaDataColumnWizardPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.CONDITION_SIMPLE.name() ) ) {
            pages.addAll( new ConditionColumnWizardPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_UPDATE_FACT_FIELD.name() ) ) {
            pages.addAll( new ActionUpdateFactPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_INSERT_FACT_FIELD.name() ) ) {
            pages.addAll( new ActionInsertFactPlugin( wizard, presenter ).getPages() );
        } else if ( type.equals( NewColumnTypes.ACTION_RETRACT_FACT.name() ) ) {
            pages.addAll( new ActionRetractFactPlugin( wizard, presenter ).getPages() );
        }

        GWT.log( type );

        wizard.start( pages );
        wizard.goTo( 1 );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public FlowPanel getContent() {
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
