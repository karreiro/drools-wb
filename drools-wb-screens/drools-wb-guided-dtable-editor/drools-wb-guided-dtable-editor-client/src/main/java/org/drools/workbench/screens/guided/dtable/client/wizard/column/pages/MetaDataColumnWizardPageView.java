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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class MetaDataColumnWizardPageView extends Composite implements MetaDataColumnWizardPage.View {

    @Inject
    @DataField("metadataBox")
    TextBox metadataBox;

    @Inject
    @DataField("error")
    Div error;

    @Inject
    @DataField("errorMessage")
    Span errorMessage;

    private MetaDataColumnWizardPage page;

    @Override
    public void init(final MetaDataColumnWizardPage page) {
        this.page = page;

        clear();
    }

    @Override
    public String getMetadataText() {
        return metadataBox.getText();
    }

    @Override
    public void showError(String errorMessage) {
        this.errorMessage.setTextContent(errorMessage);
        this.error.setHidden(false);
    }

    private void clear() {
        metadataBox.setText("");
        this.errorMessage.setTextContent("");
        error.setHidden(true);
    }
}
