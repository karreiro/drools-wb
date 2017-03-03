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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class FieldPage<T extends HasFieldPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.FieldPage_Field);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(plugin().getEditingCol() != null || isConstraintValuePredicate());
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    public DTColumnConfig52 getEditingCol() {
        return plugin().getEditingCol();
    }

    void setEditingCol(final String selectedValue) {
        plugin().setEditingCol(selectedValue);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    List<String> factFields() {
        final List<String> fields = new ArrayList<>();

        if (hasEditingPattern()) {
            final Pattern52 pattern52 = plugin().getEditingPattern();
            final String factType = pattern52.getFactType();
            final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

            oracle.getFieldCompletions(factType,
                                       FieldAccessorsAndMutators.ACCESSOR,
                                       modelFields -> fields.addAll(mapName(modelFields)));
        }

        return fields;
    }

    private List<String> mapName(final ModelField[] modelFields) {
        return Stream
                .of(modelFields)
                .map(ModelField::getName)
                .collect(Collectors.toList());
    }

    boolean hasEditingPattern() {
        return plugin().getEditingPattern() != null;
    }

    boolean isConstraintValuePredicate() {
        return plugin().getConstraintValue() == BaseSingleFieldConstraint.TYPE_PREDICATE;
    }

    public String getFactField() {
        return plugin().getFactField();
    }

    public interface View extends UberView<FieldPage> {

    }
}
