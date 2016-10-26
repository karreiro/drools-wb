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

import java.util.Arrays;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.drools.workbench.screens.guided.rule.client.resources.GuidedRuleEditorResources;
import org.drools.workbench.screens.guided.rule.client.resources.images.GuidedRuleEditorImages508;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.DirtyableHorizontalPane;
import org.uberfire.ext.widgets.common.client.common.FormStyleLayout;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public class MetaDataColumnWizardPage implements WizardPage {

    private final FormStyleLayout content = new FormStyleLayout();
    private TextBox metadataBox;

    private ListBox list;
    private String[] existingAttributeNames;

    private GuidedDecisionTableView.Presenter presenter;

    private NewGuidedDecisionTableColumnWizard wizard;

    public MetaDataColumnWizardPage( final NewGuidedDecisionTableColumnWizard wizard,
                                     final GuidedDecisionTableView.Presenter presenter ) {
        this.wizard = wizard;
        this.presenter = presenter;
        this.existingAttributeNames = presenter.getExistingAttributeNames().toArray( new String[ 0 ] );

        initialise();
    }

    @Override
    public String getTitle() {
        return GuidedRuleEditorResources.CONSTANTS.AddAnOptionToTheRule();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void initialise() {
        this.metadataBox = new TextBox();
        this.list = new ListBox();

        setMetadataPanel();
        setAttributesPanel();
    }

    private void setMetadataPanel() {
        metadataBox.getElement().setAttribute( "size", "15" );

        DirtyableHorizontalPane horizontalPane = new DirtyableHorizontalPane();

        horizontalPane.add( metadataBox );
        horizontalPane.add( getAddButton() );

        content.addAttribute( GuidedRuleEditorResources.CONSTANTS.Metadata3(), horizontalPane );

    }

    private Image getAddButton() {
        final Image addbutton = GuidedRuleEditorImages508.INSTANCE.NewItem();

        addbutton.setAltText( GuidedRuleEditorResources.CONSTANTS.AddMetadataToTheRule() );
        addbutton.setTitle( GuidedRuleEditorResources.CONSTANTS.AddMetadataToTheRule() );
        addbutton.addClickHandler( getMetadataHandler() );

        return addbutton;
    }

    protected ClickHandler getMetadataHandler() {
        return ( ClickEvent event ) -> {
            //Check MetaData has a name
            final String metaData = metadataBox.getText().trim();
            if ( metaData.isEmpty() ) {
                Window.alert( GuidedRuleEditorResources.CONSTANTS.MetadataNameEmpty() );
                return;
            }

            //Check MetaData is unique
            if ( !isMetadataUnique( metaData ) ) {
                Window.alert( metadataNotUniqueMessage( metaData ) );
                return;
            }

            handleMetadataAddition( metaData );

            wizard.close();
        };
    }

    private void setAttributesPanel() {
        for ( String item : getAttributes() ) {
            list.addItem( item );
        }

        // Remove any attributes already added
        for ( String at : getDuplicates() ) {
            for ( int iItem = 0; iItem < list.getItemCount(); iItem++ ) {
                if ( list.getItemText( iItem ).equals( at ) ) {
                    list.removeItem( iItem );
                    break;
                }
            }
        }

        list.setSelectedIndex( 0 );

        list.addChangeHandler( getListHandler( list ) );
        content.addAttribute( GuidedRuleEditorResources.CONSTANTS.Attribute1(),
                              list );

    }

    private ChangeHandler getListHandler( final ListBox list ) {
        return ( ChangeEvent event ) -> {
            handleAttributeAddition( list.getSelectedItemText() );
            wizard.close();
        };
    }

    @Override
    public void prepareView() {
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    protected String[] getAttributes() {
        String[] attributes = RuleAttributeWidget.getAttributesList();
        attributes = Arrays.copyOf( attributes, attributes.length + 1 );
        attributes[ attributes.length - 1 ] = GuidedDecisionTable52.NEGATE_RULE_ATTR;
        return attributes;
    }

    private String[] getDuplicates() {
        return existingAttributeNames;
    }

    private void handleAttributeAddition( String attributeName ) {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute( attributeName );
        presenter.appendColumn( column );
    }

    private boolean isMetadataUnique( String metadataName ) {
        return presenter.isMetaDataUnique( metadataName );
    }

    private String metadataNotUniqueMessage( String metadataName ) {
        return GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother();
    }

    private void handleMetadataAddition( String metadataName ) {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata( metadataName );
        column.setHideColumn( true );
        presenter.appendColumn( column );
    }
}
