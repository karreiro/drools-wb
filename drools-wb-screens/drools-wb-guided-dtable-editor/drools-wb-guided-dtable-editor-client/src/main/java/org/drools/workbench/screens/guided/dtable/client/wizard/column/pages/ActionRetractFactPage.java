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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.ActionColumnCommand;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.GuidedDecisionTableColumnWizard;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.FormStyleLayout;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public class ActionRetractFactPage extends AbstractDecisionTableColumnPage {

    //TODO {manstis} Popups need to MVP'ed
    private GuidedDecisionTable52 model;

    private BRLRuleModel rm;
    private GuidedDecisionTableView.Presenter presenter;
    private ActionRetractFactCol52 editingCol;
    private ActionColumnCommand refreshGrid;
    private ActionRetractFactCol52 originalCol;
    private boolean isNew;
    private boolean isReadOnly;

    private FormStyleLayout content = new FormStyleLayout();

    public ActionRetractFactPage( final GuidedDecisionTableColumnWizard wizard,
                                  final GuidedDecisionTableView.Presenter presenter ) {
//        super( GuidedDecisionTableConstants.INSTANCE.ColumnConfigurationDeleteAFact() );

        ActionRetractFactCol52 column = null;

        if ( presenter.isReadOnly() ) {
            return;
        }
        switch ( presenter.getModel().getTableFormat() ) {
            case EXTENDED_ENTRY:
                column = new ActionRetractFactCol52();
                break;
            case LIMITED_ENTRY:
                column = new LimitedEntryActionRetractFactCol52() {{
                    setValue( new DTCellValue52( "" ) );
                }};
                break;
        }

        this.model = presenter.getModel();
        this.rm = new BRLRuleModel( model );
        this.presenter = presenter;
        this.editingCol = cloneActionRetractColumn( column );
        this.originalCol = column;
        this.isNew = true;
        this.isReadOnly = presenter.getAccess().isReadOnly();

        //Show available pattern bindings, if Limited Entry
        if ( model.getTableFormat() == GuidedDecisionTable52.TableFormat.LIMITED_ENTRY ) {
            final LimitedEntryActionRetractFactCol52 ler = (LimitedEntryActionRetractFactCol52) editingCol;
            final ListBox patterns = loadBoundFacts( ler.getValue().getStringValue() );
            patterns.setEnabled( !isReadOnly );
            if ( !isReadOnly ) {
                patterns.addClickHandler( new ClickHandler() {

                    public void onClick( ClickEvent event ) {
                        int index = patterns.getSelectedIndex();
                        if ( index > -1 ) {
                            ler.getValue().setStringValue( patterns.getValue( index ) );
                        }
                    }

                } );
            }
            content.addAttribute( GuidedDecisionTableConstants.INSTANCE.FactToDeleteColon(),
                          patterns );
        }

        //Column header
        final TextBox header = new TextBox();
        header.setText( column.getHeader() );
        header.setEnabled( !isReadOnly );
        if ( !isReadOnly ) {
            header.addChangeHandler( new ChangeHandler() {
                public void onChange( ChangeEvent event ) {
                    editingCol.setHeader( header.getText() );
                }
            } );
        }
        content.addAttribute( GuidedDecisionTableConstants.INSTANCE.ColumnHeaderDescription(),
                      header );

        //Hide column tick-box
        content.addAttribute( new StringBuilder( GuidedDecisionTableConstants.INSTANCE.HideThisColumn() ).append( GuidedDecisionTableConstants.COLON ).toString(),
                      DTCellValueWidgetFactory.getHideColumnIndicator( editingCol ) );

        //Apply button
//        footer.enableOkButton( !isReadOnly );
//        add( footer );
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.ColumnConfigurationDeleteAFact();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void initialise() {
    }

    @Override
    public void prepareView() {
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public FormStyleLayout getContent() {
        return content;
    }

    public boolean unique( String header ) {
        for ( ActionCol52 o : model.getActionCols() ) {
            if ( o.getHeader().equals( header ) ) {
                return false;
            }
        }
        return true;
    }

    private ActionRetractFactCol52 cloneActionRetractColumn( ActionRetractFactCol52 col ) {
        ActionRetractFactCol52 clone = null;
        if ( col instanceof LimitedEntryCol ) {
            clone = new LimitedEntryActionRetractFactCol52();
            DTCellValue52 dcv = new DTCellValue52( ( (LimitedEntryCol) col ).getValue().getStringValue() );
            ( (LimitedEntryCol) clone ).setValue( dcv );
        } else {
            clone = new ActionRetractFactCol52();
        }
        clone.setHeader( col.getHeader() );
        clone.setHideColumn( col.isHideColumn() );
        return clone;
    }

    private ListBox loadBoundFacts( String binding ) {
        ListBox listBox = new ListBox();
        listBox.addItem( GuidedDecisionTableConstants.INSTANCE.Choose() );
        List<String> factBindings = rm.getLHSBoundFacts();

        for ( int index = 0; index < factBindings.size(); index++ ) {
            String boundName = factBindings.get( index );
            if ( !"".equals( boundName ) ) {
                listBox.addItem( boundName );
                if ( boundName.equals( binding ) ) {
                    listBox.setSelectedIndex( index + 1 );
                }
            }
        }

        listBox.setEnabled( listBox.getItemCount() > 1 );
        if ( listBox.getItemCount() == 1 ) {
            listBox.clear();
            listBox.addItem( GuidedDecisionTableConstants.INSTANCE.NoPatternBindingsAvailable() );
        }
        return listBox;
    }

    public ActionRetractFactCol52 getEditingCol() {
        return editingCol;
    }

    public boolean isNew() {
        return isNew;
    }

    public ActionRetractFactCol52 getOriginalCol() {
        return originalCol;
    }

    public void refreshGrid() {
        presenter.appendColumn( editingCol );
    }
}
