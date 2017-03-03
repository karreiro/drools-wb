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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class MetaDataColumnPageView extends Composite implements MetaDataColumnPage.View {

    @Inject
    @DataField("metaDataBox")
    TextBox metaDataBox;

    @Inject
    @DataField("error")
    Div error;

    @Inject
    @DataField("errorMessage")
    Span errorMessage;

    private MetaDataColumnPage page;

    @Override
    public void init(final MetaDataColumnPage page) {
        this.page = page;

        setup();
    }

    @EventHandler("metaDataBox")
    public void onSelectAttribute(KeyUpEvent event) {
        page.setMetadata(metaDataBox.getText());
    }

    @Override
    public void showError(String message) {
        errorMessage.setTextContent(message);
        error.setHidden(false);
    }

    private void setup() {
        metaDataBox.setText(page.getMetadata());
        errorMessage.setTextContent("");
        error.setHidden(true);
    }
}
