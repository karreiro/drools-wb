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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class FactFieldWizardPageView extends Composite implements FactFieldWizardPage.View {

    private FactFieldWizardPage page;

    @Inject
    @DataField("fieldsList")
    private ListBox fieldsList;

    @Override
    public void init( final FactFieldWizardPage page ) {
        this.page = page;

        setupFieldList();

//    private void doFieldLabel() {
//        if ( editingCol.getConstraintValueType() == BaseSingleFieldConstraint.TYPE_PREDICATE ) {
//            if ( this.editingCol.getFactField() == null || this.editingCol.getFactField().equals( "" ) ) {
//                fieldLabel.setText( GuidedDecisionTableConstants.INSTANCE.notNeededForPredicate() );
//            } else {
//                fieldLabel.setText( this.editingCol.getFactField() );
//            }
//            fieldLabelInterpolationInfo.getWidget().getElement().getStyle().setDisplay( Style.Display.INLINE );
//        } else if ( nil( editingPattern.getFactType() ) ) {
//            fieldLabel.setText( GuidedDecisionTableConstants.INSTANCE.pleaseSelectAPatternFirst() );
//            fieldLabelInterpolationInfo.getWidget().getElement().getStyle().setDisplay( Style.Display.NONE );
//        } else if ( nil( editingCol.getFactField() ) ) {
//            fieldLabel.setText( GuidedDecisionTableConstants.INSTANCE.pleaseSelectAField() );
//            fieldLabelInterpolationInfo.getWidget().getElement().getStyle().setDisplay( Style.Display.NONE );
//        } else {
//            fieldLabel.setText( this.editingCol.getFactField() );
//        }
//    }
    }

    @EventHandler("fieldsList")
    public void onPluginSelected( ChangeEvent event ) {
        page.setEditingCol( fieldsList.getSelectedValue() );
    }


    private void setupFieldList() {
        fieldsList.clear();
        fieldsList.addItem( "-- Select a field --", "" );

        page.factFields().forEach( fieldName -> fieldsList.addItem( fieldName ) );
    }
}

