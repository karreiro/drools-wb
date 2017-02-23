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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.editor.commons.client.file.popups.CommonModalBuilder;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.GenericModalFooter;

@Dependent
@Templated
public class NewPatternView implements NewPatternPresenter.View,
                                       IsElement {

    private static final String WIDTH = "550px";

    @Inject
    @DataField("body")
    private Div body;

    @Inject
    @DataField("factTypeList")
    private ListBox factTypeList;

    @Inject
    @DataField("binding")
    private TextBox binding;

    @Inject
    @DataField("negatePatternMatch")
    private CheckBox negatePatternMatch;

    private NewPatternPresenter presenter;

    private BaseModal modal;

    @Override
    public void init(NewPatternPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void show() {
        setupModal();
        showModal();
    }

    @Override
    public void hide() {
        modal.hide();
    }

    @Override
    public void clear() {
        factTypeList.setSelectedIndex(0);
        binding.setText("");
        negatePatternMatch.setValue(false);
    }

    @Override
    public boolean isNegatePatternMatch() {
        return negatePatternMatch.getValue();
    }

    @Override
    public String getSelectedFactType() {
        return factTypeList.getSelectedItemText();
    }

    @Override
    public String getBindingText() {
        return binding.getText().trim();
    }

    @Override
    public void showError(final String errorMessage) {
        Window.alert(errorMessage); // TODO
    }

    @EventHandler("binding")
    public void onBindingChange(ChangeEvent event) {
        binding.setText(binding.getText().trim());
    }

    @EventHandler("negatePatternMatch")
    public void onNegatePatternMatchChange(ChangeEvent event) {
        final boolean isPatternNegated = negatePatternMatch.getValue();

        binding.setEnabled(!isPatternNegated);
    }

    private void setupModal() {
        createModal();
        factTypesSetup();
    }

    private void createModal() {
        this.modal = new CommonModalBuilder()
                .addHeader("Create a new fact pattern")
                .addBody(body)
                .addFooter(footer())
                .build();
        this.modal.setWidth(WIDTH);
    }

    private ModalFooter footer() {
        final GenericModalFooter footer = new GenericModalFooter();

        footer.addButton("Cancel",
                         presenter::cancel,
                         ButtonType.DEFAULT);
        footer.addButton("OK",
                         presenter::addPattern,
                         IconType.PLUS,
                         ButtonType.PRIMARY);

        return footer;
    }

    private void factTypesSetup() {
        factTypeList.clear();

        presenter.getFactTypes().forEach(factTypeList::addItem);
    }

    private void showModal() {
        modal.show();
    }
}
