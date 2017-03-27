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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

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
        callback.callback(!nil(plugin().getFactField()) || isConstraintValuePredicate());
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    void setEditingCol(final String selectedValue) {
        plugin().setFactField(selectedValue);
    }

    void forEachFactField(Consumer<String> consumer) {
        if (hasEditingPattern()) {
            final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

            oracle.getFieldCompletions(factType(),
                                       getAccessor(),
                                       fieldsCallback(consumer));
        }
    }

    public FieldAccessorsAndMutators getAccessor() {
        return plugin().getAccessor();
    }

    Callback<ModelField[]> fieldsCallback(Consumer<String> consumer) {
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        return modelFields -> {
            final List<String> fieldNames = mapName(modelFields);

            if (filterEnumFields()) {
                fieldNames
                        .stream()
                        .filter(fieldName -> !oracle.hasEnums(factType(),
                                                              fieldName))
                        .forEach(consumer);
            } else {
                fieldNames
                        .forEach(consumer);
            }
        };
    }

    boolean isConstraintValuePredicate() {
        return plugin().constraintValue() == BaseSingleFieldConstraint.TYPE_PREDICATE;
    }

    boolean filterEnumFields() {
        return plugin().filterEnumFields();
    }

    boolean hasEditingPattern() {
        return !nil(factType());
    }

    private String factType() {
        return plugin().patternWrapper().getFactType();
    }

    private List<String> mapName(final ModelField[] modelFields) {
        return Stream
                .of(modelFields)
                .map(ModelField::getName)
                .collect(Collectors.toList());
    }

    public String getFactField() {
        return plugin().getFactField();
    }

    public interface View extends UberView<FieldPage> {

    }
}
