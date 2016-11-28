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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.attribute;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class AttributeColumnWizardPageView extends Composite implements AttributeColumnWizardPage.View {

    private AttributeColumnWizardPage page;

    @Inject
    @DataField("attributeList")
    private ListBox attributeList;

    @Override
    public void init( final AttributeColumnWizardPage page ) {
        this.page = page;

        setAttributesPanel();
    }

    @Override
    public String getAttributeText() {
        return attributeList.getSelectedItemText();
    }

    private void setAttributesPanel() {
        attributeList.clear();

        for ( String item : page.getAttributes() ) {
            attributeList.addItem( item );
        }

        removeAttributesAlreadyAdded( attributeList );

        attributeList.setSelectedIndex( 0 );
    }

    private void removeAttributesAlreadyAdded( final ListBox attributeList ) {
        for ( String at : page.getDuplicates() ) {
            for ( int iItem = 0; iItem < attributeList.getItemCount(); iItem++ ) {
                if ( attributeList.getItemText( iItem ).equals( at ) ) {
                    attributeList.removeItem( iItem );
                    break;
                }
            }
        }
    }
}
